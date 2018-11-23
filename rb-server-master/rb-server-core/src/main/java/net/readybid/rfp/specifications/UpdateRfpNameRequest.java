package net.readybid.rfp.specifications;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by DejanK on 2/10/2017.
 *
 */
public class UpdateRfpNameRequest {

    @NotBlank
    @Size(max = 200)
    public String name;
}
