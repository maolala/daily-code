package blockingQueue;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class SearchTask implements Runnable {
    private BlockingQueue<File> queue;
    private String keyword;
    
    public SearchTask(BlockingQueue<File> queue, String keyword) {
        this.queue = queue;
        this.keyword = keyword;
    }
    
    public void run() {
        try {
            boolean done = false;
            while (! done) {
                File file = queue.take();
                if (file == FileEnumerationTask.DUMMY) {
                    queue.put(file);
                    done = true;
                } else {
                    search(file, keyword);
                }
            }
        } catch(InterruptedException e) {
            ;
        }
    }
    
    public void search(File file, String keyword) {
        try {
            Scanner in = new Scanner(file);
            int lineNumber = 0;
            while (in.hasNextLine()) {
                lineNumber++;
                String line = in.nextLine();
                if (line.contains(keyword)) {
                    System.out.printf("%s:%s:%s\n", file.getPath(), lineNumber, line);
                }
            }
        } catch (IOException e) {
            ;
        }
    }
}
