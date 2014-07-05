package com.liusoft.tools;

import com.liusoft.tools.updatebuffer.Handler;
import com.liusoft.tools.updatebuffer.PersistentStrategy;
import com.liusoft.tools.updatebuffer.TriggerStrategy;
import com.liusoft.tools.updatebuffer.UpdateBuffer;
import com.liusoft.tools.updatebuffer.impl.DemoHandler;
import com.liusoft.tools.updatebuffer.impl.DemoStrategy;
import com.liusoft.tools.updatebuffer.impl.ScheduleTrigger;
import com.liusoft.tools.updatebuffer.impl.UpdateNumTrigger;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-7-4
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */
public class MainTest {


    private int workerSize=20;

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(workerSize, workerSize,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 更新次数触发器 单元测试
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void updateNumTrigger_test() throws InterruptedException, IOException {
        TriggerStrategy triggerStrategy = new UpdateNumTrigger();
        PersistentStrategy persistentStrategy = new DemoStrategy();

        Handler handler = new DemoHandler(triggerStrategy,persistentStrategy);
        UpdateBuffer updateBuffer = new UpdateBuffer(handler);


        for (int i = 0; i < 50; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 300; j++) {
                        updateBuffer.update("key"+j,j);
                    }
                }
            });
        }

        System.in.read();

    }

    /**
     * 定时触发器 单元测试
     * @throws IOException
     */
    @Test
    public void ScheduleTrigger_test() throws IOException {
        TriggerStrategy triggerStrategy = new ScheduleTrigger("*/5 1 * * * ?");
        PersistentStrategy persistentStrategy = new DemoStrategy();

        Handler handler = new DemoHandler(triggerStrategy,persistentStrategy);
        UpdateBuffer updateBuffer = new UpdateBuffer(handler);


        while(true){
            updateBuffer.update("key1",1);
        }


    }


    /**
     * 定时触发器
     * 多线程
     * 单元测试
     * @throws IOException
     */
    @Test
    public void ScheduleTrigger_mulThread_test() throws IOException {
        TriggerStrategy triggerStrategy = new ScheduleTrigger("*/5 * * * * ?");
        PersistentStrategy persistentStrategy = new DemoStrategy();

        Handler handler = new DemoHandler(triggerStrategy,persistentStrategy);
        UpdateBuffer updateBuffer = new UpdateBuffer(handler);


        while(true){
            for (int i = 0; i < 100; i++) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                        updateBuffer.update("key1",1);

                    }
                });
            }
        }


    }
}
