package net.readybid.entities.consultancy.logic;

import net.readybid.entities.consultancy.web.ConsultancyFacade;
import net.readybid.entities.consultancy.web.ConsultancySearchResultView;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
@Service
public class ConsultancyFacadeImpl implements ConsultancyFacade {

    private final ConsultancyViewRepository consultancyViewRepository;

    @Autowired
    public ConsultancyFacadeImpl(ConsultancyViewRepository consultancyViewRepository) {
        this.consultancyViewRepository = consultancyViewRepository;
    }

    @Override
    public ListResult<ConsultancySearchResultView> searchConsultancies(String query, int page) {
        return consultancyViewRepository.search(query, page);
    }
}
