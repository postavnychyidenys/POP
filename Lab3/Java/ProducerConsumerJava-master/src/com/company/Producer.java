package com.company;

class Producer extends Thread{

    protected final int index;
    protected Storage storage;

    public Producer(int index, Storage storage){
        this.index = index;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (storage.itemsProduced.getAndDecrement() > 0){
            try {
                storage.full.acquire();
                storage.access.acquire();

                int itemIndex = storage.put();
                System.out.println("Producer " + index + " added " + itemIndex);

                storage.access.release();
                storage.empty.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
