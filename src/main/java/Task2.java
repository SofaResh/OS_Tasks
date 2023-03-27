import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Task2 {
    public static final int INC_AMOUNT = 10000;
    public static final int THREAD_COUNT = 1234;
    public static int i;
    public static ReentrantLock mt = new ReentrantLock();
    public static AtomicInteger atom = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        start1();
        long endTime = System.currentTimeMillis();
        System.out.printf("Task 2.1 - %d ms%n", endTime - startTime);

        startTime = System.currentTimeMillis();
        start2();
        endTime = System.currentTimeMillis();
        System.out.printf("Task 2.2 - %d ms%n", endTime - startTime);

        startTime = System.currentTimeMillis();
        start3();
        endTime = System.currentTimeMillis();
        System.out.printf("Task 2.3 - %d ms%n", endTime - startTime);

    }

    public static void start1() throws InterruptedException {
        i = 0;
        List<Thread> threads = new ArrayList<>();
        for (int j = 0; j < THREAD_COUNT; j++) {
            threads.add(new Thread(() -> {
                for (int i = 0; i < INC_AMOUNT; i++) {
                    Task2.i++;
                }
            }));
        }
        startThreads(threads);
    }

    public static void start2() throws InterruptedException {
        i = 0;
        List<Thread> threads = new ArrayList<>();
        for (int j = 0; j < THREAD_COUNT; j++) {
            threads.add(new Thread(() -> {
                for (int i = 0; i < INC_AMOUNT; i++) {
                    Task2.mt.lock();
                    Task2.i++;
                    Task2.mt.unlock();
                }
            }));
        }
        startThreads(threads);
    }

    public static void start3() throws InterruptedException {
        atom.set(0);
        List<Thread> threads = new ArrayList<>();
        for (int j = 0; j < THREAD_COUNT; j++) {
            threads.add(new Thread(() -> {
                for (int i = 0; i < INC_AMOUNT; i++) {
                    Task2.atom.getAndIncrement();
                }
            }));
        }
        startThreads(threads);
    }

    private static void startThreads(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.printf("i = %d%n", i);
    }
}

