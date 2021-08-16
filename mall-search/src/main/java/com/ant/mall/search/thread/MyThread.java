package com.ant.mall.search.thread;

import java.util.concurrent.*;

public class MyThread {
    public static ExecutorService executor = Executors.newFixedThreadPool(10);


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...启动...");
        //在指定线程中执行异步任务
/*        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("当前线程"+Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果："+i);
        }, executor);*/
        /**
         * 方法执行完成后的感知
         */
/*        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
            //等待任务执行完成后继续，类似监听器
        }, executor).whenComplete((result,exception)->{

        }).exceptionally(throwable -> {
            //有异常的默认返回(修改返回结果)
            return 10;
        });*/
//        Integer integer = future.get();
//        System.out.println("结果"+integer);

        /**
         * 方法执行完成后的处理
         */
/*        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
            //等待任务执行完成后继续处理
        }, executor).handle((result,exception)->{
            if(result != null){
                return result*2;
            }
            return 0;
        });*/
        /**
         * 等待A执行结束直接执行B
         */
/*        CompletableFuture.supplyAsync(() -> {
            //任务A
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).thenRunAsync(() -> {
            //任务B
            System.out.println("任务B启动了...");
        },executor);*/
        /**
         * 等待A执行结束拿到A的返回值再执行B
         */
/*        CompletableFuture.supplyAsync(() -> {
            //任务A
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).thenAcceptAsync(res -> {
            //任务B
            System.out.println("任务B启动了..."+res);
        },executor);*/

        /**
         * 等待A执行结束拿到A的返回值再执行B，并且调用者还需要B的返回值
         */
/*        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            //任务A
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).thenApplyAsync((res) -> {
            //任务B
            System.out.println("任务B启动了..." + res);
            return "Hello" + res;
        }, executor);
        System.out.println("apply结果："+future.get());*/

        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
            //任务A
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, executor);

        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
            //任务B
            return "hello";
        }, executor);

/*        future01.runAfterBothAsync(future02,() -> {
            //任务C
            System.out.println("任务C执行了");
        },executor);*/

/*        future01.thenAcceptBothAsync(future02,(f1,f2) -> {
            //任务C
            System.out.println("任务C执行了"+f1+"--"+f2);
        },executor);*/

/*        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
            //任务C
            return f1 + " : " + f2;
        }, executor);
        System.out.println(future.get());*/

        /**
         * 其中一个执行完成就执行任务C，A、B需要相同的返回类型
         * 需要返回值
         */
/*        future01.runAfterEitherAsync(future02,() -> {
            //任务C
            System.out.println("任务C执行了");
        },executor);
        future01.acceptEitherAsync(future02,(res) -> {
            //任务C
            System.out.println(res);
        },executor);
        future01.applyToEitherAsync(future02,(res)->{
            //任务C
            return res.toString();
        },executor);*/
        /**
         * 多任务
         */

        CompletableFuture<Void> allOf = CompletableFuture.allOf(future01, future02);
        allOf.get();//等待所有结果完成
        System.out.println(future01.get()+"+++++++"+future02.get());
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future01, future02);
        anyOf.get();
        System.out.println("anyOf"+anyOf.get());
        executor.shutdown();
        System.out.println("main...结束...");
    }

/*
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...启动...");
        //Callable测试
        FutureTask<Integer> task = new FutureTask<>(new Callable01());
        //启动线程
        new Thread(task).start();
        //获取线程处理后的结果
        System.out.println(task.get());
        System.out.println("main...结束...");


    }*/


/*    static class Callable01 implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            System.out.println("线程："+Thread.currentThread().getId()+"启动");
            return 1;
        }
    }*/
}
