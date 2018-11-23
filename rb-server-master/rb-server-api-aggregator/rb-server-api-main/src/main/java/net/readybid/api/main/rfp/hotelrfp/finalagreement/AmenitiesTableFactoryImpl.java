package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.api.main.rfp.letter.AnswersHelper;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AmenitiesTableFactoryImpl implements AmenitiesTableFactory {

    private final AnswersHelper placeholderFactory;
    private final TemplateEngine templateEngine;

    @Autowired
    public AmenitiesTableFactoryImpl(
            AnswersHelper placeholderFactory,
            TemplateEngine templateEngine
    ) {
        this.placeholderFactory = placeholderFactory;
        this.templateEngine = templateEngine;
    }

    @Override
    public String create(QuestionnaireResponse response) {
        final Context ctx = new Context();
        ctx.setVariable("amenities", readAmenities(response.getAnswers()));
        return templateEngine.process("letter-components/final-agreement-amenities", ctx);
    }

    private Map<String, AmenitiesTablePlaceholderAmenity> readAmenities(Map<String, String> answers) {
        final Map<String, AmenitiesTablePlaceholderAmenity> amenities = new LinkedHashMap<>();
        amenities.put("breakfast", readBreakfast(answers));
        amenities.put("internet", readInternet(answers));
        amenities.put("parking", readParking(answers));
        amenities.put("airportShuttle", readAirportShuttle(answers));
        amenities.put("fitness", readFitness(answers));
        amenities.put("earlyCheckout", readEarlyCheckout(answers));
        amenities.put("wifi", readWifi(answers));
        amenities.put("cancelationPolicy", readCancelationPolicy(answers));
        return amenities;
    }

    private AmenitiesTablePlaceholderAmenity readCancelationPolicy(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "Cancelation Policy";
        amenity.exists = answers.containsKey("CANC_POL");
        if(amenity.exists){
            amenity.isIncluded = false;
            amenity.amount = answers.get("CANC_POL");
        }
        return amenity;

    }

    private AmenitiesTablePlaceholderAmenity readBreakfast(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "Breakfast";
        amenity.exists = answers.containsKey("BREAK_INCLUDE");
        if(amenity.exists){
            amenity.isIncluded = placeholderFactory.readYesNo(answers.get("BREAK_INCLUDE"));
            if(!amenity.isIncluded) amenity.exists = false;
            amenity.amount = placeholderFactory.readAmount(answers.get("BREAK_FEE"), "");
        }
        return amenity;
    }

    private AmenitiesTablePlaceholderAmenity readInternet(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "High Speed Internet";
        amenity.exists = answers.containsKey("HIGHSPEED_INROOM") && placeholderFactory.readYesNo(answers.get("HIGHSPEED_INROOM"));
        if(amenity.exists){
            amenity.isIncluded = placeholderFactory.readYesNo(answers.get("HSIA_INCLUDE"));
            amenity.amount = placeholderFactory.readAmount(answers.get("HSIA_FEE"), "");
        }
        return amenity;
    }

    private AmenitiesTablePlaceholderAmenity readParking(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "Parking";
        amenity.exists = answers.containsKey("PARKATTEND");
        if(amenity.exists){
            amenity.isIncluded = placeholderFactory.readAsFalse(answers.get("PARKATTEND"), "None Available");
            if(!amenity.isIncluded){
                amenity.amount = placeholderFactory.readAmount(answers.get("PARK_FEE"), "");
                amenity.isIncluded = placeholderFactory.readYesNo(answers.get("PARK_INCLUDE"));
            }
        }
        return amenity;
    }

    private AmenitiesTablePlaceholderAmenity readAirportShuttle(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "Airport Shuttle";
        amenity.exists = answers.containsKey("AIRTRANS_FEE");
        if(amenity.exists){
            amenity.isIncluded = placeholderFactory.readYesNo(answers.get("AIRTRANS_INCLUDE"));
            amenity.amount = placeholderFactory.readAmount(answers.get("AIRTRANS_FEE"), "");
        }
        return amenity;
    }

    private AmenitiesTablePlaceholderAmenity readFitness(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "Fitness Center";
        amenity.exists = answers.containsKey("FITON_CENT") && placeholderFactory.readYesNo(answers.get("FITON_CENT"));
        if(amenity.exists){
            amenity.isIncluded = placeholderFactory.readYesNo(answers.get("FITNESS_INCLUDE_ON"));
            amenity.amount = placeholderFactory.readAmount(answers.get("FITNESS_FEE_ON"), "");
        }
        return amenity;
    }

    private AmenitiesTablePlaceholderAmenity readEarlyCheckout(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "Early Checkout Rate";
        amenity.exists = false;

        if(answers.containsKey("EARLYCK_FEE")){
            final String amount = placeholderFactory.readAmount(answers.get("EARLYCK_FEE"), "$0.00");
            amenity.exists = !amount.equalsIgnoreCase("$0.00");
        }

        if(amenity.exists){
            amenity.isIncluded = placeholderFactory.readYesNo(answers.get("EARLYCK_INCLUDE"));
            amenity.amount = placeholderFactory.readMixed(answers.get("EARLYCK_UOM"), answers.get("EARLYCK_FEE"), "");
        }
        return amenity;
    }

    private AmenitiesTablePlaceholderAmenity readWifi(Map<String, String> answers) {
        final AmenitiesTablePlaceholderAmenity amenity = new AmenitiesTablePlaceholderAmenity();
        amenity.label = "WiFi";
        amenity.exists = answers.containsKey("WIRELESS") && placeholderFactory.readYesNo(answers.get("WIRELESS"));
        if(amenity.exists){
            amenity.isIncluded = placeholderFactory.readYesNo(answers.get("WIRELESS_INCLUDE"));
            amenity.amount = placeholderFactory.readAmount(answers.get("WIRELESS_FEE"), "");
        }
        return amenity;
    }
}
