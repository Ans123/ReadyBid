package net.readybid.rfphotel.bid.core.negotiations.values;

/**
 * Created by DejanK on 8/10/2017.
 */
public enum NegotiationValueType {
    FIXED, UNAVAILABLE, PERCENTAGE, MOCKED;


    public static NegotiationValueType readType(String typeString) {
        if("p".equalsIgnoreCase(typeString) || "percentage".equalsIgnoreCase(typeString)){
            return PERCENTAGE;
        } else if(null == typeString || "n".equalsIgnoreCase(typeString) || "unavailable".equalsIgnoreCase(typeString)){
            return UNAVAILABLE;
        } else {
            return FIXED;
        }
    }

    public static NegotiationValueType readType(Object typeObj) {
        return readType(String.valueOf(typeObj));
    }
}
