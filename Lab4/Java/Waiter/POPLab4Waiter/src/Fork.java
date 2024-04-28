import java.util.concurrent.Semaphore;

class Fork{
  public int id;
  public Semaphore access;

  public Fork(int id){
    this.id = id;
    this.access = new Semaphore(1);
  }
}
