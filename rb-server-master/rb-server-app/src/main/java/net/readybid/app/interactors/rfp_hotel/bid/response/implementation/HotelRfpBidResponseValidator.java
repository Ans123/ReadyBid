package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
class HotelRfpBidResponseValidator {

    private static final List<String> RATE_QUESTIONS = Collections.unmodifiableList(Arrays.asList("DYNAMIC_PCT_Discount", "LRA_S1_RT1_SGL", "LRA_S1_RT1_DBL", "NLRA_S1_RT1_SGL", "NLRA_S1_RT1_DBL", "GOVT_S1_RT1_SGL", "GOVT_S1_RT1_DBL"));

    List<String> parseAndValidate(QuestionnaireForm form, Map<String, String> answers, HotelRfpBid bid, boolean isDraft) {
        final Map<String, String> responseContext = generateResponseContext(bid);
        form.addDefaultAnswers(answers, responseContext);
        if(!isDraft) setResponseSubmissionDate(answers);
        form.parseAnswers(answers);

        final List<String> errors = form.getErrors(answers, responseContext);
        errors.addAll(doGlobalValidation(answers));
        return errors;
    }

    private static void setResponseSubmissionDate(Map<String, String> answers) {
        answers.put("RFP_DATESUBMIT", LocalDate.now().toString());
    }

    private static Map<String, String> generateResponseContext(HotelRfpBid bid) {
        final Map<String, String> context = new HashMap<>();
        context.put("programStartDate", String.valueOf(bid.getProgramStartDate()));
        context.put("programEndDate", String.valueOf(bid.getProgramEndDate()));
        return context;
    }

    private static List<String> doGlobalValidation(Map<String, String> answers) {
        final List<String> errors = new ArrayList<>();
        isAnyRatePresentCheck(errors, answers);
        return errors;
    }

    private static void isAnyRatePresentCheck(List<String> errors, Map<String, String> answers) {
        if(RATE_QUESTIONS.stream().map(answers::get).anyMatch(answer -> !(answer == null || answer.isEmpty()))) {
            return;
        }
        errors.add("No Rate Found");
    }
}
