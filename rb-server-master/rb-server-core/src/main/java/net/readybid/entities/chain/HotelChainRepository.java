package net.readybid.entities.chain;


import net.readybid.app.core.entities.entity.Entity;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
public interface HotelChainRepository {
    ListResult<HotelChainListItemViewModel> listAllChains();

    HotelChain findById(String id);

    void saveForValidation(HotelChain entity);

    HotelChain findByIdIncludingUnverified(String id);

    Entity findByIdIncludingUnverified(String id, String... fields);
}
