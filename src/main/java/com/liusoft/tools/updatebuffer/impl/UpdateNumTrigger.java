package com.liusoft.tools.updatebuffer.impl;

import com.liusoft.tools.updatebuffer.TriggerStrategy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 更新次数触发器
 * 单线程条件下，不需要考虑并发问题
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-17
 * Time: 下午4:34
 * To change this template use File | Settings | File Templates.
 */
public class UpdateNumTrigger implements TriggerStrategy {

    //可以不使用atomic包装 只有单线程在操作
    protected ConcurrentHashMap<String,AtomicInteger> counter = new ConcurrentHashMap<String,AtomicInteger>();

    private final int minUpdateNum = 10;
    //可以不使用atomic包装 只有单线程在操作
    private AtomicInteger THRESHOLD = new AtomicInteger( minUpdateNum );

    @Override
    public boolean trigger(String key) {
        boolean trigger =false;
        AtomicInteger count = counter.get(key);
        if( count == null ){
            count = new AtomicInteger(0);
            counter.put(key,count);
        }

        if( count.incrementAndGet()%THRESHOLD.get() == 0){
            trigger = true;
        }


        if( count.get() >= THRESHOLD.get() ){
            count.set(0);//重新赋值，避免溢出
        }

        return trigger;
    }

    @Override
    public void fixThreshold(Integer queueSize) {
        //小于配置阀值时，阀值不改变
        //不能完全根据队列大小设值
        if( queueSize > minUpdateNum ){
            int thresh = queueSize * queueSize;
            THRESHOLD.set( thresh );
        }else{
            THRESHOLD.set( minUpdateNum );
        }
    }


}
