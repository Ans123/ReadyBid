package net.readybid.app.entities.rfp_hotel;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.test_utils.RbRandom;

import java.util.ArrayList;
import java.util.List;

public class HotelRfpRepresentativeFactory {

    public static HotelRfpRepresentative random() {
        final HotelRfpRepresentative representative = new HotelRfpRepresentative();
        representative.id = RbRandom.idAsString();
        representative.firstName = RbRandom.name();
        representative.lastName = RbRandom.name();
        representative.emailAddress = RbRandom.emailAddress();
        representative.isUser = RbRandom.bool();

        return representative;
    }

    public static List<HotelRfpRepresentative> listOfRandom(int maxSize) {
        final int representativesCount = RbRandom.randomInt(maxSize);
        final List<HotelRfpRepresentative> representativeList = new ArrayList<>(representativesCount);

        for(int i = 0; i< representativesCount; i++){
            representativeList.add(HotelRfpRepresentativeFactory.random());
        }

        return representativeList;
    }

    public static HotelRfpRepresentative random(String emailAddress, EntityType accountType) {
        final HotelRfpRepresentative representative = random();
        representative.emailAddress = emailAddress;
        representative.accountType = accountType;

        return representative;
    }
}
