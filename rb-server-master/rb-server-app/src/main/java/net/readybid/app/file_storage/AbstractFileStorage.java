package net.readybid.app.file_storage;

import net.readybid.amazon.AwsS3FileRepository;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.utils.RbFileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

abstract class AbstractFileStorage {

    private final AwsS3FileRepository fileRepository;
    private final String bucketName;
    private final String path;

    AbstractFileStorage(AwsS3FileRepository fileRepository, String bucketName, String path) {
        this.fileRepository = fileRepository;
        this.bucketName = bucketName;
        this.path = path;
    }

    public void delete(String fileName){
        if(!(fileName== null || fileName.isEmpty()))
            fileRepository.delete(bucketName, getAbsoluteFileName(fileName));
    }

    String put(String fileNameFormat, MultipartFile multipartFile){
        try {
            final File file = RbFileUtils.fromMultipart(multipartFile);
            final String fileName = String.format(fileNameFormat, RbFileUtils.generateFileHash(file));
            fileRepository.put(bucketName, getAbsoluteFileName(fileName), file);
            file.delete();
            return fileName;
        } catch (IOException exception){
            throw new UnableToCompleteRequestException(exception.getMessage());
        }
    }

    private String getAbsoluteFileName(String fileName) {
        return path + fileName;
    }
}
