package com.company;

public class Main {

    public static void main(String[] args) {
        Init(4, 10, 4, 6);
    }
    private static void Init(int storageSize, int itemsTarget, int producers, int consumers) {
        Storage storage = new Storage(storageSize, itemsTarget);

        for (int i = 0; i < consumers; i++) {
            new Consumer(i, storage).start();
        }

        for (int i = 0; i < producers; i++) {
            new Producer(i, storage).start();
        }
    }
}
