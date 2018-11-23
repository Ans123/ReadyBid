package net.readybid.app.persistence.mongodb.repository;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import org.bson.conversions.Bson;

import java.util.List;

public interface HotelRfpBidRepository {
    void bulkWrite(List<WriteModel<HotelRfpBidImpl>> writes, BulkWriteOptions ordered);

    List<HotelRfpBidImpl> find(Bson filter, Bson projection);

    UpdateResult update(Bson filter, Bson update);

    HotelRfpBidImpl findOne(Bson filter, Bson projection);

    List<HotelRfpBidImpl> aggregate(List<Bson> pipeline);
}
