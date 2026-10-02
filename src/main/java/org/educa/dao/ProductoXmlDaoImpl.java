package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

public class ProductoXmlDaoImpl implements ProductoXmlDao {

    @Override
    public Productos readXml(String filePath) throws JAXBException {
        // Usa la clase Productos generada por JAXB en target/classes/generated/
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Productos) unmarshaller.unmarshal(new File(filePath));
    }
}