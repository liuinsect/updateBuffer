package com.liusoft.tools.updatebuffer.impl;

import com.liusoft.tools.updatebuffer.TriggerStrategy;
import com.liusoft.tools.updatebuffer.support.CronSequenceGenerator;

import java.util.Date;
import java.util.TimeZone;

/**
 * 定时更新器
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-7-4
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleTrigger implements TriggerStrategy {

    private CronSequenceGenerator sequenceGenerator;
    private long nextTime = 0;
    private Object lock = new Object();

    public ScheduleTrigger(String  expression) {
        this.sequenceGenerator = new CronSequenceGenerator(expression, TimeZone.getDefault());
    }

    @Override
    public boolean trigger(String key) {

        boolean result =  nextTime <= System.currentTimeMillis();
        if(result){
            synchronized (lock){
                if( nextTime <= System.currentTimeMillis() ){
                    Date nextDate = sequenceGenerator.next(new Date());
                    nextTime = nextDate.getTime();
//                    System.out.println(nextTime);
                }
            }

        }

        return result;
    }


    @Override
    public void fixThreshold(Integer threshold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
