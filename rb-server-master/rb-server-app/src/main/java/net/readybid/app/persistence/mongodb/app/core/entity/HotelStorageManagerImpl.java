package net.readybid.app.persistence.mongodb.app.core.entity;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.gate.HotelStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRepository;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.entities.chain.HotelChain;
import net.readybid.entities.hotel.logic.ElasticSearchModificationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.*;

@Service
class HotelStorageManagerImpl implements HotelStorageManager {

    private final HotelRepository repository;
    private final ElasticSearchOperations esOperations;

    @Autowired
    HotelStorageManagerImpl(
            HotelRepository repository,
            ElasticSearchOperations esOperations
    ) {
        this.repository = repository;
        this.esOperations = esOperations;
    }

    @Override
    public void setMasterChainBasicDetails(HotelChain chain) {
        repository.update(
                eq(MASTER_CHAIN_ID, chain.getId()),
                set(MASTER_CHAIN_NAME, chain.getName())
        );

        esOperations.update(new SetChainsBasicDetails("master", chain));
    }

    @Override
    public void setBrandBasicDetails(HotelChain chain) {
        repository.update(
                eq(CHAIN_ID, chain.getId()),
                set(CHAIN_NAME, chain.getName())
        );

        esOperations.update(new SetChainsBasicDetails("brand", chain));
    }

    private class SetChainsBasicDetails implements ElasticSearchModificationSpecification{

        private static final String ES_JSON_TEMPLATE = "{ " +
                "\"query\": { \"term\": { \"%sChainCode\": \"%s\" } }, " +
                "\"script\": {" +
                " \"lang\": \"painless\", " +
                "\"source\": \"ctx._source.%sChain = params.name\", " +
                "\"params\": { \"name\": \"%s\" } " +
                "} " +
                "}";

        private String type;
        private HotelChain chain;

        SetChainsBasicDetails (String type, HotelChain chain){
            this.type = type;
            this.chain = chain;
        }


        @Override
        public String getIndex() {
            return EntityType.HOTEL.toElasticSearchType();
        }

        @Override
        public String getBody() {
            return String.format(ES_JSON_TEMPLATE, type, chain.getCode(), type, chain.getName());
        }
    }
}
