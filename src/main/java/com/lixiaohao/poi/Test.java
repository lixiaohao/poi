package com.lixiaohao.poi;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by lixiaohao on 2017/2/9
 *
 * @Description
 * @Create 2017-02-09 14:18
 * @Company
 */
public class Test {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

        FileInputStream in = new FileInputStream("C:\\Users\\lixiaohao.ZZGRP\\Desktop\\temp\\studentsInsert.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(in);
        XSSFSheet sheet = wb.getSheetAt(0);

       sheet.shiftRows(2,3,1,true,false);
        sheet.createRow(2);

        FileOutputStream myxlsout = new FileOutputStream("C:\\Users\\lixiaohao.ZZGRP\\Desktop\\temp\\studentsInsert1.xlsx");
        wb.write(myxlsout);
        myxlsout.close();

    }


}
