package net.readybid.api.rfp_hotel.bid.export;

import net.readybid.api.main.rfp.hotelrfp.GBTAQuestionnaire;
import net.readybid.api.main.rfp.questionnaire.Question;
import net.readybid.app.use_cases.rfp_hotel.bid.implementation.reports.HotelRfpFinalAgreementReport;
import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.file_export.excel.AbstractExcelView;
import net.readybid.file_export.excel.ExcelView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.readybid.file_export.excel.CellStyleFactory.getHeaderStyle;


public class FinalAgreementsExportExcelView extends AbstractExcelView implements ExcelView {

    private static final List<Question> QUESTIONS = GBTAQuestionnaire.get();
    private final List<HotelRfpFinalAgreementReport> bids;
    private CellStyle headerStyle;

    FinalAgreementsExportExcelView(List<HotelRfpFinalAgreementReport> bids) {
        this("final_agreements", bids);
    }

    private FinalAgreementsExportExcelView(String name, List<HotelRfpFinalAgreementReport> bids) {
        super(name);
        headerStyle = getHeaderStyle(workbook);
        this.bids = bids;
    }

    protected void generate() {
        createGbtaAnswersSheet();
        createRateLoadingInformationSheets();
    }

    private void createRateLoadingInformationSheets() {
        final List<String> buyerCompaniesAlreadyAdded = new ArrayList<>();
        for(HotelRfpFinalAgreementReport bid : bids){
            if(!buyerCompaniesAlreadyAdded.contains(bid.buyerCompanyAccountId)){
                createRateLoadingInformationSheet(bid);
                buyerCompaniesAlreadyAdded.add(bid.buyerCompanyAccountId);
            }
        }
    }

    private void createRateLoadingInformationSheet(HotelRfpFinalAgreementReport bid) {
        final String sheetName = String.format("RLI - %s", bid.buyerCompanyName);
        final Sheet rateLoadingInformationSheet = workbook.createSheet(sheetName);

        int rateLoadingInstructionsRowNumber = 0;

        addRow(rateLoadingInformationSheet,
                rateLoadingInstructionsRowNumber++,
                Arrays.asList("ARC/IATA#s", "Pseudo City Code", "GDS Name", "Rate Access Code"),
                headerStyle);

        if(bid.rateLoadingInformation != null && bid.rateLoadingInformation.size() != 0) {
            for (int i = 0; i < bid.rateLoadingInformation.size(); i++) {
                final Row instructions = rateLoadingInformationSheet.createRow(rateLoadingInstructionsRowNumber + i);
                final RateLoadingInformation rateLoadingInformation = bid.rateLoadingInformation.get(i);
                addCell(instructions, 0, rateLoadingInformation.getArcIatas());
                addCell(instructions, 1, rateLoadingInformation.getPseudoCityCode());
                addCell(instructions, 2, rateLoadingInformation.getGdsName());
                addCell(instructions, 3, rateLoadingInformation.getRateAccessCode());
            }
        }
    }

    private void createGbtaAnswersSheet() {
        final Sheet gbtaSheet = workbook.createSheet("GBTA");
        final Row header = gbtaSheet.createRow(0);

        for(int i = 0; i< QUESTIONS.size(); i++){
            addCell(header, i, QUESTIONS.get(i).getId(), headerStyle);
            gbtaSheet.autoSizeColumn(i);
        }

        for(int i = 0, l = bids.size(); i < l ; i++){
            final Row gbtaDataRow = gbtaSheet.createRow(i+1);
            createGbtaAnswerRow(gbtaDataRow, bids.get(i).responses);
        }
    }

    private void createGbtaAnswerRow(Row gbtaRow, Map<String, String> questionnaire) {
        for (int j = 0; j < QUESTIONS.size(); j++) {
            final Cell cell = gbtaRow.createCell(j);
            final Question question = QUESTIONS.get(j);
            final Object answer = question.parse(questionnaire.get(question.getId()));
            setCellValue(cell, answer);
        }
    }
}