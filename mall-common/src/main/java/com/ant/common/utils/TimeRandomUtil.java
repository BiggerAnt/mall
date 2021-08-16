package com.ant.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * 获取任意日期和任意时间戳的随机时间类
 * auth lic
 * date 2021/06/29
 */
@Slf4j
public final class TimeRandomUtil {

    private TimeRandomUtil() {}

    /**
     * 生成随机时间戳
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param startTime 一天中的开始时间点 以':'英文冒号隔开
     * @param endTime   一天中的结束时间点 以':'英文冒号隔开
     * @return
     */
    public static String getRandomTime(String beginDate, String endDate, long startTime, long endTime){
        //传入
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //返回
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        String date = "";
        try{
            begin = format.parse(beginDate);
            end = format.parse(endDate);
            if(begin.getTime() >= end.getTime()){
                return "";
            }
            //随机时间戳的毫秒值
            long randomDate = getRandomDate(begin.getTime(), end.getTime(), startTime, endTime);
            //需要返回的时间
            return format1.format(new Date(randomDate));
        }catch (ParseException e){
            log.info(e.getMessage());
            return "";
        }
    }

    /**
     * 随机数生成
     * @param begin 开始时间
     * @param end   结束时间
     * @return
     */
    private static long getRandomDate(long begin, long end, long start, long endTime){
        //生成的时间随机数
        long randomTime = begin + (long)(Math.random()*(end-begin));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            //去掉小时数
            long time = format.parse(format.format(new Date(randomTime))).getTime();
            if(randomTime >= (time + start) && randomTime <= (time + endTime)){
                return randomTime;
            }else{
                //如果随机出来的值不符合传入的值，则递归
                randomTime = getRandomDate(begin, end, start, endTime);
                return randomTime;
            }
        }catch (ParseException e){
            log.info(e.getMessage());
            return 1L;
        }
    }

    /**
     * 生成任意时间的毫秒值，以英文的:冒号分隔，且不能有空格
     * @param time
     * @return
     */
    public static long getMillisecond(String time){
        String[] times = time.split(":");
        long conversionTime = 1L;
        if(times.length == 3) {
            conversionTime = Long.parseLong(times[0].trim())*1000*60*60 + Long.parseLong(times[1].trim())*1000*60 + Long.parseLong(times[2].trim())*1000;
        }else if(times.length == 2){
            conversionTime = Long.parseLong(times[0].trim())*1000*60*60 + Long.parseLong(times[1].trim())*1000*60;
        }else if(times.length == 1) {
            conversionTime = Long.parseLong(times[0].trim()) * 1000 * 60 * 60;
        }
        return conversionTime;
    }
}
