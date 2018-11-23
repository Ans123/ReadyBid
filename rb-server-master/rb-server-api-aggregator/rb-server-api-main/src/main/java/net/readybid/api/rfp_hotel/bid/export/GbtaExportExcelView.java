package net.readybid.api.rfp_hotel.bid.export;

import net.readybid.api.main.rfp.hotelrfp.GBTAQuestionnaire;
import net.readybid.api.main.rfp.questionnaire.Question;
import net.readybid.api.main.rfp.questionnaire.parsers.QuestionnaireAnswerParseException;
import net.readybid.file_export.excel.AbstractExcelView;
import net.readybid.file_export.excel.CellStyleFactory;
import net.readybid.file_export.excel.ExcelView;
import net.readybid.web.RbViewModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

class GbtaExportExcelView extends AbstractExcelView implements ExcelView {

    private static final List<Question> QUESTIONS = GBTAQuestionnaire.get();
    private final RbViewModel viewModel;

    GbtaExportExcelView(RbViewModel viewModel) {
        super("gbta-responses");
        this.viewModel = viewModel;
    }

    protected void generate() {
        createGbtaAnswersSheet();
    }

    private void createGbtaAnswersSheet() {
        final List<Map<String, Object>> responses = readViewModel(viewModel);
        final Sheet gbtaSheet = workbook.createSheet("GBTA Answers");
        addHeader(gbtaSheet);
        addResponses(gbtaSheet, responses);
    }

    private void addResponses(Sheet gbtaSheet, List<Map<String, Object>> responses) {
        int rowNumber = 1;
        for(Map<String, Object> response : responses){
            final Row gbtaDataRow = gbtaSheet.createRow(rowNumber++);
            addResponse(gbtaDataRow, response);
        }
    }

    private void addResponse(Row gbtaDataRow, Map<String, Object> response) {
        for (int j = 0; j < QUESTIONS.size(); j++) {
            final Cell cell = gbtaDataRow.createCell(j);
            final Question question = QUESTIONS.get(j);
            final Object answer = response.get(question.getId());
            try {
                final Object parsedAnswer = question.parse(answer);
                setCellValue(cell, parsedAnswer);
            } catch (QuestionnaireAnswerParseException ex){
                setCellValue(cell, String.valueOf(answer));
            }
        }
    }

    private void addHeader(Sheet gbtaSheet) {
        final CellStyle headerStyle = CellStyleFactory.getHeaderStyle(workbook);
        final Row header = gbtaSheet.createRow(0);
        for(int i = 0; i< QUESTIONS.size(); i++){
            addCell(header, i, QUESTIONS.get(i).getId(), headerStyle);
            gbtaSheet.autoSizeColumn(i);
        }
    }
}
