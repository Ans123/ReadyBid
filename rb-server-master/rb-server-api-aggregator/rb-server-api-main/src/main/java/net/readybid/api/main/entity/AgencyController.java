package net.readybid.api.main.entity;


import net.readybid.entities.agency.web.AgencyFacade;
import net.readybid.entities.agency.web.AgencySearchResultView;
import net.readybid.utils.ListResult;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
@RestController
@RequestMapping(value = "/travel-agencies")
public class AgencyController {

    private final AgencyFacade agencyFacade;

    @Autowired
    public AgencyController(AgencyFacade agencyFacade) {
        this.agencyFacade = agencyFacade;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ListResponse<AgencySearchResultView> searchEntities(
            @RequestParam(value = "query", required = true, defaultValue = "") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        final ListResponse<AgencySearchResultView> sr = new ListResponse<>();
        final ListResult<AgencySearchResultView> lr = agencyFacade.searchAgencies(query, page);
        return sr.finalizeResult(lr);
        
    }
}
