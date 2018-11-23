package net.readybid.file_export.excel;

import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.web.RbViewModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class AbstractExcelView implements ExcelView {

    protected final Workbook workbook;
    private final String name;
    protected final CellStyle dateStyle;
    protected final CellStyle dateTimeStyle;
    protected final CellStyle percentageStyle;
    protected final CellStyle amountStyle;
    protected final CellStyle decimalStyle;

    protected AbstractExcelView(String name) {
        this.name = name;
        workbook = new XSSFWorkbook();
        dateStyle = CellStyleFactory.getDateStyle(workbook);
        dateTimeStyle = CellStyleFactory.getDateTimeStyle(workbook);
        percentageStyle = CellStyleFactory.getPercentageStyle(workbook);
        amountStyle = CellStyleFactory.getAmountStyle(workbook);
        decimalStyle = CellStyleFactory.getDecimalStyle(workbook);
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        generate();
        workbook.write(outputStream);
        outputStream.close();
    }

    protected abstract void generate();

    protected static void addRow(Sheet sheet, int rowNumber, List<String> values, CellStyle style) {
        final Row row = sheet.createRow(rowNumber);
        for(int i = 0; i<values.size(); i++){
            addCell(row, i, values.get(i), style);
            sheet.autoSizeColumn(i);
        }
    }

    protected static void addCell(Row row, int position, String value, CellStyle cellStyle) {
        final Cell cell = row.createCell(position);
        cell.setCellValue(value);
        if(null != cellStyle) cell.setCellStyle(cellStyle);
    }

    protected static void addCell(Row row, int position, String value){
        addCell(row, position, value, null);
    }

    protected void setCellValue(Cell cell, Object response) {
        if(response instanceof Double){
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((double) response);
        } else if(response instanceof Long){
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((double) (long) response);
        } else if (response instanceof Date){
            final Date date = (Date) response;
            cell.setCellValue(date);
            cell.setCellStyle(dateStyle);
        } else if (response instanceof LocalDate){
            final LocalDate date = (LocalDate) response;
            cell.setCellValue(date.toString());
        } else if(response != null){
            cell.setCellType(CellType.STRING);
            cell.setCellValue(String.valueOf(response));
        }
    }

    protected List<Map<String, Object>> readViewModel(RbViewModel viewModel) {
        try {
            //noinspection unchecked
            return (List<Map<String, Object>>) viewModel.getData();
        } catch (ClassCastException cce){
            throw new UnableToCompleteRequestException("Cannot read ViewModel");
        }
    }
}
