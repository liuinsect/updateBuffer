package com.liusoft.tools.updatebuffer.impl;

import com.liusoft.tools.updatebuffer.PersistentStrategy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * c持久化策略接口的demo
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-17
 * Time: 下午4:33
 * To change this template use File | Settings | File Templates.
 */
public class DemoStrategy implements PersistentStrategy {


    @Override
    public void persisten(String key, Object value) {
        if( !(value instanceof AtomicInteger)){
            throw new IllegalArgumentException("updateValue must beNumber");
        }

        AtomicInteger integer = (AtomicInteger)value;
       //把value持久化，完成更新操作
        System.out.println("持久化："+integer);
    }


}
