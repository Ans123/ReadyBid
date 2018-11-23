package net.readybid.entities.consultancy.logic;


import net.readybid.entities.consultancy.web.ConsultancySearchResultView;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public interface ConsultancyViewRepository {
    ListResult<ConsultancySearchResultView> search(String query, int page);
}
