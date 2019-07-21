package com.hjb.blog.entity.timetable;

import java.util.List;

/**
 * 课程表对象(日)
 * @author h1525
 *
 */
public class TimeTable {

    /**
     * 日期
     */
    private String date;
    
    /**
     * 第几周
     */
    private int weeks;
    
    /**
     * 星期几
     */
    private int week;
    
    /**
     * 班级(云计算|1801)
     */
    private String clazz;
    
    /**
     * 当天天气
     */
    private String weather;
    
    /**
     * 完整html页面
     */
    private String html;
    
    /**
     * 每天的每个时间段课程信息
     */
    private List<TimeTablePerTime> data;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public List<TimeTablePerTime> getData() {
        return data;
    }

    public void setData(List<TimeTablePerTime> data) {
        this.data = data;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "TimeTable [date=" + date + ", weeks=" + weeks + ", week=" + week + ", clazz=" + clazz + ", weather="
                + weather + ", html=" + html + ", data=" + data + "]";
    }
    
    
}

