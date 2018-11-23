package net.readybid.app.persistence.mongodb.app.core.entity;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.gate.HotelChainStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelChainRepository;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.entities.chain.HotelChain;
import net.readybid.entities.hotel.logic.ElasticSearchModificationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelChainCollection.*;

@Service
class HotelChainStorageManagerImpl implements HotelChainStorageManager {

    private final HotelChainRepository repository;
    private final ElasticSearchOperations esOperations;

    @Autowired
    HotelChainStorageManagerImpl(
            HotelChainRepository repository,
            ElasticSearchOperations esOperations
    ) {
        this.repository = repository;
        this.esOperations = esOperations;
    }

    @Override
    public void setBrandMasterChainBasicDetails(HotelChain chain) {
        repository.update(
                eq(MASTER_CHAIN_ID, chain.getId()),
                set(MASTER_CHAIN_NAME, chain.getName())
        );

        esOperations.update(new SetMasterChainBasicDetails(chain));
    }

    private class SetMasterChainBasicDetails implements ElasticSearchModificationSpecification{

        private static final String ES_JSON_TEMPLATE = "{ " +
                "\"query\": { \"term\": { \"masterChainCode\": \"%s\" } }, " +
                "\"script\": {" +
                " \"lang\": \"painless\", " +
                "\"source\": \"ctx._source.masterChain = params.name\", " +
                "\"params\": { \"name\": \"%s\" } " +
                "} " +
                "}";

        private HotelChain chain;

        SetMasterChainBasicDetails (HotelChain chain){
            this.chain = chain;
        }

        @Override
        public String getIndex() {
            return EntityType.CHAIN.toElasticSearchType();
        }

        @Override
        public String getBody() {
            return String.format(ES_JSON_TEMPLATE, chain.getCode(), chain.getName());
        }
    }
}
