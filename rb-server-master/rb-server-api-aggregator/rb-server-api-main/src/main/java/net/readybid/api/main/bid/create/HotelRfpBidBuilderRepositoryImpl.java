package net.readybid.api.main.bid.create;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
@Service
public class HotelRfpBidBuilderRepositoryImpl implements HotelRfpBidBuilderRepository {

    private final MongoCollection<HotelRfpBidBuilder> hotelRfpBidBuilderCollection;
    private final UserAccountRepository userAccountsRepository;

    @Autowired
    public HotelRfpBidBuilderRepositoryImpl(
            MongoDatabase mongoDatabase,
            UserAccountRepository userAccountsRepository
    ){
        this.userAccountsRepository = userAccountsRepository;
        hotelRfpBidBuilderCollection = mongoDatabase.getCollection(AccountCollection.COLLECTION_NAME, HotelRfpBidBuilder.class);
    }

    @Override
    public HotelRfpBidBuilder getHotelRfpBidBuilder(String hotelId) {
        final HotelRfpBidBuilder builder =  hotelRfpBidBuilderCollection.aggregate(
                Arrays.asList(
                        match(new Document("entityId", oid(hotelId))),
                        project(new Document("account", "$$ROOT")
                                        .append("entityId", "$entityId")
                        ),
                        lookup("Hotel", "entityId", "_id", "hotel"),
                        unwind("$hotel", true)
                )
        ).first();

        if(builder != null) addUserAccounts(builder);

        return builder;
    }

    private void addUserAccounts(HotelRfpBidBuilder builder) {
        final List<? extends UserAccount> userAccounts = userAccountsRepository.getUserAccountsByAccountId(builder.getAccountId());
        builder.setUserAccounts(userAccounts);
    }
}
