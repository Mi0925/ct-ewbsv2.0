package cn.comtom.quartz.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;

import cn.comtom.quartz.exception.RRException;

/**
 * 定时任务工具类
 * @author wj
 */
public class ScheduleUtils {

    /**
     * 创建任务
     */
    public static void createJob(Scheduler scheduler, String cronExpression,
                                                 String jobName,String jobGroup,String jobDescription,Class clazz) {
        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName,jobGroup).withDescription(jobDescription).build();
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                    .withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RRException("创建定时任务失败", e);
        }
    }

    /**
     * 更新任务
     */
    public static void updateJob(Scheduler scheduler, String cronExpression,String jobName,String jobGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new RRException("更新定时任务失败");
        }
    }

    /**
     * 启动任务（一次，用于测试）
     */
    private static void startJob(Scheduler scheduler,String jobName,String jobGroup) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.triggerJob(jobKey);
        } catch (Exception e) {
            throw new RRException("启动定时任务失败");
        }
    }

    /**
     * 任务暂停
     */
    public static void pauseJob(Scheduler scheduler,String jobName,String jobGroup) {
        try{
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
        }catch (Exception e){
            throw new RRException("暂停定时任务失败");
        }

    }


    /**
     * 任务删除
     */
    public static void delJob(Scheduler scheduler,String jobName,String jobGroup) {
        try{
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.deleteJob(jobKey);
        }catch (Exception e){
            throw new RRException("删除定时任务失败");
        }

    }

    /**
     * 任务恢复
     */
    public static void resumeJob(Scheduler scheduler,String jobName,String jobGroup){
        try{
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
        }catch (Exception e){
            throw new RRException("恢复定时任务失败");
        }

    }

    /**
     * 全部定时任务
     */
    public static List<Map<String,Object>>  getAllJobs(Scheduler scheduler) {
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        try {
            //获取Scheduler下的所有group
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {
                //组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                //获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    //通过triggerKey在scheduler中获取trigger对象
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    //获取trigger拥有的Job
                    JobKey jobKey = trigger.getJobKey();
                    JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);

                    Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

                    //组装页面需要显示的数据
                   Map<String,Object> map=new HashMap<String,Object>();
                    map.put("groupName",groupName);
                    map.put("jobDetailName",jobDetail.getName());
                    map.put("jobDescription",jobDetail.getDescription());
                    map.put("triggerState",triggerState.name());
                    map.put("jobClassName",jobDetail.getJobClass().getName());
                    map.put("jobCronExpression",trigger.getCronExpression());
                    map.put("previousFireTime",trigger.getPreviousFireTime());
                    map.put("nextFireTime",trigger.getNextFireTime());
                    mapList.add(map);
                }
            }
        } catch (Exception e) {
        }
        return mapList;
    }

}
