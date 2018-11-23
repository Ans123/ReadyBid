package net.readybid.api.main.entity;

import net.readybid.entities.hmc.HotelManagementCompany;
import net.readybid.entities.hmc.HotelManagementCompanyFacade;
import net.readybid.entities.hmc.HotelManagementCompanyFacadeSearchResultView;
import net.readybid.entities.hmc.HotelManagementCompanyViewModel;
import net.readybid.utils.ListResult;
import net.readybid.web.GetResponse;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
@RestController
@RequestMapping(value = "/hotel-management-companies")
public class HotelManagementCompanyController {

    private final HotelManagementCompanyFacade hmcFacade;

    @Autowired
    public HotelManagementCompanyController(HotelManagementCompanyFacade hmcFacade) {
        this.hmcFacade = hmcFacade;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ListResponse<HotelManagementCompanyFacadeSearchResultView> searchEntities(
            @RequestParam(value = "query", required = true, defaultValue = "") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        ListResponse<HotelManagementCompanyFacadeSearchResultView> sr = new ListResponse<>();
        ListResult<HotelManagementCompanyFacadeSearchResultView> result = hmcFacade.search(query, page);
        return sr.finalizeResult(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GetResponse<HotelManagementCompany, HotelManagementCompanyViewModel> handleGetHotelManagementCompanyProfileRequest(
            @PathVariable String id,
            @RequestParam(value = "include-unverified", required = false, defaultValue = "") String includeUnverifiedParam
    ) {
        final GetResponse<HotelManagementCompany, HotelManagementCompanyViewModel> getResponse = new GetResponse<>();
        final HotelManagementCompany hmc = includeUnverifiedParam.equals("true") ? hmcFacade.findHotelByIdIncludingUnverified(id) : hmcFacade.findHotelById(id);
        return getResponse.finalizeResult(HotelManagementCompanyViewModel.FACTORY.createView(hmc));
    }
}
