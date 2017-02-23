import java.util.HashMap;

public class Endpoint {
  public int latency;
  public HashMap<Cache, Integer> caches;

  public Endpoint(int latency, HashMap<Cache, Integer> caches) {
    this.latency = latency;
    this.caches = caches;
  }
}