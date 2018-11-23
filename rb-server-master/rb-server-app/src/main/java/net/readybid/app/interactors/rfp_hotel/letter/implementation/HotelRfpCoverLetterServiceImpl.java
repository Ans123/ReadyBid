package net.readybid.app.interactors.rfp_hotel.letter.implementation;

import net.readybid.app.interactors.rfp.ParseLetterService;
import net.readybid.app.interactors.rfp_hotel.letter.HotelRfpCoverLetterService;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class HotelRfpCoverLetterServiceImpl implements HotelRfpCoverLetterService {

    private final ParseLetterService parseLetterService;
    private final HotelRfpCoverLetterModelFactory modelFactory;
    private final HotelRfpNamCoverLetterModelFactory namModelFactory;

    @Autowired
    public HotelRfpCoverLetterServiceImpl(
            ParseLetterService parseLetterService,
            HotelRfpCoverLetterModelFactory modelFactory,
            HotelRfpNamCoverLetterModelFactory namModelFactory
    ) {
        this.parseLetterService = parseLetterService;
        this.modelFactory = modelFactory;
        this.namModelFactory = namModelFactory;
    }

    @Override
    public void parseLetters(Rfp rfp) {
        parseHotelCoverLetter(rfp);
        parseNamCoverLetter(rfp);
    }

    @Override
    public void parseLetters(HotelRfpBid bid, boolean showPlaceholders) {
        parseHotelCoverLetter(bid, showPlaceholders);
        parseNamCoverLetter(bid, showPlaceholders);
    }

    private void parseHotelCoverLetter(Rfp rfp) {
        final Map<String, String> model = modelFactory.getWithPlaceholders(rfp);
        final String letter = parseLetterService.parse(rfp.getCoverLetter(), model);
        rfp.setCoverLetter(letter);
    }

    private void parseHotelCoverLetter(HotelRfpBid bid, boolean showPlaceholders) {
        if(bid == null || !Objects.equals(HotelRfpType.DIRECT, bid.getType())) return;
        final Map<String, String> model = showPlaceholders ? modelFactory.getWithPlaceholders(bid) : modelFactory.get(bid);
        final String letter = parseLetterService.parse(bid.getCoverLetter(), model);
        bid.setCoverLetter(letter);
    }

    private void parseNamCoverLetter(Rfp rfp) {
        if(!rfp.isChainSupported()) return;
        final Map<String, String> model = namModelFactory.getWithPlaceholders(rfp);
        final String letter = parseLetterService.parse(rfp.getNamCoverLetter(), model);
        rfp.setNamCoverLetter(letter);
    }

    private void parseNamCoverLetter(HotelRfpBid bid, boolean showPlaceholders) {
        if(bid == null || !Objects.equals(HotelRfpType.CHAIN, bid.getType())) return;
        final Map<String, String> model = showPlaceholders ? namModelFactory.getWithPlaceholders(bid) : namModelFactory.get(bid);
        final String letter = parseLetterService.parse(bid.getCoverLetter(), model);
        bid.setCoverLetter(letter);
    }
}
