package com.lixiaohao.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.junit.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by lixiaohao on 2017/3/9
 *
 * @Description
 * @Create 2017-03-09 11:57
 * @Company
 */
public class writeExcel {

    private Map<String,XSSFCell> excelMess = new HashMap<String, XSSFCell>();

    @org.junit.Test
    public void testss(){
        List<Price> prices = new ArrayList<Price>();
        Price price1 = new Price();
        price1.setOderQuanties("10");
        price1.setUnitPrice("100$");

        Price price2 = new Price();
        price2.setOderQuanties("10");
        price2.setUnitPrice("100$");

        prices.add(price1);prices.add(price2);

            InnerBean innerBean1 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao1","张三");
            innerBean1.setAddress("南京东路1010号");

            OutBean outBean1 = new OutBean("0101","outBean",innerBean1);
//        outBean1.setPrices(prices);

            InnerBean innerBean2 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao2","张三");
            innerBean2.setAddress("南京东路1010号");
            OutBean outBean2 = new OutBean("0102","outBean",innerBean2);
//        outBean2.setPrices(prices);
            InnerBean innerBean3 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao3","");

            OutBean outBean3 = new OutBean("0103","outBean",innerBean3);
//        outBean3.setPrices(prices);
            InnerBean innerBean4 = new InnerBean("ALIBABA","com.lixiaohao.poi.InnerBean","lixaohao4",null);

            OutBean outBean4 = new OutBean("0104","outBean",innerBean4);
//        outBean4.setPrices(prices);

            List<OutBean> outBeans = new ArrayList<OutBean>();
            outBeans.add(outBean1);
            outBeans.add(outBean2);
            outBeans.add(outBean3);
            outBeans.add(outBean4);
            String srcPath = "C:\\Users\\lixiaohao.ZZGRP\\Desktop\\temp\\students1.xlsx";
            String targetPath = "C:\\Users\\lixiaohao.ZZGRP\\Desktop\\temp\\copyStudents.xlsx";
        importAccordingTotemplate(outBeans,srcPath,targetPath);
    }

    public void importAccordingTotemplate(List<OutBean> outBeans,String srcPath,String targetPath){
        int startListIndex = 0;
        int endLinstIndex = 0;

        int startRow = 0;
        int endRow = 0;
        int step = 0;
        int position = 0;

        try{
            FileInputStream in = new FileInputStream(srcPath);
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);

      /*      for( int rowNum =0; rowNum <= sheet.getLastRowNum(); rowNum++ ){

                XSSFCell cell = sheet.getRow(rowNum).getCell(0);

                if( cell == null ){
                    continue;
                }
                String v = cell.getStringCellValue();
                if( v == null || v.trim().equals("") ){
                    continue;
                }
//      解析开循环开头
                if( v.startsWith(Label.LIST_START) ){
                    startListIndex = rowNum;
                    stack = 0;
                    stack ++;
                    continue;

                }
//      解析开循环结尾
                if(v.startsWith(Label.LIST_END)) {
                    endLinstIndex = rowNum;
                    stack --;
                    break;
                }
            }

            if(stack != 0){
                throw new IllegalArgumentException("excel 开始和结束的标识格式不正确。");
            }*/

            Map<String,XSSFCell> lableMap = parseLabel(sheet);
          if(lableMap == null || lableMap.size()<=0){

              startRow = sheet.getFirstRowNum();
              endRow = sheet.getLastRowNum();
              step = endRow - startRow +1;
              position = startRow + step;
          }else {
              XSSFCell startCell = lableMap.get(Label.LIST_START);
              XSSFCell endCell = lableMap.get(Label.LIST_END);
              startRow = startCell.getRowIndex()+1;
              endRow = endCell.getRowIndex()-1;

              step = endRow - startRow +1;

              position = startRow + step;
          }

        for(OutBean outBean: outBeans){
            copyRows(startRow,endRow,position,sheet);

            Map<String,XSSFCell> cellMap =  fillContent(position,position+step-1,sheet);

            if( cellMap != null && cellMap.size()>0 ){
                for( Map.Entry<String,XSSFCell> cellEntry:cellMap.entrySet() ){
                    fillCell(cellEntry.getValue(),cellEntry.getKey(),outBean);
                }
            }

            position += step;
        }

            FileOutputStream fout = new FileOutputStream(targetPath);
            wb.write(fout);
            fout.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private Map<String,XSSFCell> parseLabel(XSSFSheet sheet){

        Map<String,XSSFCell> lableMess = new HashMap<String, XSSFCell>();

        List<String> startLabels = new ArrayList<String>();
        List<String> endLabels = new ArrayList<String>();

        for( int rowNum =0; rowNum <= sheet.getLastRowNum(); rowNum++ ){
            XSSFRow row = sheet.getRow(rowNum);
            if( row == null ){
                continue;
            }

            int num = 0;
            for( int colNum = 0; colNum <= row.getLastCellNum(); colNum++ ){

                XSSFCell cell = row.getCell(colNum);

                if( cell == null ){
                    continue;
                }
                String v = cell.getStringCellValue();
                if( v == null || v.trim().equals("") ){
                    continue;
                }
                v = v.trim();
                //解析出所有的标签  (以#开头)

                if( v.startsWith("#") ){
                    num++;
                    if( num >1 ){
                        throw  new IllegalArgumentException("同一行不允许出现多个标签! 标签名:"+ v );
                    }
                    Label.existCheck(v);

                    lableMess.put(v,cell);
                    if( v.endsWith(Label.start) ){ startLabels.add(v); }
                    if( v.endsWith(Label.end) ){ endLabels.add(v); }

                }
            }
        }

        int len = startLabels.size() >= endLabels.size()? startLabels.size() : endLabels.size();
        if(startLabels.size() >= endLabels.size()){

            for( int i=0;i< len;i++ ){
                Boolean flag = false;
                String s = startLabels.get(i).replace(Label.start,"");
                for( String end:endLabels ){
                    String e = end.replace(Label.end,"");

                    if( e.equals(s) ){
                        flag = true;
                    }

                }
                if( !flag ){
                    throw  new IllegalArgumentException("标签没有闭合。缺少标签："+ s + Label.end );
                }
            }
        }else {
            for( int i=0;i< len;i++ ){
                Boolean flag = false;
                String e = endLabels.get(i).replace(Label.end,"");
                for( String start:startLabels ){
                    String s = start.replace(Label.start,"");

                    if( e.equals(s) ){
                        flag = true;
                    }

                }
                if( !flag ){
                    throw  new IllegalArgumentException("标签没有闭合。缺少标签："+ e + Label.start );
                }
            }
        }
        return lableMess;
    }


    private void fillCell(XSSFCell cell,String fieldName,OutBean outBean) throws NoSuchFieldException, IllegalAccessException {

        if(cell == null || outBean == null || fieldName == null || fieldName.trim().equals("")){
            return;
        }

        String v = getFiledValue(outBean,fieldName);

        cell.setCellValue(v);

        //检测fieldName，查看是否是一级属性
       /* int fieldLevel = getFieldLevel(fieldName);

        if(fieldLevel == -1){
            return;
        }

        Class clazz = outBean.getClass();


        if( fieldLevel == 1 ){

            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            String v = (String) field.get(outBean);
            cell.setCellValue(v);
        }

        if( fieldLevel == 2 ){

            int index = fieldName.indexOf(".");

            String parentFieldName = fieldName.substring(0,index);
            String childFieldName = fieldName.substring(index+1);

            String v = getChildFiledValue(outBean,outBean.getData().getClass(),parentFieldName,childFieldName);

            cell.setCellValue(v);

        }*/

    }

    /**
     * 获取像的属性值
     * @param obj               目标对象
     * @param fullFieldName    属性的完全名称(如：data.name)
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private String getFiledValue(Object obj,String fullFieldName) throws NoSuchFieldException, IllegalAccessException {

        String v = "";
        if( fullFieldName == null || fullFieldName.trim().equals("") ){
            return v;
        }
        fullFieldName = fullFieldName.trim();

        int maxLevel = 3;
        int currentLevel = 1;
        int startIndex = -1;
        int endIndex = -1;
        int length = fullFieldName.length();

        if(fullFieldName.charAt(0)=='.' || fullFieldName.charAt(length-1)=='.'){
            throw new IllegalArgumentException("属性名称格式有误,请检查是否多了一个 '.'："+fullFieldName);
        }
        String tempStr = fullFieldName;
    //最后一个.所在位置
        int lastIndex = fullFieldName.lastIndexOf(".");

        //如果只有一层属性，则直接取值
        if( lastIndex == -1){
            Field field = obj.getClass().getDeclaredField(fullFieldName);
            field.setAccessible(true);
             v = (String) field.get(obj);
            return v;
        }

        Object parentObject = obj;

        for( int i = 0; i<length ; i++ ){
            char c = fullFieldName.charAt(i);
                if( c =='.' ){

                    //属性层级自增1
                    if( ++currentLevel >maxLevel ){
                        throw new IllegalArgumentException("属性的层级过深,允许最大层级："+maxLevel+" 当前属性名："+fullFieldName);
                    }

                    endIndex = i;
                    startIndex = ( startIndex==-1 ? 0 : startIndex ) ;

                    tempStr = fullFieldName.substring(startIndex,endIndex);

                    if(i == lastIndex){
                        //如果是最后一层,则取值
                        String laseLevelFieldName = fullFieldName.substring(i+1);

                        Object lastLevelObject = getFieldObject(parentObject,tempStr);

                        Field field = lastLevelObject.getClass().getDeclaredField(laseLevelFieldName);
                        field.setAccessible(true);

                         v = (String) field.get(lastLevelObject);
                        return v;
                    }else {
                        parentObject = getFieldObject(parentObject,tempStr);
                    }

                    startIndex = endIndex+1;
                }
        }

        return v;
    }


    private Object getFieldObject(Object o,String fieldName){
        Object fieldObject = null;

        Class parentClass = o.getClass();
        try {

            Field parentFiled = parentClass.getDeclaredField(fieldName);
            parentFiled.setAccessible(true);

            fieldObject = parentFiled.get(o);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fieldObject;
    }

//    private String getChildFiledValue(Object obj,Class childClazz,String fieldName,String childFieldName) throws NoSuchFieldException, IllegalAccessException {
//        String v = "";
//
//        Field field = obj.getClass().getDeclaredField(fieldName);
//        field.setAccessible(true);
//        if(!field.getGenericType().toString().equals(childClazz.toString())){
//            return v;
//        }
//
//        Object childObj = field.get(obj);
//
//        Field childField = childObj.getClass().getDeclaredField(childFieldName);
//
//        childField.setAccessible(true);
//
//        v = (String) childField.get(childObj);
//        return v;
//    }


    private Map<String,XSSFCell> fillContent( int startRow,int endRow,XSSFSheet sheet){

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

        int mergedNum = sheet.getNumMergedRegions();
       for ( i = 0; i < mergedNum; i++ ) {

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
        // 拷贝单元格
        for (i = pStartRow; i <= pEndRow; i++) {
            XSSFRow sourceRow = sheet.getRow(i);
            if (sourceRow != null) {
                columnCount = sourceRow.getLastCellNum();
//                sheet.shiftRows(pPosition - pStartRow+i-1,pPosition - pStartRow + i,1,true,false);

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

        switch (srcCellType){
           case XSSFCell.CELL_TYPE_NUMERIC:
               if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                   distCell.setCellValue(srcCell.getDateCellValue());
               } else {
                   distCell.setCellValue(srcCell.getNumericCellValue());
               }
               break;
             case XSSFCell.CELL_TYPE_STRING:
                 distCell.setCellValue(srcCell.getRichStringCellValue());
            break;
             case XSSFCell.CELL_TYPE_BLANK:
            break;
             case XSSFCell.CELL_TYPE_BOOLEAN:
                 distCell.setCellValue(srcCell.getBooleanCellValue());
            break;
             case XSSFCell.CELL_TYPE_ERROR:
                 distCell.setCellErrorValue(srcCell.getErrorCellValue());
            break;
             case XSSFCell.CELL_TYPE_FORMULA :
                 distCell.setCellFormula(srcCell.getCellFormula());
            break;
            default:
                break;
        }
    }

}
