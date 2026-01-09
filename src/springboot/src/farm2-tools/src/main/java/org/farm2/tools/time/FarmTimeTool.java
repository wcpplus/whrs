package org.farm2.tools.time;

import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.test.FarmRunTimes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FarmTimeTool {

    /**
     * 测量时间并开始计时
     *
     * @return
     */
    public static FarmRunTimes startTimeStopWatch() {
        return FarmRunTimes.getInstance(I18n.msg("计时器"));
    }

    /**
     * 获得12位的日期编码yyyyMMddHHmm
     *
     * @return
     */
    public static String getTimeDate12() {
        // yyyyMMddhhmm
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Date _date = new Date();

        return _sdf.format(_date);
    }

    /**
     * 根据时间 和时间格式 校验是否正确
     *
     * @param sDate  校验的日期
     * @param format 校验的格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean isLegalDate(String sDate, String format) {
        if (StringUtils.isBlank(sDate)) {
            return false;
        }
        int legalLen = sDate.length();
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得某月天数
     *
     * @param year  4位年
     * @param month 2位月
     * @return 有几天
     */
    public static int getMonthMaxDay(String year, String month) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        if (month.trim().length() == 1) {
            month = "0" + month;
        }
        try {
            cal.setTime(sdf.parse(year + month));
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获得之前num年的年份列表
     *
     * @param num 年份数量
     * @return
     */
    public static List<String[]> getFirstYearList(int num) {
        String currentYear = getTimeDate12().substring(0, 4);
        List<String[]> list = new ArrayList<String[]>();
        Integer curentYearint = Integer.valueOf(currentYear);
        for (int i = 0; i < num; i++) {
            String[] yearNode = new String[2];
            yearNode[0] = curentYearint.toString();
            yearNode[1] = curentYearint.toString() + "年";
            list.add(yearNode);
            curentYearint--;
        }
        return list;
    }

    /**
     * 获得季度列表
     *
     * @return
     */
    public static List<String[]> getQuarterList() {
        List<String[]> list = new ArrayList<String[]>();
        String[] node = new String[2];
        node[0] = "1";
        node[1] = "一季度";
        list.add(node);

        String[] node2 = new String[2];
        node2[0] = "2";
        node2[1] = "二季度";
        list.add(node2);

        String[] node3 = new String[2];
        node3[0] = "3";
        node3[1] = "三季度";
        list.add(node3);

        String[] node4 = new String[2];
        node4[0] = "4";
        node4[1] = "四季度";
        list.add(node4);
        return list;
    }

    /**
     * 获得月份列表
     *
     * @return
     */
    public static List<String[]> getMonthList() {
        List<String[]> list = new ArrayList<String[]>();
        String[] node = new String[2];
        node[0] = "1";
        node[1] = "一月";
        list.add(node);

        String[] node2 = new String[2];
        node2[0] = "2";
        node2[1] = "二月";
        list.add(node2);

        String[] node3 = new String[2];
        node3[0] = "3";
        node3[1] = "三月";
        list.add(node3);

        String[] node4 = new String[2];
        node4[0] = "4";
        node4[1] = "四月";
        list.add(node4);

        String[] node5 = new String[2];
        node5[0] = "5";
        node5[1] = "五月";
        list.add(node5);

        String[] node6 = new String[2];
        node6[0] = "6";
        node6[1] = "六月";
        list.add(node6);

        String[] node7 = new String[2];
        node7[0] = "7";
        node7[1] = "七月";
        list.add(node7);

        String[] node8 = new String[2];
        node8[0] = "8";
        node8[1] = "八月";
        list.add(node8);

        String[] node9 = new String[2];
        node9[0] = "9";
        node9[1] = "九月";
        list.add(node9);

        String[] node10 = new String[2];
        node10[0] = "10";
        node10[1] = "十月";
        list.add(node10);

        String[] node11 = new String[2];
        node11[0] = "11";
        node11[1] = "十一月";
        list.add(node11);

        String[] node12 = new String[2];
        node12[0] = "12";
        node12[1] = "十二月";
        list.add(node12);
        return list;
    }

    /**
     * 由12位的日期编码 获得多少天后的日期编码
     *
     * @param addday 多少天后（+ 之后-之前）
     * @param date
     * @return
     */
    public static Date getTimeDateForDay(int addday, Date date) {
        Date _date = null;
        _date = date;
        return getDateAfter(_date, addday);
    }

    /**
     * 获得多少分钟前的时间
     *
     * @param frontMinute 多少分钟（+ 之后-之前）
     * @return
     */
    public static Date getTimeDateForMinute(int frontMinute, Date date) {
        Date _date = null;
        Calendar cal = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历。
        cal.setTime(date);
        cal.add(Calendar.MINUTE, frontMinute);// 取当前日期的前一天.
        _date = cal.getTime();
        return _date;
    }

    /**
     * 获得多少分钟前的时间
     *
     * @return
     */
    public static Date getTimeDateForSecond(int frontSecond, Date date) {
        Date _date = null;
        Calendar cal = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历。
        cal.setTime(date);
        cal.add(Calendar.SECOND, frontSecond);// 取当前日期的前一天.
        _date = cal.getTime();
        return _date;
    }

    /**
     * 格式化时间12位的
     *
     * @param timeDate12_14 时间
     * @return
     * @throws ParseException
     */
    public static Date getFormatTimeDate(String timeDate12_14) throws ParseException {
        if (timeDate12_14.length() > 14) {
            timeDate12_14 = timeDate12_14.substring(0, 14);
        }
        if (12 == timeDate12_14.length()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            return sdf.parse(timeDate12_14);
        } else {
            if (14 == timeDate12_14.length()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                return sdf.parse(timeDate12_14);
            } else {
                timeDate12_14 = timeDate12_14.substring(0, 8);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                return sdf.parse(timeDate12_14);
            }
        }
    }

    /**
     * 格式化时间12位的
     *
     * @param timeDate12_14  时间
     * @param yyyyMMddHHmmss 格式化字符串
     * @return
     */
    public static String getFormatTimeDate12(String timeDate12_14, String yyyyMMddHHmmss) {
        try {
            if (timeDate12_14.length() > 14) {
                timeDate12_14 = timeDate12_14.substring(0, 14);
            }
            if (12 == timeDate12_14.length()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                SimpleDateFormat newSdf = new SimpleDateFormat(yyyyMMddHHmmss);
                Date date;
                date = sdf.parse(timeDate12_14);
                return newSdf.format(date);
            } else {
                if (14 == timeDate12_14.length()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    SimpleDateFormat newSdf = new SimpleDateFormat(yyyyMMddHHmmss);
                    Date date = sdf.parse(timeDate12_14);
                    return newSdf.format(date);
                } else {
                    if (8 == timeDate12_14.length()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        SimpleDateFormat newSdf = new SimpleDateFormat(yyyyMMddHHmmss);
                        Date date = sdf.parse(timeDate12_14);
                        return newSdf.format(date);
                    } else {
                        return timeDate12_14;
                    }
                }
            }
        } catch (ParseException e) {
            return e.toString();
        }
    }

    /**
     * 获得14位的日期编码yyyyMMddHHmmss
     */
    public static String getTimeDate14() {
        /* yyyyMMddhhmmss */
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date _date = new Date();

        return _sdf.format(_date);
    }

    public static String getTimeDate14(Date date) {
        /* yyyyMMddhhmmss */
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return _sdf.format(date);
    }

    /**
     * 获得18位的随即数
     */
    public static String getRandom18() {
        String _random = UUID.randomUUID().toString();
        return _random.substring(0, 18);
    }

    @SuppressWarnings("static-access")
    /**
     * 获得32位日期加随即尾数的主键
     */
    public static String getOid() {
        String _oid = new FarmTimeTool().getTimeDate14() + new FarmTimeTool().getRandom18();
        return _oid;
    }

    /**
     * 得到相差几天的时间
     *
     * @param date 时间
     * @param day  几天（+，-）
     * @return
     */
    private static Date getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 获得当前月
     *
     * @return
     */
    public static String getCurrentMonth() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        return String.valueOf(now.get(Calendar.MONTH) + 1);
    }

    /**
     * 获得当前季度
     *
     * @return
     */
    public static String getCurrentQuarter() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int month = now.get(Calendar.MONTH) + 1;
        switch (month) {
            case 1:
                return "1";
            case 2:
                return "1";
            case 3:
                return "1";
            case 4:
                return "2";
            case 5:
                return "2";
            case 6:
                return "2";
            case 7:
                return "3";
            case 8:
                return "3";
            case 9:
                return "3";
            case 10:
                return "4";
            case 11:
                return "4";
            case 12:
                return "4";
            default:
                break;
        }
        return "-1";
    }

    /**
     * 根据年月选择日
     *
     * @param rankingYear  年
     * @param rankingMonth 月
     * @return
     */
    public static List<String> getDayListByMonth(String rankingYear, String rankingMonth) {
        List<String> list = new ArrayList<String>();
        String[] dayStr = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        // 如果是1月、3月、5月、7月、8月、10月、12月，为31天
        if ("1".equals(rankingMonth) || "3".equals(rankingMonth) || "5".equals(rankingMonth) || "7".equals(rankingMonth)
                || "8".equals(rankingMonth) || "10".equals(rankingMonth) || "12".equals(rankingMonth)) {
            for (int i = 0; i < dayStr.length; i++) {
                list.add(dayStr[i]);
            }
            // 如果是4月、6月、9月、11月，为30天
        } else if ("4".equals(rankingMonth) || "6".equals(rankingMonth) || "9".equals(rankingMonth)
                || "11".equals(rankingMonth)) {
            for (int i = 0; i < dayStr.length - 1; i++) {
                list.add(dayStr[i]);
            }
        } else {
            int yearInt = Integer.parseInt(rankingYear);
            // 判断是平年还是闰年，平年2月28天，闰年2月29天
            if (yearInt % 4 == 0 && yearInt % 100 != 0 || yearInt % 400 == 0) {
                for (int i = 0; i < dayStr.length - 2; i++) {
                    list.add(dayStr[i]);
                }
            } else {
                for (int i = 0; i < dayStr.length - 3; i++) {
                    list.add(dayStr[i]);
                }
            }
        }
        return list;
    }

    /**
     * 计算两个Date间的相差分钟
     *
     * @param dateBefore  早一点的时间
     * @param dateCurrent 晚一点的时间
     * @return
     */
    public static int countMinuteMinus(Date dateBefore, Date dateCurrent) {
        // 当前时间
        Date curentTime = dateCurrent;
        // 当前时间
        Calendar curentC = Calendar.getInstance();
        curentC.setTime(curentTime);
        // 上次访问时间
        Calendar curentV = Calendar.getInstance();
        curentV.setTime(dateBefore);
        // 相差分钟数
        long timeMillis = (curentC.getTimeInMillis() - curentV.getTimeInMillis()) / (1000 * 60);
        int time = (int) timeMillis;
        return time;
    }

    public static int countSecondsBetween(Date dateBefore, Date dateCurrent) {
        // 获取当前时间的 Calendar 实例
        Calendar calCurrent = Calendar.getInstance();
        calCurrent.setTime(dateCurrent);

        // 获取之前时间的 Calendar 实例
        Calendar calBefore = Calendar.getInstance();
        calBefore.setTime(dateBefore);

        // 计算时间差（毫秒），然后转换为秒
        long timeDiffMillis = calCurrent.getTimeInMillis() - calBefore.getTimeInMillis();
        long timeDiffSeconds = timeDiffMillis / 1000; // 只需除以 1000

        // 转换为 int 返回（注意：如果差值超过约 24.8 天，int 可能溢出）
        return (int) timeDiffSeconds;
    }

    /**
     * 计算两个Date间的相差天数
     *
     * @param dateBefore  早一点的时间
     * @param dateCurrent 晚一点的时间
     * @return
     */
    public static int countDayMinus(Date dateBefore, Date dateCurrent) {
        // 当前时间
        Date curentTime = dateCurrent;
        // 当前时间
        Calendar curentC = Calendar.getInstance();
        curentC.setTime(curentTime);
        // 上次访问时间
        Calendar curentV = Calendar.getInstance();
        curentV.setTime(dateBefore);
        // 相差分钟数
        long timeMillis = (curentC.getTimeInMillis() - curentV.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        int time = (int) timeMillis;
        return time;
    }

    /**
     * 计算两个Date间的相差天数
     *
     * @return
     * @throws ParseException
     */
    public static int countDayMinus(String starttime14, String endtime14) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return countDayMinus(sdf.parse(starttime14), sdf.parse(endtime14));
    }

    /**
     * 计算两个时间之间的秒数，如果开始时间大于结束时间就返回负数。
     *
     * @param starttime14 开始时间，14位时间字符串（格式：yyyyMMddHHmmss）
     * @param endtime14   结束时间，14位时间字符串（格式：yyyyMMddHHmmss）
     * @return 两个时间之间的秒数差异，如果开始时间大于结束时间则返回负数
     * @throws ParseException 如果提供的日期字符串格式不正确
     */
    public static int countSecond(String starttime14, String endtime14) {
        // 定义时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        // 解析时间字符串为Date对象
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(starttime14);
            endDate = sdf.parse(endtime14);
        } catch (ParseException e) {
            throw new RuntimeException("时间格式解析错误：" + starttime14 + ":" + endtime14);
        }
        // 计算时间差（毫秒）
        long diffInMillis = endDate.getTime() - startDate.getTime();

        // 将时间差转换为秒数并返回
        return (int) (diffInMillis / 1000);
    }



    /**
     * 解析日期
     *
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateStr, String yyyyMMddHHmmss) throws ParseException {
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern(yyyyMMddHHmmss);
        Date ddTest = df.parse(dateStr);
        return ddTest;
    }

    /**
     * 获得几天后的日期
     *
     * @param date
     * @param i
     * @return
     */
    public static Date nextDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        Date date1 = new Date(calendar.getTimeInMillis());
        return date1;
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param yyyyMMddHHmmss
     * @return
     */
    public static String format(Date date, String yyyyMMddHHmmss) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        return sdf.format(date);
    }

    /**
     * 获得星期数（周日为0）
     *
     * @param date
     * @return
     */
    public static int getWeekNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    /**
     * 获得日
     *
     * @param date
     * @return
     */
    public static int getDayNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_MONTH);
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    public static String getTimeDate(Date timeDate12ForDay, String yyyyMMddHHmmss) {
        /* yyyyMMddhhmmss */
        SimpleDateFormat _sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        return _sdf.format(timeDate12ForDay);
    }

    public static String getCurrentYear() {
        /* yyyyMMddhhmmss */
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy");
        Date _date = new Date();
        return _sdf.format(_date);
    }

    public static String getCurrentDay() {
        // 创建一个Calendar实例，并设置为当前日期和时间
        Calendar calendar = Calendar.getInstance();
        // 获取日的部分，并确保它是两位数格式
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedDayOfMonth = String.format("%02d", dayOfMonth);
        return formattedDayOfMonth;
    }

    public static String getTimeDate14ForDay(int addDay, Date date) {
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date _date = getDateAfter(date, addDay);
        return _sdf.format(_date);
    }

    /**
     * 计算n个小时后的date
     *
     * @param startDate
     * @param hourNum
     * @return
     */
    public static Date getDateAddHour(Date startDate, int hourNum) {
        // 将 Date 转换为 LocalDateTime
        LocalDateTime startDateTime = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        // 增加 n 小时
        LocalDateTime endDateTime = startDateTime.plusHours(hourNum);
        // 将 LocalDateTime 转换回 Date
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return endDate;
    }

    /**
     * 比较当前时间与给定的时间范围。
     * <p>
     * 如果当前时间早于开始时间，返回-1；
     * 如果当前时间在开始时间和结束时间之间（包括等于开始时间和结束时间），返回0；
     * 如果当前时间晚于结束时间，返回1。
     *
     * @param startTime   开始时间，格式为"yyyyMMddHHmmss"
     * @param endTime     结束时间，格式为"yyyyMMddHHmmss"
     * @param currentTime 当前时间，格式为"yyyyMMddHHmmss"
     * @return int 返回值 -1: 当前时间早于开始时间；0: 当前时间在范围内；1: 当前时间晚于结束时间
     */
    public static int compareDateWithRange(String startTime, String endTime, String currentTime) {
        // 定义日期时间格式化器，用于解析字符串为LocalDateTime对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 将字符串转换为LocalDateTime对象
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);
        LocalDateTime current = LocalDateTime.parse(currentTime, formatter);

        // 比较当前时间与开始时间和结束时间
        if (current.isBefore(start)) {
            return -1;  // 当前时间早于开始时间
        } else if (current.isAfter(end)) {
            return 1;   // 当前时间晚于结束时间
        } else {
            return 0;   // 当前时间在开始和结束时间之间（包括等于开始时间和结束时间）
        }
    }
}
