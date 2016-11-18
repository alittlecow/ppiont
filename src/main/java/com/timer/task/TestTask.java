package com.timer.task;

import com.timer.quartz.SimpleJob;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;

/**
 * @author pengx
 * @date 2016/11/18
 */
public class TestTask {

    private static final String QUARTZ_CFG = "0/2 * * * * ?";

    public static void main(String[] args) throws ParseException {
        SchedulerFactory factory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = factory.getScheduler();

            JobDetail jobDetail= JobBuilder.newJob(SimpleJob.class)
                    .withIdentity("testJob_1","group_1")
                    .build();

            Trigger trigger= TriggerBuilder
                    .newTrigger()
                    .withIdentity("trigger_1","group_1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(2) //时间间隔
                            .withRepeatCount(5)        //重复次数(将执行6次)
                    )
                    .build();

            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();



        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }



}


