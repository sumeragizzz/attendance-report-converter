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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class SebAttendanceReportRepository {

    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy年 M月");

    @Autowired
    ParameterConfiguration config;

    public Optional<AttendanceReport> findByYearMonth(YearMonth targetYearMonth) {
        System.out.println("********");
        System.out.println(config.getTargetYearMonth());
        System.out.println(config.getUrl());
        System.out.println(config.getId());
        System.out.println(config.getPassword());
        System.out.println("********");

        // WebDriver生成
        WebDriver driver = createWebDriver();
        try {
            // ログイン
            login(driver, config.getUrl(), config.getId(), config.getPassword());

            // 勤務実績照会
            driver.findElement(By.id("btn00")).click();

            // 対象年月のページに切り替え
            moveTargetYearMonth(driver, config.getTargetYearMonth());

            // テーブル解析
            parseTable(driver, config.getTargetYearMonth());

        } finally {
            driver.quit();
        }

        // TODO 未実装
        return Optional.of(new AttendanceReport(List.of(new Attendance(
                LocalDate.of(2021, 5, 6),
                LocalTime.of(9, 0),
                LocalTime.of(18,0),
                Duration.ofHours(8)))));
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

        // パスワード入力 →　ログイン
        WebElement passwordField = driver.findElement(By.id("Password"));
        passwordField.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
    }

    void moveTargetYearMonth(WebDriver driver, YearMonth targetYearMonth) {
        // 現在表示中の年月を取得
        By yearMonthCondition = By.id("js-attendanceResultTime__yearMonth");
        String currentYearMonthText = driver.findElement(yearMonthCondition).getText();
        YearMonth currentYearMonth = YearMonth.parse(currentYearMonthText, FORMATTER);

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
        int daysOfMonth = targetYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysOfMonth; i++) {
            // 申請 - 日付 - 曜日
            String dateText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=1]", i)).text();

            // 事由 〜
            String startTimeText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=4]", i)).text();
            String endTimeText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=5]", i)).text();
            String activeTimeText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=6]", i)).text();

            System.out.format("%d : %s : %s : %s : %s%n", i, dateText, startTimeText, endTimeText, activeTimeText);
        }

        return List.of();
    }

}
