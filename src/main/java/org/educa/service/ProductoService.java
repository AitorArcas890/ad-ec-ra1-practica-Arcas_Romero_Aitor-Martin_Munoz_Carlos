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


    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Productos productos = (Productos) unmarshaller.unmarshal(new File(fileXml));

        List<ProductoEntity> lista = new ArrayList<>();
        for (Producto p : productos.getProducto()) {
            ProductoEntity entity = new ProductoEntity();
            entity.setProducto(p);

            BigDecimal precio = p.getPrecio();
            BigDecimal descuento = p.getDescuento();
            BigDecimal precioFinal = precio.subtract(descuento);
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
