package com.company;

class ComputeThread extends Thread{

  private final ThreadControle threadControle;
  private final int[] array;
  private final int threadIndex;
  private final int threadsCount;

  private int minIndex;
  private int minElement;


  public ComputeThread(int[] array, int threadIndex, int threadsCount, ThreadControle threadControle) {
    this.array = array;
    this.threadIndex = threadIndex;
    this.threadsCount = threadsCount;
    this.threadControle = threadControle;
  }

  @Override
  public void run() {
    int firstIndex = threadIndex * array.length / threadsCount;
    int lastIndex = (threadIndex + 1) * array.length / threadsCount;
    minIndex = firstIndex;
    minElement = array[firstIndex];
    for (int i = firstIndex; i < lastIndex; i++) {
      if (array[i] < minElement) {
        minElement = array[i];
        minIndex = i;
      }
    }
    threadControle.incrementStopThreadsCount(threadIndex);
  }

  public int getMinIndex() {
    return minIndex;
  }

  public int getMinElement() {
    return minElement;
  }
}

