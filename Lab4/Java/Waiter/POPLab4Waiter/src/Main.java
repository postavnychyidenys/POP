
public class Main {
  public static void main(String[] args) {
    Fork[] forks = new Fork[5];
    for (int i = 0; i < 5; i++) {
      forks[i] = new Fork(i);
    }
    Waiter waiter = new Waiter();

    Philosopher[] philosophers = new Philosopher[5];
    for (int i = 0; i < 5; i++) {
      philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % 5], waiter);
    }

    for (int i = 0; i < 5; i++) {
      new Thread(philosophers[i]).start();
    }
  }
}