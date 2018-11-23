package net.readybid.rfphotel.bid.web;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class BidsQueryRequest {

//    private static final String REPORT_FIELD_PREFIX = "report.%s";

    @Deprecated
    public String template;

    @NotNull
    @NotBlank
    public String viewId;

    @NotNull
    public List<String> fields;

    public Map<String, Object> filter;

    public String group;
}
