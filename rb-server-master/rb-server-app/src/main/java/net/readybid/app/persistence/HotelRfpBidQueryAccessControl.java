package net.readybid.app.persistence;

import net.readybid.auth.permission.Permission;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.nin;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.BUYER_COMPANY_ACCOUNT_ID;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.STATE_STATUS;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID;

public class HotelRfpBidQueryAccessControl {

    private HotelRfpBidQueryAccessControl(){}

    public static Bson applyPermissionsFilter(Bson filter, AuthenticatedUser user) {
        final Bson accessFilter = RfpType.HOTEL.isSupplier(user.getAccountType())
                ? applySupplierPermissions(user.getAccountIdsWithPermission(Permission.SUPPLIER_READ))
                : applyBuyerPermissions(user.getAccountIdsWithPermission(Permission.BUYER_READ));

        return and(accessFilter, filter);
    }

    private static Bson applyBuyerPermissions(List<ObjectId> accountIdsWithPermission) {
        return and(
                in(BUYER_COMPANY_ACCOUNT_ID, accountIdsWithPermission),
                nin(STATE_STATUS, HotelRfpBidStateStatus.DELETED)
        );
    }

    private static Bson applySupplierPermissions(List<ObjectId> accountIdsWithPermission) {
        return and(
                in(SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID, accountIdsWithPermission),
                nin(STATE_STATUS, HotelRfpBidStateStatus.CREATED, HotelRfpBidStateStatus.DELETED)
        );
    }

}
