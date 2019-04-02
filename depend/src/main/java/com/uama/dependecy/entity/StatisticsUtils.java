package com.uama.dependecy.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 方法统计工具类
 */
public class StatisticsUtils {
    private static Map<String, ArrayList<Statistics>> methodStatisticMap = new HashMap<>();

    public static String getKey(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }

    public static void insertData(Statistics methodWaitUpdate,String key){
        ArrayList<Statistics> list = methodStatisticMap.get(key);
        if(list!=null){
            list.add(methodWaitUpdate);
        }else {
            ArrayList<Statistics> child = new ArrayList<>();
            child.add(methodWaitUpdate);
            methodStatisticMap.put(key,child);
        }

    }

    public static ArrayList<Statistics> getData(String key){
        return methodStatisticMap.get(key);
    }

    public static List<Statistics> getAllData(){
        List<Statistics> list = new ArrayList<>();
        for(String str:methodStatisticMap.keySet()){
            if(methodStatisticMap.get(str)!=null){
                list.addAll(methodStatisticMap.get(str));
            }
        }
        return list;
    }
}
