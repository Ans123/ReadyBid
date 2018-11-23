package net.readybid.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AwsS3FileRepositoryImpl implements AwsS3FileRepository {

    private final TransferManager transferManager;

    @Autowired
    public AwsS3FileRepositoryImpl(AmazonS3 amazonS3){
        transferManager = TransferManagerBuilder
                .standard()
                .withS3Client(amazonS3)
                .build();
    }

    @Override
    public void put(String bucketName, String filePath, File file){
        try {
            final Upload upload = transferManager.upload(bucketName, filePath, file);
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String bucketName, String filePath){
        transferManager.getAmazonS3Client().deleteObject(bucketName, filePath);
    }
}
