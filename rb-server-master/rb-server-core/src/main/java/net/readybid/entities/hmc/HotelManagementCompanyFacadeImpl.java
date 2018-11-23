package net.readybid.entities.hmc;


import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
@Service
public class HotelManagementCompanyFacadeImpl implements HotelManagementCompanyFacade {

    private final HotelManagementCompanyViewRepository hmcViewRepository;
    private final HotelManagementCompanyRepository hmcRepository;

    @Autowired
    public HotelManagementCompanyFacadeImpl(HotelManagementCompanyViewRepository hmcViewRepository, HotelManagementCompanyRepository hmcRepository) {
        this.hmcViewRepository = hmcViewRepository;
        this.hmcRepository = hmcRepository;
    }

    @Override
    public ListResult<HotelManagementCompanyFacadeSearchResultView> search(String query, int page) {
        return hmcViewRepository.search(query, page);
    }

    @Override
    public HotelManagementCompany findHotelById(String id) {
        return hmcRepository.findById(id);
    }

    @Override
    public HotelManagementCompany findHotelByIdIncludingUnverified(String id) {
        return hmcRepository.findByIdIncludingUnverified(id);
    }
}
