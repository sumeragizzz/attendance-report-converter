package dev.sumeragizzz.attendancereportconverter.repository;

import dev.sumeragizzz.attendancereportconverter.config.AnsConfiguration;
import dev.sumeragizzz.attendancereportconverter.domain.Attendance;
import dev.sumeragizzz.attendancereportconverter.domain.AttendanceReport;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

@Repository
public class AnsAttendanceReportRepository {
    @Autowired
    AnsConfiguration config;

    public void store(AttendanceReport attendanceReport) {
        // Excelファイルオープン
        try (Workbook book = openBook(config.getTemplateFile())) {
            Sheet sheet = book.getSheet("勤務表");

            // 年月設定
            sheet.getRow(0).getCell(4).setCellValue(attendanceReport.getYearMonth().getYear());
            sheet.getRow(0).getCell(6).setCellValue(attendanceReport.getYearMonth().getMonthValue());

            // 最大31日分の入力欄をループして処理
            for (int i = 0; i < 31; i++) {
                Row row = sheet.getRow(9 + i);

                // 入力欄セル初期化
                for (int j = 0; j < 5; j++) {
                    row.getCell(4 + j).setBlank();
                }

                if (i >= attendanceReport.getAttendances().size()) {
                    break;
                }

                // 出勤時間/退社時間を設定
                Attendance attendance = attendanceReport.getAttendances().get(i);
                attendance.getStartedAt().map(LocalTime::toString).ifPresent(startedAt -> row.getCell(4).setCellValue(startedAt));
                attendance.getEndedAt().map(LocalTime::toString).ifPresent(endedAt -> row.getCell(5).setCellValue(endedAt));
            }

            // Excelファイル保存
            saveBook(book, config.getOutputFile());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Workbook openBook(Path reportFile) throws IOException {
        // Fileを渡すと、参照しただけでExcelファイルが更新されてしまう為、InputStreamを使用
        try (InputStream input = Files.newInputStream(reportFile)) {
            return WorkbookFactory.create(input);
        }
    }

    void saveBook(Workbook book, Path reportFile) throws IOException {
        try (OutputStream output = Files.newOutputStream(reportFile)) {
            book.write(output);
        }
    }
}
