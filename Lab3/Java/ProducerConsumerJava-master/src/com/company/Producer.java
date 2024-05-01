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
        while (storage.itemsProduced < storage.itemsTarget){
            try {
                storage.full.acquire();
                storage.access.acquire();

                if (storage.itemsProduced >= storage.itemsTarget){
                    storage.full.release();
                    storage.access.release();
                    storage.empty.release();
                    return;
                }

                int itemIndex = storage.put();
                System.out.println("Producer " + index + " added " + itemIndex);
                storage.itemsProduced++;

                storage.access.release();
                storage.empty.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}