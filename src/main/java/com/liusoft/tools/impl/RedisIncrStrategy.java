package com.liusoft.tools.impl;

import com.liusoft.tools.PersistentStrategy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-17
 * Time: 下午4:33
 * To change this template use File | Settings | File Templates.
 */
public class RedisIncrStrategy  implements PersistentStrategy {



    @Override
    public void persisten(String key, Object value) {
        if( !(value instanceof AtomicInteger)){
            throw new IllegalArgumentException("updateValue must beNumber");
        }

        AtomicInteger integer = (AtomicInteger)value;
       //把value持久化，完成更新操作
    }


}
