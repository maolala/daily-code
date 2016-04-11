package future;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FutureTest {
    public static void main(String [] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter base directory (e.g. /usr/local): ");
        String directory = in.nextLine();
        System.out.print("Enter keyword (e.g. volatie): ");
        String keyword = in.nextLine();
        
        MatchCounter counter = new MatchCounter(new File(directory), keyword);
        FutureTask<Integer> task = new FutureTask<>(counter);
        Thread t = new Thread(task);
        t.start();
        
        try {
            System.out.println(task.get() + "matching files.");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            ;
        }
    }
}
