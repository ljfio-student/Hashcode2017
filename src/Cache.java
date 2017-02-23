import java.util.ArrayList;
import java.util.Optional;

public class Cache {
  public int id;
  public int size;
  public ArrayList<Video> videos = new ArrayList<>();

  public Cache(int id, int size) {
    this.id = id;
    this.size = size;
  }

  public boolean addToCache(Video video) {
    if (!videos.contains(video)) {
      videos.add(video);
      size -= video.size;

      return true;
    }

    return false;
  }

  public boolean videoInCache(Video video) {
    return videos.contains(video);
  }

  public boolean hasSpaceFor(Video video) {
    return video.size <= size;
  }

  public String output() {
    Optional<String> values = videos.stream()
      .map((v) -> v.id)
      .map(Object::toString)
      .reduce((a, b) -> a + " " + b.toString());

    return id + " " + (values.isPresent() ? values.get() : "");
  }
}
