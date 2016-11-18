package com.timer.support;

/**
 * 文件说明: 定时任务管理类
 */

import com.timer.quartz.SimpleJob;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

/**
 * 定时任务管理类
 */
public class QuartzManager {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "LITTLE_JOB_GROUP";
    private static String TRIGGER_GROUP_NAME = "LITTLE_TRIGGER_GROUP";

    private static final Logger log = Logger.getLogger(QuartzManager.class);

    /**
     * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobName  任务名
     * @param jobClass 任务
     * @param cronSchedule     时间设置，参考quartz说明文档
     * @throws SchedulerException
     * @throws ParseException
     */
    public static void addJob(String jobName, Class<? extends Job> jobClass, String cronSchedule) {
        if (StringUtils.isBlank(cronSchedule)) {
            log.warn("Job:" + jobName + ",cronSchedule is blank!");
            return;
        }

        try {

            Scheduler scheduler = gSchedulerFactory.getScheduler();
            JobDetail jobDetail= JobBuilder.newJob(SimpleJob.class)
                    .withIdentity(jobName,JOB_GROUP_NAME)
                    .build();

            Trigger trigger= TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobName,TRIGGER_GROUP_NAME)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
