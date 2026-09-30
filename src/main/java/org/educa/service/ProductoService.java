package org.educa.service;

import generated.Costes;
import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    /**
     * Lee un archivo XML de productos y lo convierte en una lista de productos
     * @param fileXml ruta al archivo XML a procesar
     * @return lista de {@link ProductoEntity} con los campos
     * (precioFinal, coste, beneficio) a partir del XML
     * @throws JAXBException si el archivo no existe, o el XML no es válido
     */

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {

        //INICIA LA CLASE QUE VA A RECOGER LOS DATOS
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Productos productos = (Productos) unmarshaller.unmarshal(new File(fileXml));

        List<ProductoEntity> lista = new ArrayList<>();

        //ITERA TODOS LOS PRODUCTOS
        for (Producto p : productos.getProducto()) {
            ProductoEntity entity = new ProductoEntity();
            entity.setProducto(p);

            //RECUPERAR LOS DATOS DE LOS PRODUCTOS DE LA CLASE Producto
            BigDecimal precio = p.getPrecio();
            BigDecimal descuento = p.getDescuento();

            //COGEMOS EL PRECIO Y LE QUITAMOS EL DESCUENTO
            BigDecimal precioFinal = precio.subtract(descuento);

            //ACTUALIZA EL PRECIO FINAL AL PRODUCTO
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

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        //TODO: Implementar

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
