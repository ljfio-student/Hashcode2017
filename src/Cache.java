import java.util.ArrayList;

public class Cache {
  public int id;
  public int size;
  public ArrayList<Video> videos = new ArrayList<>();

  public Cache(int id, int size) {
    this.id = id;
    this.size = size;
  }

  public void addToCache(Video video) {
    videos.add(video);
    size -= video.size;
  }

  public boolean hasSpaceFor(Video video) {
    return video.size <= size;
  }

  public String output() {
    return id + " " + videos.stream()
      .map((v) -> v.id)
      .map(Object::toString)
      .reduce((a, b) -> a + " " + b.toString());
  }
}