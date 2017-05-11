package com.tqmall.monkey.domain.entity.sys;

import lombok.Data;

import java.util.Date;

@Data
public class SysRecordDO {
    private Integer id;

    private Integer userId;

    private String resourceName;

    private String content;

    private Integer status;

    private Date gmtModified;

    private Date gmtCreate;

    //额外的  序号
    private Integer indexs;
    //日期文本
    private String modifiedString;
    //状态文本
    private String statusString;


    public  enum Status {
        OK("操作完成", 0), NOTOK("后台正在处理", 1),FAIL("失败",2);
        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private Status(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (Status c : Status.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}