package com.lixiaohao.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.junit.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiaohao on 2017/3/9
 *
 * @Description
 * @Create 2017-03-09 11:57
 * @Company
 */
public class writeExcel {


    @Test
    public void testss(){

            InnerBean innerBean1 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao1","张三");

            OutBean outBean1 = new OutBean("0101","outBean",innerBean1);

            InnerBean innerBean2 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao2","张三");

            OutBean outBean2 = new OutBean("0102","outBean",innerBean2);

            InnerBean innerBean3 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao3","张三");

            OutBean outBean3 = new OutBean("0103","outBean",innerBean3);

            List<OutBean> outBeans = new ArrayList<OutBean>();
            outBeans.add(outBean1);
            outBeans.add(outBean2);
            outBeans.add(outBean3);
        copyTest(outBeans);
    }

//    @org.junit.Test
    public void copyTest(List<OutBean> outBeans){
        int startListIndex = 0;
        int endLinstIndex = 0;

        int startRow = 0;
        int endRow = 0;
        int step = 0;
        int position = 0;


        File file = new File("C:\\Users\\lixiaohao.ZZGRP\\Desktop\\temp\\students.xlsx");
        try{
            FileInputStream in = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            int stack = -1;

            for( int rowNum =0; rowNum <= sheet.getLastRowNum(); rowNum++ ){

                XSSFCell cell = sheet.getRow(rowNum).getCell(0);

                if( cell == null ){
                    continue;
                }
                String v = cell.getStringCellValue();
                if( v == null || v.trim().equals("") ){
                    continue;
                }

                if( v.startsWith("#list") ){
                    if( stack == -1 ){
                        startListIndex = rowNum;
                        stack = 0;
                        stack ++;
                        continue;
                    }else {
                        endLinstIndex = rowNum;
                        stack --;
                        break;
                    }
                }

            }

            startRow = startListIndex+1;
            endRow = endLinstIndex-1;

            step = endRow - startRow +1;

            position = startRow + step;
//                position = 0;


        for(OutBean outBean: outBeans){
            copyRows(startRow,endRow,position,sheet);

            Map<String,XSSFCell> cellMap =  fillContent(position-step+1,position,sheet);

            if( cellMap != null && cellMap.size()>0 ){
                for( Map.Entry<String,XSSFCell> cellEntry:cellMap.entrySet() ){
                    fillCell(cellEntry.getValue(),cellEntry.getKey(),outBean);
                }
            }

            position += step;
        }

            FileOutputStream fout = new FileOutputStream("C:\\Users\\lixiaohao.ZZGRP\\Desktop\\temp\\copyStudents.xlsx");
            wb.write(fout);
            fout.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void fillCell(XSSFCell cell,String fieldName,OutBean outBean) throws NoSuchFieldException, IllegalAccessException {

        if(cell == null || outBean == null || fieldName == null || fieldName.trim().equals("")){
            return;
        }

        //检测fieldName，查看是否是一级属性
        int fieldLevel = getFieldLevel(fieldName);

        if(fieldLevel == -1){
            return;
        }

        Class clazz = outBean.getClass();


        if( fieldLevel == 1 ){

            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            String v = (String) field.get(outBean);
            if(v !=null){
                cell.setCellValue(v);
                return;
            }
        }

        if( fieldLevel == 2 ){

            int index = fieldName.indexOf(".");

            String parentFieldName = fieldName.substring(0,index);
            String childFieldName = fieldName.substring(index+1);

//            Field field = clazz.getDeclaredField(fieldName);
//            field.setAccessible(true);

            String v = getChildFiledValue(outBean,outBean.getData().getClass(),parentFieldName,childFieldName);

            if(v != null  && !v.equals("")){
                cell.setCellValue(v);
                return;
            }

        }


    }

    private int getFieldLevel(String fieldName){

        int fieldLevel = 1;

        if(fieldName == null || fieldName.trim().equals("")){
            fieldLevel = -1;
            return fieldLevel;
        }

        int length = fieldName.length();

       for( int i=0 ;i<length; i++){
           char c = fieldName.charAt(i);
           if(c == '.'){
               if(i == 0 || i==length-1){
                   continue;
               }
               fieldLevel++;
           }
       }

        return fieldLevel;
    }


@Test
    public void tessss(){
    InnerBean innerBean1 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao1","张三");

    OutBean outBean1 = new OutBean("0101","outBean",innerBean1);
    try {
        String v = getChildFiledValue(outBean1,outBean1.getData().getClass(),"data","contact");
        System.out.println(v);
    } catch (NoSuchFieldException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    }
}

    public String getChildFiledValue(Object obj,Class childClazz,String fieldName,String childFieldName) throws NoSuchFieldException, IllegalAccessException {
        String v = "";

        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        if(!field.getGenericType().toString().equals(childClazz.toString())){
            return v;
        }

        Object childObj = field.get(obj);

        Field childField = childObj.getClass().getDeclaredField(childFieldName);

        childField.setAccessible(true);

        v = (String) childField.get(childObj);
        return v;
    }


    public Map<String,XSSFCell> fillContent( int startRow,int endRow,XSSFSheet sheet){

        Map<String,XSSFCell> cellMap = new HashMap<String, XSSFCell>();

        if( startRow <0 || startRow>=endRow ){
            return cellMap;
        }
        endRow = endRow <= sheet.getLastRowNum() ? endRow:sheet.getLastRowNum();

        for( int rowNum = startRow; rowNum<=endRow ;rowNum++ ){
            XSSFRow row = sheet.getRow(rowNum);

            if( row ==  null ){
                continue;
            }

            for( int cellNum = row.getFirstCellNum(); cellNum <= row.getLastCellNum() ;cellNum++ ){

                XSSFCell cell = row.getCell(cellNum);
                if( cell == null ){
                    continue;
                }
                String v = getCellValue(cell);

                if( v == null || v.trim().equals("") ){
                    continue;
                }

                if( v.startsWith("$") ){
                    String fieldName = v.substring(1);
                    cellMap.put(fieldName,cell);
                }
            }
        }

        return cellMap;
    }



    /**
     * 仅支持  文本类型
     * @param xssfCell
     * @return
     */
    private static String getCellValue(XSSFCell xssfCell){

        String value = null;

        if( xssfCell == null )
        {
            return value;
        }

        CellType cellType = xssfCell.getCellTypeEnum();
        switch ( cellType ){
            case STRING :
                value = (xssfCell.getStringCellValue() == null)?null:xssfCell.getStringCellValue().trim();
                break;
            case NUMERIC:
                HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                value = dataFormatter.formatCellValue(xssfCell);
                break;
            case BLANK:
                break;
            default:
                throw new IllegalStateException("Cannot get a STRING value from a "+xssfCell.getCellTypeEnum()+" cell");
        }

        return value;
    }

    //copy
    public void copyRows(int startRow, int endRow, int pPosition,
                         XSSFSheet sheet) {
        int pStartRow = startRow;
        int pEndRow = endRow ;
        int targetRowFrom;
        int targetRowTo;
        int columnCount;
        CellRangeAddress region = null;
        int i;
        int j;
        if (pStartRow == 0 || pEndRow == 0) {
            return;
        }
        // 拷贝合并的单元格
        for ( i = 0; i < sheet.getNumMergedRegions(); i++ ) {
            region = sheet.getMergedRegion(i);
            if ((region.getFirstRow() >= pStartRow)
                    && (region.getLastRow() <= pEndRow)) {
                targetRowFrom = region.getFirstRow() - pStartRow + pPosition;
                targetRowTo = region.getLastRow() - pStartRow + pPosition;
                CellRangeAddress newRegion = region.copy();
                newRegion.setFirstRow(targetRowFrom);
                newRegion.setFirstColumn(region.getFirstColumn());
                newRegion.setLastRow(targetRowTo);
                newRegion.setLastColumn(region.getLastColumn());
                sheet.addMergedRegion(newRegion);
            }
        }
        // 设置列宽
        for (i = pStartRow; i <= pEndRow; i++) {
            XSSFRow sourceRow = sheet.getRow(i);
            columnCount = sourceRow.getLastCellNum();
            if (sourceRow != null) {
                XSSFRow newRow = sheet.createRow(pPosition - pStartRow + i);
                newRow.setHeight(sourceRow.getHeight());
                for (j = 0; j < columnCount; j++) {
                    XSSFCell templateCell = sourceRow.getCell(j);
                    if (templateCell != null) {
                        XSSFCell newCell = newRow.createCell(j);
                        copyCell(templateCell, newCell);
                    }
                }
            }
        }
    }


    private void copyCell(XSSFCell srcCell, XSSFCell distCell) {
        distCell.setCellStyle(srcCell.getCellStyle());
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        int srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                distCell.setCellValue(srcCell.getDateCellValue());
            } else {
                distCell.setCellValue(srcCell.getNumericCellValue());
            }
        } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
            distCell.setCellValue(srcCell.getRichStringCellValue());
        } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
            // nothing21
        } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
            distCell.setCellValue(srcCell.getBooleanCellValue());
        } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
            distCell.setCellErrorValue(srcCell.getErrorCellValue());
        } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
            distCell.setCellFormula(srcCell.getCellFormula());
        } else { // nothing29

        }
    }
}
