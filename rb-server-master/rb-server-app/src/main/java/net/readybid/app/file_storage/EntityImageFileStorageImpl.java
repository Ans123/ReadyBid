package net.readybid.app.file_storage;

import net.readybid.amazon.AwsS3FileRepository;
import net.readybid.app.interactors.core.entity.gate.EntityImageFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EntityImageFileStorageImpl extends AbstractFileStorage implements EntityImageFileStorage {

    @Autowired
    public EntityImageFileStorageImpl(
            AwsS3FileRepository fileRepository,
            Environment environment
    ) {
        super(
                fileRepository,
                environment.getRequiredProperty("com.amazon.key.s3.entity-image.bucket"),
                environment.getRequiredProperty("com.amazon.key.s3.entity-image.path")
        );
    }

    @Override
    public String put(String entityId, MultipartFile multipartFile) {
        return super.put(entityId + "_%s.jpg", multipartFile);
    }
}
