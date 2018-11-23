package net.readybid.file_export.common;

import net.readybid.file_export.FileExportView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileExportService {
    void setResponse(HttpServletResponse response, FileExportView view) throws IOException;
}
