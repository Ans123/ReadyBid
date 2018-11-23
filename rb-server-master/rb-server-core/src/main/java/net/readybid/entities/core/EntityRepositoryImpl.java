package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.agency.core.Agency;
import net.readybid.entities.agency.logic.AgencyRepository;
import net.readybid.entities.chain.HotelChain;
import net.readybid.entities.chain.HotelChainRepository;
import net.readybid.entities.company.core.Company;
import net.readybid.entities.company.logic.CompanyRepository;
import net.readybid.entities.consultancy.core.Consultancy;
import net.readybid.entities.consultancy.logic.ConsultancyRepository;
import net.readybid.entities.hmc.HotelManagementCompany;
import net.readybid.entities.hmc.HotelManagementCompanyRepository;
import net.readybid.entities.hotel.logic.HotelRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
@Service
public class EntityRepositoryImpl implements EntityRepository {

    private final HotelRepository hotelRepository;
    private final HotelChainRepository chainRepository;
    private final HotelManagementCompanyRepository hotelManagementCompanyRepository;
    private final CompanyRepository companyRepository;
    private final AgencyRepository agencyRepository;
    private final ConsultancyRepository consultancyRepository;

    @Autowired
    public EntityRepositoryImpl(
            HotelRepository hotelRepository,
            HotelChainRepository chainRepository,
            HotelManagementCompanyRepository hotelManagementCompanyRepository,
            CompanyRepository companyRepository,
            AgencyRepository agencyRepository,
            ConsultancyRepository consultancyRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.chainRepository = chainRepository;
        this.hotelManagementCompanyRepository = hotelManagementCompanyRepository;
        this.companyRepository = companyRepository;
        this.agencyRepository = agencyRepository;
        this.consultancyRepository = consultancyRepository;
    }

     @Override
    public void saveForValidation(Entity entity) {
        switch(entity.getType()){
            case HOTEL:
                if(entity instanceof Hotel) hotelRepository.saveForValidation((Hotel) entity); else throwIllegalArgumentException();
                break;
            case CHAIN:
                if(entity instanceof HotelChain)
                    chainRepository.saveForValidation((HotelChain) entity); else throwIllegalArgumentException();
                break;
            case HMC:
                if(entity instanceof HotelManagementCompany)
                    hotelManagementCompanyRepository.saveForValidation((HotelManagementCompany) entity);
                else throwIllegalArgumentException();
                break;
            case COMPANY:
                if(entity instanceof Company) companyRepository.saveForValidation((Company) entity); else throwIllegalArgumentException();
                break;
            case TRAVEL_AGENCY:
                if(entity instanceof Agency) agencyRepository.saveForValidation((Agency) entity); else throwIllegalArgumentException();
                break;
            case TRAVEL_CONSULTANCY:
                if(entity instanceof Consultancy) consultancyRepository.saveForValidation((Consultancy) entity); else throwIllegalArgumentException();
                break;
            default:
                throwIllegalArgumentException();
        }
    }

    @Override
    public Entity findByIdIncludingUnverified(EntityType entityType, String id){
        final String[] fields = new String[]{"name", "type", "industry", "website", "image", "location", "created", "status", "logo"};

        switch(entityType){
            case HOTEL:
                return hotelRepository.findByIdIncludingUnverified(id, fields);
            case CHAIN:
                return chainRepository.findByIdIncludingUnverified(id, fields);
            case HMC:
                return hotelManagementCompanyRepository.findByIdIncludingUnverified(id, fields);
            case COMPANY:
                return companyRepository.findByIdIncludingUnverified(id, fields);
            case TRAVEL_AGENCY:
                return agencyRepository.findByIdIncludingUnverified(id, fields);
            case TRAVEL_CONSULTANCY:
                return consultancyRepository.findByIdIncludingUnverified(id, fields);
            default:
                return throwIllegalArgumentException();
        }
    }

    @Override
    public Entity findByIdIncludingUnverified(EntityType entityType, ObjectId entityId) {
        return findByIdIncludingUnverified(entityType, String.valueOf(entityId));
    }

    @Override
    public Hotel getHotelIncludingUnverified(String hotelId) {
        return hotelRepository.findByIdIncludingUnverified(hotelId);
    }

    @Override
    public Entity findByIdIncludingUnverified(String entityId) {
        final String[] fields = new String[]{"name", "type", "industry", "website", "image", "location", "created", "status", "logo"};

        Entity e = companyRepository.findByIdIncludingUnverified(entityId, fields);
        if(null == e) e = agencyRepository.findByIdIncludingUnverified(entityId, fields);
        if(null == e) e = consultancyRepository.findByIdIncludingUnverified(entityId, fields);
        if(null == e) e = hotelRepository.findByIdIncludingUnverified(entityId, fields);
        if(null == e) e = chainRepository.findByIdIncludingUnverified(entityId, fields);
        if(null == e) e = hotelManagementCompanyRepository.findByIdIncludingUnverified(entityId, fields);

        return e;
    }

    @Override
    public Entity findByIdIncludingUnverified(ObjectId entityId) {
        return findByIdIncludingUnverified(String.valueOf(entityId));
    }

    private Entity throwIllegalArgumentException() {
        throw new IllegalArgumentException();
    }
}
