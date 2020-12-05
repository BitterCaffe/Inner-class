package com.caffe.inner.klass.threadlocallearn;

/**
 * @author BitterCaffe
 * @date 2020/12/4
 * @description: TODO
 */
public class MyThread {

    private MyThreadLocal.MyThreadLocalMap myThreadLocalMap;

    public MyThread() {
    }


    public MyThreadLocal.MyThreadLocalMap getMyThreadLocalMap() {
        return myThreadLocalMap;
    }

    public void setMyThreadLocalMap(MyThreadLocal.MyThreadLocalMap myThreadLocalMap) {
        this.myThreadLocalMap = myThreadLocalMap;
    }
}
