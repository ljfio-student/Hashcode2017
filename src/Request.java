public class Request {
  public int amount;
  public Video video;
  public Endpoint endpoint;

  public Request(Video video, Endpoint endpoint, int amount) {
    this.video = video;
    this.endpoint = endpoint;
    this.amount = amount;
  }

  public String toString() {
    return String.format("%d %s %s", amount, video.toString(), endpoint.toString());
  }
}