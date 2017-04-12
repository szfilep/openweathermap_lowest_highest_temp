import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pollakt on 2016.07.14..
 */
public class CityReader {
    static final String INPUT_FILE = "city_list.txt";
    private int limit;

    CityReader(int limit) {
        this.limit = limit;
    }

    public List<String> readCityIds() {
        List<String> fileLines = getLines();
        List<String> cityIds = getIds(fileLines);
        return cityIds;
    }

    private List<String> getLines() {
        Path path = Paths.get(INPUT_FILE);
        List<String> lines = new ArrayList<String>();

        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private List<String> getIds(List<String> fileLines) {
        List<String> ids = new ArrayList<String>();

        // Remove the header (first line)
        fileLines.remove(0);

        int processedCities = 0;

        for (String line : fileLines) {
            String[] parts = line.split("\t");
            ids.add(parts[0]);
            processedCities++;

            if (processedCities == limit) {
                break;
            }
        }
        return ids;
    }
}