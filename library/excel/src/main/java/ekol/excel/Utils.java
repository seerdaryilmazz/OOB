package ekol.excel;

import org.apache.poi.ss.usermodel.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel dosyasından veri okumayla ilgili kodlar bu adres baz alınarak yazıldı: http://poi.apache.org/spreadsheet/quick-guide.html#FileInputStream
 */
public class Utils {

    /**
     * @param excelFile okuyacağımız Excel dosyası
     * @param sheetNumber okuyacağımız sayfa numarası; 0 veya daha büyük bir sayı
     * @param firstRowNumber okuyacağımız ilk satır numarası; 0 veya daha büyük bir sayı
     * @param lastRowNumber okuyacağımız son satır numarası; 0 veya daha büyük bir sayı; bilmiyorsak (yani belli değilse, değişkense) -1
     * @param firstCellNumber okuyacağımız ilk hücre/sütun numarası; 0 veya daha büyük bir sayı
     * @param lastCellNumber okuyacağımız son hücre/sütun numarası; 0 veya daha büyük bir sayı; bilmiyorsak (yani belli değilse, değişkense) -1
     * @return satır satır hücre değerleri
     * @throws RuntimeException tüm hatalı durumlarda
     */
    public static List<List<Object>> extractData(File excelFile, int sheetNumber, int firstRowNumber, int lastRowNumber, int firstCellNumber, int lastCellNumber) {

        List<List<Object>> rowValues = new ArrayList<>();
        Workbook workbook = null;

        try {

            workbook = WorkbookFactory.create(excelFile);

            Sheet sheet = workbook.getSheetAt(sheetNumber);

            if (lastRowNumber == -1) {
                lastRowNumber = sheet.getLastRowNum();
            }

            for (int rowNumber = firstRowNumber; rowNumber <= lastRowNumber; rowNumber++) {
                Row row = sheet.getRow(rowNumber);
                if (row != null) {
                    rowValues.add(extractCellValues(row, firstCellNumber, lastCellNumber));
                }
            }

            workbook.close();

        } catch (Exception e) {
            closeQuietly(workbook);
            throw new RuntimeException(e);
        }

        return rowValues;
    }

    public static int getNumberOfSheets(File excelFile) {

        int numberOfSheets;
        Workbook workbook = null;

        try {
            workbook = WorkbookFactory.create(excelFile);
            numberOfSheets = workbook.getNumberOfSheets();
            workbook.close();
        } catch (Exception e) {
            closeQuietly(workbook);
            throw new RuntimeException(e);
        }

        return numberOfSheets;
    }

    public static List<Object> extractCellValues(Row row, int firstCellNumber, int lastCellNumber) {

        List<Object> cellValues = new ArrayList<>();

        if (lastCellNumber == -1) {
            // Aşağıda neden 1 çıkardık? Cevap için row.getLastCellNum() metodunun Javadoc'unu okuyunuz.
            lastCellNumber = row.getLastCellNum() - 1;
        }

        for (int cellNumber = firstCellNumber; cellNumber <= lastCellNumber; cellNumber++) {
            Cell cell = row.getCell(cellNumber);
            if (cell != null) {
                cellValues.add(extractCellValue(cell));
            } else {
                cellValues.add(null);
            }
        }

        return cellValues;
    }

    public static Object extractCellValue(Cell cell) {

        Object cellValue = null;
        CellType cellType = cell.getCellType();

        if (cellType.equals(CellType.BLANK)) {
            cellValue = null;
        } else if (cellType.equals(CellType.BOOLEAN)) {
            cellValue = Boolean.valueOf(cell.getBooleanCellValue());
        } else if (cellType.equals(CellType.ERROR)) {
            cellValue = Byte.valueOf(cell.getErrorCellValue());
        } else if (cellType.equals(CellType.FORMULA)) {
            cellValue = cell.getCellFormula();
        } else if (cellType.equals(CellType.NUMERIC)) {
            cellValue = BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cellType.equals(CellType.STRING)) {
            cellValue = cell.getStringCellValue();
        }

        return cellValue;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
