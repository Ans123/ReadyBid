package net.readybid.api.main.access;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 4/6/2017.
 *
 */
public interface BidAccessControlService {

    void readAsAny(String bidId);

    void readAsAny(List<String> bidsIds);

    void updateAsAny(String bidId);

    void updateAsBuyer(String bidId);

    void updateAsBuyer(List<String> bidsIds);

    void readAsBuyer(String bidId);

    void readAsSupplier(String bidId);

    void updateAsSupplier(String bidId);

    void updateAsSupplier(List<String> bidsIds);

    ObjectId previewBidWithToken(String token);
}
