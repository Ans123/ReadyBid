package net.readybid.rfp.specifications;


import net.readybid.exceptions.BadRequestException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by DejanK on 2/10/2017.
 *
 */
public class UpdateProgramPeriodRequest {

    @NotNull
    public LocalDate programStartDate;

    @NotNull
    public LocalDate programEndDate;

    public void validate() {
        if(programStartDate.isAfter(programEndDate)){
            throw new BadRequestException("Program start date cannot be after program end date");
        }
    }
}
