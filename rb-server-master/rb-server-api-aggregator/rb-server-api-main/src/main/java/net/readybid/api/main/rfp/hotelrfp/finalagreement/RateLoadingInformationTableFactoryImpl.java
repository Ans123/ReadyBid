package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.entities.rfp.RateLoadingInformationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.LinkedList;

@Service
public class RateLoadingInformationTableFactoryImpl implements RateLoadingInformationTableFactory {

    private final TemplateEngine templateEngine;

    @Autowired
    public RateLoadingInformationTableFactoryImpl(
            TemplateEngine templateEngine
    ) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String create(RateLoadingInformationList rateLoadingInformationList) {
        final Context ctx = new Context();
        ctx.setVariable("list", createList(rateLoadingInformationList));
        return templateEngine.process("letter-components/final-agreement-rate-loading-information", ctx);

    }

    private LinkedList<RateLoadingInformation> createList(RateLoadingInformationList rateLoadingInformationList) {
        final LinkedList<RateLoadingInformation> ll = new LinkedList<>();
        for(int i = 0; i< rateLoadingInformationList.getRateLoadingInformationSize(); i++){
            ll.add(rateLoadingInformationList.getRateLoadingInformation(i));
        }
        return ll;
    }
}
