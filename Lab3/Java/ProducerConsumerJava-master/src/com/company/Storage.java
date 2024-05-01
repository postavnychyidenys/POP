package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class Storage{
  public int size;
  public Semaphore access;
  public Semaphore empty;
  public Semaphore full;
  public List<String> items;
  public volatile int itemsTarget;
  public AtomicInteger itemsProduced;
  public AtomicInteger itemsReceived;

  private int lastIndex = 0;

  public Storage(int size, int itemsTarget){
    this.size = size;
    this.access = new Semaphore(1);
    this.empty = new Semaphore(0);
    this.full = new Semaphore(size);
    this.itemsTarget = itemsTarget;
    this.itemsProduced = new AtomicInteger(itemsTarget);
    this.itemsReceived = new AtomicInteger(itemsTarget);
    items = new ArrayList<>();
  }

  public int put(){
    items.add("item " + lastIndex);
    lastIndex++;
    return lastIndex - 1;
  }
}
