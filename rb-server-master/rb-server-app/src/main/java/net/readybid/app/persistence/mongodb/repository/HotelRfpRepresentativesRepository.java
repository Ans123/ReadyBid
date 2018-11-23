package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public interface HotelRfpRepresentativesRepository {
    List<HotelRfpRepresentative> aggregateAccount(List<Bson> pipeline);

    List<HotelRfpRepresentative> aggregateBid(List<Bson> pipeline);

    Document findInAccount(Bson query, Bson projection);

    List<Document> aggregateHotel(List<Bson> pipeline);
}
