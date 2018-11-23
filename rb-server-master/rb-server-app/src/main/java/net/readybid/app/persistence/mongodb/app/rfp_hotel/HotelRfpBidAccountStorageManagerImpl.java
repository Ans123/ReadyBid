package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.distance.Distance;
import net.readybid.app.core.entities.location.distance.DistanceImpl;
import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidAccountStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.auth.account.core.Account;
import net.readybid.entities.chain.HotelChain;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.*;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.oid;

@Service
class HotelRfpBidAccountStorageManagerImpl implements HotelRfpBidAccountStorageManager {

    private final HotelRfpBidRepository repository;

    @Autowired
    HotelRfpBidAccountStorageManagerImpl(HotelRfpBidRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateBuyerLogo(String accountId, String logo) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();
        writes.add(new UpdateManyModel<>(eq(BUYER_COMPANY_ACCOUNT_ID, oid(accountId)),set(BUYER_COMPANY_LOGO, logo)));
        writes.add(new UpdateManyModel<>(eq(BUYER_CONTACT_COMPANY_ACCOUNT_ID, oid(accountId)),set(BUYER_CONTACT_COMPANY_LOGO, logo)));

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    @Override
    public void updateBuyerBasicInformation(Account account) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();
        writes.add(updateBuyerCompanyBasicInfo(account));
        writes.add(updateBuyerContactCompanyBasicInfo(account));
        writes.add(updateResponseDraftClientName(String.valueOf(account.getId()), account.getName()));
        writes.add(updateResponseClientName(String.valueOf(account.getId()), account.getName()));

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    @Override
    public void updateHotelManagementCompanyBasicInformation(Account account) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();
        writes.add(updateSupplierContactCompanyBasicInfo(account));

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    @Override
    public void updateHotelMasterChainBasicInformation(HotelChain chain, Account account) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();
        writes.add(updateSupplierContactCompanyBasicInfo(account));
        writes.add(updateSupplierCompanyMasterChainBasicInfo(chain));

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    @Override
    public void updateHotelBrandChainBasicInformation(HotelChain chain, Account account) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();
        writes.add(updateSupplierContactCompanyBasicInfo(account));
        writes.add(updateSupplierCompanyChainBasicInfo(chain));

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    @Override
    public void updateHotelBasicInformation(Entity entity, Account account) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();
        writes.add(updateSupplierCompanyBasicInfo(account));
        writes.add(updateSupplierContactCompanyBasicInfo(account));
        writes.add(updateResponseSupplierData(account, entity, true));
        writes.add(updateResponseSupplierData(account, entity, false));

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));

        updateDistancesForHotel(String.valueOf(account.getId()), account.getCoordinates());
    }

    private UpdateManyModel<HotelRfpBidImpl> updateResponseDraftClientName(String accountId, String accountName){
        return new UpdateManyModel<>(
                and(Arrays.asList(
                        eq(BUYER_COMPANY_ACCOUNT_ID, oid(accountId)),
                        gt(PROGRAM_END_DATE, LocalDate.now()),
                        exists(QUESTIONNAIRE_RESPONSE_DRAFT, true)
                )),
                set(QUESTIONNAIRE_RESPONSE_DRAFT_ANSWERS_CLIENT_NAME, accountName)
        );
    }

    private UpdateManyModel<HotelRfpBidImpl> updateResponseClientName(String accountId, String accountName){
        return new UpdateManyModel<>(
                and(Arrays.asList(
                        eq(BUYER_COMPANY_ACCOUNT_ID, oid(accountId)),
                        gt(PROGRAM_END_DATE, LocalDate.now())
                )),
                set(QUESTIONNAIRE_RESPONSE_ANSWERS_CLIENT_NAME, accountName)
        );
    }

    private UpdateManyModel<HotelRfpBidImpl> updateResponseSupplierData(Account account, Entity entity, boolean responseDraftExists) {
        final List<Bson> queryList = generateUpdateResponseSupplierDataFilter(account.getId(), responseDraftExists);
        final List<Bson> updateList = generateUpdateResponseSupplierDataUpdates(entity, responseDraftExists);

        return new UpdateManyModel<>(and(queryList),combine(updateList));
    }

    private List<Bson> generateUpdateResponseSupplierDataFilter(ObjectId id, boolean responseDraftExists) {
        final List<Bson> queryList = new ArrayList<>();
        queryList.add(eq(SUPPLIER_COMPANY_ACCOUNT_ID, id));
        queryList.add(gt(PROGRAM_END_DATE, LocalDate.now()));
        queryList.add(exists(QUESTIONNAIRE_RESPONSE_DRAFT, responseDraftExists));
        return queryList;
    }

    private List<Bson> generateUpdateResponseSupplierDataUpdates(Entity entity, boolean responseDraftExists) {
        final List<Bson> updateList = new ArrayList<>();

        final List<Bson> responseUpdates =
                HotelRfpBidResponseUtils.createEntityUpdates(entity, QUESTIONNAIRE_RESPONSE_ANSWERS);
        if(responseUpdates != null) updateList.addAll(responseUpdates);

        if(responseDraftExists){
            final List<Bson> responseDraftUpdates =
                    HotelRfpBidResponseUtils.createEntityUpdates(entity, QUESTIONNAIRE_RESPONSE_DRAFT_ANSWERS);
            if(responseDraftUpdates != null) updateList.addAll(responseDraftUpdates);
        }

        return updateList;
    }

    private void updateDistancesForHotel(String accountId, Coordinates hotelCoordinates) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();

        repository
                .find(eq(SUPPLIER_COMPANY_ACCOUNT_ID, oid(accountId)), include(SUBJECT_LOCATION_COORDINATES))
                .forEach(hotelRfpBid -> {
                    final Coordinates tdCoordinates = hotelRfpBid.getTravelDestinationCoordinates();
                    final Distance distance = new DistanceImpl(tdCoordinates, hotelCoordinates, DistanceUnit.MI);

                    writes.add(new UpdateOneModel<>(
                            byId(hotelRfpBid.getId()),
                            combine(
                                    set(DISTANCE_KM, distance.getDistance(DistanceUnit.KM)),
                                    set(DISTANCE_MI, distance.getDistance(DistanceUnit.MI))
                            )
                    ));
                });

        repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    private WriteModel<HotelRfpBidImpl> updateBuyerCompanyBasicInfo(Account account){
        return updateAccountModel(
                account,
                BUYER_COMPANY_ACCOUNT_ID,
                BUYER_COMPANY_NAME,
                BUYER_COMPANY_INDUSTRY,
                BUYER_COMPANY_LOCATION,
                BUYER_COMPANY_WEBSITE
        );
    }

    private WriteModel<HotelRfpBidImpl> updateBuyerContactCompanyBasicInfo(Account account){
        return updateAccountModel(
                account,
                BUYER_CONTACT_COMPANY_ACCOUNT_ID,
                BUYER_CONTACT_COMPANY_NAME,
                BUYER_CONTACT_COMPANY_INDUSTRY,
                BUYER_CONTACT_COMPANY_LOCATION,
                BUYER_CONTACT_COMPANY_WEBSITE
        );
    }

    private WriteModel<HotelRfpBidImpl> updateSupplierCompanyBasicInfo(Account account){
        return updateAccountModel(
                account,
                SUPPLIER_COMPANY_ACCOUNT_ID,
                SUPPLIER_COMPANY_NAME,
                SUPPLIER_COMPANY_INDUSTRY,
                SUPPLIER_COMPANY_LOCATION,
                SUPPLIER_COMPANY_WEBSITE
        );
    }

    private WriteModel<HotelRfpBidImpl> updateSupplierContactCompanyBasicInfo(Account account){
        return updateAccountModel(
                account,
                SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID,
                SUPPLIER_CONTACT_COMPANY_NAME,
                SUPPLIER_CONTACT_COMPANY_INDUSTRY,
                SUPPLIER_CONTACT_COMPANY_LOCATION,
                SUPPLIER_CONTACT_COMPANY_WEBSITE
        );
    }

    private WriteModel<HotelRfpBidImpl> updateSupplierCompanyChainBasicInfo(Entity chain){
        return updateEntityModel(
                chain,
                SUPPLIER_COMPANY_CHAIN_ID,
                SUPPLIER_COMPANY_CHAIN_NAME,
                SUPPLIER_COMPANY_CHAIN_INDUSTRY,
                SUPPLIER_COMPANY_CHAIN_LOCATION,
                SUPPLIER_COMPANY_CHAIN_WEBSITE);
    }

    private WriteModel<HotelRfpBidImpl> updateSupplierCompanyMasterChainBasicInfo(Entity chain){
        return updateEntityModel(
                chain,
                SUPPLIER_COMPANY_CHAIN_MASTER_ID,
                SUPPLIER_COMPANY_CHAIN_MASTER_NAME,
                SUPPLIER_COMPANY_CHAIN_MASTER_INDUSTRY,
                SUPPLIER_COMPANY_CHAIN_MASTER_LOCATION,
                SUPPLIER_COMPANY_CHAIN_MASTER_WEBSITE);
    }

    private WriteModel<HotelRfpBidImpl> updateAccountModel(
            Account account,
            String idField, String nameField, String industryField, String locationField, String websiteField
    ) {
        return new UpdateManyModel<>(
                eq(idField, account.getId()),
                combine(
                        set(nameField, account.getName()),
                        set(industryField, account.getIndustry()),
                        set(locationField, account.getLocation()),
                        account.getWebsite() == null || account.getWebsite().isEmpty()
                                ? unset(websiteField) : set(websiteField, account.getWebsite())
                )
        );
    }

    private WriteModel<HotelRfpBidImpl> updateEntityModel(
            Entity entity,
            String idField, String nameField, String industryField, String locationField, String websiteField
    ) {
        return new UpdateManyModel<>(
                eq(idField, entity.getId()),
                combine(
                        set(nameField, entity.getName()),
                        set(industryField, entity.getIndustry()),
                        set(locationField, entity.getLocation()),
                        entity.getWebsite() == null || entity.getWebsite().isEmpty()
                                ? unset(websiteField) : set(websiteField, entity.getWebsite())
                )
        );
    }
}
