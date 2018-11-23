package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.api.main.rfp.letter.AnswersHelper;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BlackoutDatesTableFactoryImpl implements BlackoutDatesTableFactory {

    private static final String BD_START = "BD%s_START";
    private static final String BD_END = "BD%s_END";

    private final AnswersHelper placeholderFactory;
    private final TemplateEngine templateEngine;

    @Autowired
    public BlackoutDatesTableFactoryImpl(
            AnswersHelper placeholderFactory,
            TemplateEngine templateEngine
    ) {
        this.placeholderFactory = placeholderFactory;
        this.templateEngine = templateEngine;
    }

    @Override
    public String create(QuestionnaireResponse response) {
        final Context ctx = new Context();
        ctx.setVariable("periods", readBlackoutPeriods(response.getAnswers()));
        return templateEngine.process("letter-components/final-agreement-blackout-dates", ctx);
    }

    private List<String> readBlackoutPeriods(Map<String, String> answers) {
        final List<String> periods = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            if(!answers.containsKey(String.format(BD_START, i))) break;
            final String period = placeholderFactory.createPeriod(
                    answers.get(String.format(BD_START, i)), answers.get(String.format(BD_END, i)));
            if(period == null){
                break;
            } else {
                periods.add(period);
            }
        }
        if(periods.isEmpty()) periods.add("None");
        return periods;
    }
}
