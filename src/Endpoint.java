import java.util.HashMap;

public class Endpoint {
  public int id;
  public int latency;
  public HashMap<Cache, Integer> caches;

  public Endpoint(int id, int latency, HashMap<Cache, Integer> caches) {
    this.id = id;
    this.latency = latency;
    this.caches = caches;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Endpoint) {
      Endpoint e = (Endpoint)o;

      return e.id == this.id;
    }

    return false;
  }
}