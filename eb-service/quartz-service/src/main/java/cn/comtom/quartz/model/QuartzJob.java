package cn.comtom.quartz.model;

import lombok.Data;

@Data
public class QuartzJob {

    private String groupName;


    private String jobDetailName;


    private String jobDescription;


    private String triggerState;


    private String jobClassName;


    private String jobCronExpression;


    private String previousFireTime;


    private String nextFireTime;
}
