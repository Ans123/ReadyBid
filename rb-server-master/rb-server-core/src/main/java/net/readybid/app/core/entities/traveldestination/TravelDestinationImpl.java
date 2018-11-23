package net.readybid.app.core.entities.traveldestination;

import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.utils.CreationDetails;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class TravelDestinationImpl implements TravelDestination {

    private String id;
    private String rfpId;
    private TravelDestinationType type;
    private String name;
    private Long estimatedSpend;
    private Integer estimatedRoomNights;
    private Location location;
    private TravelDestinationHotelFilter filter;
    private CreationDetails creationDetails;
    private TravelDestinationStatusDetails statusDetails;

    public TravelDestinationImpl() {}

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEstimatedSpend(Long estimatedSpend) {
        this.estimatedSpend = estimatedSpend;
    }

    public void setEstimatedRoomNights(Integer estimatedRoomNights) {
        this.estimatedRoomNights = estimatedRoomNights;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCreationDetails(CreationDetails creationDetails) {
        this.creationDetails = creationDetails;
    }

    public CreationDetails getCreationDetails() {
        return creationDetails;
    }

    public void setStatusDetails(TravelDestinationStatusDetails statusDetails) {
        this.statusDetails = statusDetails;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getEstimatedSpend() {
        return estimatedSpend;
    }

    @Override
    public Integer getEstimatedRoomNights() {
        return estimatedRoomNights;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public CreationDetails getCreated() {
        return creationDetails;
    }

    @Override
    public TravelDestinationStatusDetails getStatus() {
        return statusDetails;
    }

    public void setRfpId(String rfpId) {
        this.rfpId = rfpId;
    }

    public String getRfpId() {
        return rfpId;
    }

    public void setFilter(TravelDestinationHotelFilter filter) {
        this.filter = filter;
    }

    public TravelDestinationHotelFilter getFilter() {
        return filter;
    }

    @Override
    public Address getAddress() {
        return location == null ? null : location.getAddress();
    }

    public void setType(TravelDestinationType type) {
        this.type = type;
    }

    public TravelDestinationType getType() {
        return type;
    }

    @Override
    public boolean hasCoordinates(Coordinates coordinates) {
        return location != null && location.hasCoordinates(coordinates);
    }

    @Override
    public String getFullAddress() {
        return location == null ? "" : location.getFullAddress();
    }

    @Override
    public Coordinates getCoordinates() {
        return location == null ? null : location.getCoordinates();
    }
}
