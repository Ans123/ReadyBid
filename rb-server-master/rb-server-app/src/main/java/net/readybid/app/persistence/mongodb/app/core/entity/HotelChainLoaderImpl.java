package net.readybid.app.persistence.mongodb.app.core.entity;

import net.readybid.app.interactors.core.entity.gate.HotelChainLoader;
import net.readybid.app.persistence.mongodb.repository.HotelChainRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.EntityCollection;
import net.readybid.entities.chain.HotelChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
class HotelChainLoaderImpl implements HotelChainLoader {

    private final HotelChainRepository repository;

    @Autowired
    HotelChainLoaderImpl(HotelChainRepository repository) {
        this.repository = repository;
    }

    @Override
    public HotelChain load(String id) {
        return repository.findOne(
                byId(id),
                EntityCollection.INCLUDE_UNVALIDATED
        );
    }
}
