package com.hjb.blog.entity.timetable;

/**
 * 每天的每个时间段课程信息
 * @author h1525
 *
 */
public class TimeTablePerTime {

    /**
     * 第几节课
     */
    private int no;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 上课时间
     */
    private String upTime;

    /**
     * 上课的教室
     */
    private String address;

    /**
     * 上课的老师
     */
    private String teacher;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }


    @Override
    public String toString() {
        return "TimeTablePerTime [no=" + no + ", courseName=" + courseName + ", upTime=" + upTime + ", address="
                + address + ", teacher=" + teacher + "]";
    }

}