package com.lixiaohao.poi;

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

    /**
     56      * Read the Excel 2010+
     57      * @param path the path of the excel file
     58      * @return
     59      * @throws IOException
     60      */
      public List<Map<String,String>> readXlsx(FileInputStream is) throws IOException {

          List<Map<String,String>> excelValues = new ArrayList<Map<String, String>>();

          Map<String, String> excelValue ;

          XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

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

                        XSSFCell nos = xssfRow.getCell(cellIndex);

                        if(nos  == null)
                            continue;

                        String value = (nos.getStringCellValue() == null)?null:nos.getStringCellValue().trim();
                        excelValue.put(headArgs[cellIndex],value);
                    }

                      excelValues.add(excelValue);

                  }
              }

        return excelValues;

      }

      public List<Corporation> fillCorporations(List<Map<String,String>> maps){

          List<Corporation> corporations = new ArrayList<Corporation>();

          if(maps == null || maps.size()<=0)
              return corporations;
          try {
          for(Map<String,String> map:maps){

              Corporation corporation = new Corporation();
              Class clazz = corporation.getClass();

              for( Map.Entry<String,String> entry : map.entrySet() ){

                  String key = entry.getKey();

                  if(key ==  null || key.trim().equals("")) {
                      continue;
                  }

                  String value = entry.getValue();

                  Field field = clazz.getDeclaredField(key);

                  field.setAccessible(true);

                  field.set(corporation,value);

              }
            corporations.add(corporation);

          }

          } catch (NoSuchFieldException e) {
              e.printStackTrace();
          }catch (IllegalAccessException e) {
              e.printStackTrace();
          }
          return corporations;
      }

    public static void main(String[] args) {
        ReadUtils readUtils = new ReadUtils();
        String path = "E:\\workspace\\git\\poi\\doc\\test.xlsx";
        try {

            List<Map<String,String>> excelValues =  readUtils.readXlsx(new FileInputStream(path));
            for (Map<String,String> excelValue:excelValues){

                System.out.println("第"+excelValues.indexOf(excelValue)+"条");
                for(Map.Entry<String,String> entry:excelValue.entrySet()){
                    System.out.print("key:"+entry.getKey()+"---value:"+entry.getValue()+"--");
                }
                System.out.println("");

            }
            System.out.println("---------------------------------------");
            List<Corporation> corporations = readUtils.fillCorporations(excelValues);

            for(Corporation corporation:corporations){
                System.out.println(corporation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
