package dev.sumeragizzz.attendancereportconverter.repository;

import dev.sumeragizzz.attendancereportconverter.ParameterConfiguration;
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
import java.util.Optional;

@Repository
public class AnsAttendanceReportRepository {

    @Autowired
    ParameterConfiguration config;

    public void store(AttendanceReport attendanceReport) {
        attendanceReport.getAttendances().forEach(System.out::println);
        System.out.println(config.getReportFile());

        // Excelファイルオープン
        try (Workbook book = openBook(config.getReportFile())) {
            // TODO 編集処理
            Sheet sheet = book.getSheet("勤務表");
            for (int i = 0; i < 31; i++) {
                Row row = sheet.getRow(9 + i);
                Cell startedAtCell = row.getCell(4);
                Cell endedAtCell = row.getCell(5);
                System.out.println(startedAtCell.getStringCellValue() + " : " + endedAtCell.getStringCellValue());

                // FIXME 日数の差異はクリアする必要あり
                if (i >= attendanceReport.getAttendances().size()) {
                    continue;
                }
                Attendance attendance = attendanceReport.getAttendances().get(i);

                startedAtCell.setCellValue(Optional.ofNullable(attendance.startedAt()).map(LocalTime::toString).orElse(""));
                endedAtCell.setCellValue(Optional.ofNullable(attendance.endedAt()).map(LocalTime::toString).orElse(""));
            }

            // TODO Excelファイル保存
            saveBook(book, Paths.get("target/output.xlsx"));

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
