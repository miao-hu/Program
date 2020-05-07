package lab;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class 通过计数的办法判断任务是否全部完成 {
    private static int COUNT=10;  //总数10个
    /*
        private static int successCount = 0;     successCount++;   不是原子的
        private static int failureCount = 0;     failureCount++;   不是原子的
     */
    private static AtomicInteger successCount=new AtomicInteger(0);   //原子类
    private static AtomicInteger failureCount=new AtomicInteger(0);
   //  原子类：CAS    CPU 指令集就有 CAS ,CPU 保证了原子性

    private static class job implements Runnable{

        @Override
        public void run() {
            try{
                work();
                successCount.getAndIncrement();    //seccessCount++;
            }catch(IOException | InterruptedException e){
                failureCount.getAndIncrement();    //failureCount++;
            }
        }

        private void work() throws IOException, InterruptedException{
            Random random=new Random();
            int n=random.nextInt(5);   //随机产生一个 0-4 的数字
            if(n<2){
                throw new IOException();      //抛出异常
            }
            Thread.sleep(5);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        //启动 10 个线程
        for(int i=0;i<COUNT;i++){
            Thread thread=new Thread(new job());
            thread.start();
        }

        while(successCount.get()+failureCount.get()!=COUNT){
            Thread.sleep(1000);   //线程睡眠一秒
            System.out.println("任务没有全部完成");
        }

        System.out.println("任务全部完成");
    }
}
