package com.liusoft.tools;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import com.liusoft.tools.UpdateBuffer.Entry;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-18
 * Time: 下午5:21
 * To change this template use File | Settings | File Templates.
 */
public class HandlThread implements Runnable {

    private Logger logger = Logger.getLogger(HandlThread.class);

    private LinkedBlockingQueue<Entry> queue = new LinkedBlockingQueue<Entry>();

    private volatile  boolean cancel = false;

    private Handler handler;

    private AtomicInteger pos;

    private UpdateBuffer updateBuffer;

    public HandlThread(Handler handler,int position , UpdateBuffer updateBuffer) {
        this.handler = handler;
        this.pos = new AtomicInteger(position);
        this.updateBuffer = updateBuffer;
    }

    @Override
    public void run() {
        try{
            for(;;){
                if( cancel ){
                    updateBuffer.removeHandler(pos.get(),this);
                    break;
                }
                try {
                    Entry entry = queue.take();
                    handler.fixThreshold(queue.size());
                    handler.update(entry);
                } catch (InterruptedException e) {
                    break;
                }

            }
        }catch (Exception e){
            logger.error(Thread.currentThread().getName()+",pos:"+pos+",has exception:",e);
            cancel = true;
            updateBuffer.removeHandler(pos.get(),this);
        }
    }

    public void addToQueue(Entry entry){
        queue.add(entry);
    }
    //TODO 入口
    public void cancel(){
        cancel = true;
    }

}
