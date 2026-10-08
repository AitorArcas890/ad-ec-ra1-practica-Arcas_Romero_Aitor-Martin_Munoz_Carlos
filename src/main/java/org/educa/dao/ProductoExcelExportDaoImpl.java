package org.educa.dao;

import generated.Producto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

public class ProductoExcelExportDaoImpl implements ProductoExcelExportDao {

    @Override
    public void exportExcel(List<ProductoEntity> productos, String path, String fileXml) throws IOException, ParseException {

        File xmlFile = new File(fileXml);
        // Proceso para crear esta vez un fichero excel
        String fileName = xmlFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String mesAno = baseName.replace("inventario", "");
        String outputFileName = "export_" + mesAno + ".xlsx";
        Path outputDir = Paths.get(path);
        Files.createDirectories(outputDir);
        Path outputPath = outputDir.resolve(outputFileName);

        // Usamos XSSF que es de Apache POI y permite leer y escribir excels
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             FileOutputStream excel = new FileOutputStream(outputPath.toFile())) {

            // Abrimos el flujo para escribir el excel
            Sheet sheet = workbook.createSheet("Productos");


            // estilos de todas las celdas
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle evenRowStyle = workbook.createCellStyle();
            evenRowStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            evenRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            evenRowStyle.setAlignment(HorizontalAlignment.CENTER);
            evenRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            evenRowStyle.setBorderBottom(BorderStyle.THIN);
            evenRowStyle.setBorderTop(BorderStyle.THIN);
            evenRowStyle.setBorderLeft(BorderStyle.THIN);
            evenRowStyle.setBorderRight(BorderStyle.THIN);

            CellStyle oddRowStyle = workbook.createCellStyle();
            oddRowStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            oddRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            oddRowStyle.setAlignment(HorizontalAlignment.CENTER);
            oddRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            oddRowStyle.setBorderBottom(BorderStyle.THIN);
            oddRowStyle.setBorderTop(BorderStyle.THIN);
            oddRowStyle.setBorderLeft(BorderStyle.THIN);
            oddRowStyle.setBorderRight(BorderStyle.THIN);

            CellStyle evenRowLeftStyle = workbook.createCellStyle();
            evenRowLeftStyle.cloneStyleFrom(evenRowStyle);
            evenRowLeftStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle oddRowLeftStyle = workbook.createCellStyle();
            oddRowLeftStyle.cloneStyleFrom(oddRowStyle);
            oddRowLeftStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle evenRowRightStyle = workbook.createCellStyle();
            evenRowRightStyle.cloneStyleFrom(evenRowStyle);
            evenRowRightStyle.setAlignment(HorizontalAlignment.RIGHT);

            CellStyle oddRowRightStyle = workbook.createCellStyle();
            oddRowRightStyle.cloneStyleFrom(oddRowStyle);
            oddRowRightStyle.setAlignment(HorizontalAlignment.RIGHT);

            // Array con el nombre de los headers
            String[] headers = {
                    "Codigo", "Numero de serie", "Precio", "Descuento",
                    "PrecioFinal", "CostesEnvio", "CostesAlmacenaje", "Beneficio"
            };

            // Creamos la primera fila (headers) y for vamos rellenando las columnas con los nombres del array.
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            //Rellena con datos la tabla del Excel
            for (int i = 0; i < productos.size(); i++) {
                ProductoEntity entity = productos.get(i);
                Producto p = entity.getProducto();

                Row row = sheet.createRow(i + 1);

                // Establece el estilo de la fila dependiendo si es par o impar
                CellStyle rowStyle = (i % 2 == 0) ? evenRowStyle : oddRowStyle;


                //Array de contenido
                Object[] values = {
                        p.getCodigo(),
                        p.getNumeroSerie(),
                        p.getPrecio(),
                        p.getDescuento(),
                        entity.getPrecioFinal(),
                        p.getCostes().getCostesEnvio(),
                        p.getCostes().getCostesAlmacenaje(),
                        entity.getProfit()
                };

                //Rellena la tabla con los datos del array de contenido
                for (int j = 0; j < values.length; j++) {
                    Cell cell = row.createCell(j);
                    Object val = values[j];
                    String cellValue;
                    if (val instanceof BigDecimal) {
                        BigDecimal bd = (BigDecimal) val;
                        double d = bd.doubleValue();
                        // Aplicar formato según columna
                        if (j == 3) {
                            // añade el simbolo de porcentaje
                            cellValue = String.format("%.2f%%", d);
                        } else if (j == 2 || j == 4 || j == 5 || j == 6 || j == 7) {
                            // Añade € al Precio, PrecioFinal, CostesEnvio, CostesAlmacenaje, Beneficio
                            cellValue = String.format("%.2f €", d);
                        } else {
                            cellValue = String.valueOf(d);
                        }
                    } else if (val instanceof Integer) {
                        cellValue = String.valueOf((Integer) val);
                    } else {
                        cellValue = val.toString();
                    }
                    cell.setCellValue(cellValue);

                    // Aplicar estilo según columna
                    CellStyle cellStyle;
                    if (j == 1) {
                        // Aplica el estilo al numero de serie
                        cellStyle = (i % 2 == 0) ? evenRowLeftStyle : oddRowLeftStyle;
                    } else if (j == 3 || j == 5 || j == 6 || j == 7) {
                        // Aplica el estilo al Descuento, CostesEnvio, CostesAlmacenaje, Benefici
                        cellStyle = (i % 2 == 0) ? evenRowRightStyle : oddRowRightStyle;
                    } else {
                        cellStyle = rowStyle;
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            //Adapta el ancho de las columnas en base al valor que va en la celda
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(excel);
        }
    }
}