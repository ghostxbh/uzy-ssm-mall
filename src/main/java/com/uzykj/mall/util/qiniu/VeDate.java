package com.uzykj.mall.util.qiniu;



import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class VeDate {
	
	
	public static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_TIME_OF_hh = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制
	public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat DATE_TIME_OF_SHORT = new SimpleDateFormat("yyyyMMdd HHmmss");
	public static final SimpleDateFormat DATE_TIME_OF_SHORT_NOSPACE = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat YEAR_MONTH = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat YEAR_MONTH_OF_SHORT = new SimpleDateFormat("yyyyMM");
	
	
	
 /**
  * 获取现在时间
  * 
  * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
  */
 public static Date getNowDate() {
  Date currentTime = new Date();
  String dateString = VeDate.DATE_TIME.format(currentTime);
  ParsePosition pos = new ParsePosition(8);
  Date currentTime_2 = VeDate.DATE_TIME.parse(dateString, pos);
  return currentTime_2;
 }

 /**
  * 获取现在日期
  * 
  * @return返回短时间格式 yyyy-MM-dd
  */
 public static Date getNowDateShort() {
  Date currentTime = new Date();
  String dateString = VeDate.DATE.format(currentTime);
  ParsePosition pos = new ParsePosition(8);
  Date currentTime_2 = VeDate.DATE.parse(dateString, pos);
  return currentTime_2;
 }

 /**
  * 获取现在时间
  * 
  * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
  */
 public static String getStringDate() {
  Date currentTime = new Date();
  String dateString = VeDate.DATE_TIME.format(currentTime);
  return dateString;
 }

 /**
  * 获取现在日期
  * 
  * @return 返回短时间字符串格式yyyy-MM-dd
  */
 public static String getStringDateShort() {
  Date currentTime = new Date();
  String dateString = VeDate.DATE.format(currentTime);
  return dateString;
 }

 /**
  * 获取时间 小时:分;秒 HH:mm:ss
  * 
  * @return
  */
 public static String getTimeShort() {
  Date currentTime = new Date();
  String dateString = VeDate.TIME.format(currentTime);
  return dateString;
 }

 /**
  * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
  * 
  * @param strDate
  * @return
  */
 public static Date strToDateLong(String strDate) {
  ParsePosition pos = new ParsePosition(0);
  Date strtodate = VeDate.DATE_TIME.parse(strDate, pos);
  return strtodate;
 }

 /**
  * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
  * 
  * @param dateDate
  * @return
  */
 public static String dateToStrLong(Date dateDate) {
  String dateString = VeDate.DATE_TIME.format(dateDate);
  return dateString;
 }

 /**
  * 将短时间格式时间转换为字符串 yyyy-MM-dd
  * 
  * @param dateDate
  * @param k
  * @return
  */
 public static String dateToStr(Date dateDate) {
  String dateString = VeDate.DATE.format(dateDate);
  return dateString;
 }

 /**
  * 将短时间格式字符串转换为时间 yyyy-MM-dd 
  * 
  * @param strDate
  * @return
  */
 public static Date strToDate(String strDate) {
  ParsePosition pos = new ParsePosition(0);
  Date strtodate = VeDate.DATE.parse(strDate, pos);
  return strtodate;
 }

 /**
  * 得到现在时间
  * 
  * @return
  */
 public static Date getNow() {
  Date currentTime = new Date();
  return currentTime;
 }

 /**
  * 提取一个月中的最后一天
  * 
  * @param day
  * @return
  */
 public static Date getLastDate(long day) {
  Date date = new Date();
  long date_3_hm = date.getTime() - 3600000 * 34 * day;
  Date date_3_hm_date = new Date(date_3_hm);
  return date_3_hm_date;
 }

 
 /**
  * 根据字符串 年-月 获取当月中的最后一天 
  * @param yearMonth    例格式： 2018-04
  * @return   例格式： 2018-04-30
  */
 public static String getLastDateOfString(String yearMonth){
	 String[] yearMonthString = yearMonth.split("-");
	 int year = Integer.valueOf(yearMonthString[0]);
	 int month = Integer.valueOf(yearMonthString[1]);
	 Calendar cal = Calendar.getInstance();
     //设置年份
     cal.set(Calendar.YEAR,year);
     //设置月份
     cal.set(Calendar.MONTH, month-1);
     //获取某月最大天数
     int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
     //设置日历中月份的最大天数
     cal.set(Calendar.DAY_OF_MONTH, lastDay);
     //格式化日期
     String lastDayOfMonth = VeDate.DATE.format(cal.getTime());
      
     return lastDayOfMonth;
	 
 }
 /**
  * 得到现在时间
  * 
  * @return 字符串 yyyyMMdd HHmmss
  */
 public static String getStringToday() {
  Date currentTime = new Date();
  String dateString = VeDate.DATE_TIME_OF_SHORT.format(currentTime);
  return dateString;
 }

 /**
  * 得到现在小时
  */
 public static String getHour() {
  Date currentTime = new Date();
  String dateString = VeDate.DATE_TIME.format(currentTime);
  String hour;
  hour = dateString.substring(11, 13);
  return hour;
 }

 /**
  * 得到现在分钟
  * 
  * @return
  */
 public static String getTime() {
  Date currentTime = new Date();
  String dateString = VeDate.DATE_TIME.format(currentTime);
  String min;
  min = dateString.substring(14, 16);
  return min;
 }

 /**
  * 根据用户传入的当前类的SimpleDateFormat常量值，返回该常量值的格式的时间 
  * 
  * @param VeDate.DATE_TIME : "yyyy-MM-dd HH:mm:ss"
  * 		...
  *            
  * @return
  */
 public static String getUserDate(SimpleDateFormat sdf) {
  Date currentTime = new Date();
  String dateString = sdf.format(currentTime);
  return dateString;
 }

 /**
  * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
  */
 public static String getTwoHour(String st1, String st2) {
  String[] kk = null;
  String[] jj = null;
  kk = st1.split(":");
  jj = st2.split(":");
  if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
   return "0";
  else {
   double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
   double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
   if ((y - u) > 0)
    return y - u + "";
   else
    return "0";
  }
 }

 /**
  * 得到二个日期间的间隔天数
  */
 public static String getTwoDay(String sj1, String sj2) {
  long day = 0;
  try {
   Date date = VeDate.DATE.parse(sj1);
   Date mydate = VeDate.DATE.parse(sj2);
   day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
  } catch (Exception e) {
   return "";
  }
  return day + "";
 }

 /**
  * 时间前推或后推分钟,其中JJ表示分钟.
  */
 public static String getPreTime(String sj1, String jj) {
  String mydate1 = "";
  try {
   Date date1 = VeDate.DATE_TIME.parse(sj1);
   long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
   date1.setTime(Time * 1000);
   mydate1 = VeDate.DATE_TIME.format(date1);
  } catch (Exception e) {
  }
  return mydate1;
 }

 /**
  * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
  */
 public static String getNextDay(String nowdate, String delay) {
  try{
  String mdate = "";
  Date d = strToDate(nowdate);
  long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
  d.setTime(myTime * 1000);
  mdate = VeDate.DATE.format(d);
  return mdate;
  }catch(Exception e){
   return "";
  }
 }

 /**
  * 判断是否润年
  * 
  * @param ddate
  * @return
  */
 public static boolean isLeapYear(String ddate) {

  /**
   * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
   * 3.能被4整除同时能被100整除则不是闰年
   */
  Date d = strToDate(ddate);
  GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
  gc.setTime(d);
  int year = gc.get(Calendar.YEAR);
  if ((year % 400) == 0)
   return true;
  else if ((year % 4) == 0) {
   if ((year % 100) == 0)
    return false;
   else
    return true;
  } else
   return false;
 }

 /**
  * 返回美国时间格式 26 Apr 2006
  * 
  * @param str
  * @return
  */
 public static String getEDate(String str) {
  ParsePosition pos = new ParsePosition(0);
  Date strtodate = VeDate.DATE.parse(str, pos);
  String j = strtodate.toString();
  String[] k = j.split(" ");
  return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
 }

 /**
  * 获取一个月的最后一天
  * 参数yyyy-MM-dd格式
  * @param dat
  * @return
  */
 public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
  String str = dat.substring(0, 8);
  String month = dat.substring(5, 7);
  int mon = Integer.parseInt(month);
  if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
   str += "31";
  } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
   str += "30";
  } else {
   if (isLeapYear(dat)) {
    str += "29";
   } else {
    str += "28";
   }
  }
  return str;
 }

 /**
  * 获取一个月的最后一天
  * 参数yyyy-MM格式
  * @param dat
  * @return
  */
 public static String getEndDateOfMonth2(String dat) {// yyyy-MM
  String str = dat.substring(0, 7);
  String month = dat.substring(5, 7);
  int mon = Integer.parseInt(month);
  if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
   str += "-31";
  } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
   str += "-30";
  } else {
   if (isLeapYear(dat)) {
    str += "-29";
   } else {
    str += "-28";
   }
  }
  return str;
 }
 
 /**
  * 判断二个时间是否在同一个周
  * 
  * @param date1
  * @param date2
  * @return
  */
 public static boolean isSameWeekDates(Date date1, Date date2) {
  Calendar cal1 = Calendar.getInstance();
  Calendar cal2 = Calendar.getInstance();
  cal1.setTime(date1);
  cal2.setTime(date2);
  int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
  if (0 == subYear) {
   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
    return true;
  } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
   // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
    return true;
  } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
    return true;
  }
  return false;
 }

 /**
  * 产生周序列,即得到当前时间所在的年度是第几周
  * 
  * @return
  */
 public static String getSeqWeek() {
  Calendar c = Calendar.getInstance(Locale.CHINA);
  String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
  if (week.length() == 1)
   week = "0" + week;
  String year = Integer.toString(c.get(Calendar.YEAR));
  return year + week;
 }

 /**
  * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
  * 
  * @param sdate
  * @param num
  * @return
  */
 public static String getWeek(String sdate, String num) {
  // 再转换为时间
  Date dd = VeDate.strToDate(sdate);
  Calendar c = Calendar.getInstance();
  c.setTime(dd);
  if (num.equals("1")) // 返回星期一所在的日期
   c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
  else if (num.equals("2")) // 返回星期二所在的日期
   c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
  else if (num.equals("3")) // 返回星期三所在的日期
   c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
  else if (num.equals("4")) // 返回星期四所在的日期
   c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
  else if (num.equals("5")) // 返回星期五所在的日期
   c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
  else if (num.equals("6")) // 返回星期六所在的日期
   c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
  else if (num.equals("0")) // 返回星期日所在的日期
   c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
  return VeDate.DATE.format(c.getTime());
 }

 /**
  * 根据一个日期，返回是星期几的字符串
  * 
  * @param sdate
  * @return
  */
 public static String getWeek(String sdate) {
  // 再转换为时间
  Date date = VeDate.strToDate(sdate);
  Calendar c = Calendar.getInstance();
  c.setTime(date);
  // int hour=c.get(Calendar.DAY_OF_WEEK);
  // hour中存的就是星期几了，其范围 1~7
  // 1=星期日 7=星期六，其他类推
  return new SimpleDateFormat("EEEE").format(c.getTime());
 }
 public static String getWeekStr(String sdate){
  String str = "";
  str = VeDate.getWeek(sdate);
  if("1".equals(str)){
   str = "星期日";
  }else if("2".equals(str)){
   str = "星期一";
  }else if("3".equals(str)){
   str = "星期二";
  }else if("4".equals(str)){
   str = "星期三";
  }else if("5".equals(str)){
   str = "星期四";
  }else if("6".equals(str)){
   str = "星期五";
  }else if("7".equals(str)){
   str = "星期六";
  }
  return str;
 }

 /**
  * 两个时间之间的天数
  * 
  * @param date1
  * @param date2
  * @return
  */
 public static long getDays(String date1, String date2) {
  if (date1 == null || date1.equals(""))
   return 0;
  if (date2 == null || date2.equals(""))
   return 0;
  // 转换为标准时间
  Date date = null;
  Date mydate = null;
  try {
   date = VeDate.DATE.parse(date1);
   mydate = VeDate.DATE.parse(date2);
  } catch (Exception e) {
  }
  long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
  return day;
 }

 /**
  * 形成如下的日历 ， 根据传入的一个时间返回一个结构 星期日 星期一 星期二 星期三 星期四 星期五 星期六 下面是当月的各个时间
  * 此函数返回该日历第一行星期日所在的日期
  * 
  * @param sdate
  * @return
  */
 public static String getNowMonth(String sdate) {
  // 取该时间所在月的一号
  sdate = sdate.substring(0, 8) + "01";

  // 得到这个月的1号是星期几
  Date date = VeDate.strToDate(sdate);
  Calendar c = Calendar.getInstance();
  c.setTime(date);
  int u = c.get(Calendar.DAY_OF_WEEK);
  String newday = VeDate.getNextDay(sdate, (1 - u) + "");
  return newday;
 }

 /**
  * 取得数据库主键 生成格式为yyyymmddhhmmss+k位随机数
  * 
  * @param k
  *            表示是取几位随机数，可以自己定
  */

 public static String getNo(int k) {

  return getUserDate(VeDate.DATE_TIME_OF_SHORT_NOSPACE) + getRandom(k);
 }

 /**
  * 返回一个随机数
  * 
  * @param i
  * @return
  */
 public static String getRandom(int i) {
  Random jjj = new Random();
  // int suiJiShu = jjj.nextInt(9);
  if (i == 0)
   return "";
  String jj = "";
  for (int k = 0; k < i; k++) {
   jj = jj + jjj.nextInt(9);
  }
  return jj;
 }
 
 // string转换为long
 public static long getDateLong(String dateStr) {
	Date d = strToDateLong(dateStr);
	return d.getTime();
 }
 
 // long转换为String
 public static String getDateString(long dateLong) {
	 TimeZone tz1 = TimeZone.getTimeZone("GMT+08:00");  
	 TimeZone.setDefault(tz1);  
	//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
	Date dt = new Date(dateLong);
	String sDateTime = VeDate.DATE_TIME.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
	System.out.println(sDateTime);
	 return sDateTime;
 }

 /**
  * 
  * @param args
  */
 public static boolean RightDate(String date) {

  SimpleDateFormat sdf = VeDate.DATE_TIME;
  ;
  if (date == null)
   return false;
  if (date.length() > 10) {
   sdf = VeDate.DATE_TIME_OF_hh;
  } else {
   sdf = VeDate.DATE;
  }
  try {
   sdf.parse(date);
  } catch (ParseException pe) {
   return false;
  }
  return true;
 }
 
 /**
  * 计算时间差（分）
  * @param from_time 原时间
  * @param to_time 现时间
  * @return
  * @throws ParseException
  */
 public static int shijiancha_fen(String from_time, String to_time) throws ParseException {
	 SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 ParsePosition pos = new ParsePosition(0);
	 Date _from_time = simpleFormat.parse(from_time, pos);
	 pos = new ParsePosition(0);
	 Date _to_time = simpleFormat.parse(to_time, pos);
	 String fromDate = simpleFormat.format(_from_time);  
	 String toDate = simpleFormat.format(_to_time);  
	 pos = new ParsePosition(0);
	 Date _fromDate = simpleFormat.parse(fromDate, pos);
	 pos = new ParsePosition(0);
	 Date _toDate = simpleFormat.parse(toDate, pos);
	  
	 long from = _fromDate.getTime();  
	 long to = _toDate.getTime();  
	 int minutes = (int) ((to - from)/(1000 * 60));  
	 return minutes;
 }
 
/**
 * 使用当前月份,得到上一个月的月份:月份的格式是:yyyy-MM  

 * @DateTime 2018年8月18日 下午12:02:34
 * @param currentDate
 * @return
 */
 public static String getPrevMonth(String currentDate) {

	 Date date = null;  
     try {  
         date = VeDate.YEAR_MONTH.parse(currentDate);  
     } catch (ParseException e) { 
         e.printStackTrace();  
         System.out.println("参数日期格式需为:yyyy-MM");
     }  
     Calendar c = Calendar.getInstance();  
     c.setTime(date);  
     c.add(Calendar.MONTH, -1);  
	 return VeDate.YEAR_MONTH.format(c.getTime());

 } 
 
 /**
  * 使用当前月份,得到上一个月的月份:月份的格式是:yyyyMM

  * @DateTime 2018年8月18日 上午11:46:13
  * @param currentDate
  * @return yyyyMM
  */
 public static String getPrevMonthOfShort(String currentDate) {

	 Date date = null;  
	 if(currentDate.length()>6){
		 throw new RuntimeException("参数日期格式需为:yyyyMM");
	 }
     try {  
         date = VeDate.YEAR_MONTH_OF_SHORT.parse(currentDate);  
     } catch (ParseException e) { 
         e.printStackTrace();  
         System.out.println("参数日期格式需为:yyyyMM");
     }  
     Calendar c = Calendar.getInstance();  
     c.setTime(date);  
     c.add(Calendar.MONTH, -1);  
	 return VeDate.YEAR_MONTH_OF_SHORT.format(c.getTime());
 } 
 
 
 /**
  * 
  * 计算两个日期相差的月份数
  * 
  * @param date1 日期1
  * @param date2 日期2
  * @param pattern  日期1和日期2的日期格式
  * @return  相差的月份数
  * @throws ParseException
  */
 public static int countMonths(String date1,String date2,String pattern) throws ParseException{
     SimpleDateFormat sdf=new SimpleDateFormat(pattern);
     
     Calendar c1=Calendar.getInstance();
     Calendar c2=Calendar.getInstance();
     
     c1.setTime(sdf.parse(date1));
     c2.setTime(sdf.parse(date2));
     
     int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
     
     //开始日期若小月结束日期
     if(year<0){
         year=-year;
         return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
     }
    
     return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
 }
 
 
 /**
  * 判断startMonth是否在endMonth之前
  * @param startMonth
  * @param endMonth
  * @return
  */
 public static boolean isBefore(String  startMonth, String endMonth){
	 boolean flag = true;
	 Date startDate = null;
	 Date endDate = null;
	 try {
		 startDate = VeDate.YEAR_MONTH.parse(startMonth);
		 endDate = VeDate.YEAR_MONTH.parse(endMonth);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	 if(!(startDate.getTime()<=endDate.getTime())){
		 return false;
	 }
	 return flag;
 }
 
 
 /**
  * 获取两个月份之间的所有月份
  * @param startMonth
  * @param endMonth
  * @return String[]
  * @throws Exception
  */
 public static String[] getBetweenMonths(String startMonth, String endMonth)throws Exception{
	 
	 long start = VeDate.YEAR_MONTH_OF_SHORT.parse(startMonth).getTime();
	 long end = VeDate.YEAR_MONTH_OF_SHORT.parse(endMonth).getTime();
	 if(start>end){
		 throw new RuntimeException("开始月份不能大于结束月份");
	 }
	 String[] months = new String[0];
	 long current = start;
	 while(current<=end){
		 Date date = new Date(current);
		 months = Arrays.copyOf(months, months.length+1);
		 months[months.length-1] =VeDate.YEAR_MONTH_OF_SHORT.format(date);
		 
		 Calendar cur = Calendar.getInstance();
		 cur.setTime(date);
		 cur.add(Calendar.MONTH, 1);
		 current = cur.getTimeInMillis();
	 }
	 return months;
 }
 
/**
 * 获得账期的最后一天
 * 
 * @param yearMonth
 * @return
 */
public static String getLastDateByPeriod(String yearMonth){
	 int year = Integer.valueOf(yearMonth.substring(0,4));
	 int month = Integer.valueOf(yearMonth.substring(4,6));
	 Calendar cal = Calendar.getInstance();
     //设置年份
     cal.set(Calendar.YEAR,year);
     //设置月份
     cal.set(Calendar.MONTH, month-1);
     //获取某月最大天数
     int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
     //设置日历中月份的最大天数
     cal.set(Calendar.DAY_OF_MONTH, lastDay);
     //格式化日期
     String lastDayOfMonth = VeDate.DATE.format(cal.getTime());
      
     return lastDayOfMonth;
 }

/**
 * 获得账期的第一天
 * 
 * @param yearMonth
 * @return
 */
public static String getFirstDateByPeriod(String yearMonth){
	 int year = Integer.valueOf(yearMonth.substring(0,4));
	 int month = Integer.valueOf(yearMonth.substring(4,6));
	 Calendar cal = Calendar.getInstance();
     //设置年份
     cal.set(Calendar.YEAR,year);
     //设置月份
     cal.set(Calendar.MONTH, month-1);
     //设置日历中月份的最大天数
     cal.set(Calendar.DAY_OF_MONTH, 1);
     //格式化日期
     String firstDayOfMonth = VeDate.DATE.format(cal.getTime());
     return firstDayOfMonth;
}

 public static void main(String[] args) throws Exception {
	 System.out.println(getPrevMonth("2018-01"));
 }

}