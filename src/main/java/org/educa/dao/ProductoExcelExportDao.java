package org.educa.dao;

import org.educa.entity.ProductoEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ProductoExcelExportDao {
    void exportExcel(List<ProductoEntity> productos, String path, String fileXml) throws IOException, ParseException;
}