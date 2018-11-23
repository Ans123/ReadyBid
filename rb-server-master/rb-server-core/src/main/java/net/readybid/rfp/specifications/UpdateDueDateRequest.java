package net.readybid.rfp.specifications;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by DejanK on 2/10/2017.
 *
 */
public class UpdateDueDateRequest {

    @NotNull
//    @RbDate
    public LocalDate dueDate;
}
