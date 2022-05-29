package com.mytype.douyin.until.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /*
     * 时间戳转换为yyyy-MM-dd格式/
     */
    public static void StampToTime() {
        final long nowTime = System.currentTimeMillis();//获取系统当前时间

        long stamp = 1653450298000L;//设置你的时间
        final Date date = new Date(stamp);//新建一个时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//你要转换成的时间格式,大小写不要变
        final String yourtime = sdf.format(date);//转换你的时间
        final String nowtime = sdf.format(nowTime);
        System.out.println("当前系统时间：" + nowtime + "\n" + stamp + " 转换后是：" + yourtime);//打印出你转换后的时间
    }


    /*
     *yyyy-MM-dd格式转换为时间戳 /
     */
    public void TimeToStamp() throws Exception {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate = "2022-05-28 12:04:24";
        final Date datetime = sdf.parse(mydate);//将你的日期转换为时间戳
        final long time = datetime.getTime();
        System.out.println(mydate + "转换后是：" + time);

    }
}
