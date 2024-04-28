class Philosopher implements Runnable{
  private final int id;
  private final Fork leftFork;
  private final Fork rightFork;
  private final Waiter waiter;

  public Philosopher(int id, Fork leftFork, Fork rightFork, Waiter waiter){
    this.id = id;
    this.leftFork = leftFork;
    this.rightFork = rightFork;
    this.waiter = waiter;
  }

  @Override
  public void run() {
    try{
      for (int i = 0; i < 10; i++) {
        System.out.println("Philosopher " + id + " thinking time " + i);

        waiter.access.acquire();
        leftFork.access.acquire();
        System.out.println("Philosopher " + id + " took fork " + leftFork.id);
        rightFork.access.acquire();
        System.out.println("Philosopher " + id + " took fork " + rightFork.id);

        System.out.println("Philosopher " + id + " eating time " + i);

        rightFork.access.release();
        System.out.println("Philosopher " + id + " put fork " + rightFork.id);
        leftFork.access.release();
        System.out.println("Philosopher " + id + " put fork " + leftFork.id);
        waiter.access.release();
      }
    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }
}
