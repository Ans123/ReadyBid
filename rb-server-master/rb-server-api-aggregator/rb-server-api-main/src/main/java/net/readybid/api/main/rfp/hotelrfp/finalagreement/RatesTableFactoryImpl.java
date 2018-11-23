package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.api.main.rfp.letter.AnswersHelper;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class RatesTableFactoryImpl implements RatesTableFactory {

    private final AnswersHelper placeholderFactory;
    private final TemplateEngine templateEngine;

    @Autowired
    public RatesTableFactoryImpl(
            AnswersHelper placeholderFactory,
            TemplateEngine templateEngine
    ) {
        this.placeholderFactory = placeholderFactory;
        this.templateEngine = templateEngine;
    }

    @Override
    public String create(QuestionnaireConfigurationItem ratesTableConfig, List<String> acceptedRates, QuestionnaireResponse response) {
        final Context ctx = new Context();
        ctx.setVariable("rates", readRates(ratesTableConfig, acceptedRates, response));
        return templateEngine.process("letter-components/final-agreement-rates", ctx);
    }

    private RatesTableData readRates(QuestionnaireConfigurationItem config, List<String> acceptedRates, QuestionnaireResponse response) {
        final RatesTableData placeholder = new RatesTableData(placeholderFactory);
        placeholder.readResponse(config, acceptedRates, response);
        return placeholder;
    }
}
