package com.caffe.inner.klass.threadlocallearn;

import java.io.Serializable;

/**
 * @author BitterCaffe
 * @date 2020/12/5
 * @description: TODO
 */
public class MyParent implements Serializable {

    private static final long serialVersionUID = -6860365972172764462L;

    public String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private MyParent name(String name) {
        this.name = name;
        return this;
    }

    public static MyParent build(String name) {
        return new MyParent().name(name);
    }
}
