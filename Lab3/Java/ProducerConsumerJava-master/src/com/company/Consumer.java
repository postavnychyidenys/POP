package com.company;

class Consumer extends Thread{

    protected final int index;
    protected Storage storage;

    public Consumer(int index, Storage storage){
        this.index = index;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (storage.itemsReceived < storage.itemsTarget){
            try {
                storage.empty.acquire();
                storage.access.acquire();

                if (storage.itemsReceived >= storage.itemsTarget){
                    storage.empty.release();
                    storage.full.release();
                    storage.access.release();
                    return;
                }
                String item = storage.items.remove(0);
                System.out.println("Consumer " + index + " took " + (item));
                storage.itemsReceived++;

                storage.access.release();
                storage.full.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}