package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidSetBidContactDataRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelRfpBidSetBidContactDataRepositoryImpl implements HotelRfpBidSetBidContactDataRepository {

    final MongoCollection<HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl> collection;

    @Autowired
    public HotelRfpBidSetBidContactDataRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection(HotelRfpBidCollection.COLLECTION_NAME, HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl.class);
    }

    @Override
    public List<HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl> aggregate(List<? extends Bson> pipeline) {
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}
