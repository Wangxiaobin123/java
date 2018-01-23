package thread;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author shengbin
 */
public class ThreadPoolUtils implements ThreadFactory {

    private String threadName;
    private int counter;
    private List<String> stats;

    ThreadPoolUtils(String threadName) {
        this.threadName = threadName;
        counter=0;
        stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread t = new Thread(r, threadName + "-Thread");
        counter++;
        stats.add(String.format("Created thread %d with name %s on%s\n" ,t.getId() ,t.getName() ,new Date()));
        return t;

    }

    public String getStas() {
        StringBuilder buffer = new StringBuilder();
        for (String stat : stats) {
            buffer.append(stat);
        }
        return buffer.toString();
    }
}
