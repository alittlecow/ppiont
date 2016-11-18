package com.timer.quartz;

import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author pengx
 * @date 2016/11/18
 */
//job
public class SimpleJob implements Job {
    private static int num = 0;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("[JOB] welcome to Quartz.  "+
                DateFormatUtils.format(new Date(),"HH:mm:ss") +"  num: "+num++);
    }
}