package net.readybid.app.entities.authorization.dirty;

import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 4/7/2017.
 *
 */
public class InvolvedAccounts {

    public static InvolvedAccounts getNullCompanies() {
        return new InvolvedAccounts();
    }

    public ObjectId buyer;
    public ObjectId supplier;

    public boolean isEmpty() {
        return buyer == null && supplier == null;
    }

    public static InvolvedAccounts create(HotelRfpBid bid) {
        if(bid == null) return null;
        final InvolvedAccounts companies = new InvolvedAccounts();
        companies.buyer =  bid.getBuyerCompanyAccountId();
        companies.supplier =  bid.getSupplierContactCompanyAccountId();
        return companies;
    }

    public static InvolvedAccounts create(Rfp rfp) {
        if(rfp == null) return null;
        final InvolvedAccounts companies = new InvolvedAccounts();
        companies.buyer =  rfp.getBuyerCompanyAccountId();
        return companies;
    }
}
