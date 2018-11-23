package net.readybid.app;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Country;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.persistence.mongodb.repository.mapping.codec.*;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpBidQueryViewMapping;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpContactAccountMapping;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpRepresentativeMapping;
import net.readybid.app.persistence.mongodb.repository.mapping.mapping.HotelRfpSupplierContactMapping;
import net.readybid.auth.account.core.AccountStatus;
import net.readybid.auth.account.db.AccountImplCodecWithProvider;
import net.readybid.auth.account.db.AccountStatusDetailsCodecWithProvider;
import net.readybid.auth.permissions.PermissionsImplCodecWithProvider;
import net.readybid.auth.user.UserStatus;
import net.readybid.auth.useraccount.UserAccountStatus;
import net.readybid.entities.chain.HotelChainImplCodecWithProvider;
import net.readybid.location.db.AddressImplCodecWithProvider;
import net.readybid.location.db.CoordinatesImplCodecWithProvider;
import net.readybid.location.db.LocationImplCodecWithProvider;
import net.readybid.mongodb.CreationDetailsCodecWithProvider;
import net.readybid.mongodb.EnumCodec;
import net.readybid.mongodb.GeoJsonPointCodecWithProvider;
import net.readybid.mongodb.LocalDateCodec;
import net.readybid.rfp.specifications.RfpSpecificationsImplCodecWithProvider;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.supplier.HotelRfpSupplierCodecWithProvider;
import net.readybid.rfphotel.supplier.RfpHotelImplCodecWithProvider;
import net.readybid.user.BasicUserDetailsImplCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@Configuration
@Profile("integrationTest")
public class MongoIntegrationTestConfiguration{

    private final Environment env;
    private MongoClient client;

    @Autowired
    public MongoIntegrationTestConfiguration(Environment env) {
        this.env = env;
    }

    private String getDatabaseName() {
        return env.getProperty("mongodb.main.database");
    }

    private void mongoClient() {
        MongoClientOptions options = MongoClientOptions
                .builder()
                .codecRegistry(createCodecRegistry())
                .connectTimeout(6000)
                .build();

        client = new MongoClient(getHosts(), options);
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

//    @Override
//    public CustomConversions customConversions() {
//        return new CustomConversions(Arrays.asList(new LocalDateToStringConverter(), new StringToLocalDateConverter()));
//    }

    private CodecRegistry createCodecRegistry() {
        final BsonTypeClassMap bsonTypeClassMap = new BsonTypeClassMap();

        return CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(
                        // address
                        new EnumCodec<>(Country.class),

                        // entity
                        new EnumCodec<>(EntityType.class),
                        new EnumCodec<>(EntityIndustry.class),
//                        new EnumCodec<>(EntityStatus.class),
//                        new EnumCodec<>(HotelChainType.class),

                        // RFP
                        new EnumCodec<>(RfpType.class),
//                        new EnumCodec<>(RfpStatus.class),
//                        new EnumCodec<>(TravelDestinationStatus.class),
//                        new EnumCodec<>(TravelDestinationType.class),
                        new EnumCodec<>(HotelRfpBidStateStatus.class),
//                        new EnumCodec<>(DistanceUnit.class),
//
//                        new EnumCodec<>(BidManagerViewType.class),
//                        new EnumCodec<>(BidManagerViewSide.class),
                        new EnumCodec<>(QuestionnaireSectionType.class),
                        new EnumCodec<>(QuestionnaireQuestionType.class),

                        // Values
//                        new EnumCodec<>(ValueType.class),
//                        new EnumCodec<>(NegotiationValueType.class),
//                        new EnumCodec<>(NegotiationValueChangeType.class),

                        // UserRepository
                        new EnumCodec<>(UserStatus.class),
                        new EnumCodec<>(UserAccountStatus.class),

                        new EnumCodec<>(AccountStatus.class),
//                        new EnumCodec<>(Permission.class),
//                        new EnumCodec<>(InvitationStatus.class),

                        //
                        new LocalDateCodec(),
//                        new BigDecimalCodec()
                        new IdCodec()
                ),
                fromProviders(
//                        new LinkedHashMapCodec(bsonTypeClassMap),
//                        new HashMapCodec(bsonTypeClassMap),

                        // core
                        new CreationDetailsCodecWithProvider(bsonTypeClassMap),

                        // entity
//                        new EntityStatusDetailsCodecWithProvider(bsonTypeClassMap),
//                        new EntityImageImplCodecWithProvider(bsonTypeClassMap),
//                        new EntityImageViewCodecWithProvider(bsonTypeClassMap),
//
//                        new AgencyImplCodecWithProvider(bsonTypeClassMap),
//
//                        new HotelChainListItemViewModelCodecWithProvider(bsonTypeClassMap),
//
//                        new CompanyImplCodecWithProvider(bsonTypeClassMap),
//
//                        new HotelManagementCompanyImplCodecWithProvider(bsonTypeClassMap),
                        new HotelChainImplCodecWithProvider(bsonTypeClassMap),
//
//                        new ConsultancyImplCodecWithProvider(bsonTypeClassMap),
//
//                        new EntityImplCodecWithProvider(bsonTypeClassMap),
//
//                        new HotelImplCodecWithProvider(bsonTypeClassMap),
//                        new TravelDestinationManagerHotelViewModelCodecWithProvider(bsonTypeClassMap),
//                        new HotelCategoryImplCodecWithProvider(bsonTypeClassMap),

                        // entity rfp
//                        new EntityRateLoadingInformationImplCodecWithProvider(bsonTypeClassMap),
//                        new RateLoadingInformationImplCodecWithProvider(bsonTypeClassMap),

                        // location
                        new LocationImplCodecWithProvider(bsonTypeClassMap),
//                        new LocationViewModelCodecWithProvider(bsonTypeClassMap),
                        new AddressImplCodecWithProvider(bsonTypeClassMap),
//                        new AddressViewCodecWithProvider(bsonTypeClassMap),
                        new CoordinatesImplCodecWithProvider(bsonTypeClassMap),
//                        new CoordinatesViewCodecWithProvider(bsonTypeClassMap),
//                        new DistanceImplCodecWithProvider(bsonTypeClassMap),

                        // mongo
                        new GeoJsonPointCodecWithProvider(bsonTypeClassMap),

                        // rfp
//                        new RfpTemplateListItemViewModelCodecWithProvider(bsonTypeClassMap),
//                        new RfpTemplateImplCodecWithProvider(bsonTypeClassMap),
//
                        new RfpImplCodecWithProvider(bsonTypeClassMap),
//
                        new RfpSpecificationsImplCodecWithProvider(bsonTypeClassMap),
//                        new BuyerImplCodecWithProvider(bsonTypeClassMap),
//
//                        new RfpCompanyImplCodecWithProvider(bsonTypeClassMap),
//                        new RfpContactImplCodecWithProvider(bsonTypeClassMap),
//                        new RfpStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new QuestionnaireImplCodecWithProvider(bsonTypeClassMap),
                        new QuestionnaireResponseImplCodecWithProvider(bsonTypeClassMap),
                        new QuestionnaireConfigurationItemCodecWithProvider(bsonTypeClassMap),
                        new HotelRfpDefaultResponseCodec(bsonTypeClassMap),

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
                        new HotelRfpBidStateCodec(bsonTypeClassMap),
                        new HotelRfpBidSimpleStateCodec(bsonTypeClassMap),
                        new HotelRfpBidReceivedStateCodec(bsonTypeClassMap),
//                        new HotelRfpBidOfferImplCodecWithProvider(bsonTypeClassMap),
//                        new HotelRfpBidOfferDetailsCodecWithProvider(bsonTypeClassMap),

                        // bid manager view
//                        new BidManagerViewImplCodecWithProvider(bsonTypeClassMap),

                        // travel destination
//                        new TravelDestinationImplCodecWithProvider(bsonTypeClassMap),
//                        new TravelDestinationStatusDetailsCodecWithProvider(bsonTypeClassMap),
//                        new TravelDestinationHotelFilterCodecWithProvider(bsonTypeClassMap),
//                        new TravelDestinationListItemViewModelCodecWithProvider(bsonTypeClassMap),

                        // negotiations
//                        new HotelRfpNegotiationsImplCodecWithProvider(bsonTypeClassMap),
//                        new HotelRfpNegotiationsConfigCodecWithProvider(bsonTypeClassMap),
//                        new NegotiationsPartiesImplCodecWithProvider(bsonTypeClassMap),
//                        new NegotiatorImplCodecWithProvider(bsonTypeClassMap),
//                        new HotelRfpNegotiationCodecWithProvider(bsonTypeClassMap),
//                        new HotelRfpNegotiationValuesCodecWithProvider(bsonTypeClassMap),

                        // negotiation values
//                        new NegotiationFixedValueCodecWithProvider(bsonTypeClassMap),
//                        new NegotiationInvalidValueCodecWithProvider(bsonTypeClassMap),
//                        new NegotiationMockedValueCodecWithProvider(bsonTypeClassMap),
//                        new NegotiationPercentageValueCodecWithProvider(bsonTypeClassMap),
//                        new NegotiationUnavailableValueCodecWithProvider(bsonTypeClassMap),

//                        new ValueCodecWithProvider(bsonTypeClassMap),

                        // user
                        new BasicUserDetailsImplCodecWithProvider(bsonTypeClassMap),
//                        new AuthenticatedUserImplCodecWithProvider(bsonTypeClassMap),
//                        new UserStatusDetailsCodecWithProvider(bsonTypeClassMap),
//
//                        new UserAccountStatusDetailsCodecWithProvider(bsonTypeClassMap),
//                        new UserAccountImplCodecWithProvider(bsonTypeClassMap),
//                        new UserImplCodecWithProvider(bsonTypeClassMap),


                        new AccountStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new AccountImplCodecWithProvider(bsonTypeClassMap),
                        new PermissionsImplCodecWithProvider(bsonTypeClassMap)
//                        new HotelRfpBidBuilderCodecWithProvider(bsonTypeClassMap),
//                        new InvitationImplCodecWithProvider(bsonTypeClassMap),
//                        new InvitationStatusDetailsCodecWithProvider(bsonTypeClassMap),
//
//                        new LoginAttemptCodecWithProvider(bsonTypeClassMap)
                ),
                MongoClient.getDefaultCodecRegistry());
    }
}
