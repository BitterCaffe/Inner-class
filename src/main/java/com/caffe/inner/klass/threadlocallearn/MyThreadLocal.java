package com.caffe.inner.klass.threadlocallearn;

import com.alibaba.fastjson.JSON;

import java.lang.ref.WeakReference;

/**
 * @author BitterCaffe
 * @date 2020/12/3
 * @description: TODO
 */
public class MyThreadLocal<T> {
    /**
     * 模拟线程
     */
    private MyThread myThread;

    /**
     * 模拟绑定线程
     *
     * @param myThread
     */
    public MyThreadLocal(MyThread myThread) {
        this.myThread = myThread;
    }

    /**
     * 对外暴露的设置值接口
     *
     * @param v
     */
    public void set(T v) {
        MyThreadLocalMap myThreadLocalMap = myThread.getMyThreadLocalMap();
        if (null == myThreadLocalMap) {
            myThreadLocalMap = new MyThreadLocalMap();
            //模拟当前线程绑定ThreadLocalMap
            myThread.setMyThreadLocalMap(myThreadLocalMap);
        }
        myThreadLocalMap.set(this, v);
    }

    /**
     * 对外暴露的获取值接口
     *
     * @return
     */
    public T get() {
        MyThreadLocalMap myThreadLocalMap = myThread.getMyThreadLocalMap();
        MyThreadLocalMap.MyEntry myEntry = myThreadLocalMap.get(this);
        return (T) myEntry.value;
    }


    /**
     * 具体功能实现静态内部类
     */
    public static class MyThreadLocalMap {
        private int capacity = 16;

        MyEntry[] tab;

        MyThreadLocalMap() {
            tab = new MyEntry[capacity];
        }

        /**
         * 弱引用对象作为key，value作为value组成Entry保存
         */
        static class MyEntry extends WeakReference<MyThreadLocal<?>> {
            Object value;

            public MyEntry(MyThreadLocal<?> referent) {
                super(referent);
            }

            public MyEntry(MyThreadLocal<?> reference, Object v) {
                this(reference);
                value = v;
            }
        }


        /**
         * 添加新的值
         * 1、如果没有值则直接添加
         * 2、如果有值，如果相等则覆盖
         * 2、如果有值但不相等则向前线性探测并判断是否相等如果相等则覆盖
         * 3、判断弱引用是否被回收，如果回收则释放内存
         * 4、线性探测直到null吧新节点添加返回
         *
         * @param myThreadLocal
         * @param t
         */
        private void set(MyThreadLocal myThreadLocal, Object t) {
            int index = myThreadLocal.hashCode() % (capacity);
            MyEntry myEntry = new MyEntry(myThreadLocal, t);
            MyEntry[] table = tab;
            int len = table.length;
            for (; table[index] != null; index = nextIndex(index, len)) {
                MyEntry e = table[index];
                if (e.get() == myThreadLocal) {
                    e.value = t;
                    return;
                }
                if (e.get() == null) {
                    expungeStaleEntry(index);
                }
            }
            tab[index] = myEntry;
        }


        /**
         * 添加的时候有线性探测所以获取的时候也要判断
         * 1、按照index获取MyEntry
         * 2、判断是否相等，如果相等则返回
         *
         * @param myThreadLocal
         * @return
         */
        private MyEntry get(MyThreadLocal myThreadLocal) {
            int index = myThreadLocal.hashCode() % (capacity);
            MyEntry myEntries = tab[index];
            if (null != myEntries && myThreadLocal == myEntries.get()) {
                return myEntries;
            }
            return getAfterMyEntry(index, myEntries, myThreadLocal);
        }

        /**
         * 线性探测获取值
         *
         * @param index
         * @param myEntry
         * @param myThreadLocal
         * @return
         */
        MyEntry getAfterMyEntry(int index, MyEntry myEntry, MyThreadLocal<?> myThreadLocal) {
            MyEntry[] table = tab;
            int len = tab.length;

            while (myEntry != null) {
                MyThreadLocal k = myEntry.get();
                if (k == myThreadLocal) {
                    return myEntry;
                }
                //弱引用被gc收集
                if (null == k) {
                    //遍历释放被GC收集的key对应的value资源
                    expungeStaleEntry(index);
                }
                index = nextIndex(index, len);
                myEntry = table[index];
            }
            return null;
        }

        /**
         * GC之后被weakReference 引用对象被收集，则释放内存空间
         *
         * @param staleSlot
         */
        private void expungeStaleEntry(int staleSlot) {
            MyEntry[] table = tab;
            int len = table.length;
            //如果key被回收则释放内存，然后遍历知道MyEntry  is null
            System.out.println("资源释放slot=" + staleSlot + " value=" + JSON.toJSONString(table[staleSlot].value));
            MyEntry myEntry = table[staleSlot];
            if (null != myEntry && myEntry.get() != null) {
                System.out.println("未失效结束");
                return;
            }
            table[staleSlot].value = null;
            table[staleSlot] = null;
            MyEntry weakRef;
            //遍历线性探测模拟
            for (int i = nextIndex(staleSlot, len); (weakRef = table[i]) != null; i = nextIndex(i, len)) {
                MyThreadLocal<?> myThreadLocal = weakRef.get();
                if (myThreadLocal == null) {
                    System.out.println("资源释放线性探测释放value:" + JSON.toJSONString(table[i].value));
                    table[i].value = null;
                    table[i] = null;
                }
            }
        }

        /**
         * 环形队列（向后移动，如果到最大则移动到对头）
         *
         * @param index
         * @param len
         * @return
         */
        private int nextIndex(int index, int len) {
            return (index + 1) > len ? 0 : index + 1;
        }

        /**
         * 环形队列（向前移动，如果到最小值则移动到队尾）
         *
         * @param index
         * @param len
         * @return
         */
        private int prevIndex(int index, int len) {
            return (index - 1 >= 0) ? index - 1 : len - 1;
        }

        public MyEntry[] getTab() {

            return tab;
        }

        /**
         * 为了验证弱引用被回收所以提供一个public方法，按照封装性要求是不应该提供，因为直接把核心暴露了
         *
         * @param index
         * @return
         */
        public MyEntry[] getTab(int index) {
            //获取的时候先进行一次被GC收集数据清理操作
            expungeStaleEntry(index);
            return tab;
        }
    }


}
