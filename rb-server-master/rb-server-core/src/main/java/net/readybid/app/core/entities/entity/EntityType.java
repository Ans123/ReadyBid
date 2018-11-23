package net.readybid.app.core.entities.entity;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
public enum EntityType {
    HOTEL("hotel"),
    CHAIN("chain"),
    HMC("hotel_management_company"),
    COMPANY("company"),
    TRAVEL_AGENCY("travel_agency"),
    TRAVEL_CONSULTANCY("travel_consultancy");

    private String elasticSearchAliasName;

    EntityType(String elasticSearchAliasName) {
        this.elasticSearchAliasName = elasticSearchAliasName;
    }

    public String toElasticSearchType(){
        return elasticSearchAliasName;
    }
}
