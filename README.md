# Práctica RA1 - Acceso a Datos

Proyecto Maven para la práctica de Acceso a Datos (DAM) que procesa inventarios XML, calcula métricas de negocio y
exporta resultados a TXT y Excel.

## Descripción

La aplicación lee un archivo XML de inventario de productos tecnológicos, lo valida contra un esquema XSD, calcula
precios finales, costes y beneficios, y genera dos tipos de reportes:

- **Resumen en TXT** (`result_junio2026.txt`): estadísticas globales del inventario
- **Detalle en Excel** (`export_junio2026.xlsx`): listado completo con formato condicional

## Tecnologías

| Tecnología                 | Versión | Uso                                  |
|----------------------------|---------|--------------------------------------|
| Java                       | 21      | Lenguaje principal                   |
| Maven                      | -       | Gestión de dependencias y build      |
| JAXB (Jakarta XML Binding) | 4.0.0   | Unmarshalling XML → Objetos Java     |
| Apache POI                 | 5.5.1   | Generación de archivos Excel (.xlsx) |
| jaxb2-maven-plugin         | 3.1.0   | Generación de clases Java desde XSD  |

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── org/educa/
│   │       ├── app/
│   │       │   ├── Activity1.java    # Lectura y muestra por consola
│   │       │   ├── Activity2.java    # Exporta resumen a TXT
│   │       │   └── Activity3.java    # Exporta detalle a Excel
│   │       ├── dao/
│   │       │   ├── ProductoXmlDao.java       # Interfaz DAO
│   │       │   └── ProductoXmlDaoImpl.java   # Implementación JAXB
│   │       ├── entity/
│   │       │   ├── ProductoEntity.java   # Entidad enriquecida con cálculos
│   │       │   └── SummaryEntity.java    # Resumen estadístico
│   │       ├── handler/
│   │       │   └── ValidationHandler.java # Manejo eventos validación XML
│   │       └── service/
│   │           └── ProductoService.java  # Lógica de negocio
│   └── resources/
│       ├── xml/
│       │   └── inventario_junio2026.xml  # Datos de entrada (5 productos)
│       └── xsd/
│           └── inventario_junio2026.xsd  # Esquema de validación
```

## Modelo de Datos (XSD)

El esquema define:

- **Producto**: código, número de serie, marca, modelo, categoría, año lanzamiento, garantía, proveedor, tipo conexión,
  precio, descuento, costes
- **Proveedor**: empresa, ciudad, país, código postal
- **Costes**: envío, almacenaje

## Cálculos Realizados

| Campo        | Fórmula                          |
|--------------|----------------------------------|
| Precio Final | `precio - descuento`             |
| Coste Total  | `costesEnvio + costesAlmacenaje` |
| Beneficio    | `precioFinal - costeTotal`       |

### Actividad 1 - Lectura y consola

Muestra por consola los 5 productos con todos sus campos calculados.

### Actividad 2 - Exportar a TXT

Genera `src/main/resources/export/result_junio2026.txt` con:

- Fecha (mes/año)
- Número de vehículos/productos
- Beneficio total
- Ruta, nombre y tamaño del fichero origen

### Actividad 3 - Exportar a Excel

Genera `src/main/resources/export/export_junio2026.xlsx` con:

- Hoja "Productos"
- Cabeceras estilizadas
- Filas alternadas (verde claro/blanco)
- Formato monetario (€) y porcentajes
- Columnas auto-ajustadas

## Plugin JAXB (Generación de Código)

El plugin `jaxb2-maven-plugin` genera automáticamente las clases en `target/generated-sources/jaxb/generated/` a partir
del XSD durante la fase `generate-sources`:

- `Productos.java`
- `Producto.java`
- `Proveedor.java`
- `Costes.java`
- `ObjectFactory.java`

## Autores

- **Arcas Romero Aitor**
- **Martin Muñoz Carlos**