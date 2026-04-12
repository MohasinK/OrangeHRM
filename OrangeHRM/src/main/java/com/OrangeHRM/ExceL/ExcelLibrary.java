package com.OrangeHRM.ExceL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelLibrary {

    private Workbook workbook;
    private Path filePath;
    private final DataFormatter formatter = new DataFormatter();

    // No-arg constructor: delegate to a default test data location

    public ExcelLibrary() throws IOException {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("TestData/TestData.xlsx");

        if (is == null) {
            throw new FileNotFoundException(
                "Classpath path not found: TestData/TestData.xlsx");
        }

        this.workbook = WorkbookFactory.create(is);
    }
    
    

    // Create or open workbook at given path
    public ExcelLibrary(String filePathStr) throws IOException {
        this.filePath = Paths.get(filePathStr);
        try (FileInputStream fis = new FileInputStream(this.filePath.toFile())) {
            this.workbook = WorkbookFactory.create(fis);
        }
    }

    // Read cell as String using sheetName, rowIndex (0-based), colIndex (0-based)
    public String getCellData(String sheetName, int rowIndex, int colIndex) {
        try {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return "";
            Row row = sheet.getRow(rowIndex);
            if (row == null) return "";
            Cell cell = row.getCell(colIndex);
            if (cell == null) return "";
            return formatter.formatCellValue(cell);
        } catch (Exception e) {
            System.err.println("Error reading cell: " + e.getMessage());
            return "";
        }
    }

    // Write String value to cell and save workbook. Creates sheet/row/cell if needed.
    public boolean setCellData(String sheetName, int rowIndex, int colIndex, String value) {
        try {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }
            cell.setCellValue(value);

            // Save changes
            try (FileOutputStream fos = new FileOutputStream(this.filePath.toFile())) {
                workbook.write(fos);
                fos.flush();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error writing cell: " + e.getMessage());
            return false;
        }
    }

    // Create a new sheet with the given name (returns true if created, false if already exists or error)
    public boolean createSheet(String sheetName) {
        try {
            if (sheetName == null || sheetName.trim().isEmpty()) {
                throw new IllegalArgumentException("sheetName cannot be null or empty");
            }
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                // Sheet already exists
                return false;
            }
            workbook.createSheet(sheetName);
            // Save workbook
            try (FileOutputStream fos = new FileOutputStream(this.filePath.toFile())) {
                workbook.write(fos);
                fos.flush();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error creating sheet: " + e.getMessage());
            return false;
        }
    }

    // Close workbook resources
    public void close() {
        try {
            if (workbook != null) workbook.close();
        } catch (IOException e) {
            System.err.println("Error closing workbook: " + e.getMessage());
        }
    }

}