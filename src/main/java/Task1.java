import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Task1 {

    public static int SIZE_I = 2000;
    public static int SIZE_J = 2000;

    public static int THREADS_COUNT = 16;

    public static void main(String[] args) throws InterruptedException {
        int[][] a = new int[SIZE_I][SIZE_J];
        int[][] b = new int[SIZE_I][SIZE_J];

        int[][] c = new int[SIZE_I][SIZE_J];

        generateRandomMatrix(a);
        generateRandomMatrix(b);

//        printMatrix(a);
//        System.out.println();
//        printMatrix(b);
//        System.out.println();

        for (int i = 1; i <= THREADS_COUNT; i++) {
            calculate(a, b, c, i);
        }
    }

    private static void calculate(int[][] a, int[][] b, int[][] c, int threadsCount) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

//        System.out.println("Dividing into:");
        int offset = (int) Math.ceil((float) a.length / threadsCount);
        for (int start = 0; start < a.length; start += offset) {
            int end = Math.min(start + offset, a.length);
//            System.out.printf("%d, %d\n", start, end);
            Runnable runnable = new MatrixMultiplier(a, b, c, start, end);
            Thread thread = new Thread(runnable);
            threads.add(thread);
        }

        long startTime = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.printf("With %d threads = %d ms\n", threadsCount, System.currentTimeMillis() - startTime);
    }

    public static void multiplyMatrices(int[][] a, int[][] b, int[][] result) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                int sum = 0;
                for (int k = 0; k < b.length; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = sum;
            }
        }
    }

    public static void printMatrix(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void generateRandomMatrix(int[][] a) {
        Random random = new Random();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                a[i][j] = random.nextInt(100);
            }
        }
    }
}

class MatrixMultiplier implements Runnable {

    int[][] a;
    int[][] b;
    int[][] c;

    int begin, end;

    public MatrixMultiplier(int[][] a, int[][] b, int[][] c, int begin, int end) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = begin; i < end; i++) {
            for (int j = 0; j < b[0].length; j++) {
                int sum = 0;
                for (int k = 0; k < b.length; k++) {
                    sum += a[i][k] * b[k][j];
                }
                c[i][j] = sum;
            }
        }
    }
}
