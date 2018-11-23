package net.readybid.app.file_storage;

import net.readybid.amazon.AwsS3FileRepository;
import net.readybid.app.interactors.authentication.user.gate.ProfilePictureFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfilePictureFileStorageImpl extends AbstractFileStorage implements ProfilePictureFileStorage {

    @Autowired
    public ProfilePictureFileStorageImpl(
            AwsS3FileRepository fileRepository,
            Environment environment
    ) {
        super(
                fileRepository,
                environment.getRequiredProperty("com.amazon.key.s3.user-profile-picture.bucket"),
                environment.getRequiredProperty("com.amazon.key.s3.user-profile-picture.path")
        );
    }

    @Override
    public String put(String userId, MultipartFile multipartFile) {
        return super.put(userId + "_%s.jpg", multipartFile);
    }
}
