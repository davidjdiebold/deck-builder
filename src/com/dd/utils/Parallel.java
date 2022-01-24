package com.dd.utils;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * User: ddiebold
 * Date: 19/06/13
 * Time: 11:38
 */
public class Parallel
{

    public static ExecutorService prepareExecutor()
    {
        return prepareExecutor(1.);
    }

    public static ExecutorService prepareExecutor(double processorRatio)
    {
        int cpus = (int) Math.max(1, Runtime.getRuntime().availableProcessors() * processorRatio);
        return Executors.newFixedThreadPool(cpus);
    }

    public static void join(ExecutorService service)
    {
        service.shutdown();
        try
        {
            service.awaitTermination(1000, TimeUnit.DAYS);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e.toString());
        }
    }

}

