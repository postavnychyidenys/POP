import java.util.concurrent.Semaphore;

class Waiter{
  public Semaphore access;
  public Waiter(){
    this.access = new Semaphore(4);
  }
}
