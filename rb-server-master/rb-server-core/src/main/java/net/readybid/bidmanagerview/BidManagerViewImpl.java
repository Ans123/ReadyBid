package net.readybid.bidmanagerview;

import net.readybid.rfp.type.RfpType;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 1/28/2017.
 *
 */
public class BidManagerViewImpl implements BidManagerView {

    private ObjectId id;
    private String name;
    private List<String> columns;
    private Map<String, Object> filter;
    private Map<String, Object> sort;
    private String group;
    private ObjectId ownerId;
    private RfpType rfpType;
    private BidManagerViewType type;
    private ObjectId rfpId;
    private BidManagerViewSide side;

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public ObjectId getId() {
        return id;
    }

    @Override
    public RfpType getRfpType() {
        return rfpType;
    }

    @Override
    public BidManagerViewType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getColumns() {
        return columns;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    @Override
    public ObjectId getOwner() {
        return ownerId;
    }

    public void setOwner(ObjectId owner) {
        this.ownerId = owner;
    }

    public void setSort(Map<String, Object> sort) {
        this.sort = sort;
    }

    public Map<String, Object> getSort() {
        return sort;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public ObjectId getRfpId() {
        return rfpId;
    }

    public void setRfpType(RfpType rfpType) {
        this.rfpType = rfpType;
    }

    public void setType(BidManagerViewType type) {
        this.type = type;
    }

    public void setRfpId(ObjectId rfpId) {
        this.rfpId = rfpId;
    }

    public void setSide(BidManagerViewSide side) {
        this.side = side;
    }

    public BidManagerViewSide getSide() {
        return side;
    }
}
