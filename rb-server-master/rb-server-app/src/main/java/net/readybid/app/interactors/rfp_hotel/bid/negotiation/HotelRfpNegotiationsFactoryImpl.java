package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsConfig;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 7/26/2017.
 *
 */
@Service
public class HotelRfpNegotiationsFactoryImpl implements HotelRfpNegotiationsFactory {

    private final HotelRfpNegotiationsConfigFactory configFactory;
    private final HotelRfpNegotiationFactory hotelRfpNegotiationFactory;

    @Autowired
    public HotelRfpNegotiationsFactoryImpl(
            HotelRfpNegotiationsConfigFactory configFactory,
            HotelRfpNegotiationFactory hotelRfpNegotiationFactory
    ) {
        this.configFactory = configFactory;
        this.hotelRfpNegotiationFactory = hotelRfpNegotiationFactory;
    }

    @Override
    public HotelRfpNegotiationsImpl createNegotiations(Map<String, String> answers, AuthenticatedUser currentUser) {
        final HotelRfpNegotiationsImpl negotiations = new HotelRfpNegotiationsImpl();
        final HotelRfpNegotiationsConfig config = configFactory.create(answers);

        negotiations.setConfig(config);
        negotiations.setCommunication(createCommunication(config, answers, currentUser));
        return negotiations;
    }

    private List<HotelRfpNegotiation> createCommunication(
            HotelRfpNegotiationsConfig config,
            Map<String, String> response,
            AuthenticatedUser currentUser
    ) {
        final List<HotelRfpNegotiation> communication = new ArrayList<>();
        communication.add(hotelRfpNegotiationFactory.create(config, response, currentUser));
        return communication;
    }
}
