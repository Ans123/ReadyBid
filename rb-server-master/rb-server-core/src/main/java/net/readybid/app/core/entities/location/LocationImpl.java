package net.readybid.app.core.entities.location;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.address.AddressImpl;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.coordinates.CoordinatesImpl;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public class LocationImpl implements Location {

    @JsonDeserialize(as = AddressImpl.class)
    private Address address;

    private String fullAddress;

    @JsonDeserialize(as = CoordinatesImpl.class)
    private Coordinates coordinates;

    public LocationImpl(){}

    public LocationImpl(AddressImpl address, CoordinatesImpl coordinates){
        setAddress(address);
        setCoordinates(coordinates);
        updateFullAddress();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Override
    public String getFullAddress() {
        return fullAddress;
    }

    @Override
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean hasCoordinates(Coordinates coordinates) {
        return this.coordinates != null && this.coordinates.equals(coordinates);
    }

    public void updateFullAddress() {
        fullAddress = address.getFullAddress();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !LocationImpl.class.isAssignableFrom(obj.getClass())) return false;
        final LocationImpl other = (LocationImpl) obj;

        boolean different = isDifferent(this.address, other.address);
        different = different || isDifferent(this.fullAddress, other.fullAddress);
        different = different || isDifferent(this.coordinates, other.coordinates);
        return !different;
    }

    private boolean isDifferent(Object o1, Object o2){
        return o1 == null ? o2 != null : !o1.equals(o2);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = addToHash(hash, this.address);
        hash = addToHash(hash, this.fullAddress);
        hash = addToHash(hash, this.coordinates);
        return hash;
    }

    private int addToHash(int hash, Object o) {
        return 53 * hash + (o == null ? 0: o.hashCode());
    }
}
