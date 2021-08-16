package com.ant.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * ��ȡ�������ں�����ʱ��������ʱ����
 * auth lic
 * date 2021/06/29
 */
@Slf4j
public final class TimeRandomUtil {

    private TimeRandomUtil() {}

    /**
     * �������ʱ���
     * @param beginDate ��ʼ����
     * @param endDate   ��������
     * @param startTime һ���еĿ�ʼʱ��� ��':'Ӣ��ð�Ÿ���
     * @param endTime   һ���еĽ���ʱ��� ��':'Ӣ��ð�Ÿ���
     * @return
     */
    public static String getRandomTime(String beginDate, String endDate, long startTime, long endTime){
        //����
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //����
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
            //���ʱ����ĺ���ֵ
            long randomDate = getRandomDate(begin.getTime(), end.getTime(), startTime, endTime);
            //��Ҫ���ص�ʱ��
            return format1.format(new Date(randomDate));
        }catch (ParseException e){
            log.info(e.getMessage());
            return "";
        }
    }

    /**
     * ���������
     * @param begin ��ʼʱ��
     * @param end   ����ʱ��
     * @return
     */
    private static long getRandomDate(long begin, long end, long start, long endTime){
        //���ɵ�ʱ�������
        long randomTime = begin + (long)(Math.random()*(end-begin));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            //ȥ��Сʱ��
            long time = format.parse(format.format(new Date(randomTime))).getTime();
            if(randomTime >= (time + start) && randomTime <= (time + endTime)){
                return randomTime;
            }else{
                //������������ֵ�����ϴ����ֵ����ݹ�
                randomTime = getRandomDate(begin, end, start, endTime);
                return randomTime;
            }
        }catch (ParseException e){
            log.info(e.getMessage());
            return 1L;
        }
    }

    /**
     * ��������ʱ��ĺ���ֵ����Ӣ�ĵ�:ð�ŷָ����Ҳ����пո�
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
