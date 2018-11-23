package net.readybid.rfphotel.supplier;


import net.readybid.rfp.contact.RfpContactViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 2/11/2017.
 *
 */
public class HotelRfpSupplierViewModel implements ViewModel<HotelRfpSupplier> {

    public static final ViewModelFactory<HotelRfpSupplier, HotelRfpSupplierViewModel> FACTORY = HotelRfpSupplierViewModel::new;

    public RfpHotelViewModel company;
    public RfpContactViewModel contact;

    public HotelRfpSupplierViewModel(HotelRfpSupplier supplier) {
        company = RfpHotelViewModel.FACTORY.createAsPartial(supplier.getCompany());
        contact = RfpContactViewModel.FACTORY.createAsPartial(supplier.getContact());
    }
}
