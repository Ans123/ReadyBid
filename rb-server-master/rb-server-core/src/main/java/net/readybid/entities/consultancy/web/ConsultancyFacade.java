package net.readybid.entities.consultancy.web;


import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public interface ConsultancyFacade {
    ListResult<ConsultancySearchResultView> searchConsultancies(String query, int page);
}
