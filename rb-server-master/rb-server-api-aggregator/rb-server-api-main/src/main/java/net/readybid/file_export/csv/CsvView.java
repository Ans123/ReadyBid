package net.readybid.file_export.csv;

import net.readybid.file_export.FileExportView;

public interface CsvView extends FileExportView {
    default String getExtension(){
        return "csv";
    }

    default String getContentType(){
        return "text/csv";
    }
}
