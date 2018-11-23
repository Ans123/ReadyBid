package net.readybid.file_export.excel;

import org.apache.poi.ss.usermodel.*;

public class CellStyleFactory {

    public static CellStyle getHeaderStyle(Workbook workbook) {
        final CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        final Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return headerStyle;
    }

    public static CellStyle getDateStyle(Workbook workbook) {
        return getCellFormat(workbook, "m/d/yy");
    }

    public static CellStyle getDateTimeStyle(Workbook workbook) {
        return getCellFormat(workbook, "m/d/yy HH:mm:ss");
    }

    public static CellStyle getPercentageStyle(Workbook workbook) {
        return getCellFormat(workbook, "0.00%");
    }

    public static CellStyle getAmountStyle(Workbook workbook) {
        return getCellFormat(workbook, "$0.00");
    }

    public static CellStyle getDecimalStyle(Workbook workbook) {
        return getCellFormat(workbook, "0.0000");
    }

    private static CellStyle getCellFormat(Workbook workbook, String format) {
        final CreationHelper createHelper = workbook.getCreationHelper();
        final CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(
                createHelper.createDataFormat().getFormat(format));
        return dateStyle;
    }
}
