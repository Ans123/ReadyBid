package net.readybid.bidmanagerview;

import net.readybid.rfp.type.RfpType;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 1/28/2017.
 *
 */
public interface BidManagerView {
    ObjectId getId();

    RfpType getRfpType();

    BidManagerViewType getType();

    String getName();

    List<String> getColumns();

    Map<String, Object> getFilter();

    ObjectId getOwner();

    Map<String,Object> getSort();

    String getGroup();

    ObjectId getRfpId();

    BidManagerViewSide getSide();
}
