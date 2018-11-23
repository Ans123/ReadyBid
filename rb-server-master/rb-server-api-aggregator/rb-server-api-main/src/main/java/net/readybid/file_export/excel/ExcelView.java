package net.readybid.file_export.excel;

import net.readybid.file_export.FileExportView;

public interface ExcelView extends FileExportView {
    default String getExtension(){
        return "xlsx";
    }

    default String getContentType(){
        return "application/vnd.google-apps.spreadsheet";
    }
}
