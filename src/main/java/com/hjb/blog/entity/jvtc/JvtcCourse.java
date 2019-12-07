package com.hjb.blog.entity.jvtc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课表实体
 *
 * @author 胡江斌
 * @version 1.0
 * @title: JvtcCourse
 * @projectName blog
 * @description: TODO
 * @date 2019/11/24 17:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JvtcCourse {

    /**
     * ?
     */
    private String sjbz;
    /**
     * 课程名称
     */
    private String kcmc;
    /**
     * 教学周(2-5,7-17)
     */
    private String kkzc;
    /**
     * 课程设计(10102)第一个1表示星期,0102表示第一和第二节课
     */
    private String kcsj;
    /**
     * 开始时间
     */
    private String kssj;
    /**
     * 结束时间
     */
    private String jssj;
    /**
     * 教室地点
     */
    private String jsmc;
    /**
     * 老师名字
     */
    private String jsxm;

    public List<Integer[]> getKkzc() {
        List<Integer[]> res = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(this.kkzc)) {
                String[] kkzcList = this.kkzc.split(",");
                for (String kkzcStr : kkzcList) {
                    Integer[] kkzcArray = new Integer[2];
                    String[] kkzcWeek = kkzcStr.split("-");
                    kkzcArray[0] = Integer.parseInt(kkzcWeek[0]);
                    kkzcArray[1] = Integer.parseInt(kkzcWeek[1]);
                    res.add(kkzcArray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 课程设计(10102)第一个1表示星期,0102表示第一和第二节课
     */
    public Integer[] getKcsj() {
        Integer[] res = new Integer[3];
        try {
            if (!StringUtils.isEmpty(kcsj)) {
                res[0] = Integer.parseInt(kcsj.substring(0, 1));
                res[1] = Integer.parseInt(kcsj.substring(1, 3));
                res[2] = Integer.parseInt(kcsj.substring(3, 5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
