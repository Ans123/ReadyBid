package net.readybid.app.persistence.mongodb.repository;

import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import net.readybid.rfp.core.RfpImpl;
import org.bson.conversions.Bson;

import java.util.List;

public interface HotelRfpRepository {

    void bulkWrite(List<WriteModel<RfpImpl>> writes);

    RfpImpl findOne(Bson filter, Bson project);

    UpdateResult updateOne(Bson filter, Bson update);
}
