package net.readybid.app.entities.rfp_hotel;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.entities.IBuilder;
import net.readybid.entities.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HotelRfpDefaultResponse {

    public final Id hotelId;
    public final Map<String, String> answers;
    public final List<QuestionnaireConfigurationItem> state;
    public final boolean isValid;

    public HotelRfpDefaultResponse(Id hotelId, QuestionnaireResponse response) {
        this.hotelId = hotelId;
        if (response == null) {
            answers = null;
            state = null;
            isValid = false;
        } else {
            answers = Collections.unmodifiableMap(response.getAnswers());
            state = Collections.unmodifiableList(response.getState());
            isValid = response.isValid();
        }
    }

    public HotelRfpDefaultResponse(Id hotelId, Map<String, String> answers) {
        this(hotelId, answers, new ArrayList<>(), false);
    }

    private HotelRfpDefaultResponse(Id hotelId, Map<String, String> answers, List<QuestionnaireConfigurationItem> state, boolean isValid) {
        this.hotelId = hotelId;
        this.answers = answers == null ? null : Collections.unmodifiableMap(answers);
        this.state = state == null ? null : Collections.unmodifiableList(state);
        this.isValid = isValid;
    }

    public static class Builder implements IBuilder<HotelRfpDefaultResponse> {

        private Map<String, String> answers;
        private boolean isValid;
        private List<QuestionnaireConfigurationItem> state;
        private Id hotelId;

        @Override
        public HotelRfpDefaultResponse build() {
            return new HotelRfpDefaultResponse(hotelId, answers, state, isValid);
        }

        public void setHotelId(Id hotelId) {
            this.hotelId = hotelId;
        }

        public void setAnswers(Map<String, String> answers) {
            this.answers = answers;
        }

        public void setIsValid(boolean isValid) {
            this.isValid = isValid;
        }

        public void setState(List<QuestionnaireConfigurationItem> state) {
            this.state = state;
        }
    }
}
