package net.readybid.file_export.common;

import net.readybid.file_export.FileExportView;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class FileExportServiceImpl implements FileExportService {

    @Override
    public void setResponse(HttpServletResponse response, FileExportView view) throws IOException {
        final String filename = view.getFileName() + "." + view.getExtension();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setHeader("RB-Filename", filename);
        response.setHeader("Content-Type", view.getContentType());

        final OutputStream out = response.getOutputStream();
        view.write( out );
        out.close();
    }
}
