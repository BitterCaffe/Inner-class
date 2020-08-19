package com.caffe.inner.klass;

/**
 * @author BitterCaffe
 * @date 2020/8/19
 * @description: 这里为何要写内部类，因为在看各种源码的时候都会使用内部类，而且是各种内部类，
 * 如果对内部类的用法不熟悉或为何使用这种内部类不熟悉的话那就很难理解为何这么写了。所以在这里来聊聊各种内部类的用法！
 */

public class AnonymousInnerClass {
    private Integer age = 10;
    private String name = "name";


    public int add() {
        return this.doAdd(new IAnonymousInnerClass() {

            @Override
            public Integer add(int a, int b) {
                System.out.println(String.format("age:%s&name:%s", age, name));
                int c = a + b;
                // 可以灵活使用私有内部类
                c = c >= age ? c : age;
                return c;
            }
        }, 1, 3);
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
    public Integer doAdd(IAnonymousInnerClass iAnonymousInnerClass, int a, int b) {
        return iAnonymousInnerClass.add(a, b);
    }
}
