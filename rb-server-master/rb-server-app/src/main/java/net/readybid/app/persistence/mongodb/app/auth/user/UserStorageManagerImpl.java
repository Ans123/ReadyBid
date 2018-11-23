package net.readybid.app.persistence.mongodb.app.auth.user;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import net.readybid.app.interactors.authentication.user.BasicInformation;
import net.readybid.app.interactors.authentication.user.gate.UserStorageManager;
import net.readybid.app.persistence.mongodb.repository.UserRepository;
import net.readybid.auth.user.User;
import net.readybid.mongodb.RbDuplicateKeyException;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.*;
import static net.readybid.app.persistence.mongodb.repository.mapping.UserCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
class UserStorageManagerImpl implements UserStorageManager {

    private final UserRepository userRepository;

    @Autowired
    UserStorageManagerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User updateBasicDetails(String userId, BasicInformation basicInformation) {
        final List<Bson> updates = new ArrayList<>();
        updates.add(set(FIRST_NAME, basicInformation.firstName));
        updates.add(set(LAST_NAME, basicInformation.lastName));
        updates.add(basicInformation.phone == null || basicInformation.phone.isEmpty()
                ? unset(PHONE)
                : set(PHONE, basicInformation.phone)
        );
        return userRepository.findAndUpdate(byId(userId), combine(updates), createReturnNew());
    }

    @Override
    public User updateEmailAddress(String userId, String emailAddress) {
        try {
            return userRepository.findAndUpdate(
                    byId(userId),
                    set(EMAIL_ADDRESS, emailAddress),
                    createReturnNew()
            );
        } catch (RbDuplicateKeyException dke) {
            throw new RbDuplicateKeyException("EMAIL_ADDRESS_ALREADY_EXISTS");
        }
    }

    @Override
    public void updatePassword(String userId, String password) {
        userRepository.update(byId(userId), set(PASSWORD, password));
    }

    @Override
    public User updateProfilePicture(String userId, String pictureUrl) {
        return userRepository.findAndUpdate(
                byId(userId),
                set(PROFILE_PICTURE, pictureUrl),
                createReturnNew()
        );
    }

    private FindOneAndUpdateOptions createReturnNew() {
        return new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
    }
}
