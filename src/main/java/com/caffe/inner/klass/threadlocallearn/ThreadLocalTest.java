package com.caffe.inner.klass.threadlocallearn;

import com.alibaba.fastjson.JSON;

/**
 * @author BitterCaffe
 * @date 2020/12/5
 * @description: TODO
 */
public class ThreadLocalTest {

    public static void main(String[] args) {
        weakReferenceTest();
    }

    public static void weakReferenceTest() {
        MyParent myParent = MyParent.build("hello reference 1");
        MyParent myParent2 = MyParent.build("hello reference 2");
        MyParent myParent3 = MyParent.build("hello reference 3");
        MyParent myParent4 = MyParent.build("hello reference 4");
        MyParent myParent5 = MyParent.build("hello reference 5");
        //模拟线程
        MyThread myThread = new MyThread();

        //自定义ThreadLocal工具类，模拟获取thread 对象
        MyThreadLocal<MyParent> myThreadLocal = new MyThreadLocal<>(myThread);
        MyThreadLocal<MyParent> myThreadLocal2 = new MyThreadLocal<>(myThread);
        MyThreadLocal<MyParent> myThreadLocal3 = new MyThreadLocal<>(myThread);
        MyThreadLocal<MyParent> myThreadLocal4 = new MyThreadLocal<>(myThread);
        MyThreadLocal<MyParent> myThreadLocal5 = new MyThreadLocal<>(myThread);
        MyThreadLocal<MyParent> myThreadLocal6 = new MyThreadLocal<>(myThread);
        MyThreadLocal<MyParent> myThreadLocal7 = new MyThreadLocal<>(myThread);
        myThreadLocal.set(myParent);
        myThreadLocal2.set(myParent2);
        myThreadLocal3.set(myParent3);
        myThreadLocal4.set(myParent4);
        myThreadLocal5.set(myParent5);
        myThreadLocal6.set(myParent5);
        myThreadLocal7.set(myParent5);

        //索引信息，方便查看
        int index = myThreadLocal.hashCode() % 16;
        int index2 = myThreadLocal2.hashCode() % 16;
        int index3 = myThreadLocal3.hashCode() % 16;
        int index4 = myThreadLocal4.hashCode() % 16;
        int index5 = myThreadLocal5.hashCode() % 16;
        int index6 = myThreadLocal6.hashCode() % 16;
        int index7 = myThreadLocal7.hashCode() % 16;

        System.out.println("index=" + index + " index2=" + index2 + " index3=" + index3 + " index4=" + index4 + " " +
                "index5=" + index5 + " index6=" + index6 + " index7=" + index7);

        //获取当前线程绑定ThreadLocal.ThreadLocalMap 对象
        MyThreadLocal.MyThreadLocalMap myThreadLocalMap = myThread.getMyThreadLocalMap();

        //为了验证ThreadLocal强引用对Entry中的ThreadLocal弱引用的影响这里提供一个方法获取Entry数组
        MyThreadLocal.MyThreadLocalMap.MyEntry[] res = myThreadLocalMap.getTab(index7);

        System.out.println("GC前******************");
        for (MyThreadLocal.MyThreadLocalMap.MyEntry re : res) {
            if (re != null) {
                System.out.println("Entry对象中key即ThreadLocal的WeakReference=" + re.get());
                System.out.println("Entry对象中value即设置的值" + JSON.toJSONString(re.value));
            }
        }
        //断开强引用
        myParent = null;
        myThreadLocal = null;
        myThreadLocal2 = null;
        myThreadLocal3 = null;
        myThreadLocal4 = null;
        myThreadLocal5 = null;
        myThreadLocal6 = null;
        myThreadLocal7 = null;

        System.gc();
        System.out.println("GC后******************");
        MyThreadLocal.MyThreadLocalMap.MyEntry[] res1 = myThreadLocalMap.getTab(index7);
        for (MyThreadLocal.MyThreadLocalMap.MyEntry re : res) {
            if (re != null) {
                System.out.println("Entry对象中key即ThreadLocal的WeakReference=" + re.get());
                System.out.println("Entry对象中value即设置的值" + JSON.toJSONString(re.value));
            }
        }
    }
}
