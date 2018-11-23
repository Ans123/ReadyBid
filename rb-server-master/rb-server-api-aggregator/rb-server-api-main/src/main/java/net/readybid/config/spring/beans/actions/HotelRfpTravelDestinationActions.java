package net.readybid.config.spring.beans.actions;

import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.interactor.CreateTravelDestinationInteractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelRfpTravelDestinationActions {

    @Bean
    public CreateTravelDestinationAction createTravelDestinationAction(IdFactory idFactory){
        return new CreateTravelDestinationInteractor(idFactory);
    }
}
