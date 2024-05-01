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
        while (storage.itemsReceived.getAndDecrement() > 0){
            try {
                storage.empty.acquire();
                storage.access.acquire();

                String item = storage.items.remove(0);
                System.out.println("Consumer " + index + " took " + (item));

                storage.access.release();
                storage.full.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

