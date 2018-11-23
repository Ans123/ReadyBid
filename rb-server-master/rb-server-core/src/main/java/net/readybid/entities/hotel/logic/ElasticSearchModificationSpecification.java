package net.readybid.entities.hotel.logic;

import java.util.Collections;
import java.util.Map;

public interface ElasticSearchModificationSpecification {

    String getIndex();

    default String getPath(){
        return "_update_by_query?conflicts=proceed";
    }

    String getBody();

    default Map<String,String> getParams(){
        return Collections.emptyMap();
    }
}
