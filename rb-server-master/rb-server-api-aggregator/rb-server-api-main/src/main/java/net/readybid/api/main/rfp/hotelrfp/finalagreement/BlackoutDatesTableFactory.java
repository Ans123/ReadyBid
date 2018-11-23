package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;

public interface BlackoutDatesTableFactory {

    String create(QuestionnaireResponse answers);
}
