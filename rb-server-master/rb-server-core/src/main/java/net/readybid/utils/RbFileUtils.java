package net.readybid.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.Adler32;

public class RbFileUtils {

    public static File fromMultipart(MultipartFile multipartFile) throws IOException {
        final File convFile = new File(multipartFile.getOriginalFilename());
        final FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public static String generateFileHash(File file) throws IOException {
        final Adler32 checksum = new Adler32();
        checksum.update(Files.readAllBytes(file.toPath()));
        return Long.toHexString(checksum.getValue());
    }
}
