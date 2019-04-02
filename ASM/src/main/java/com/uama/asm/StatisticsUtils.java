package com.uama.asm;

import com.uama.asm.entity.Statistics;

import java.util.HashMap;
import java.util.Map;


/**
 * 方法统计工具类
 */
public class StatisticsUtils {
    private static Map<String, Statistics> methodStatisticMap = new HashMap<>();

    public static String getKey(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }

    public static void insertData(Statistics methodWaitUpdate,String key){
        methodStatisticMap.put(key,methodWaitUpdate);
    }

    public static Statistics getData(String key){
        return methodStatisticMap.get(key);
    }
}
