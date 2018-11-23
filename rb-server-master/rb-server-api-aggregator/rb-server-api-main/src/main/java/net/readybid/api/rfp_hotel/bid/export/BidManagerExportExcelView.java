package net.readybid.api.rfp_hotel.bid.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.readybid.app.use_cases.rfp_hotel.bid.HotelRfpBidReportFactory;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.exceptions.BadRequestException;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.file_export.excel.AbstractExcelView;
import net.readybid.file_export.excel.CellStyleFactory;
import net.readybid.file_export.excel.ExcelView;
import net.readybid.utils.RbMapUtils;
import net.readybid.web.RbViewModel;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class BidManagerExportExcelView extends AbstractExcelView implements ExcelView {

    private final List<String> bidsIds;
    private final List<Column> columns;
    private AuthenticatedUser currentUser;
    private final HotelRfpBidReportFactory reportFactory;
    private final ObjectMapper mapper;

    private BidManagerExportExcelView(Builder builder) {
        super("bid-manager");
        this.reportFactory = builder.reportFactory;
        this.mapper = builder.mapper;
        this.bidsIds = builder.bidsIds;
        columns = builder.columns;
        this.currentUser = builder.currentUser;
    }

    @Override
    protected void generate() {
        final RbViewModel vm = reportFactory.getBidManagerFields(bidsIds, getListOfFields(), currentUser);
        final List<Map<String, Object>> data = readViewModel(vm);
        final Sheet sheet = workbook.createSheet("Bid Manager");
        addHeader(sheet);
        addData(sheet, data);
    }

    protected List<Map<String, Object>> readViewModel(RbViewModel viewModel) {
        try {
            //noinspection unchecked
            return (List<Map<String, Object>>) mapper.readValue(mapper.writeValueAsString(viewModel.getData()), List.class);
        } catch (ClassCastException | IOException cce){
            throw new UnableToCompleteRequestException("Cannot read ViewModel");
        }
    }

    private void addData(Sheet sheet, List<Map<String, Object>> data) {
        int rowNumber = 1;
        for(Map<String, Object> d : data){
            final Row row = sheet.createRow(rowNumber++);
            addDataRow(row, d);
        }
    }

    private void addDataRow(Row row, Map<String, Object> d) {
        for (int j = 0; j < columns.size(); j++) {
            final Cell cell = row.createCell(j);
            final Column column = columns.get(j);
            final Object value = RbMapUtils.getObject(d, column.field);
            column.setCell(this, cell, value);
        }
    }

    private void addHeader(Sheet sheet) {
        final CellStyle headerStyle = CellStyleFactory.getHeaderStyle(workbook);
        final Row header = sheet.createRow(0);
        for(int i = 0; i< columns.size(); i++){
            addCell(header, i, columns.get(i).label, headerStyle);
            sheet.autoSizeColumn(i);
        }
    }

    private List<String> getListOfFields() {
        return columns.stream().map(c -> c.field).collect(Collectors.toList());
    }

    private static class Column {
        final String label;
        final String type;
        final String field;

        Column(Map<String, String> rawColumnData) {
            this.label = rawColumnData.get("label");
            this.type = rawColumnData.get("type");
            this.field = rawColumnData.get("field");
        }

        void setCell(BidManagerExportExcelView view, Cell cell, Object value) {
            final RbCell rbCell = new RbCell(view, cell);
            rbCell.set(type, value);
        }
    }

    private static class RbCell {
        private BidManagerExportExcelView view;
        private Cell cell;

        RbCell(BidManagerExportExcelView view, Cell cell) {
            this.view = view;
            this.cell = cell;
        }


        void set(String type, Object value) {
            if(value == null) {
                setString(null);
            } else {
                switch (type){
                    case "Integer":
                        setInteger(value);
                        break;
                    case "Decimal":
                        setDouble(value, view.decimalStyle);
                        break;
                    case "Percentage":
                        setDouble(value, view.percentageStyle);
                        break;
                    case "Amount":
                        setDouble(value, view.amountStyle);
                        break;
                    case "Rate":
                    case "Amenity":
                        setAmenity(value);
                        setAmenity(value);
                        break;
                    case "Date":
                        setDate(value);
                        break;
                    case "DateTime":
                        setDateTime(value);
                        break;
                    case "String":
                    default:
                        setString(value);
                        break;
                }
            }
        }

        private void setDateTime(Object value) {
            try {
                Date date;
                if(value instanceof Long){
                    date = new Date((long) value);
                } else {
                    date = new Date(Long.parseLong(String.valueOf(value)));
                }
                cell.setCellValue(date);
                cell.setCellStyle(view.dateTimeStyle);
            } catch (NumberFormatException nfe){
                setString(value);
            }
        }

        private void setDate(Object value) {
            try {
                final LocalDate date = LocalDate.parse(String.valueOf(value));
                cell.setCellValue(date.toString());
                cell.setCellStyle(view.dateStyle);
            } catch (DateTimeParseException dtpe){
                setString(value);
            }
        }

        private void setAmenity(Object o) {
            try {
                if(o == null) return;
                final Map value = (Map) o;
                if(Boolean.parseBoolean(String.valueOf(value.get("isIncluded")))){
                    setString("inc");
                } else {
                    final String type = String.valueOf(value.get("type"));
                    final Object amount = value.get("amount");
                    switch (type){
                        case "PERCENTAGE":
                            setDouble(amount, view.percentageStyle);
                            break;
                        case "FIXED":
                            setDouble(amount, view.amountStyle);
                            break;
                    }
                }
            } catch (ClassCastException ignore){}
        }

        private void setString(Object s) {
            if(s != null){
                cell.setCellType(CellType.STRING);
                cell.setCellValue(String.valueOf(s));
            }
        }

        private void setInteger(Object value) {
            try {
                final long l = Long.parseLong(String.valueOf(value), 10);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue((double) (long) l);
            } catch (NumberFormatException nfe){
                setString(value);
            }
        }

        private void setDouble(Object value, CellStyle decimalStyle) {
            try {
                final double d = Double.parseDouble(String.valueOf(value));
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(decimalStyle);
                cell.setCellValue(d);
            } catch (NumberFormatException nfe){
                setString(value);
            }
        }
    }

    static class Builder {
        private List<String> bidsIds;
        private List<Column> columns;
        private HotelRfpBidReportFactory reportFactory;
        private ObjectMapper mapper;
        private AuthenticatedUser currentUser;

        public Builder setBidsIds(List<String> bidsIds) {
            this.bidsIds = bidsIds;
            return this;
        }

        Builder setOptions(Map<String, Object> options) {
            try{
                //noinspection unchecked
                final List<Map<String, String>> rawColumnsList = (List<Map<String, String>>) options.get("columns");
                this.columns = rawColumnsList.stream().map(Column::new).collect(Collectors.toList());
            } catch (Exception e){
                throw new BadRequestException("Cannot load Columns");
            }
            return this;
        }

        Builder setReportFactory(HotelRfpBidReportFactory reportFactory) {
            this.reportFactory = reportFactory;
            return this;
        }

        Builder setMapper(ObjectMapper mapper) {
            this.mapper = mapper;
            return this;
        }

        public Builder setCurrentUser(AuthenticatedUser currentUser) {
            this.currentUser = currentUser;
            return this;
        }

        BidManagerExportExcelView createBidManagerExportExcelView() {
            return new BidManagerExportExcelView(this);
        }
    }
}
