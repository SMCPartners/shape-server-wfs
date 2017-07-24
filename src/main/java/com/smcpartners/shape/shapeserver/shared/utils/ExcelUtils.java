package com.smcpartners.shape.shapeserver.shared.utils;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/21/17
 */
public class ExcelUtils {

    /**
     * Convert the double value in a cell to an int
     *
     * @param row
     * @param cell
     * @return
     * @throws Exception - If the given cell can't be converted to an int
     */
    public static int convertToInt(Sheet sheet, int row, int cell) throws Exception {
        double doubleValue = sheet.getRow(row).getCell(cell).getNumericCellValue();
        return (new Double(doubleValue)).intValue();
    }
}
