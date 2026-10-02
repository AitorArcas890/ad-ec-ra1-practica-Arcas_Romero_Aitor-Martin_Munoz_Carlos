package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;

public interface ProductoXmlDao {
    Productos readXml(String filePath) throws JAXBException;
}
