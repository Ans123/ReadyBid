package net.readybid.rfphotel.bid.core.negotiations.values;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public enum NegotiationValueChangeType {
    INIT, NONE, INC, DESC;


    public static NegotiationValueChangeType getFromComparison(int comparisonResult){
        if(comparisonResult > 0){
            return NegotiationValueChangeType.INC;
        } else if(comparisonResult < 0){
            return NegotiationValueChangeType.DESC;
        } else {
            return NegotiationValueChangeType.NONE;
        }
    }
}
