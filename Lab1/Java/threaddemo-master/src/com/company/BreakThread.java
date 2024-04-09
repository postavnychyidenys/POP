package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class BreakThread implements Runnable{
    private Boolean[] canBreaks = null;

    private ArrayList<ThreadInfo> threadInfo = null;

    @Override
    public void run() {
        int threadId = 0;
        canBreaks = new Boolean[threadInfo.size()];
        Arrays.fill(canBreaks, false);
        for (int i = 0; i < threadInfo.size(); i++) {
            long sleepTime = threadInfo.get(i).getTime();
            try {
                if (i != 0) {
                    sleepTime -= threadInfo.get(i - 1).getTime();
                }

                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canBreaks[threadInfo.get(i).getThreadId() - 1] = true;
        }
    }

    synchronized public boolean isCanBreak(int id) {
        if (canBreaks == null) {
            return false;
        }
        return canBreaks[id - 1];
    }

    public void setThreadInfo(ArrayList<ThreadInfo> threadInfo) {
        this.threadInfo = threadInfo;
    }
}
