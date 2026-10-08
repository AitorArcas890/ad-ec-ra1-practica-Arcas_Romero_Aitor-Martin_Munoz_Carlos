package org.educa.service;


import generated.Costes;
import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.*;
import org.educa.entity.ProductoEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private final ProductoXmlDao productoXmlDao = new ProductoXmlDaoImpl();
    private final ProductoTxtExportDao txtExportDao = new ProductoTxtExportDaoImpl();
    private final ProductoExcelExportDao excelExportDao = new ProductoExcelExportDaoImpl();

    /**
     * Lee un archivo XML de productos y lo convierte los productos de
     * {@link ProductoEntity} en una lista
     *
     * @param fileXml ruta al archivo XML a procesar
     * @return lista de {@link ProductoEntity} con los campos
     * (precioFinal, coste, beneficio) a partir del XML
     * @throws JAXBException si el archivo no existe, o el XML no es válido
     */

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {

        // 1. EL DAO lee el archivo XML y nos da el objeto Productos
        Productos productos = productoXmlDao.readXml(fileXml);

        List<ProductoEntity> lista = new ArrayList<>();

        // ITERA TODOS LOS PRODUCTOS
        for (Producto p : productos.getProducto()) {
            ProductoEntity entity = new ProductoEntity();
            entity.setProducto(p);

            // RECUPERAR LOS DATOS DE LOS PRODUCTOS DE LA CLASE Producto
            BigDecimal precio = p.getPrecio();
            BigDecimal descuento = p.getDescuento();

            // COGEMOS EL PRECIO Y LE QUITAMOS EL DESCUENTO
            BigDecimal precioFinal = precio.subtract(descuento);

            // ACTUALIZA EL PRECIO FINAL AL PRODUCTO
            entity.setPrecioFinal(precioFinal);

            Costes costes = p.getCostes();
            BigDecimal cost = costes.getCostesEnvio().add(costes.getCostesAlmacenaje());
            entity.setCost(cost);

            BigDecimal profit = precioFinal.subtract(cost);
            entity.setProfit(profit);

            lista.add(entity);
        }
        return lista;
    }

    /**
     * Exporta un resumen de los productos del archivo XML a un fichero de texto.
     * El resumen incluye información calculada como precio final, costes y beneficios.
     *
     * @param path    directorio donde se generará el fichero de texto
     * @param fileXml ruta al archivo XML de entrada
     * @throws JAXBException si el archivo no existe, o el XML no es válido
     * @throws IOException   si hay error al crear directorios o escribir el fichero
     */

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        List<ProductoEntity> productos = readFile(fileXml);
        txtExportDao.exportSummary(productos, path, fileXml);
    }


    /**
     * Exporta los productos del archivo XML a un fichero Excel con formato tabular.
     * Incluye todos los datos de los productos junto con los campos calculados
     * (precio final, costes y beneficios) con formato y estilos aplicados.
     *
     * @param path    directorio donde se generará el archivo Excel
     * @param fileXml ruta del archivo XML de entrada con los datos de productos
     * @throws JAXBException  si el archivo XML no existe o no es válido
     * @throws IOException    si hay error al crear directorios o escribir el fichero Excel
     * @throws ParseException si hay error al parsear fechas
     */
    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        List<ProductoEntity> productos = readFile(fileXml);
        excelExportDao.exportExcel(productos, path, fileXml);
    }
}
