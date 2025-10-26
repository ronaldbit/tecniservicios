package com.example.simplemvc.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class ExcelService {
    /**
     * Genera un archivo Excel .xlsx a partir de una lista List.
     *
     * @param titulo     El título para la tabla dentro de la hoja.
     * @param listaDatos La lista de entidades (ej. List<Usuario>) de JPA.
     * @return Un ByteArrayInputStream que contiene el archivo Excel.
     */
    public ByteArrayInputStream generarExcel(String titulo, List<?> listaDatos) throws IOException {

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet(titulo);

            if (listaDatos == null || listaDatos.isEmpty()) {
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            }

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Field[] fields = listaDatos.getFirst().getClass().getDeclaredFields();
            int numColumnas = fields.length;

            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints((float) 18);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titulo);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, numColumnas - 1));

            Row headerRow = sheet.createRow(2);

            for (int i = 0; i < numColumnas; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 3;

            for (Object dato : listaDatos) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < numColumnas; i++) {
                    try {
                        Field field = fields[i];
                        field.setAccessible(true);
                        Object value = field.get(dato);

                        Cell cell = row.createCell(i);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue("");
                        }
                    } catch (IllegalAccessException e) {
                        row.createCell(i).setCellValue("Error");
                    }
                }
            }

            for (int i = 0; i < numColumnas; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}