package net.readybid.entities.chain;

import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
@Service
public class HotelChainFacadeImpl implements HotelChainFacade {

    private final HotelChainViewRepository chainViewRepository;
    private final HotelChainRepository chainRepository;

    @Autowired
    public HotelChainFacadeImpl(HotelChainViewRepository chainViewRepository, HotelChainRepository chainRepository) {
        this.chainViewRepository = chainViewRepository;
        this.chainRepository = chainRepository;
    }

    @Override
    public ListResult<HotelChainListItemViewModel> listAllChains() {
        return chainRepository.listAllChains();
    }

    @Override
    public ListResult<HotelChainSearchResultView> searchChains(String query, int page) {
        return chainViewRepository.search(query, page);
    }

    @Override
    public HotelChain findHotelByIdIncludingUnverified(String id) {
        return chainRepository.findByIdIncludingUnverified(id);
    }

    @Override
    public HotelChain findHotelById(String id) {
        return chainRepository.findById(id);
    }
}
