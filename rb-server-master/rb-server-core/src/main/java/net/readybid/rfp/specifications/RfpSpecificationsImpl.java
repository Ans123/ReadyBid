package net.readybid.rfp.specifications;

import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.type.RfpType;
import org.bson.types.ObjectId;

import java.time.LocalDate;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public class RfpSpecificationsImpl implements RfpSpecifications {

    private String name;
    private RfpType type;
    private boolean chainSupported = false;
    private LocalDate dueDate;
    private LocalDate programStartDate;
    private LocalDate programEndDate;
    private int programYear;
    private Buyer buyer;
    private LocalDate sentDate;

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setProgramStartDate(LocalDate programStartDate) {
        this.programStartDate = programStartDate;
    }

    public void setProgramEndDate(LocalDate programEndDate) {
        this.programEndDate = programEndDate;
    }

    public LocalDate getProgramStartDate() {
        return programStartDate;
    }

    @Override
    public LocalDate getProgramEndDate() {
        return programEndDate;
    }

    public void setProgramYear(int programYear) {
        this.programYear = programYear;
    }

    public int getProgramYear() {
        return programYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(RfpType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RfpType getType() {
        return type;
    }

    public Buyer getBuyer(){
        return buyer;
    }

    @Override
    public ObjectId getBuyerCompanyAccountId() {
        return null == buyer ? null : buyer.getCompanyAccountId();
    }

    @Override
    public LocalDate getBidSentDate() {
        return sentDate;
    }

    public void setBuyer(Buyer buyer){
        this.buyer = buyer;
    }

    public void setBidSentDate(LocalDate bidSentDate) {
        this.sentDate = bidSentDate;
    }

    public boolean isChainSupportEnabled() {
        return chainSupported;
    }

    void setChainSupported(boolean isChainSupported) {
        this.chainSupported = isChainSupported;
    }
}
