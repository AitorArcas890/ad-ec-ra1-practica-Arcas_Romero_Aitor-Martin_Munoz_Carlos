package org.educa.dao;

import org.educa.entity.ProductoEntity;

import java.io.IOException;
import java.util.List;

public interface ProductoTxtExportDao {
    void exportSummary(List<ProductoEntity> productos, String path, String fileXml) throws IOException;
}