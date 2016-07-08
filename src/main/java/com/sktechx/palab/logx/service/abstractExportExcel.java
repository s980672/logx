package com.sktechx.palab.logx.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 8/7/15.
 */
public abstract class abstractExportExcel {

    XSSFWorkbook wb = new XSSFWorkbook();

    List<Sheet> sheets = new ArrayList< Sheet >();

    public Sheet addSheet(String name) {


        //create a sheet with a name
        Sheet sh = wb.createSheet(name);
        sheets.add(sh);
        return sh;
    }


    public Sheet getSheet(String sheetName){

        for(Sheet sheet:sheets){
            if ( sheet.getSheetName().equals(sheetName)) {
                return sheet;
            }
        }
        return null;
    }

    public XSSFWorkbook getWorkBook() {
        return wb;
    }

    public List<Sheet> getSheets() {
        return sheets;
    }

    public CellStyle getDataStyle(){

        // Create a new font and alter it.
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("나눔고딕");
        //font.setBold(true);


        // Fonts are set into a style so create a new one to use.
        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        //set alignment
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);

        return style;
    }


    public CellStyle getHeaderStyle2(){

        // Create a new font and alter it.
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("나눔고딕");
        font.setBold(true);


        // Fonts are set into a style so create a new one to use.
        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

        //set alignment
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);


        return style;
    }


    public abstract void createData(String sheetName, Object object);

    protected void setCellValue(Sheet sh, int row, int col, String value, CellStyle style){
        if ( sh.getRow(row) == null ) {
            Row row1 = sh.createRow(row);
            if ( row1.getCell(col) == null ) {
                Cell cell = row1.createCell(col);
                cell.setCellValue(value);
            }else{
                row1.getCell(col).setCellValue(value);
            }
        }else{
            if ( sh.getRow(row).getCell(col) == null ){
                sh.getRow(row).createCell(col).setCellValue(value);
            }
            else{
                sh.getRow(row).getCell(col).setCellValue(value);
            }
        }

        if ( style != null ){
            sh.getRow(row).getCell(col).setCellStyle(style);
        }

    }

}
