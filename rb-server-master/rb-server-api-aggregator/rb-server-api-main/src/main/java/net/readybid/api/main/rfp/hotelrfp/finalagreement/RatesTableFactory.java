package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;

import java.util.List;

public interface RatesTableFactory {
    String create(QuestionnaireConfigurationItem ratesTableConfig, List<String> acceptedRates, QuestionnaireResponse response);
}
