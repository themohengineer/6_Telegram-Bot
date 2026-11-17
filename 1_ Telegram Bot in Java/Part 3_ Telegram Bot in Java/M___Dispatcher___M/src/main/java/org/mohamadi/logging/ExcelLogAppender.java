package org.mohamadi.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelLogAppender extends AppenderBase<ILoggingEvent> {

    private static final String[] HEADERS = {"Timestamp", "Level", "Logger", "Message"};
    private static final SimpleDateFormat TIMESTAMP_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private String filePath;
    private XSSFWorkbook workbook;
    private Sheet sheet;
    private int rowIndex;

    public void setFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void start() {
        if (filePath == null || filePath.isBlank()) {
            addError("ExcelLogAppender: file path is not set");
            return;
        }

        File file = new File(filePath);
        try {
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fis);
                }
                sheet = workbook.getSheetAt(0);
                rowIndex = sheet.getLastRowNum() + 1;
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Logs");
                Row header = sheet.createRow(0);
                for (int i = 0; i < HEADERS.length; i++) {
                    header.createCell(i).setCellValue(HEADERS[i]);
                }
                rowIndex = 1;
            }
        } catch (IOException e) {
            addError("ExcelLogAppender: failed to open workbook", e);
            return;
        }

        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (workbook == null || sheet == null) {
            return;
        }

        synchronized (this) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(TIMESTAMP_FORMAT.format(new Date(event.getTimeStamp())));
            row.createCell(1).setCellValue(event.getLevel().toString());
            row.createCell(2).setCellValue(event.getLoggerName());
            row.createCell(3).setCellValue(event.getFormattedMessage());

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            } catch (IOException e) {
                addError("ExcelLogAppender: failed to write workbook", e);
            }
        }
    }

    @Override
    public void stop() {
        if (workbook != null) {
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            } catch (IOException e) {
                addError("ExcelLogAppender: failed to save workbook on stop", e);
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    addError("ExcelLogAppender: failed to close workbook", e);
                }
                workbook = null;
            }
        }
        super.stop();
    }
}
