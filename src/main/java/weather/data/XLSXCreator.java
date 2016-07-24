package weather.data;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Daniel Zvir on 19.07.2016.
 */
public class XLSXCreator {
    /**
     * Saves a list of the content in the specified folder into an excel spreadsheet.
     *
     * @param folderToScan content of this folder will be scanned
     * @param sheetName    name of the sheet to store data in
     * @param outputStream result will be saved here
     */
    public void createXLSXwithDirectoryContent(File folderToScan, String sheetName, OutputStream outputStream) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            final Sheet sheet = wb.createSheet(sheetName);
            final File[] listOfFiles = folderToScan.listFiles();

            int rowNumber = 0;
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    final Row row = sheet.createRow(rowNumber++);
                    final Cell cellFileName = row.createCell(0);
                    cellFileName.setCellValue(file.toString());
                    final Cell cellSize = row.createCell(1);

                    final long size = FileUtils.sizeOf(file);
                    cellSize.setCellValue(size / 1024 / 1024 + " MB");
                }
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
            }
            wb.write(outputStream);
        }
    }

    /**
     * Saves a list of the content in the specified folder into an excel spreadsheet.
     *
     * @param folderToScan content of this folder will be scanned
     * @param sheetName    name of the sheet to store data in
     * @param outputFile   result will be saved here
     */
    public void createXLSXwithDirectoryContent(String folderToScan, String sheetName, String outputFile) throws IOException {
        File input = new File(folderToScan);
        File output = new File(outputFile);
        createXLSXwithDirectoryContent(input, sheetName, output);
    }

    /**
     * Saves a list of the content in the specified folder into an excel spreadsheet.
     *
     * @param folderToScan content of this folder will be scanned
     * @param sheetName    name of the sheet to store data in
     * @param outputFile   result will be saved here
     */
    public void createXLSXwithDirectoryContent(File folderToScan, String sheetName, File outputFile) throws IOException {
        OutputStream output = new FileOutputStream(outputFile);
        createXLSXwithDirectoryContent(folderToScan, sheetName, output);
    }
}
