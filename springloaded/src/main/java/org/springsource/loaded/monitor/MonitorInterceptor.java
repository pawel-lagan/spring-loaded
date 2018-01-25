package org.springsource.loaded.monitor;

import java.util.LinkedList;
import java.util.logging.*;

public class MonitorInterceptor {

    private static Logger log = Logger.getLogger(MonitorApi.class.getName());

    public static  LinkedList<Long> timeStack = new LinkedList<Long>();

    public static void before(){
       timeStack.add(System.currentTimeMillis());
    }

    public static void after(){
        log.info("Method execution time: " + (System.currentTimeMillis() - timeStack.pollLast()));
    }
}
