package net.readybid.rfp.specifications;

import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.type.RfpType;
import org.bson.types.ObjectId;

import java.time.LocalDate;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public interface RfpSpecifications {

    String getName();

    RfpType getType();

    LocalDate getDueDate();

    int getProgramYear();

    LocalDate getProgramStartDate();

    LocalDate getProgramEndDate();

    Buyer getBuyer();

    ObjectId getBuyerCompanyAccountId();

    LocalDate getBidSentDate();

    void setBidSentDate(LocalDate sentDate);

    boolean isChainSupportEnabled();
}
