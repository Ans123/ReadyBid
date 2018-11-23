package net.readybid.app.persistence.mongodb.repository;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.WriteModel;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import org.bson.conversions.Bson;

import java.util.List;

public interface HotelRfpDefaultResponseRepository {
    void update(Bson filter, Bson update);

    void bulkWrite(List<WriteModel<HotelRfpDefaultResponse>> writes, BulkWriteOptions ordered);

    HotelRfpDefaultResponse findOne(Bson filter);

    List<HotelRfpDefaultResponse> find(Bson filter, Bson projection);
}
