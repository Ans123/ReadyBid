package net.readybid.app.persistence.mongodb.app.rfp;

import net.readybid.app.entities.rfp.LetterTemplate;
import net.readybid.app.interactors.rfp.gate.LetterTemplateLibrary;
import net.readybid.app.persistence.mongodb.repository.LetterTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Projections.include;
import static net.readybid.app.persistence.mongodb.repository.mapping.LetterTemplateCollection.HOTEL_RFP_CHAIN_COVER_LETTER_ID;
import static net.readybid.app.persistence.mongodb.repository.mapping.LetterTemplateCollection.TEMPLATE;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
public class LetterTemplateLibraryImpl implements LetterTemplateLibrary {

    private final LetterTemplateRepository repository;

    @Autowired
    public LetterTemplateLibraryImpl(LetterTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getHotelRfpChainCoverLetter() {
        final LetterTemplate letterTemplate = repository.findOne(byId(HOTEL_RFP_CHAIN_COVER_LETTER_ID), include(TEMPLATE));
        return letterTemplate == null ? null : letterTemplate.getTemplate();
    }
}
