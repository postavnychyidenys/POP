package com.company;

public class ThreadInfo {

  private int threadId;
  private long time;

  public ThreadInfo(int threadId, int time) {
    this.threadId = threadId;
    this.time = time;
  }

  public long getTime() {
    return time;
  }

  public int getThreadId() {
    return threadId;
  }
}
