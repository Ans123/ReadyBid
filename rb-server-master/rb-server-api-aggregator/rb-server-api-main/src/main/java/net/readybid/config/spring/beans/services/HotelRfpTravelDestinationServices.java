package net.readybid.config.spring.beans.services;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.RfpRepository;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.service.ListBidsRepository;
import net.readybid.app.core.service.traveldestination.CreateTravelDestinationService;
import net.readybid.app.core.service.traveldestination.GetTravelDestinationService;
import net.readybid.app.core.service.traveldestination.LoadTravelDestinationRepository;
import net.readybid.app.core.service.traveldestination.SaveTravelDestinationRepository;
import net.readybid.app.core.transaction.CreateTravelDestinationTransaction;
import net.readybid.app.core.transaction.GetTravelDestinationTransaction;
import net.readybid.core.rfp.hotel.bid.DeleteBidsTransaction;
import net.readybid.core.rfp.hotel.bid.UpdateBidsTransaction;
import net.readybid.core.rfp.hotel.traveldestination.DeleteTravelDestinationTransaction;
import net.readybid.core.rfp.hotel.traveldestination.DeleteTravelDestinationTransactionImpl;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationTransaction;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationTransactionImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelRfpTravelDestinationServices {

    @Bean
    public GetTravelDestinationService getTravelDestinationService(
            RfpAccessControlService rfpAccessControlService,
            LoadTravelDestinationRepository loadTravelDestinationRepository,
            ListBidsRepository listBidsRepository
    ){
        return new GetTravelDestinationTransaction(rfpAccessControlService, loadTravelDestinationRepository, listBidsRepository);
    }

    @Bean
    public CreateTravelDestinationService createTravelDestinationService(
            RfpAccessControlService rfpAccessControlService,
            CreateTravelDestinationAction createTravelDestinationInteractor,
            SaveTravelDestinationRepository travelDestinationRepository,
            RfpRepository rfpRepository
    ){
        return new CreateTravelDestinationTransaction(rfpAccessControlService, createTravelDestinationInteractor, travelDestinationRepository, rfpRepository);
    }

    @Bean
    public UpdateTravelDestinationTransaction updateTravelDestinationTransaction(
            CreateTravelDestinationAction createTravelDestinationInteractor,
            SaveTravelDestinationRepository saveTravelDestinationRepository,
            LoadTravelDestinationRepository loadTravelDestinationRepository,
            UpdateBidsTransaction updateBidsTransaction
    ){
        return new UpdateTravelDestinationTransactionImpl(
                createTravelDestinationInteractor,
                loadTravelDestinationRepository,
                saveTravelDestinationRepository,
                updateBidsTransaction
        );
    }

    @Bean
    public DeleteTravelDestinationTransaction deleteTravelDestinationTransaction(
            CreateTravelDestinationAction travelDestinationFactory,
            DeleteBidsTransaction deleteBidsTransaction,
            SaveTravelDestinationRepository saveTravelDestinationRepository,
            LoadTravelDestinationRepository loadTravelDestinationRepository
    ){
        return new DeleteTravelDestinationTransactionImpl(
                deleteBidsTransaction,
                loadTravelDestinationRepository,
                saveTravelDestinationRepository,
                travelDestinationFactory
        );
    }
}
