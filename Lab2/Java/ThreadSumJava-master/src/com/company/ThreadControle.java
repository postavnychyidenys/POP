package com.company;

import java.util.Random;

public class ThreadControle {

  private int stopThreadsCount = 0;
  private final int threadsCount = 8;
  private final int maxArraySize = 10000;

  private int minIndex = 0;
  private int minElement = 0;
  private ComputeThread[] threads;

  public void EntryPoint(){
    System.out.println("Threads count: " + threadsCount);
    int[] array = new int[maxArraySize];
    RandomizeArray(array);

    threads = new ComputeThread[threadsCount];
    for (int i = 0; i < threadsCount; i++) {
      threads[i] = new ComputeThread(array, i, threadsCount, this);
      threads[i].start();
    }
    WaitForThreadsStop();

    System.out.println("Min index: " + minIndex + ", min element: " + array[minIndex]);
  }

  synchronized private void WaitForThreadsStop(){
    while (threadsCount > stopThreadsCount){
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  synchronized public void incrementStopThreadsCount(int threadIndex){
    System.out.println("Thread " + threadIndex + " done with min index: " + threads[threadIndex].getMinIndex() + ", with value: " + threads[threadIndex].getMinElement());

    if (threads[threadIndex].getMinElement() < minElement) {
      minIndex = threads[threadIndex].getMinIndex();
      minElement = threads[threadIndex].getMinElement();
    }

    stopThreadsCount++;
    notify();
  }

  private void RandomizeArray(int[] array) {
    Random random = new Random();
    int randomIndex = random.nextInt(array.length);
    int randomValue = random.nextInt(Integer.MIN_VALUE, 0);
    array[randomIndex] = randomValue;
    System.out.println("Randomize min index in: " + randomIndex + ", with value: " + randomValue);
  }
}
