package com.mytype.douyin.until.time;

public class TimeMain {
    public static void main(String[] args) throws Exception {
        /*
         * 时间戳转化为 yyyy-MM-dd格式/
         */
        TimeUtil.StampToTime();//static静态类可以直接使用类名.方法

        /*
         * yyyy-MM-dd格式转换为时间戳
         */
        final TimeUtil ts = new TimeUtil();
        ts.TimeToStamp();
    }
}
