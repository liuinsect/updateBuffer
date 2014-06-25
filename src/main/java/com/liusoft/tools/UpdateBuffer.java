package com.liusoft.tools;

import org.apache.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * 异步更新工具类
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-6-17
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class UpdateBuffer {

    private static Logger logger = Logger.getLogger(String.valueOf(UpdateBuffer.class));

    private ConcurrentHashMap<Integer,HandlThread> handles = new ConcurrentHashMap<Integer,HandlThread>();

    private final int workerSize = 20;

    private Handler handler;

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(workerSize, workerSize,
                                                                       60L, TimeUnit.SECONDS,
                                                                       new LinkedBlockingQueue<Runnable>(),
                                                                       new BufferThreadFactory(),
                                                                       new ThreadPoolExecutor.CallerRunsPolicy());

    private ReentrantLock handleMapLock = new ReentrantLock();

    public UpdateBuffer(Handler handler) {
        if( handler == null ){
            throw new IllegalArgumentException("handler is null");
        }
        this.handler = handler;
    }

    public void update(final String key , final Object value ){
        int code = key.hashCode();
        //取绝对值后，再取摸
        int pos = Math.abs(code)%workerSize;
        HandlThread handlThread = handles.get(pos);
        if( handlThread == null ){
            handlThread = new HandlThread(handler,pos,this);
            try{
                handleMapLock.lock();
                HandlThread tmpThread = handles.get(pos);
                if( tmpThread == null ){
                    handles.put(pos, handlThread);//保存映射关系
                    threadPool.execute(handlThread);//启动该线程

                }else{
                    //如果其他KEY已经 创建handlerThread 则entry应该添加在已经创建的queue中
                    handlThread = tmpThread;

                }
            }finally {
                handleMapLock.unlock();
            }

        }
        handlThread.addToQueue(new Entry(key, value));

    }

    /**
     * @param pos  偏移量
     * @param handlThread 处理线程
     */
    public void removeHandler(int pos,HandlThread handlThread){
        try{
            handleMapLock.lock();
            HandlThread  ht = handles.get(pos);
            if( ht!= null && ht.equals(handlThread) ){
                handles.remove(pos);
            }
        }finally {
            handleMapLock.unlock();
        }
    }

    public Object getFromBuffer(String key){
        return handler.getFromBuffer(key);
    }

    public static class Entry{

        private String key;
        private Object value;

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }



    class BufferThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNum = new AtomicInteger(0);

        public BufferThreadFactory() {
        }

        @Override
        public synchronized Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setName("BufferThreadFactory -" + threadNum.getAndIncrement());
            t.setUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler(){
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    logger.error(t.getName()+" has Exception:",e);

                }
            });
            return t;
        }
    }
}

