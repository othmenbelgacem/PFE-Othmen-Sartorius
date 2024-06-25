package com.sartorius.tma.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellType;


/**
 * @author Rokaya
 * @Date 25/09/2022
 */
public class ImportUtils {
  public static boolean isRowEmpty(Row row) {
    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
      Cell cell = row.getCell(c);
      if (cell != null && cell.getCellType() != CellType.BLANK)
        return false;
    }
    return true;
  }
}
