package net.readybid.api.main.entity;

import net.readybid.entities.chain.*;
import net.readybid.utils.ListResult;
import net.readybid.web.GetResponse;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
@RestController
@RequestMapping(value = "/hotel-chains")
public class HotelChainController {

    private final HotelChainFacade chainFacade;

    @Autowired
    public HotelChainController(HotelChainFacade chainFacade) {
        this.chainFacade = chainFacade;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ListResponse<HotelChainListItemViewModel> listAllChains(HttpServletResponse httpResponse) {
        final ListResponse<HotelChainListItemViewModel> sr = new ListResponse<>();
        final ListResult<HotelChainListItemViewModel> result = chainFacade.listAllChains();
        httpResponse.setHeader("Cache-Control", CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic().getHeaderValue());
        return sr.finalizeResult(result);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ListResponse<HotelChainSearchResultView> searchEntities(
            @RequestParam(value = "query", required = true, defaultValue = "") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        ListResponse<HotelChainSearchResultView> sr = new ListResponse<>();
        ListResult<HotelChainSearchResultView> result = chainFacade.searchChains(query, page);
        return sr.finalizeResult(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GetResponse<HotelChain, HotelChainViewModel> handleGetChainProfileRequest(
            @PathVariable String id,
            @RequestParam(value = "include-unverified", required = false, defaultValue = "") String includeUnverifiedParam
    ) {
        final GetResponse<HotelChain, HotelChainViewModel> getResponse = new GetResponse<>();
        final HotelChain chain = includeUnverifiedParam.equals("true") ? chainFacade.findHotelByIdIncludingUnverified(id) : chainFacade.findHotelById(id);
        return getResponse.finalizeResult(HotelChainViewModel.FACTORY.createView(chain));
    }

}
