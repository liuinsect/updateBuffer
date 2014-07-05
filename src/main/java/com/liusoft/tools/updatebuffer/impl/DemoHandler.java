package com.liusoft.tools.updatebuffer.impl;

import com.liusoft.tools.updatebuffer.Handler;
import com.liusoft.tools.updatebuffer.PersistentStrategy;
import com.liusoft.tools.updatebuffer.TriggerStrategy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 示例代码
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-25
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public class DemoHandler  extends Handler{


    public DemoHandler(TriggerStrategy triggerStrategy, PersistentStrategy persistentStrategy) {
        super(triggerStrategy, persistentStrategy);
    }

    @Override
    protected void doUpdate(String key, Object value) {
        AtomicInteger num = (AtomicInteger) buffer.get(key);
        if( num == null ){
            num = new AtomicInteger(0);
            initBufferline(key,num);
        }
        num.incrementAndGet();

        //do someting

    }
}
