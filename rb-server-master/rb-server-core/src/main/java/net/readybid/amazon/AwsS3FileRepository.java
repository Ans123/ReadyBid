package net.readybid.amazon;

import java.io.File;

public interface AwsS3FileRepository {
    void put(String bucketName, String filePath, File file);

    void delete(String bucketName, String filePath);
}
