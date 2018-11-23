package net.readybid.api.main.access;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 4/6/2017.
 *
 */
public interface RfpAccessControlService {

    void create(ObjectId forAccountId);

    void read(String rfpId);

    void update(String rfpId);

    void delete(String rfpId);

    Bson query();

    Document queryAsDocument();
}
