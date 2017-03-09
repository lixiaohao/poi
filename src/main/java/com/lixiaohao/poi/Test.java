package com.lixiaohao.poi;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lixiaohao on 2017/2/9
 *
 * @Description
 * @Create 2017-02-09 14:18
 * @Company
 */
public class Test {
    public static void main(String[] args) {
        String testStr = "1,2,3,4,5";
        String[] str = testStr.split(",");
        for(Object o:str){
            if(o.equals("2")){
                o = "0";
                System.out.println("ddd");
            }
        }
        System.out.println(testStr);
    }
}
