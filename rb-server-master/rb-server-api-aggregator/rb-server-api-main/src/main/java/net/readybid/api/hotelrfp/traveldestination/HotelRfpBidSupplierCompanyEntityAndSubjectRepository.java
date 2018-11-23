package net.readybid.api.hotelrfp.traveldestination;

import net.readybid.rfphotel.bid.core.HotelRfpBidSupplierCompanyEntityAndSubject;
import org.bson.types.ObjectId;

import java.util.List;

public interface HotelRfpBidSupplierCompanyEntityAndSubjectRepository {
    List<HotelRfpBidSupplierCompanyEntityAndSubject> getBuyerBidsFromLastYearOrInDestination(ObjectId buyerCompanyAccountId, int lastProgramYear, String destinationId);
}
