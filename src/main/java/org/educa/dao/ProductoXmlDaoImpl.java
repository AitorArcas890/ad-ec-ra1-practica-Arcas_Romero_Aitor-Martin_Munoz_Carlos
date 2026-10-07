package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.educa.handler.ValidationHandler;

import java.io.File;

public class ProductoXmlDaoImpl implements ProductoXmlDao {

    /**
     * Lee un archivo XML y lo deserializa a un objeto {@link Productos}
     *
     * @param filePath ruta del archivo XML a leer
     * @return objeto {@link Productos} con los datos del XML
     * @throws JAXBException si el archivo no existe o el XML no es válido
     */
    @Override
    public Productos readXml(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        // unmarshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd));
        unmarshaller.setEventHandler(new ValidationHandler());
        return (Productos) unmarshaller.unmarshal(new File(filePath));
    }
}