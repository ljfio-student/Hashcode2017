import java.util.ArrayList;
import java.util.HashSet;

public class Video {
  public int size;
  public int id;
  public ArrayList<Request> requests = new ArrayList<>();
  // public HashSet<Endpoint> endpoints = new HashSet<>();
  public int amount;

  public Video(int id, int size) {
    this.id = id;
    this.size = size;
  }

  public void addRequests(Request r) {
    amount += r.amount;
    requests.add(r);
    // endpoints.add(r.endpoint);
  }

  @Override
  public String toString() {
    return String.format("%d %d %d", id, size, amount);
  }
}