package net.readybid.rfp.specifications;

import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.type.RfpType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
@Service
public class RfpSpecificationsFactoryImpl implements RfpSpecificationsFactory {

    @Override
    public RfpSpecifications createSpecifications(RfpType rfpType, Buyer buyer) {
        RfpSpecificationsImpl rfpSpecs =  new RfpSpecificationsImpl();

        rfpSpecs.setType(rfpType);
        rfpSpecs.setDueDate(createDueDate());
        rfpSpecs.setProgramStartDate(createProgramStartDate(rfpSpecs.getDueDate()));
        rfpSpecs.setProgramEndDate(createProgramEndDate(rfpSpecs.getDueDate()));
        rfpSpecs.setProgramYear(createProgramYear(rfpSpecs.getProgramStartDate()));
        rfpSpecs.setName(createName(rfpType, buyer, rfpSpecs.getProgramYear()));
        rfpSpecs.setBuyer(buyer);

        return rfpSpecs;
    }

    private int createProgramYear(LocalDate programStartDate) {
        return programStartDate.getYear();
    }

    private String createName(RfpType rfpType, Buyer buyer, int year) {
        return String.format("%s %d %s RFP", buyer.getCompanyName(), year, rfpType);
    }

    private LocalDate createDueDate() {
        LocalDate dueDate = LocalDate.now();
        return dueDate.plusDays(45);
    }

    private LocalDate createProgramStartDate(final LocalDate dueDate){
        final LocalDate nextYear = dueDate.plusYears(1);
        return nextYear.withDayOfYear(1);
    }

    private LocalDate createProgramEndDate(final LocalDate dueDate){
        final LocalDate nextYear = dueDate.plusYears(1);
        return nextYear.withDayOfYear(nextYear.lengthOfYear());
    }
}
