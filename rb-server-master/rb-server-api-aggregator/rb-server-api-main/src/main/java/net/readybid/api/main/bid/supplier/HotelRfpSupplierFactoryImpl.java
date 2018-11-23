package net.readybid.api.main.bid.supplier;

import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.app.interactors.rfp_hotel.dirty.RfpHotelFactory;
import net.readybid.auth.rfp.contact.RfpContactFactory;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.rfphotel.supplier.HotelRfpSupplierImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
@Service
public class HotelRfpSupplierFactoryImpl implements HotelRfpSupplierFactory {

    private final RfpContactFactory rfpContactFactory;
    private final RfpHotelFactory rfpHotelFactory;

    @Autowired
    public HotelRfpSupplierFactoryImpl(RfpContactFactory rfpContactFactory, RfpHotelFactory rfpHotelFactory) {
        this.rfpContactFactory = rfpContactFactory;
        this.rfpHotelFactory = rfpHotelFactory;
    }

    @Override
    public HotelRfpSupplier create(HotelRfpBidBuilder hotelRfpBidBuilder) {
        final HotelRfpSupplierImpl supplier = new HotelRfpSupplierImpl();

        final UserAccount supplierContact = hotelRfpBidBuilder.getSupplierContact();
        if(supplierContact != null){
            supplier.setContact(rfpContactFactory.createContact(supplierContact));
        }

        supplier.setCompany(rfpHotelFactory.create(hotelRfpBidBuilder));

        return supplier;
    }
}
