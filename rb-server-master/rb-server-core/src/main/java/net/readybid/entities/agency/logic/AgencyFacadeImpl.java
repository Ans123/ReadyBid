package net.readybid.entities.agency.logic;

import net.readybid.entities.agency.web.AgencyFacade;
import net.readybid.entities.agency.web.AgencySearchResultView;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
@Service
public class AgencyFacadeImpl implements AgencyFacade {

    private final AgencyViewRepository agencyViewRepository;

    @Autowired
    public AgencyFacadeImpl(AgencyViewRepository agencyViewRepository) {
        this.agencyViewRepository = agencyViewRepository;
    }

    @Override
    public ListResult<AgencySearchResultView> searchAgencies(String query, int page) {
        return agencyViewRepository.search(query, page);
    }
}
