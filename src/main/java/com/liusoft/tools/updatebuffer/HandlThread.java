package com.liusoft.tools.updatebuffer;

import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-18
 * Time: 下午5:21
 * To change this template use File | Settings | File Templates.
 */
public class HandlThread implements Runnable {

    private Logger logger = Logger.getLogger(HandlThread.class);

    private LinkedBlockingQueue<UpdateBuffer.Entry> queue = new LinkedBlockingQueue<UpdateBuffer.Entry>();

    private volatile  boolean cancel = false;

    private Handler handler;

    private AtomicInteger pos;

    private UpdateBuffer updateBuffer;

    public HandlThread(Handler handler,int position ,UpdateBuffer updateBuffer) {
        this.handler = handler;
        this.pos = new AtomicInteger(position);
        this.updateBuffer = updateBuffer;
    }

    @Override
    public void run() {
        try{
            for(;;){
                if( cancel ){
                    break;
                }
                //打断的异常单独响应，响应后当前线程退出
                UpdateBuffer.Entry entry = queue.take();
                //如果内部执行异常抛出，当前线程退出，丢失已在队列里的更新请求
                //下次再重建线程
                handler.fixThreshold(queue.size());
                handler.update(entry);

            }
        }catch (InterruptedException ine){
            logger.error(Thread.currentThread().getName()+",pos:"+pos+",has InterruptedException:",ine);
        }catch (Exception e){
            logger.error(Thread.currentThread().getName()+",pos:"+pos+",has exception:",e);
        }finally {
            cleanSelf();
        }
    }

    /**
     * 将当前线程和updateBuffer里的映射关系解除
     */
    private void cleanSelf(){
        updateBuffer.removeHandler(pos.get(),this);
    }

    public void addToQueue(UpdateBuffer.Entry entry){
        queue.add(entry);
    }
    //TODO 入口
    public void cancel(){
        cancel = true;
    }

}
