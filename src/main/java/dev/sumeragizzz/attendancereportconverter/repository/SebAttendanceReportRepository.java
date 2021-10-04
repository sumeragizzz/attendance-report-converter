package dev.sumeragizzz.attendancereportconverter.repository;

import dev.sumeragizzz.attendancereportconverter.ParameterConfiguration;
import dev.sumeragizzz.attendancereportconverter.domain.Attendance;
import dev.sumeragizzz.attendancereportconverter.domain.AttendanceReport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SebAttendanceReportRepository {

    /** 年月パーサー */
    static final DateTimeFormatter PARSER_YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy年ppM月");

    /** 月日パーサー */
    static final DateTimeFormatter PARSER_MONTH_DAY = DateTimeFormatter.ofPattern("M/ppd");

    /** 時分パーサー */
    static final DateTimeFormatter PARSER_HOUR_MINUTE = DateTimeFormatter.ofPattern("H:mm");

    /** 時間パターン */
    static final Pattern PATTERN_HOURS = Pattern.compile("(\\d{1,2}):(\\d{2})");

    @Autowired
    ParameterConfiguration config;

    // TODO 戻り値の型を再検討
    public Optional<AttendanceReport> findByYearMonth(YearMonth targetYearMonth) {
        // WebDriver生成
        WebDriver driver = createWebDriver();
        try {
            // ログイン
            login(driver, config.getUrl(), config.getId(), config.getPassword());

            // 勤務実績照会
            driver.findElement(By.id("btn00")).click();

            // 対象年月のページに切り替え
            moveTargetYearMonth(driver, targetYearMonth);

            // テーブル解析
            return Optional.of(new AttendanceReport(parseTable(driver, targetYearMonth)));

        } finally {
            driver.quit();
        }
    }

    WebDriver createWebDriver() {
        // ヘッドレスモード
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        // WebDriver
        // TODO WebDriverのパス指定
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
        ChromeDriver driver = new ChromeDriver(options);

        // 暗黙的な待機
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }

    void login(WebDriver driver, URL url, String id, String password) {
        // サイトへ移動
        driver.navigate().to(url);

        // ID入力
        WebElement idField = driver.findElement(By.id("OBCID"));
        idField.sendKeys(id);
        WebElement checkButton = driver.findElement(By.id("checkAuthPolisyBtn"));
        checkButton.click();

        // パスワード入力 → ログイン
        WebElement passwordField = driver.findElement(By.id("Password"));
        passwordField.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
    }

    void moveTargetYearMonth(WebDriver driver, YearMonth targetYearMonth) {
        // 現在表示中の年月を取得
        By yearMonthCondition = By.id("js-attendanceResultTime__yearMonth");
        String currentYearMonthText = driver.findElement(yearMonthCondition).getText();
        YearMonth currentYearMonth = YearMonth.parse(currentYearMonthText, PARSER_YEAR_MONTH);

        // 対象年月になっている場合は処理終了
        if (targetYearMonth.equals(currentYearMonth)) {
            return;
        }
        // 対象年月が現在表示中の年月より未来の場合、不正な年月が指定されたものとして例外発生させる。
        if (targetYearMonth.isAfter(currentYearMonth)) {
            throw new IllegalArgumentException();
        }

        // 前月ボタンを押下して過去を遡る
        driver.findElement(By.id("js-beforeMonthBtn")).click();
        // 年月要素は最初から存在しており「暗黙的な待機」では待機されない為、値が変化したことを条件に待機する。
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(yearMonthCondition, currentYearMonthText)));

        // 再帰
        moveTargetYearMonth(driver, targetYearMonth);
    }

    List<Attendance> parseTable(WebDriver driver, YearMonth targetYearMonth) {
        // JSoup
        Document document = Jsoup.parse(driver.getPageSource());

        // 日数ループ
        List<Attendance> attendances = new ArrayList<>();
        for (int i = 1, daysOfMonth = targetYearMonth.lengthOfMonth(); i <= daysOfMonth; i++) {
            // 日付
            LocalDate targetedOn = parseDate(document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=1]", i)).text(), targetYearMonth.getYear()).orElseThrow();

            // 出勤時刻 - 退出時刻 - 総労働時間
            LocalTime startedAt = parseTime(document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=4]", i)).text()).orElse(null);
            LocalTime endedAt = parseTime(document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=5]", i)).text()).orElse(null);
            Duration workingHours = parseHours(document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=6]", i)).text()).orElse(null);

            // ドメインモデル生成
            attendances.add(new Attendance(targetedOn, startedAt, endedAt, workingHours));
        }

        return attendances;
    }

    Optional<LocalDate> parseDate(String text, int year) {
        if (text == null || text.length() == 0) {
            return Optional.empty();
        }
        return Optional.of(MonthDay.parse(text, PARSER_MONTH_DAY).atYear(year));
    }

    Optional<LocalTime> parseTime(String text) {
        if (text == null || text.length() == 0) {
            return Optional.empty();
        }
        return Optional.of(LocalTime.parse(text.strip(), PARSER_HOUR_MINUTE));
    }

    Optional<Duration> parseHours(String text) {
        if (text == null || text.length() == 0) {
            return Optional.empty();
        }
        Matcher matcher = PATTERN_HOURS.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }
        return Optional.of(Duration.ofHours(Integer.parseInt(matcher.group(1)))
                .plus(Duration.ofMinutes(Integer.parseInt(matcher.group(2)))));
    }

}
