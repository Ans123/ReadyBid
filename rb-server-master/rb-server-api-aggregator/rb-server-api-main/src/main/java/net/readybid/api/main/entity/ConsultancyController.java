package net.readybid.api.main.entity;

import net.readybid.entities.consultancy.web.ConsultancyFacade;
import net.readybid.entities.consultancy.web.ConsultancySearchResultView;
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
@RequestMapping(value = "/travel-consultancies")
public class ConsultancyController {

    private final ConsultancyFacade consultancyFacade;

    @Autowired
    public ConsultancyController(ConsultancyFacade consultancyFacade) {
        this.consultancyFacade = consultancyFacade;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ListResponse<ConsultancySearchResultView> searchEntities(
            @RequestParam(value = "query", required = true, defaultValue = "") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        final ListResponse<ConsultancySearchResultView> sr = new ListResponse<>();
        final ListResult<ConsultancySearchResultView> lr = consultancyFacade.searchConsultancies(query, page);
        return sr.finalizeResult(lr);
    }
}
