import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
  public ArrayList<Video> videos = new ArrayList<>();
  public ArrayList<Cache> caches = new ArrayList<>();
  public ArrayList<Endpoint> endpoints = new ArrayList<>();
  public ArrayList<Request> requests = new ArrayList<>();

  public int cache_size;

  public static void main(String[] args) {
    new Main(args[0], args[1]);
  }

  public Main(String inputFile, String outputFile) {
    loadFile(inputFile);
    printInfo();
    // removeTooBig();

    requests.stream().forEach((Request r) -> {
      r.video.addRequests(r);
    });


    boolean added;

    HashMap<Video, HashMap<Cache, Integer>> videoMap = new HashMap<>();
    HashMap<Video, Integer> bestSaving = new HashMap<>();

    for (Video v : videos) {
      HashMap<Cache, Integer> latencyMap = new HashMap<>();

      v.requests.forEach((Request r) ->  {
        for (Cache c : r.endpoint.caches.keySet()) {
          int total_latency = latencyMap.containsKey(c) ? latencyMap.get(c) : 0;
          int current_latency = Math.abs(r.endpoint.caches.get(c) - r.endpoint.latency);

          latencyMap.put(c, total_latency + (r.amount * current_latency));
        }
      });

      videoMap.put(v, latencyMap);

      int best = latencyMap.values().stream()
        .mapToInt(a -> a)
        .max()
        .orElse(0);

      bestSaving.put(v, best);
    }

    System.out.println("Calculated lowest latency");

    Collections.sort(videos, (Video a, Video b) -> {
      return Integer.compare(bestSaving.get(b), bestSaving.get(a));
    });

    do {
      added = false;

      for (Video v : videos) {
        HashMap<Cache, Integer> latencyMap = videoMap.get(v);

        Optional<Entry<Integer, ArrayList<Cache>>> item = latencyMap
          .entrySet().stream() // Map from Caches with Latency to inverse
          .filter((c) -> !c.getKey().videoInCache(v) && c.getKey().hasSpaceFor(v))
          .reduce(new HashMap<Integer, ArrayList<Cache>>(), (a, b) -> {
            ArrayList<Cache> value = a.containsKey(b.getValue()) ?
              a.get(b.getValue()) : new ArrayList<>();

            value.add(b.getKey());

            a.put(b.getValue(), value);

            return a;
          }, (a, b) -> {
            for (Integer k : a.keySet()) {
              if (b.containsKey(k)) {
                a.get(k).addAll(b.get(k));
              }
            }

            return a;
          })
          .entrySet().stream() // Find the best latency
          .sorted((a, b) -> Integer.compare(b.getKey(), a.getKey()))
          .findFirst();

        if (!item.isPresent()) {
          continue;
        }

        Optional<Cache> cs = item.get().getValue()
          .stream()
          .sorted((a, b) -> Integer.compare(b.size, a.size))
          .findFirst();

        if (cs.isPresent()) {
          Cache c = cs.get();
          added |= c.addToCache(v);
        }
      }
    } while(added);

    caches.stream()
      .filter(c -> c.size > 0)
      .forEach(c -> {
        System.out.println(c.size);
      });

    String output = "";

    long count = caches.stream().filter((c) -> c.videos.size() > 0).count();

    output += count + "\n";
    output += caches.stream()
      .filter((c) -> c.videos.size() > 0)
      .map((c) -> c.output())
      .reduce((a, b) -> a + "\n" + b)
      .orElse("");

    saveFile(outputFile, output);
  }

  public void removeTooBig() {
    ArrayList<Video> toRemove = new ArrayList<>();

    for (Video video : videos) {
      if (video.size > cache_size) {
        toRemove.add(video);
      }
    }

    videos.removeAll(toRemove);
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
      cache_size = Integer.parseInt(amounts[4]);

      for (int i = 0; i < cache_count; i++) {
        caches.add(new Cache(i, cache_size));
      }

      // Load in the video information
      String[] video_info = reader.readLine().split(" ");

      for (int i = 0; i < video_count; i++) {
        videos.add(new Video(i, Integer.parseInt(video_info[i])));
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

        endpoints.add(new Endpoint(i, data_center_latency, latency_map));
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

      writer.write(data);
      writer.flush();

      writer.close();
    } catch (IOException ex) {

    }
  }
}
