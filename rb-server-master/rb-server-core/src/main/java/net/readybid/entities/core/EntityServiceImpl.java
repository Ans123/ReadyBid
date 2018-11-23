package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.agency.logic.AgencyFactory;
import net.readybid.entities.chain.HotelChainFactory;
import net.readybid.entities.company.logic.CompanyFactory;
import net.readybid.entities.consultancy.logic.ConsultancyFactory;
import net.readybid.entities.hmc.HotelManagementCompanyFactory;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.hotel.logic.HotelFactory;
import net.readybid.user.BasicUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/24/2017.
 *
 */
@Service
public class EntityServiceImpl implements EntityService {

    private final HotelFactory hotelFactory;
    private final HotelChainFactory chainFactory;
    private final HotelManagementCompanyFactory hotelManagementCompanyFactory;
    private final CompanyFactory companyFactory;
    private final AgencyFactory agencyFactory;
    private final ConsultancyFactory consultancyFactory;

    private final EntityRepository entityRepository;

    @Autowired
    public EntityServiceImpl(
            HotelFactory hotelFactory,
            HotelChainFactory chainFactory,
            HotelManagementCompanyFactory hotelManagementCompanyFactory,
            CompanyFactory companyFactory,
            AgencyFactory agencyFactory,
            ConsultancyFactory consultancyFactory,

            EntityRepository entityRepository
    ) {
        this.hotelFactory = hotelFactory;
        this.chainFactory = chainFactory;
        this.hotelManagementCompanyFactory = hotelManagementCompanyFactory;
        this.companyFactory = companyFactory;
        this.agencyFactory = agencyFactory;
        this.consultancyFactory = consultancyFactory;

        this.entityRepository = entityRepository;
    }


    @Override
    public Entity createEntity(CreateEntityRequest request, BasicUserDetails user) {
        final EntityFactory factory = getEntityFactory(request.type);
        final Entity entity = factory.create(request);
        entity.setForValidation(user);
        entityRepository.saveForValidation(entity);
        return entity;
    }

    @Override
    public Entity getEntityIncludingUnverified(EntityType entityType, String entityId) {
        return entityRepository.findByIdIncludingUnverified(entityType, entityId);
    }

    @Override
    public Hotel getHotelIncludingUnverified(String hotelId) {
        return entityRepository.getHotelIncludingUnverified(hotelId);
    }

    @Override
    public Entity getEntityIncludingUnverified(String entityId) {
        return entityRepository.findByIdIncludingUnverified(entityId);
    }

    private EntityFactory getEntityFactory(EntityType type) {
        switch (type){
            case COMPANY:
                return companyFactory;
            case TRAVEL_AGENCY:
                return agencyFactory;
            case TRAVEL_CONSULTANCY:
                return consultancyFactory;
            case HOTEL:
                return hotelFactory;
            case CHAIN:
                return chainFactory;
            case HMC:
                return hotelManagementCompanyFactory;
            default:
                throw new IllegalArgumentException();
        }
    }
}
