package com.timer.task;

import com.timer.quartz.SimpleJob;
import com.timer.support.QuartzConfig;
import com.timer.support.QuartzManager;
import org.apache.log4j.Logger;

import java.text.ParseException;

/**
 * @author pengx
 * @date 2016/11/18
 */
public class TestTask {


    private static Logger log = Logger.getLogger(TestTask.class);

    public static void main(String[] args) {
        SimpleTaskByQuartz();
    }



    public static void SimpleTaskByQuartz() {
        QuartzManager.addJob(QuartzConfig.SIMPLE_JOB_NAME, SimpleJob.class, QuartzConfig.SIMPLE_JOB_QUARTZ_CFG);

        log.info("Timer : TestTask @ "+QuartzConfig.SIMPLE_JOB_QUARTZ_CFG);
    }

}


