import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
  public ArrayList<Video> videos = new ArrayList<>();
  public ArrayList<Cache> caches = new ArrayList<>();
  public ArrayList<Endpoint> endpoints = new ArrayList<>();
  public ArrayList<Request> requests = new ArrayList<>();

  public static void main(String[] args) {
    new Main(args[0], args[1]);
  }

  public Main(String inputFile, String outputFile) {
    loadFile(inputFile);
    printInfo();
  }

  public void printInfo() {
    System.out.printf("Videos: %d\nCaches: %d\nEndpoints: %d\nRequests: %d\n", videos.size(), caches.size(), endpoints.size(), requests.size());
  }

  public void loadFile(String inputFile) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));

      // Pull in the amounts of data counts
      String[] amounts = reader.readLine().split(" ");

      int video_count = Integer.parseInt(amounts[0]);
      int endpoint_count = Integer.parseInt(amounts[1]);
      int request_count = Integer.parseInt(amounts[2]);

      // Create caches
      int cache_count = Integer.parseInt(amounts[3]);
      int cache_size = Integer.parseInt(amounts[4]);

      for (int i = 0; i < cache_count; i++) {
        caches.add(new Cache(i + 1, cache_size));
      }

      // Load in the video information
      String[] video_info = reader.readLine().split(" ");

      for (int i = 0; i < video_count; i++) {
        videos.add(new Video(i + 1, Integer.parseInt(video_info[i])));
      }

      // Read in the rest of the data
      for (int i = 0; i < endpoint_count; i++) {
        String[] endpoint_info = reader.readLine().split(" ");

        int data_center_latency = Integer.parseInt(endpoint_info[0]);
        int latency_count = Integer.parseInt(endpoint_info[1]);

        HashMap<Cache, Integer> latency_map = new HashMap<Cache, Integer>();

        for (int j = 0; j < latency_count; j++) {
          String[] latency_info = reader.readLine().split(" ");

          int cache_index = Integer.parseInt(latency_info[0]);
          int cache_latency = Integer.parseInt(latency_info[1]);

          latency_map.put(caches.get(cache_index), new Integer(cache_latency));
        }

        endpoints.add(new Endpoint(data_center_latency, latency_map));
      }

      // Read in the requests
      for (int i = 0; i < request_count; i++) {
        String[] request_info = reader.readLine().split(" ");

        int video_index = Integer.parseInt(request_info[0]);
        int endpoint_index = Integer.parseInt(request_info[1]);
        int amount = Integer.parseInt(request_info[2]);

        requests.add(new Request(videos.get(video_index), endpoints.get(endpoint_index), amount));
      }

      reader.close();
    } catch (IOException ex) {

    }
  }

  public void saveFile(String outputFile, String data) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

      writer.close();
    } catch (IOException ex) {

    }
  }
}
