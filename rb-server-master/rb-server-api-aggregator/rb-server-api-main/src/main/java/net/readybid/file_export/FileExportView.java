package net.readybid.file_export;

import java.io.IOException;
import java.io.OutputStream;

public interface FileExportView {
    void write(OutputStream outputStream) throws IOException;

    String getFileName();

    String getExtension();

    String getContentType();
}
