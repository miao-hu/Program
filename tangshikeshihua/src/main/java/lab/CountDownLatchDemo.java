package lab;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    private static class job implements Runnable{
        private CountDownLatch countDownLatch;

        job(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
        @Override
        public void run() {
            Random random = new Random();
            try {
                Thread.sleep(random.nextInt(30) * 1000);   //线程沉睡 0-3 秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();   //每次减1  （原来最初为 10）
            System.out.println("一个线程的任务结束了");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(10);    // CountDownLatch 对象的属性值为 10
        for(int i=0;i<10;i++){    //启动10个线程
            Thread thread=new Thread(new job(countDownLatch));
            thread.start();
        }

        System.out.println("等待10个线程全部结束");
        countDownLatch.await();   // 一直等到 CountDownLatch 对象的属性值为 0
        System.out.println("10个线程全部结束了");
    }
}
