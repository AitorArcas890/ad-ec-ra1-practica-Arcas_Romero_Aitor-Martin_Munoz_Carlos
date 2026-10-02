package org.educa.service;



import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductoXmlDao;
import org.educa.dao.ProductoXmlDaoImpl;
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
import java.text.ParseException;
import java.util.List;

public class ProductoService {

    // Instanciamos el DAO y el Handler que hemos creado
    private final ProductoXmlDao productoXmlDao = new ProductoXmlDaoImpl();
    private final ProductoHandler productoHandler = new ProductoHandler();

    /**
     * Lee un archivo XML de productos y lo convierte en una lista de productos
     *
     * @param fileXml ruta al archivo XML a procesar
     * @return lista de {@link ProductoEntity} con los campos
     * (precioFinal, coste, beneficio) a partir del XML
     * @throws JAXBException si el archivo no existe, o el XML no es válido
     */

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {

        // 1. EL DAO lee el archivo XML y nos da el objeto Productos
        Productos productos = productoXmlDao.readXml(fileXml);

        // 2. EL Handler procesa el objeto Productos, iterando y calculando cada campo
        List<ProductoEntity> lista = productoHandler.processProductos(productos);

        // 3. Devolvemos la lista final
        return lista;
    }

    /**
     * Exporta los productos del archivo XML a un fichero txt
     *
     * @param path    directorio donde se generará el txt
     * @param fileXml ruta al archivo XML de entrada
     * @throws JAXBException si el archivo no existe, o el XML no es válido
     * @throws IOException   si hay error al crear directorios o escribir el fichero
     */

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        // Llama al metodo readFile para recoger los datos
        List<ProductoEntity> productos = readFile(fileXml);

        // Crear el fichero apuntando al file
        File xmlFile = new File(fileXml);

        // Almacenar el nombre del fichero
        String fileName = xmlFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String mesAno = baseName.replace("inventario", "");
        String outputFileName = "result" + mesAno + ".txt";

        // Toda la logica de la ruta del fichero
        Path outputDir = Paths.get(path);
        Files.createDirectories(outputDir);

        // Junta la ruta y el nombre del fichero
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

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
