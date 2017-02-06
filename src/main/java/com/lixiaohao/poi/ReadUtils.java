package com.lixiaohao.poi;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
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
     56      * Read the Excel 2010
     57      * @param path the path of the excel file
     58      * @return
     59      * @throws IOException
     60      */
      public void readXlsx(FileInputStream is) throws IOException {

          List<Map<String,String>> excelValues = new ArrayList<Map<String, String>>();

          Map<String, String> excelValue ;

          XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
          // Read the Sheet
          for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {

              XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);

              if (xssfSheet == null) {

                  continue;

              }

              // Read the Row


              int lastRowNum = xssfSheet.getLastRowNum();

              if(lastRowNum<=0)continue;

              XSSFRow headRow = xssfSheet.getRow(0);

              String[] headArgs = new String[headRow.getLastCellNum()+1];

              for(int cellNum = 0 ;cellNum<headRow.getLastCellNum(); cellNum++){
                  String value = headRow.getCell(cellNum).getStringCellValue();
                  headArgs[cellNum] = value;
              }


              for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
                  XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                  if (xssfRow != null) {

                      XSSFCell nos = xssfRow.getCell(0);

                      XSSFCell name = xssfRow.getCell(1);

                      XSSFCell age = xssfRow.getCell(2);



                      System.out.println(nos.getStringCellValue()+"--"+name.getStringCellValue()+"--"+age.getStringCellValue());
                      System.out.println();
                      System.out.println();

                  }
              }
          }


      }

    public static void main(String[] args) {
        ReadUtils readUtils = new ReadUtils();
        String path = "/Users/lixiaohao/Documents/workspace/idea/poi/doc/test.xlsx";
        try {
            readUtils.readXlsx(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
