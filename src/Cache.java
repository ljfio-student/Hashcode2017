import java.util.ArrayList;

public class Cache {
  public int id;
  public int size;
  public ArrayList<Video> videos = new ArrayList<>();

  public Cache(int id, int size) {
    this.id = id;
    this.size = size;
  }
}