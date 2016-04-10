package blockingQueue;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class BlockingQueueTest {
    public static void main(String [] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter base directory (e.g. /user/local): ");
        String directory = in.nextLine();
        System.out.println("Enter keyword (e.g. volatile): ");
        String keyword = in.nextLine();
        
        final int FILE_QUEUE_SIZE = 10;
        final int SEARCH_THREADS = 100;
        
        BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);
        FileEnumerationTask enumorator = new FileEnumerationTask(queue, new File(directory));
        new Thread(enumorator).start();
        for(int i = 0; i < SEARCH_THREADS; i++) {
            new Thread(new SearchTask(queue, keyword)).start();
        }
    }
}
