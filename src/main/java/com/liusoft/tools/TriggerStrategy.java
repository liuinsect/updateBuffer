package com.liusoft.tools;

/**
 * 单线程方式处理
 * 触发器
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-17
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 */
public interface TriggerStrategy {

    public boolean trigger(String key);


    public void fixThreshold(Integer threshold);


}
