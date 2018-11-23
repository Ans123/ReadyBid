package net.readybid.api.auth.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import net.readybid.api.auth.db.ResetPasswordAttemptImplCodecWithProvider;
import net.readybid.api.auth.db.UserRegistrationImplCodecWithProvider;
import net.readybid.api.auth.resetpassword.ResetPasswordAttemptState;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Country;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.persistence.mongodb.repository.mapping.ContactUsMessageCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.CustomRfpMessageCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.codec.*;
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
import net.readybid.auth.user.UserStatus;
import net.readybid.auth.user.UserStatusDetailsCodecWithProvider;
import net.readybid.auth.useraccount.UserAccountStatus;
import net.readybid.auth.useraccount.UserAccountStatusDetailsCodecWithProvider;
import net.readybid.auth.useraccount.db.UserAccountImplCodecWithProvider;
import net.readybid.bidmanagerview.BidManagerViewImplCodecWithProvider;
import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.bidmanagerview.BidManagerViewType;
import net.readybid.location.db.AddressImplCodecWithProvider;
import net.readybid.location.db.CoordinatesImplCodecWithProvider;
import net.readybid.location.db.LocationImplCodecWithProvider;
import net.readybid.mongodb.*;
import net.readybid.rfp.company.RfpCompanyImplCodecWithProvider;
import net.readybid.rfp.contact.RfpContactImplCodecWithProvider;
import net.readybid.rfp.type.RfpType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

/**
 * Created by DejanK on 9/7/2016.
 *
 */
@Configuration
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
    public MongoClient getClient() throws Exception {
        if(client == null) mongoClient();
        return client;
    }

    @Bean(name = "mongoDatabaseMain")
    public MongoDatabase getDatabase() throws Exception {
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
//                        new EnumCodec<>(EntityStatus.class),

                        // RFP
                        new EnumCodec<>(RfpType.class),
//                        new EnumCodec<>(RfpStatus.class),
//                        new EnumCodec<>(TravelDestinationStatus.class),
//                        new EnumCodec<>(DistanceUnit.class),

                        new EnumCodec<>(BidManagerViewType.class),
                        new EnumCodec<>(BidManagerViewSide.class),
                        new EnumCodec<>(QuestionnaireSectionType.class),
                        new EnumCodec<>(QuestionnaireQuestionType.class),

                        // User
                        new EnumCodec<>(UserStatus.class),
                        new EnumCodec<>(UserAccountStatus.class),
                        new EnumCodec<>(AccountStatus.class),
                        new EnumCodec<>(Permission.class),

                        new EnumCodec<>(ResetPasswordAttemptState.class),
                        new EnumCodec<>(InvitationStatus.class),

                        //
                        new LocalDateCodec(),
                        new ZonedDateTimeCodec()
                ),
                fromProviders(
                        new LinkedHashMapCodec(bsonTypeClassMap),

                        // core
                        new CreationDetailsCodecWithProvider(bsonTypeClassMap),

//                        // entity
//                        new EntityStatusDetailsCodecWithProvider(bsonTypeClassMap),
//                        new EntityImageImplCodecWithProvider(bsonTypeClassMap),
//
//                        new AgencyImplCodecWithProvider(bsonTypeClassMap),
//
//                        new ChainBasicDetailsImplCodecWithProvider(bsonTypeClassMap),
//                        new HotelChainListItemViewModelCodecWithProvider(bsonTypeClassMap),
//
//                        new CompanyImplCodecWithProvider(bsonTypeClassMap),
//
//                        new HotelManagementCompanyImplCodecWithProvider(bsonTypeClassMap),
//                        new HotelChainImplCodecWithProvider(bsonTypeClassMap),
//
//                        new ConsultancyImplCodecWithProvider(bsonTypeClassMap),
//
//                        new EntityImplCodecWithProvider(bsonTypeClassMap),
//
//                        new HotelImplCodecWithProvider(bsonTypeClassMap),
//                        new HotelCategoryImplCodecWithProvider(bsonTypeClassMap),

                        // guest
//                        new GuestRepresentativeCodecWithProvider(bsonTypeClassMap),
//                        new ContactUsMessageImplCodecWithProvider(bsonTypeClassMap),
//                        new CustomRfpMessageImplCodecWithProvider(bsonTypeClassMap),

                        // location
                        new LocationImplCodecWithProvider(bsonTypeClassMap),
                        new AddressImplCodecWithProvider(bsonTypeClassMap),
                        new CoordinatesImplCodecWithProvider(bsonTypeClassMap),
//                        new DistanceImplCodecWithProvider(bsonTypeClassMap),

                        // mongo
                        new GeoJsonPointCodecWithProvider(bsonTypeClassMap),

                        // rfp
//                        new RfpTemplateListItemViewModelCodecWithProvider(bsonTypeClassMap),
//                        new RfpTemplateImplCodecWithProvider(bsonTypeClassMap),

//                        new RfpImplCodecWithProvider(bsonTypeClassMap),

//                        new RfpSpecificationsImplCodecWithProvider(bsonTypeClassMap),
//                        new BuyerImplCodecWithProvider(bsonTypeClassMap),
                        new RfpCompanyImplCodecWithProvider(bsonTypeClassMap),
                        new RfpContactImplCodecWithProvider(bsonTypeClassMap),
//                        new RfpStatusDetailsCodecWithProvider(bsonTypeClassMap),

                        // bid
//                        new BidImplCodecWithProvider(bsonTypeClassMap),
//                        new HotelRfpBidStatusDetailsCodecWithProvider(bsonTypeClassMap),
//                        new HotelRfpSupplierCodecWithProvider(bsonTypeClassMap),

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
                        new ListQuestionnaireQuestionOptionCodec(bsonTypeClassMap),
                        new QuestionnaireQuestionValidatorCodec(bsonTypeClassMap),

                        // bid manager view
                        new BidManagerViewImplCodecWithProvider(bsonTypeClassMap),

                        // travel destination
//                        new TravelDestinationImplCodecWithProvider(bsonTypeClassMap),
//                        new TravelDestinationStatusDetailsCodecWithProvider(bsonTypeClassMap),
//                        new TravelDestinationHotelFilterCodecWithProvider(bsonTypeClassMap),
//                        new TravelDestinationListItemViewModelCodecWithProvider(bsonTypeClassMap),

                        // user
                        new BasicUserDetailsImplCodecWithProvider(bsonTypeClassMap),
                        new AuthenticatedUserImplCodecWithProvider(bsonTypeClassMap),
                        new UserRegistrationImplCodecWithProvider(bsonTypeClassMap),
                        new ResetPasswordAttemptImplCodecWithProvider(bsonTypeClassMap),
                        new LoginAttemptCodecWithProvider(bsonTypeClassMap),

                        new UserStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new UserAccountStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new UserAccountImplCodecWithProvider(bsonTypeClassMap),
                        new PermissionsImplCodecWithProvider(bsonTypeClassMap),

                        new AccountStatusDetailsCodecWithProvider(bsonTypeClassMap),
                        new AccountImplCodecWithProvider(bsonTypeClassMap),

                        new InvitationImplCodecWithProvider(bsonTypeClassMap),
                        new InvitationStatusDetailsCodecWithProvider(bsonTypeClassMap),

                        ContactUsMessageCollection.codec(bsonTypeClassMap),
                        CustomRfpMessageCollection.codec(bsonTypeClassMap)
                ),
                MongoClient.getDefaultCodecRegistry());
    }
}
