package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BreakThread breakThread = new BreakThread();
        ArrayList<ThreadInfo> threadInfo = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть кількість потоків:");
        int number = scanner.nextInt();

        for (int i = 1; i <= number; i++) {
            new MainThread(i, breakThread, i * 3).start();
            System.out.println("Введіть час роботи в секундах:");
            threadInfo.add(new ThreadInfo(i, scanner.nextInt() * 1000));
        }

        threadInfo.sort(Comparator.comparingLong(ThreadInfo::getTime));
        breakThread.setThreadInfo(threadInfo);

        new Thread(breakThread).start();
    }
}
