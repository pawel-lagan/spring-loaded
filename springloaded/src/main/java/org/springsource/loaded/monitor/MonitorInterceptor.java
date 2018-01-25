package org.springsource.loaded.monitor;

import java.util.LinkedList;

public class MonitorInterceptor {

    public static  LinkedList<Long> timeStack = new LinkedList<Long>();

    public static void before(){
       timeStack.add(System.currentTimeMillis());
    }

    public static void after(){
        System.out.println(System.currentTimeMillis() - timeStack.pollLast());
    }
}
