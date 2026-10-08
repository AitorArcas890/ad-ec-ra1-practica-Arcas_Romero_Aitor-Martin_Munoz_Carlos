package org.educa.dao;

import org.educa.entity.ProductoEntity;
import org.educa.entity.SummaryEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ProductoTxtExportDaoImpl implements ProductoTxtExportDao {

    @Override
    public void exportSummary(List<ProductoEntity> productos, String path, String fileXml) throws IOException {
        File xmlFile = new File(fileXml);
        // Almacenar el nombre del fichero
        String fileName = xmlFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String mesAno = baseName.replace("inventario", "");
        String outputFileName = "result" + mesAno + ".txt";
        // Toda la logica de la ruta del fichero
        Path outputDir = Paths.get(path);
        Files.createDirectories(outputDir);
        Path outputPath = outputDir.resolve(outputFileName);

        int numeroProductos = productos.size();
        // Recoge de cada producto su profit
        BigDecimal beneficioTotal = productos.stream()
                .map(ProductoEntity::getProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Se crea un SummaryEntity
        SummaryEntity summary = new SummaryEntity(
                mesAno,
                numeroProductos,
                beneficioTotal,
                xmlFile.getAbsolutePath(),
                baseName,
                xmlFile.length()
        );

        Files.writeString(outputPath, summary.toPrint(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}