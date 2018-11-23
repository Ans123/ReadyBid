package net.readybid.api.rfp_hotel.bid.export;

import net.readybid.api.main.rfp.hotelrfp.GBTAQuestionnaire;
import net.readybid.api.main.rfp.questionnaire.Question;
import net.readybid.file_export.csv.CsvView;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.web.RbViewModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

class GbtaExportCsvView implements CsvView {

    private static final List<Question> QUESTIONS = GBTAQuestionnaire.get();
    private static final String SEPARATOR = ",";
    private static final String LINE_SEPARATOR = "\r\n";
    private RbViewModel viewModel;
    private StringBuilder response;

    GbtaExportCsvView(RbViewModel viewModel) {
        this.viewModel = viewModel;
        response = new StringBuilder("");
    }

    @Override
    public String getFileName() {
        return "responses-gbta";
    }

    @Override
    public void write(OutputStream out) throws IOException {
        final PrintWriter writer = new PrintWriter(out);
        generateCsv();
        writer.write(response.toString());
        writer.close();
    }

    private void generateCsv() {
        final List<Map<String, String>> responses = readResponses(viewModel);
        addHeader();
        addResponses(responses);
    }

    private List<Map<String, String>> readResponses(RbViewModel viewModel) {
        try {
            //noinspection unchecked
            return (List<Map<String, String>>) viewModel.getData();
        } catch (ClassCastException cce){
            throw new UnableToCompleteRequestException("Cannot load responses");
        }
    }

    private void addHeader() {
        for(int i = 0; i< QUESTIONS.size(); i++){
            if(i != 0) response.append(SEPARATOR);
            response.append(QUESTIONS.get(i).getId());
        }
        response.append(LINE_SEPARATOR);
    }

    private void addResponses(List<Map<String, String>> responses) {
        for(Map<String, String> response : responses){
            addResponse(response);
            this.response.append(LINE_SEPARATOR);
        }
    }

    private void addResponse(Map<String, String> response) {
        for (int j = 0; j < QUESTIONS.size(); j++) {
            if(j != 0) this.response.append(SEPARATOR);

            final String answer = response.get(QUESTIONS.get(j).getId());
            if(answer != null)
                this.response.append(answer);
        }
    }
}
