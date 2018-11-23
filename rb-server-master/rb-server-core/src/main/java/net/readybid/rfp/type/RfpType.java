package net.readybid.rfp.type;

import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 12/29/2016.
 *
 */
public enum RfpType {
    HOTEL,
    AIRLINE,
    CAR,
    CORPORATE_CARD,
    MEETINGS,
    TMC,
    CREW_ACCOMMODATION,
    GROUP,
    LIMOUSINE,
    VIRTUAL_CARD,
    EXPENSE_MANAGEMENT;

    public boolean isSupplier(EntityType type) {
        switch (this){
            case HOTEL:
                return type.equals(EntityType.CHAIN) || type.equals(EntityType.HOTEL) || type.equals(EntityType.HMC);
        }
        throw new IllegalArgumentException();
    }
}
