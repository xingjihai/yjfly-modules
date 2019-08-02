package cn.ejfy.web.tools.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tom
 */
public class DateTool {
	private StringBuffer buffer = new StringBuffer();
	private static String ZERO = "0";
//	public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//	public static SimpleDateFormat format1 = new SimpleDateFormat(
//			"yyyyMMdd HH:mm:ss");
	private static ThreadLocal<SimpleDateFormat> localformat=new ThreadLocal<>();
	public static SimpleDateFormat getDateFormat(){
        SimpleDateFormat df=localformat.get();
        if(df==null){
            df=new  SimpleDateFormat("yyyyMMdd");
            localformat.set(df);
        }
        return df;
    }
    private static ThreadLocal<SimpleDateFormat> localformat1=new ThreadLocal<>();
    
    public static SimpleDateFormat getDateFormat1(){
        SimpleDateFormat df=localformat1.get();
        if(df==null){
            df=new  SimpleDateFormat("yyyyMMdd HH:mm:ss");
            localformat1.set(df);
        }
        return df;
    }
	
	
	public static SimpleDateFormat common_format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final Logger log = LoggerFactory.getLogger(DateTool.class);

	public String getNowString() {
		Calendar calendar = getCalendar();
		buffer.delete(0, buffer.capacity());
		buffer.append(getYear(calendar));

		if (getMonth(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getMonth(calendar));

		if (getDate(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getDate(calendar));
		if (getHour(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getHour(calendar));
		if (getMinute(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getMinute(calendar));
		if (getSecond(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getSecond(calendar));
		return buffer.toString();
	}

	private static int getDateField(Date date, int field) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.get(field);
	}

	public static int getYearsBetweenDate(Date begin, Date end) {
		int bYear = getDateField(begin, Calendar.YEAR);
		int eYear = getDateField(end, Calendar.YEAR);
		return eYear - bYear;
	}

	public static int getMonthsBetweenDate(Date begin, Date end) {
		int bMonth = getDateField(begin, Calendar.MONTH);
		int eMonth = getDateField(end, Calendar.MONTH);
		return eMonth - bMonth;
	}

	public static int getWeeksBetweenDate(Date begin, Date end) {
		int bWeek = getDateField(begin, Calendar.WEEK_OF_YEAR);
		int eWeek = getDateField(end, Calendar.WEEK_OF_YEAR);
		return eWeek - bWeek;
	}

	public static int getDaysBetweenDate(Date begin, Date end) {
		return (int) ((end.getTime()-begin.getTime())/(1000 * 60 * 60 * 24));
	}


	/**
	 * 获取date年后的amount年的第一天的开始时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficYearStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, amount);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取date年后的amount年的最后一天的终止时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficYearEnd(Date date, int amount) {
		Date temp = getStartDate(getSpecficYearStart(date, amount + 1));
		Calendar cal = Calendar.getInstance();
		cal.setTime(temp);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}

	/**
	 * 获取date后的amount季度开始时间
	 * 1-3,4-6,7-9,10-12
	 */
    public static Date getSpecficQuarterStart(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.MONTH, amount*3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getStartDate(c.getTime());
	}
	
	/**
	 * 获取date后的amount季度结束时间
	 */
    public static Date getSpecficQuarterEnd(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            c.add(Calendar.MONTH, amount*3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getFinallyDate(c.getTime());
	}
	 /** 
     * 获取前/后半年的开始时间 
     * @param 
     * @return 
     */ 
    public static  Date getHalfYearStartTime(Date date, int amount){ 
        Calendar c = Calendar.getInstance(); 
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1; 
        int count = 0;
        if(amount>=0){
        	count=(currentMonth+amount*6)/12;
        }else{
        	count=(currentMonth+amount*6-12)/12;
        }
        c.add(Calendar.YEAR, count);
//        currentMonth=(12+currentMonth+amount*6)%12;
        currentMonth=(12+currentMonth+(amount*6%12) )%12;
        try { 
            if (currentMonth >= 1 && currentMonth <= 6){ 
                c.set(Calendar.MONTH, 0); 
            }else if ( (currentMonth >= 7 && currentMonth <= 12 )||currentMonth==0  ){ 
                c.set(Calendar.MONTH, 6); 
            } 
            c.set(Calendar.DATE, 1); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return getStartDate(c.getTime());
        
    } 
    public static void main(String[] args) {
        Date date=getHalfYearStartTime(getDateByString("2018-12-31", "yyyy-MM-dd"), -3);
        Date date2=getHalfYearEndTime(getDateByString("2018-12-31", "yyyy-MM-dd"), -3);
        System.out.println( toString(date,"yyyy-MM-dd") );
        System.out.println( toString(date2,"yyyy-MM-dd") );
    }
    /** 
     * 获取前/后半年的结束时间 
     * @return 
     */ 
    public static  Date getHalfYearEndTime(Date date, int amount){ 
        Calendar c = Calendar.getInstance(); 
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1; 
        int count = 0;
        if(amount>=0){
        	count=(currentMonth+amount*6)/12;
        }else{
        	count=(currentMonth+amount*6-12)/12;
        }
        c.add(Calendar.YEAR, count);
//      currentMonth=(12+currentMonth+amount*6)%12;
        currentMonth=(12+currentMonth+(amount*6%12) )%12;
        try { 
            if (currentMonth >= 1 && currentMonth <= 6){ 
                c.set(Calendar.MONTH, 5); 
                c.set(Calendar.DATE, 30); 
            }else if ( (currentMonth >= 7 && currentMonth <= 12)||currentMonth==0 ){ 
                c.set(Calendar.MONTH, 11); 
                c.set(Calendar.DATE, 31); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return getFinallyDate(c.getTime());
    } 
	/**
	 * 获取date月后的amount月的第一天的开始时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficMonthStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date == null ? new Date() : date);
		cal.add(Calendar.MONTH, amount);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取当前自然月后的amount月的最后一天的终止时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficMonthEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getSpecficMonthStart(date, amount + 1));
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}

	/**
	 * 获取date周后的第amount周的开始时间（这里星期一为一周的开始）
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficWeekStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
		cal.add(Calendar.WEEK_OF_MONTH, amount);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取date周后的第amount周的最后时间（这里星期日为一周的最后一天）
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficWeekEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
		cal.add(Calendar.WEEK_OF_MONTH, amount);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return getFinallyDate(cal.getTime());
	}

	/**
	 * 获取date后的第amount天的开始时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficDateStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return getStartDate(cal.getTime());
	}
	
	/**
	 * 获取date后的第amount天的最后时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficDateEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return getFinallyDate(cal.getTime());
	}
	
	/**
	 * 获取date后的第amount天
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getAmountDate(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return cal.getTime();
	}
	
	/**
	 * 根据两段时间获取 月份（开始和结束）集合 --包含startDate和endDate所在的月份
	 * @param startDate
	 * @param endDate
	 * @return list- Map(Date stm -开始日期,Date etm -结束日期) yyyy-MM-dd HH:mm:ss
	 */
	@SuppressWarnings("rawtypes")
    public static List<Map>  getMonthList(Date startDate,Date endDate){
		List<Map> monthList=new ArrayList<>();
		int amount=0;
		while(true){
			Map<String,Object> calMap=new HashMap<>();
			Date stm=getSpecficMonthStart(startDate,amount);
			Date etm=getSpecficMonthEnd(startDate, amount);
			calMap.put("stm",stm);
			calMap.put("etm", etm);
			monthList.add(calMap);
			amount++;
			if(etm.after(endDate)||etm.equals(endDate)){
				break;
			}
		}
		return monthList;
	}
	/**
     * 根据两段时间获取中间日期集合 --包含startDate和endDate所在的日期
     * @param startDate
     * @param endDate
     * @return  list(Date)   yyyy-MM-dd
     */
    @SuppressWarnings({ "unused" })
    public static List<Date>  getBetweenDateList(Date startDate,Date endDate){
        List<Date> dateList=new ArrayList<>();
        int amount=0;
        while(true){
            Map<String,Object> calMap=new HashMap<>();
            Date stm=getSpecficDateStart(startDate,amount);
            Date etm=getSpecficDateEnd(startDate,amount);
            dateList.add(stm);
            amount++;
            if(etm.after(endDate)||etm.equals(endDate)){
                break;
            }
        }
        return dateList;
    }

	/**
	 * 得到指定日期的一天的的最后时刻23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFinallyDate(Date date) {
//		String temp = format.format(date);
		String temp = getDateFormat().format(date);
		temp += " 23:59:59";

		try {
//			return format1.parse(temp);
			return getDateFormat1().parse(temp);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 得到指定日期的一天的开始时刻00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date) {
//		String temp = format.format(date);
		String temp = getDateFormat().format(date);
		temp += " 00:00:00";

		try {
//			return format1.parse(temp);
		    return getDateFormat1().parse(temp);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param date
	 *            指定比较日期
	 * @param compareDate
	 * @return
	 */
	public static boolean isInDate(Date date, Date compareDate) {
		if (compareDate.after(getStartDate(date))
				&& compareDate.before(getFinallyDate(date))) {
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * 获取两个时间的差值秒
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Integer getSecondBetweenDate(Date d1,Date d2){
		Long second=(d2.getTime()-d1.getTime())/1000;
		return second.intValue();
	}

	private int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	private int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONDAY) + 1;
	}

	private int getDate(Calendar calendar) {
		return calendar.get(Calendar.DATE);
	}

	private int getHour(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	private int getMinute(Calendar calendar) {
		return calendar.get(Calendar.MINUTE);
	}

	private int getSecond(Calendar calendar) {
		return calendar.get(Calendar.SECOND);
	}

	private static Calendar getCalendar() {
		return Calendar.getInstance();
	}

	
	/**
     * 把日期转换成字符串型
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Date date, String pattern){
        if(date == null){
            return "";
        }
        if(pattern == null){
            pattern = "yyyy-MM-dd";
        }
        String dateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            dateString = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateString;
    }
    
    /**
     * 将毫秒数转换为指定格式的日期/时间
     * @param time
     * @param pattern
     * @return
     */
    public static String toString(Long time,String pattern){
        if(pattern == null){
            pattern = "yyyy-MM-dd";
        }
        
        if(time>0){
            if(time.toString().length()==10){
                time = time*1000;
            }
            Date date = new Date(time);
            String str  = toString(date, pattern);
            return str;
        }
        return "";
    }
    
    /**
     * 取得指定格式日期的时间戳
     * @param date
     * @param pattern
     * @return
     */
    public static Long getDateTime(String date, String pattern){
    	if(date==null||"".equals(date)){
    		return null;
    	}
        if(pattern == null || "".equals(pattern)){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Long dtTime = 0L;
        try {
            dtTime = sdf.parse(date).getTime();
        } catch (ParseException e) {
            if(log.isErrorEnabled()){
                log.error("日期：" + date + " 转换格式：" + pattern + "\r\n日期转换发生异常：\r\n", e);
            }
            dtTime = (new Date()).getTime();
        }
        return dtTime/1000;
    }
    public static Date getDateByString(String DateStr,String pattern){
    	if(DateStr==null||"".equals(DateStr)){
    		return null;
    	}
    	if(pattern == null || "".equals(pattern)){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	Date date=null;
    	try {
			date=sdf.parse(DateStr);
		} catch (ParseException e) {
			if(log.isErrorEnabled()){
                log.error("字符串：" + DateStr + " 转换格式：" + pattern + "\r\n日期转换发生异常：\r\n", e);
            };
		}
		return date;
    }
    
    /**
     * 取得指定格式日期的时间戳
     * @param date
     * @return
     */
    public static Long getDateTime(Date date){
        return date.getTime()/1000;
    }
    /**
     * 统一当前时间获取入口，使时间字段一致。（10位）
     * @return
     */
    public static Long getCurrentTime(){
        return System.currentTimeMillis()/1000;
    }
    /**
     * 获取当前时间 字符串："yyyy-MM-dd hh:mm:ss"
     */
    public static String getCurrentTimeString(){
    	return toString(new Date(), "yyyy-MM-dd hh:mm:ss");
    }
}
