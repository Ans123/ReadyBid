package net.readybid.app.file_storage;

import net.readybid.amazon.AwsS3FileRepository;
import net.readybid.app.interactors.core.entity.gate.LogoFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LogoFileStorageImpl extends AbstractFileStorage implements LogoFileStorage {

    @Autowired
    public LogoFileStorageImpl(
            AwsS3FileRepository fileRepository,
            Environment environment
    ) {
        super(
                fileRepository,
                environment.getRequiredProperty("com.amazon.key.s3.entity-logo.bucket"),
                environment.getRequiredProperty("com.amazon.key.s3.entity-logo.path")
        );
    }

    @Override
    public String put(String entityId, MultipartFile multipartFile) {
        return super.put(entityId + "_%s.jpg", multipartFile);
    }
}
