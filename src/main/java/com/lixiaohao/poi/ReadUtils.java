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
    XSSFWorkbook xssfWorkbook = null;


    /**
     * Read the Excel 2010+
     * @param
     * @return
     * @throws IOException
           */
      public List<Map<String,String>> readXlsx(FileInputStream is) throws IOException {

          List<Map<String,String>> excelValues = new ArrayList<Map<String, String>>();

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
         }

        return excelValues;

      }

    /**
     * 仅支持  文本类型
     * @param xssfCell
     * @return
     */
      public String getCellValue(XSSFCell xssfCell){

          String value = null;

          if( xssfCell == null )
               value = null;

          switch ( xssfCell.getCellType() ){
              case XSSFCell.CELL_TYPE_STRING :
                  value = (xssfCell.getStringCellValue() == null)?null:xssfCell.getStringCellValue().trim();
                  break;
//              case XSSFCell.CELL_TYPE_NUMERIC :
//                  double numberic =  xssfCell.getNumericCellValue();
//                  value = String.valueOf(numberic);
//                  break;
              default:
                  throw new IllegalStateException("Cannot get a STRING value from a "+xssfCell.getCellTypeEnum()+" cell");
          }

          return value;
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

                  //certifications
                  if(key.equals("certifications")){

                      if( value == null || value.equals(""))
                          continue;

                      List<Certification> certifications = new ArrayList<Certification>();

                      String[] certificationNames = value.trim().split(",");

                      Certification certification = null;

                      for(String certificationName:certificationNames){

                          if(certificationName == null || certificationName.trim().equals(""))
                              continue;

                          certification = new Certification();

                          certification.setName(certificationName.trim());

                          certifications.add(certification);
                      }

                     if(certifications.size()>0){
                         Field field = clazz.getDeclaredField(key);

                         field.setAccessible(true);

                         field.set(corporation,certifications);
                     }

                      continue;
                  }

                  //tradeShows
                  if(key.equals("tradeShows") ){

                      if(value == null  || value.equals(""))
                          continue;

                      List<TradeShow> tradeShows = new ArrayList<TradeShow>();

                      String[] tradeShowsValues = value.trim().split("\\[");

                      TradeShow tradeShow = null;

                      for(String tradeShowsValue:tradeShowsValues){

                          if( tradeShowsValue == null || tradeShowsValue.trim().equals("") )
                              continue;

                          tradeShow = new TradeShow();

                          String[] tradeShowStr = tradeShowsValue.replace("]","").split(",");

                          for(String tradeShowValue:tradeShowStr){

                              if(tradeShowValue == null || tradeShowValue.equals(""))
                                  continue;

                              int index = tradeShowValue.indexOf(":");

                              if(index == -1)
                                  continue;

                              String valueStr = tradeShowValue.substring(index+1,tradeShowValue.length()).trim();

                              if(valueStr == null || valueStr.equals(""))
                              continue;

                              if(tradeShowValue.contains("tradeShowName")){

                                  tradeShow.setTradeShowName(valueStr);

                                  continue;
                              }
                             if(tradeShowValue.contains("dateAttended")){

                                  tradeShow.setDateAttended(valueStr);

                                 continue;
                              }
                              if(tradeShowValue.contains("hostCountryOrRegion")){

                                  tradeShow.setHostCountryOrRegion(valueStr);

                                  continue;
                              }

                          }

                          tradeShows.add(tradeShow);
                      }

                      if(tradeShows.size()>0){

                          Field field = clazz.getDeclaredField(key);

                          field.setAccessible(true);

                          field.set(corporation,tradeShows);
                      }

                      continue;
                  }


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
            System.out.println("---------------------------------------");
            List<Corporation> corporations = readUtils.fillCorporations(excelValues);

            for(Corporation corporation:corporations){
                System.out.println(corporation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

//        String originalStr = "[a:b]";
////        System.out.println(originalStr.substring(originalStr.indexOf(":")+1,originalStr.length()));
//        System.out.println(originalStr.indexOf("00"));
    }

}
