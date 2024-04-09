package com.company;

public class MainThread extends Thread {
    private final int id;
    private final BreakThread breakThread;
    private final int step;

    public MainThread(int id, BreakThread breakThread, int step) {
        this.id = id;
        this.breakThread = breakThread;
        this.step = step;
    }

    @Override
    public void run() {
        long sum = 0;
        long addCount = 0;
        do{
            sum += step;
            addCount++;
        } while (!breakThread.isCanBreak(id));
        System.out.println("Порядковий номер: " + id + " Знайдена сума: " + sum + " Кількість доданків: " + addCount);
    }
}
