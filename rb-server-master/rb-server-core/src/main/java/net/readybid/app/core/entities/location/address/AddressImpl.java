package net.readybid.app.core.entities.location.address;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
public class AddressImpl implements Address {

    private String address1;
    private String address2;
    private String city;
    private String county;
    private String state;
    private String region;
    private String zip;
    private Country country;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String getStateOrRegion() {
        return state != null ? state : region;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    @Override
    public String getShortAddress() {
        final String spaceSeparator = " ";
        final String partSeparator = ", ";

        final StringBuilder sb;
        if(address1 == null || address1.isEmpty()){
            sb = new StringBuilder(city);
        } else {
            sb = new StringBuilder(address1);
            sb.append(partSeparator).append(city);
        }

        appendIfNotNull(sb, spaceSeparator, state);
        sb.append(partSeparator).append(country.getFullName());

        return sb.toString();
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public String printCountry() {
        return country == null ? "" : country.getFullName();
    }

    @Override
    public String printStateOrRegion() {
        final String addressStateRegion = state != null && !state.isEmpty() ? State.valueOf(state).fullName : region;
        return (addressStateRegion != null && !addressStateRegion.isEmpty() ? addressStateRegion + ", " : "") + printCountry();
    }

    @Override
    public String printCity() {
        if(city == null || city.isEmpty()) return "";
        final String stateRegion = printStateOrRegion();
        return StringUtils.capitalize(city) + ", " + ( stateRegion.isEmpty() ? printCountry() : stateRegion );
    }

    @Override
    public String getCountryName() {
        return country == null ? "" : country.getFullName();
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String getFullAddress() {
        final String spaceSeparator = " ";
        final String partSeparator = ", ";

        final StringBuilder sb;
        if(address1 == null || address1.isEmpty()){
            sb = new StringBuilder(city);
        } else {
            sb = new StringBuilder(address1);
            appendIfNotNull(sb, spaceSeparator, address2);
            sb.append(partSeparator).append(city);
        }
        appendIfNotNull(sb, spaceSeparator, state);

        if(!city.equalsIgnoreCase(region)) {
            appendIfNotNull(sb, spaceSeparator, region);
        }

        appendIfNotNull(sb, spaceSeparator, zip);
        sb.append(partSeparator).append(country.getFullName());

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !AddressImpl.class.isAssignableFrom(obj.getClass())) return false;
        final AddressImpl other = (AddressImpl) obj;

        boolean different = isDifferent(this.address1, other.address1);
        different = different || isDifferent(this.address2, other.address2);
        different = different || isDifferent(this.city, other.city);
        different = different || isDifferent(this.county, other.county);
        different = different || isDifferent(this.state, other.state);
        different = different || isDifferent(this.region, other.region);
        different = different || isDifferent(this.zip, other.zip);
        different = different || isDifferent(this.country, other.country);
        return !different;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = addToHash(hash, address1);
        hash = addToHash(hash, address2);
        hash = addToHash(hash, city);
        hash = addToHash(hash, county);
        hash = addToHash(hash, state);
        hash = addToHash(hash, region);
        hash = addToHash(hash, zip);
        hash = addToHash(hash, country);
        return hash;
    }

    private boolean isDifferent(Object o1, Object o2){
        return o1 == null ? o2 != null : !o1.equals(o2);
    }

    private int addToHash(int hash, Object o) {
        return 53 * hash + (o == null ? 0: o.hashCode());
    }

    private void appendIfNotNull(StringBuilder sb, String separator, String stringToAppend){
        if(stringToAppend != null && !stringToAppend.isEmpty()) {
            sb.append(separator).append(stringToAppend);
        }
    }
}
