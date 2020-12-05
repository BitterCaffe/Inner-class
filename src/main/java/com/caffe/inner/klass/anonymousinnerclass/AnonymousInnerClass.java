package com.caffe.inner.klass.anonymousinnerclass;

/**
 * @author BitterCaffe
 * @date 2020/8/19
 * @description: 这里为何要写内部类，因为在看各种源码的时候都会使用内部类，而且是各种内部类，
 * 如果对内部类的用法不熟悉或为何使用这种内部类不熟悉的话那就很难理解为何这么写了。
 * 所以在这里来聊聊各种内部类的用法！
 * <p>
 * <p>
 * <p>
 * <p>
 * 匿名内部类：
 * （1）、匿名内部类和普通类，普通内部类之间的关系，相比匿名内部类又那些优势，在那些场景下使用匿名内部类
 * （2）、用匿名内部类实现回调功能，下面这种方式就是回调功能的实现
 */

public class AnonymousInnerClass {
    private Integer age = 10;
    private String name = "name";


    public void add(int a, int b) {
        this.doAdd(new IAnonymousInnerClass() {

            @Override
            public void add(int a, int b) {
                System.out.println(String.format("age:%s&name:%s", age, name));
                int c = a + b;
                // 可以灵活使用私有内部类
                c = c >= age ? c : age;
                //匿名内部类中做一些事情
                System.out.println("比较大的值" + c);
            }
        }, a, b);
    }

    /**
     * 方法参数为接口实现类，和两个int型变量；
     * 方法内容是调用接口实现类方法并返回接口实现类方法返回值；
     * <p>
     * 看方法调用处即上面add方法，这里调用直接使用匿名实现类来给doAdd方法传参
     * 想一下如果这里不使用匿名内部类而是直接创建一个接口实现类从add方法传参传过来会怎么样？
     * <p>
     * 其实这也就是匿名内部类和普通类的区别
     *
     * @param iAnonymousInnerClass
     * @param a
     * @param b
     * @return
     */
    public void doAdd(IAnonymousInnerClass iAnonymousInnerClass, int a, int b) {
        iAnonymousInnerClass.add(a, b);
    }
}
