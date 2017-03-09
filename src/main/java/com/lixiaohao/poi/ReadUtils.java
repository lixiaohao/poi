package com.lixiaohao.poi;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiaohao
 * Date:${DATA}
 * Description:
 */
public class ReadUtils {

    private static XSSFWorkbook xssfWorkbook = null;


    /***
     * 支持多个sheet
     * @param is
     * @return
     */
    public static Map<String,List<Map<String, String>>> readXlsxs(FileInputStream is) {
        Map<String,List<Map<String, String>>> sheetMaps = new HashMap<String, List<Map<String,String>>>();

        try {
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Read the Sheet

        //支持多个sheet
        if(xssfWorkbook == null || xssfWorkbook.getNumberOfSheets() <= 0 ){
            return sheetMaps;
        }

        for(int i = 0; i<xssfWorkbook.getNumberOfSheets();i++){

            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(i);

            if( xssfSheet == null ){
                continue;
            }

            String sheetName = xssfSheet.getSheetName();

            List<Map<String, String>> sheetRows = readSheet(xssfSheet);

            if(sheetRows != null && sheetRows.size()>0){
                sheetMaps.put(sheetName, sheetRows);
            }

        }

        return sheetMaps;
    }


    /**
     * Read the Excel 2010+
     * 仅支持字符串类型
     * 仅支持一个sheet
     * @param
     * @return
     * @throws IOException
     */
    public static List<Map<String,String>> readXlsx(FileInputStream is) {

        List<Map<String,String>> excelValues = new ArrayList<Map<String, String>>();

        String exceptionField = "";
        try {
            Map<String, String> excelValue ;

            xssfWorkbook = new XSSFWorkbook(is);

            // Read the Sheet

            //仅支持一个sheet
            if(xssfWorkbook.getNumberOfSheets() <= 0 )
                return excelValues;


            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

            if (xssfSheet == null)
                return excelValues;

            // Read the Row
            int lastRowNum = xssfSheet.getLastRowNum();

            if(lastRowNum<=1)
                return excelValues;

            XSSFRow headRow = xssfSheet.getRow(1);

            String[] headArgs = new String[headRow.getLastCellNum()+1];

            for(int cellNum = 0 ;cellNum<headRow.getLastCellNum(); cellNum++){
                XSSFCell cell = headRow.getCell(cellNum);
                String value = "";
                if(cell != null){
                    value = (cell.getStringCellValue()==null)?"":cell.getStringCellValue().trim();
                }
                headArgs[cellNum] = value;
            }


            for (int rowNum = 2; rowNum <= lastRowNum; rowNum++) {

                XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                if (xssfRow != null) {

                    excelValue = new HashMap<String, String>();
                    for(int cellIndex = 0;cellIndex < headArgs.length-1;cellIndex++){


                        exceptionField = headArgs[cellIndex];

                        XSSFCell nos = xssfRow.getCell(cellIndex);

                        if(nos  == null)
                            continue;
                        String value = getCellValue(nos);
                        excelValue.put(headArgs[cellIndex],value);
                    }

                    excelValues.add(excelValue);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("错误行字段:"+ exceptionField);
        }

        return excelValues;

    }


    /**
     * Read the Excel 2010+
     * 仅支持字符串类型
     * @param
     * @return
     * @throws IOException
     */
    public static List<Map<String,String>> readSheet(XSSFSheet xssfSheet) {

        List<Map<String,String>> excelValues = new ArrayList<Map<String, String>>();

        String     exceptionField = "";
        Integer    exceptionRowNum= null;
        try {
            Map<String, String> excelValue ;

            if (xssfSheet == null)
                return excelValues;

            // Read the Row
            int lastRowNum = xssfSheet.getLastRowNum();

            if(lastRowNum<=1)
                return excelValues;

            XSSFRow headRow = xssfSheet.getRow(1);

            String[] headArgs = new String[headRow.getLastCellNum()+1];

            for(int cellNum = 0 ;cellNum<headRow.getLastCellNum(); cellNum++){
                XSSFCell cell = headRow.getCell(cellNum);
                String value = "";
                if(cell != null){
                    value = (cell.getStringCellValue()==null)?"":cell.getStringCellValue().trim();
                }
                headArgs[cellNum] = value;
            }


            for (int rowNum = 2; rowNum <= lastRowNum; rowNum++) {

                XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                if (xssfRow != null) {

                    excelValue = new HashMap<String, String>();
                    for(int cellIndex = 0;cellIndex < headArgs.length-1;cellIndex++){


                        exceptionField = headArgs[cellIndex];
                        exceptionRowNum= rowNum;

                        XSSFCell nos = xssfRow.getCell(cellIndex);

                        if(nos  == null)
                            continue;
                        String value = getCellValue(nos);
                        excelValue.put(headArgs[cellIndex],value);
                    }

                    excelValues.add(excelValue);

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("错误行字段:"+ exceptionField+"  行号:"+exceptionRowNum+3);
        }

        return excelValues;

    }

    /**
     * 仅支持  文本类型
     * @param xssfCell
     * @return
     */
      private static String getCellValue(XSSFCell xssfCell){

          String value = null;

          if( xssfCell == null )
               value = null;


          switch ( xssfCell.getCellTypeEnum() ){
              case STRING :
                  value = (xssfCell.getStringCellValue() == null)?null:xssfCell.getStringCellValue().trim();
                  break;
              case NUMERIC:
                  HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                  value = dataFormatter.formatCellValue(xssfCell);
                  break;
              default:
                  throw new IllegalStateException("Cannot get a STRING value from a "+xssfCell.getCellTypeEnum()+" cell");
          }

          return value;
      }

}
