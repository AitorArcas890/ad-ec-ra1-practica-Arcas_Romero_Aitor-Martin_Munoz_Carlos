package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.educa.handler.ValidationHandler;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class ProductoXmlDaoImpl implements ProductoXmlDao {

    @Override
    public Productos readXml(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        // unmarshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd));
        unmarshaller.setEventHandler(new ValidationHandler());
        return (Productos) unmarshaller.unmarshal(new File(filePath));
    }
}