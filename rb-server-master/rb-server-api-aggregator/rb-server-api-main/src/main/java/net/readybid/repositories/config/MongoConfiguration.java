package net.readybid.repositories.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import net.readybid.api.main.bid.create.HotelRfpBidBuilderCodecWithProvider;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Country;
import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatus;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.app.entities.rfp_hotel.bid.offer.ValueType;
import net.readybid.app.gate.repository.TravelDestinationManagerHotelViewModelCodecWithProvider;
import net.readybid.app.persistence.mongodb.repository.mapping.LetterTemplateCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.codec.*;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpBidQueryViewMapping;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpContactAccountMapping;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpRepresentativeMapping;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpSupplierContactMapping;
import net.readybid.auth.account.core.AccountStatus;
import net.readybid.auth.account.db.AccountImplCodecWithProvider;
import net.readybid.auth.account.db.AccountStatusDetailsCodecWithProvider;
import net.readybid.auth.invitation.InvitationImplCodecWithProvider;
import net.readybid.auth.invitation.InvitationStatus;
import net.readybid.auth.invitation.InvitationStatusDetailsCodecWithProvider;
import net.readybid.auth.login.LoginAttemptCodecWithProvider;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.permissions.PermissionsImplCodecWithProvider;
import net.readybid.auth.user.AuthenticatedUserImplCodecWithProvider;
import net.readybid.auth.user.UserImplCodecWithProvider;
import net.readybid.auth.user.UserStatus;
import net.readybid.auth.user.UserStatusDetailsCodecWithProvider;
import net.readybid.auth.useraccount.UserAccountStatus;
import net.readybid.auth.useraccount.UserAccountStatusDetailsCodecWithProvider;
import net.readybid.auth.useraccount.db.UserAccountImplCodecWithProvider;
import net.readybid.bidmanagerview.BidManagerViewImplCodecWithProvider;
import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.bidmanagerview.BidManagerViewType;
import net.readybid.entities.agency.db.AgencyImplCodecWithProvider;
import net.readybid.entities.chain.HotelChainImplCodecWithProvider;
import net.readybid.entities.chain.HotelChainType;
import net.readybid.entities.chain.db.HotelChainListItemViewModelCodecWithProvider;
import net.readybid.entities.company.db.CompanyImplCodecWithProvider;
import net.readybid.entities.consultancy.db.ConsultancyImplCodecWithProvider;
import net.readybid.entities.core.EntityStatus;
import net.readybid.entities.core.db.EntityImageImplCodecWithProvider;
import net.readybid.entities.core.db.EntityImageViewCodecWithProvider;
import net.readybid.entities.core.db.EntityImplCodecWithProvider;
import net.readybid.entities.core.db.EntityStatusDetailsCodecWithProvider;
import net.readybid.entities.hmc.HotelManagementCompanyImplCodecWithProvider;
import net.readybid.entities.hotel.db.HotelCategoryImplCodecWithProvider;
import net.readybid.entities.hotel.db.HotelImplCodecWithProvider;
import net.readybid.entities.rfp.EntityRateLoadingInformationImplCodecWithProvider;
import net.readybid.entities.rfp.RateLoadingInformationImplCodecWithProvider;
import net.readybid.location.db.*;
import net.readybid.mongodb.*;
import net.readybid.rfp.buyer.BuyerImplCodecWithProvider;
import net.readybid.rfp.company.RfpCompanyImplCodecWithProvider;
import net.readybid.rfp.contact.RfpContactImplCodecWithProvider;
import net.readybid.rfp.core.RfpStatus;
import net.readybid.rfp.core.RfpStatusDetailsCodecWithProvider;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfp.specifications.RfpSpecificationsImplCodecWithProvider;
import net.readybid.rfp.template.RfpTemplateListItemViewModelCodecWithProvider;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.negotiations.*;
import net.readybid.rfphotel.bid.core.negotiations.values.*;
import net.readybid.rfphotel.destinations.db.TravelDestinationHotelFilterCodecWithProvider;
import net.readybid.rfphotel.destinations.db.TravelDestinationImplCodecWithProvider;
import net.readybid.rfphotel.destinations.db.TravelDestinationListItemViewModelCodecWithProvider;
import net.readybid.rfphotel.destinations.db.TravelDestinationStatusDetailsCodecWithProvider;
import net.readybid.rfphotel.supplier.HotelRfpSupplierCodecWithProvider;
import net.readybid.rfphotel.supplier.RfpHotelImplCodecWithProvider;
import net.readybid.user.BasicUserDetailsImplCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

/**
 * Created by DejanK on 9/7/2016.
 *
 */
@Configuration
@EnableMongoRepositories 
public class MongoConfiguration extends AbstractMongoConfiguration {

    private final Environment env;
    private MongoClient client;

    @Autowired
    public MongoConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    protected String getDatabaseName() {
        return env.getProperty("mongodb.main.database");
    }

    @Override
    public MongoClient mongoClient() {
        MongoClientOptions options = MongoClientOptions
                .builder()
                .codecRegistry(createCodecRegistry())
                .connectTimeout(6000)
                .build();

        client = new MongoClient(getHosts(), options);
        return client;
    }

    private List<ServerAddress> getHosts() {
        final String[] hosts = env.getProperty("mongodb.main.hosts").split(",");
        final List<ServerAddress> serverAddressList = new ArrayList<>();
        for(String h : hosts){
            h = h.trim();
            String[] hostParts = h.split(":");
            ServerAddress address = hostParts.length == 2
                    ? new ServerAddress(hostParts[0], Integer.parseInt(hostParts[1]))
                    : new ServerAddress(hostParts[0]);
            serverAddressList.add(address);
        }
        return serverAddressList;
    }

    @Bean(name = "mongoClientMain")
    public MongoClient getClient() {
        if(client == null) mongoClient();
        return client;
    }

    @Bean(name = "mongoDatabaseMain")
    public MongoDatabase getDatabase() {
        if(client == null) mongoClient();
        return client.getDatabase(getDatabaseName());
    }

    @Bean
    public CodecRegistry getCodecRegistry() {
        if(client == null) mongoClient();
        return client.getMongoClientOptions().getCodecRegistry();
    }

    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(new LocalDateToStringConverter(), new StringToLocalDateConverter()));
    }

    private CodecRegistry createCodecRegistry() {
        final BsonTypeClassMap bsonTypeClassMap = new BsonTypeClassMap();

        return CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(
                        // address
                        new EnumCodec<>(Country.class),

                        // entity
                        new EnumCodec<>(EntityType.class),
                        new EnumCodec<>(EntityIndustry.class),
                        new EnumCodec<>(EntityStatus.class),
                        new EnumCodec<>(HotelChainType.class),

                        // RFP
                        new EnumCodec<>(RfpType.class),
                        new EnumCodec<>(HotelRfpType.class),
                        new EnumCodec<>(RfpStatus.class),
                        new EnumCodec<>(TravelDestinationStatus.class),
                        new EnumCodec<>(TravelDestinationType.class),
                        new EnumCodec<>(HotelRfpBidStateStatus.class),
                        new EnumCodec<>(DistanceUnit.class),

                        new EnumCodec<>(BidManagerViewType.class),
                        new EnumCodec<>(BidManagerViewSide.class),
                        new EnumCodec<>(QuestionnaireSectionType.class),
                        new EnumCodec<>(QuestionnaireQuestionType.class),

                        // Values
                        new EnumCodec<>(ValueType.class),
                        new EnumCodec<>(NegotiationValueType.class),
                        new EnumCodec<>(NegotiationValueChangeType.class),

                        // UserRepository
                        new EnumCodec<>(UserStatus.class),
                        new EnumCodec<>(UserAccountStatus.class),

                        new EnumCodec<>(AccountStatus.class),
                        new EnumCodec<>(Permission.class),
                        new EnumCodec<>(InvitationStatus.class),

                        //
                        new LocalDateCodec(),
                        new BigDecimalCodec(),
                        new IdCodec()
                ),
                fromProviders(
                        new LinkedHashMapCodec(bsonTypeClassMap),
                        new HashMapCodec(bsonTypeClassMap),

                        // core
                        new CreationDetailsCodecWithProvider(bsonTypeClassMap),

                        // entity
                        new EntityStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new EntityImageImplCodecWithProvider(bsonTypeClassMap),
                        new EntityImageViewCodecWithProvider(bsonTypeClassMap),

                        new AgencyImplCodecWithProvider(bsonTypeClassMap),

                        new HotelChainListItemViewModelCodecWithProvider(bsonTypeClassMap),

                        new CompanyImplCodecWithProvider(bsonTypeClassMap),

                        new HotelManagementCompanyImplCodecWithProvider(bsonTypeClassMap),
                        new HotelChainImplCodecWithProvider(bsonTypeClassMap),

                        new ConsultancyImplCodecWithProvider(bsonTypeClassMap),

                        new EntityImplCodecWithProvider(bsonTypeClassMap),

                        new HotelImplCodecWithProvider(bsonTypeClassMap),
                        new TravelDestinationManagerHotelViewModelCodecWithProvider(bsonTypeClassMap),
                        new HotelCategoryImplCodecWithProvider(bsonTypeClassMap),

                        // entity rfp
                        new EntityRateLoadingInformationImplCodecWithProvider(bsonTypeClassMap),
                        new RateLoadingInformationImplCodecWithProvider(bsonTypeClassMap),

                        // location
                        new LocationImplCodecWithProvider(bsonTypeClassMap),
                        new LocationViewModelCodecWithProvider(bsonTypeClassMap),
                        new AddressImplCodecWithProvider(bsonTypeClassMap),
                        new AddressViewCodecWithProvider(bsonTypeClassMap),
                        new CoordinatesImplCodecWithProvider(bsonTypeClassMap),
                        new CoordinatesViewCodecWithProvider(bsonTypeClassMap),
                        new DistanceImplCodecWithProvider(bsonTypeClassMap),

                        // mongo
                        new GeoJsonPointCodecWithProvider(bsonTypeClassMap),

                        // rfp
                        new RfpTemplateListItemViewModelCodecWithProvider(bsonTypeClassMap),
                        new RfpTemplateImplCodecWithProvider(bsonTypeClassMap),

                        new RfpImplCodecWithProvider(bsonTypeClassMap),

                        new RfpSpecificationsImplCodecWithProvider(bsonTypeClassMap),
                        new BuyerImplCodecWithProvider(bsonTypeClassMap),

                        new RfpCompanyImplCodecWithProvider(bsonTypeClassMap),
                        new RfpContactImplCodecWithProvider(bsonTypeClassMap),
                        new RfpStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new QuestionnaireImplCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpAcceptedRatesCodec(bsonTypeClassMap),
                        new QuestionnaireResponseImplCodecWithProvider(bsonTypeClassMap),
                        new QuestionnaireConfigurationItemCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpDefaultResponseCodec(bsonTypeClassMap),
                        LetterTemplateCollection.codec(bsonTypeClassMap),

                        // questionnaire
                        new QuestionnaireFormImplCodec(bsonTypeClassMap),
                        new QuestionnaireModuleCodec(bsonTypeClassMap),
                        new QuestionnaireSectionCodec(bsonTypeClassMap),
                        new QuestionnaireGroupCodec(bsonTypeClassMap),
                        new QuestionnaireTableCodec(bsonTypeClassMap),
                        new QuestionnaireTableRowCodec(bsonTypeClassMap),
                        new QuestionnaireTableRowCellCodec(bsonTypeClassMap),
                        new QuestionnaireHotelRfpRateGridCodec(bsonTypeClassMap),
                        new QuestionnaireQuestionCodec(bsonTypeClassMap),
                        new DateQuestionnaireQuestionCodec(bsonTypeClassMap),
                        new DecimalQuestionnaireQuestionCodec(bsonTypeClassMap),
                        new ListQuestionnaireQuestionCodec(bsonTypeClassMap),
                        new NumberQuestionnaireQuestionCodec(bsonTypeClassMap),
                        new QuestionnaireQuestionImplCodec(bsonTypeClassMap),
                        new TextQuestionnaireQuestionCodec(bsonTypeClassMap),
                        new UserDefinedQuestionnaireQuestionCodec(bsonTypeClassMap),
                        new ListQuestionnaireQuestionOptionCodec(bsonTypeClassMap),
                        new QuestionnaireQuestionValidatorCodec(bsonTypeClassMap),

                        // hotel rfp
                        HotelRfpRepresentativeMapping.codec(bsonTypeClassMap),
                        HotelRfpSupplierContactMapping.codec(bsonTypeClassMap),
                        HotelRfpContactAccountMapping.codec(bsonTypeClassMap),
                        HotelRfpBidQueryViewMapping.codec(bsonTypeClassMap),

                        // bid
                        new HotelRfpBidImplCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpSupplierCodecWithProvider(bsonTypeClassMap),
                        new RfpHotelImplCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpBidOfferImplCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpBidOfferDetailsCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpBidStateCodec(bsonTypeClassMap),
                        new HotelRfpBidSimpleStateCodec(bsonTypeClassMap),
                        new HotelRfpBidReceivedStateCodec(bsonTypeClassMap),
                        new HotelRfpBidSetSupplierContactAndSendBidCommandProducerImplDecoder(bsonTypeClassMap),

                        // bid manager view
                        new BidManagerViewImplCodecWithProvider(bsonTypeClassMap),

                        // travel destination
                        new TravelDestinationImplCodecWithProvider(bsonTypeClassMap),
                        new TravelDestinationStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new TravelDestinationHotelFilterCodecWithProvider(bsonTypeClassMap),
                        new TravelDestinationListItemViewModelCodecWithProvider(bsonTypeClassMap),

                        // negotiations
                        new HotelRfpNegotiationsImplCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpNegotiationsConfigCodecWithProvider(bsonTypeClassMap),
                        new NegotiationsPartiesImplCodecWithProvider(bsonTypeClassMap),
                        new NegotiatorImplCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpNegotiationCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpNegotiationValuesCodecWithProvider(bsonTypeClassMap),

                        // negotiation values
                        new NegotiationFixedValueCodecWithProvider(bsonTypeClassMap),
                        new NegotiationInvalidValueCodecWithProvider(bsonTypeClassMap),
                        new NegotiationMockedValueCodecWithProvider(bsonTypeClassMap),
                        new NegotiationPercentageValueCodecWithProvider(bsonTypeClassMap),
                        new NegotiationUnavailableValueCodecWithProvider(bsonTypeClassMap),

                        new ValueCodecWithProvider(bsonTypeClassMap),

                        // user
                        new BasicUserDetailsImplCodecWithProvider(bsonTypeClassMap),
                        new AuthenticatedUserImplCodecWithProvider(bsonTypeClassMap),
                        new UserStatusDetailsCodecWithProvider(bsonTypeClassMap),

                        new UserAccountStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new UserAccountImplCodecWithProvider(bsonTypeClassMap),
                        new UserImplCodecWithProvider(bsonTypeClassMap),


                        new AccountStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new AccountImplCodecWithProvider(bsonTypeClassMap),
                        new PermissionsImplCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpBidBuilderCodecWithProvider(bsonTypeClassMap),
                        new InvitationImplCodecWithProvider(bsonTypeClassMap),
                        new InvitationStatusDetailsCodecWithProvider(bsonTypeClassMap),

                        new LoginAttemptCodecWithProvider(bsonTypeClassMap)


                ),
                MongoClient.getDefaultCodecRegistry());
    }
}
