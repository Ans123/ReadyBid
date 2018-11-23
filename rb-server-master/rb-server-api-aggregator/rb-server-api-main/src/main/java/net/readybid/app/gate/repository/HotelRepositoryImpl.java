package net.readybid.app.gate.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.core.service.LoadHotelRepository;
import net.readybid.api.hotelrfp.traveldestination.FindHotelsNearCoordinatesRepository;
import net.readybid.api.hotelrfp.traveldestination.TravelDestinationManagerHotelViewModel;
import net.readybid.entities.hotel.core.HotelImpl;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.readybid.mongodb.RbMongoFilters.*;

@Service
public class HotelRepositoryImpl implements LoadHotelRepository, FindHotelsNearCoordinatesRepository {

    private final MongoCollection<HotelImpl> hotelCollection;
    private final MongoCollection<HotelImpl> unvalidatedHotelCollection;
    private final MongoCollection<TravelDestinationManagerHotelViewModel> travelDestinationManagerHotelViewModelCollection;

    @Autowired
    public HotelRepositoryImpl(
            MongoDatabase defaultMongoDatabase
    ) {
        hotelCollection = defaultMongoDatabase.getCollection(HotelCollection.COLLECTION_NAME, HotelImpl.class);
        unvalidatedHotelCollection = defaultMongoDatabase.getCollection(HotelCollection.UNVALIDATED_COLLECTION_NAME, HotelImpl.class);
        travelDestinationManagerHotelViewModelCollection = defaultMongoDatabase.getCollection(HotelCollection.COLLECTION_NAME, TravelDestinationManagerHotelViewModel.class);;
    }

    @Override
    public Hotel getAnswersById(String hotelId) {
        final HotelImpl hotel = hotelCollection.find(byId(hotelId)).projection(include("answers")).first();
        if(hotel == null) unvalidatedHotelCollection.find(byId(hotelId)).projection(include("answers")).first();

        return hotel;
    }

    @Override
    public List<TravelDestinationManagerHotelViewModel> search(List coordinates, double maxDistance, List<String> chains) {
        return travelDestinationManagerHotelViewModelCollection.aggregate(
                Arrays.asList(
                        doc("$geoNear",
                                doc("near", doc("type", "Point").append("coordinates", coordinates))
                                        .append("distanceField", "distanceM")
                                        .append("maxDistance", maxDistance)
                                        .append("query", getFilter(chains))
                                        .append("spherical", true)
                                        .append("limit", 5000)),
                        project(
                                doc("name", 1)
                                        .append("amenities", 1)
                                        .append("location", 1)
                                        .append("chain", 1)
                                        .append("phone", 1)
                                        .append("rating", 1)
                                        .append("image", 1)
                                        .append("distanceKm", doc("$divide", Arrays.asList("$distanceM", 1000)))
                                        .append("distanceMi", doc("$divide", Arrays.asList("$distanceM", 1609.344))))
                )
        ).into(new ArrayList<>());
    }

    private Bson getFilter(List<String> chains) {
        final ArrayList<Document> filters = new ArrayList<>();
        filters.add(new Document("status.value", new Document("$eq", "ACTIVE")));
        if(chains != null && !chains.isEmpty()) filters.add(createChainsFilter(chains));

        return new Document("$and", filters);
    }

    private Document createChainsFilter(List<String> chains) {
        final List<ObjectId> ids = new ArrayList<>();
        boolean includeWithoutChain = false;
        for(String c : chains){
            if(c.equals("NoChain")){
                includeWithoutChain = true;
            } else if(ObjectId.isValid(c)){
                ids.add(new ObjectId(c));
            }
        }
        final List<Document> chainFilters = new ArrayList<>();
        chainFilters.add(new Document("chain._id", new Document("$in", ids)));
        if(includeWithoutChain) chainFilters.add(new Document("chain", new Document("$exists", false)));
        return new Document("$or", chainFilters);
    }
}
