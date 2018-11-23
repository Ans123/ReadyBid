package net.readybid.entities.rfp;

import org.bson.types.ObjectId;

import java.util.List;

public interface EntityRateLoadingInformation {
    ObjectId getEntityId();

    String getEntityName();

    List<? extends RateLoadingInformation> getInformation();

    boolean containsInformation();
}
