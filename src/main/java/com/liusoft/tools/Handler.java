package com.liusoft.tools;


import java.util.concurrent.ConcurrentHashMap;
import com.liusoft.tools.UpdateBuffer.Entry;

/**
 * 单线程方式处理
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-18
 * Time: 下午5:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class Handler {


    private PersistentStrategy persistentStrategy;

    private TriggerStrategy triggerStrategy;

    protected ConcurrentHashMap<String,Object> buffer = new ConcurrentHashMap<String,Object>();

    public Handler(TriggerStrategy triggerStrategy, PersistentStrategy persistentStrategy) {
        if( persistentStrategy == null || triggerStrategy == null){
            throw new IllegalArgumentException("persistentStrategy  or triggerStrategy is null");
        }
        this.persistentStrategy = persistentStrategy;
        this.triggerStrategy = triggerStrategy;
    }

    public void update(Entry entry){
        doUpdate(entry.getKey(), entry.getValue());
        if( triggerStrategy.trigger( entry.getKey() ) ){
            //做一些清空buffer的策略。
            Object oldValue = buffer.get(entry.getKey());
            buffer.remove(entry.getKey());
            persistentStrategy.persisten(entry.getKey(),oldValue);
        }
   }

    public void fixThreshold(Integer threshold){
        this.triggerStrategy.fixThreshold(threshold);
    }

    protected boolean initBufferline(String key,Object object){
        synchronized (buffer){//可以不需要同步，因为，单线程再操作
            if( buffer.get(key) == null ){
                buffer.put(key,object);
                return true;
            }
            return false;
        }
    }

    Object getFromBuffer(String key){
        return buffer.get(key);
    }

    protected abstract Object doUpdate(String key,Object value);


}
