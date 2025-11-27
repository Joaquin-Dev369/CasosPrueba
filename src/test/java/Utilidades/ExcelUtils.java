package Utilidades;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public class ExcelUtils {
    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell Cell;
    private static XSSFRow Row;
    private static String FilePath; // Guardamos la ruta para guardar después

    // 1. Configurar la ruta del archivo y la hoja
    public static void setExcelFileSheet(String Path, String SheetName) throws Exception {
        try {
            FilePath = Path;
            FileInputStream ExcelFile = new FileInputStream(Path);
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
        } catch (Exception e) {
            throw (e);
        }
    }

    // 2. Leer datos de una celda específica (MEJORADO PARA EVITAR NULL POINTER)
    public static String getCellData(int RowNum, int ColNum) throws Exception {
        try {
            Row = ExcelWSheet.getRow(RowNum);
            if (Row == null) {
                return ""; // Si la fila no existe, devolvemos vacío en vez de error
            }

            Cell = Row.getCell(ColNum, MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (Cell == null) {
                return ""; // Si la celda está vacía, devolvemos texto vacío
            }

            String CellData = "";
            try {
                CellData = Cell.getStringCellValue();
            } catch (Exception e) {
                // Si es numérico (ej: cuenta o monto), lo convertimos a texto sin decimales .0
                double numericData = Cell.getNumericCellValue();
                long longData = (long) numericData;
                CellData = String.valueOf(longData);
            }
            return CellData;
        } catch (Exception e) {
            return ""; // En caso de cualquier otro error, no rompemos el test
        }
    }

    // 3. Escribir resultados en el Excel
    public static void setCellData(String Result, int RowNum, int ColNum) throws Exception {
        try {
            Row = ExcelWSheet.getRow(RowNum);
            if (Row == null) {
                Row = ExcelWSheet.createRow(RowNum);
            }

            Cell = Row.getCell(ColNum);
            if (Cell == null) {
                Cell = Row.createCell(ColNum);
            }

            Cell.setCellValue(Result);

            FileOutputStream fileOut = new FileOutputStream(FilePath);
            ExcelWBook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            // Ignoramos error de escritura para no detener el reporte
            System.err.println("No se pudo escribir en el Excel (quizás está abierto): " + e.getMessage());
        }
    }
}