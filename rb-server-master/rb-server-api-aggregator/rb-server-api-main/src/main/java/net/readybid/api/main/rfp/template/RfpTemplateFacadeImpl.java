package net.readybid.api.main.rfp.template;

import net.readybid.api.main.rfp.core.RfpService;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.interactors.rfp_hotel.letter.HotelRfpCoverLetterService;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.core.EntityRepository;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfp.template.RfpTemplateListItemViewModel;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class RfpTemplateFacadeImpl implements RfpTemplateFacade {

    private final RfpTemplateRepository rfpTemplatesRepository;
    private final RfpService rfpService;
    private final HotelRfpCoverLetterService coverLetterService;
    private final EntityRepository entityRepository;

    @Autowired
    public RfpTemplateFacadeImpl(
            RfpTemplateRepository rfpTemplatesRepository,
            RfpService rfpService,
            HotelRfpCoverLetterService coverLetterService,
            EntityRepository entityRepository
    ) {
        this.rfpTemplatesRepository = rfpTemplatesRepository;
        this.rfpService = rfpService;
        this.coverLetterService = coverLetterService;
        this.entityRepository = entityRepository;
    }

    @Override
    public ListResult<RfpTemplateListItemViewModel> listRfpTemplates(String rfpType) {
        return rfpTemplatesRepository.listRfpTemplates(rfpType);
    }

    @Override
    public RfpTemplate getRfpTemplatePreview(String id, AuthenticatedUser user) {
        final RfpTemplate template = rfpTemplatesRepository.getRfpTemplate(id);
        final Account account = user.getAccount();
        final Entity entity = entityRepository.findByIdIncludingUnverified(account.getType(), String.valueOf(account.getEntityId()));
        final Rfp rfp = rfpService.previewRfpTemplate(template, user, entity);

        coverLetterService.parseLetters(rfp);
        template.setCoverLetter(rfp.getCoverLetter());
        template.setQuestionnaire(rfp.getQuestionnaire());
        return template;
    }
}
