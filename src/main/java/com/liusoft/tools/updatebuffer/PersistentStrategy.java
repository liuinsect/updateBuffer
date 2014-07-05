package com.liusoft.tools.updatebuffer;

/**
 * 单线程方式处理
 * 持久化策略
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-17
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
public interface PersistentStrategy {

    /**
     * 持久化的方式
     * @param key
     * @param value
     */
    public void persisten(String key, Object value);

}
