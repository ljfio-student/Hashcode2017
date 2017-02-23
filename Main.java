import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class Main {
  public static void main(String[] args) {

  }

  public Main() {

  }

  public void loadFile(String inputFile) {
    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
  }

  public void saveFile(String outputFile, String data) {
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
  }
}
