import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by amenahem on 2/23/17.
 */
public class CacheParser
{
    int videoSize;
    int endPointsNum;
    int requestsNums;
    int cacheNums;
    int cacheCapacity;

    Map<Integer, Video> videos;
    Map<Integer, Endpoint> endpoints;

    public CacheParser(String fileName) throws IOException
    {
        List<String> fileLines = Files.readAllLines(Paths.get(fileName));

        String firstRow = fileLines.get(0);
        List<Integer> nums = lineToNums(firstRow);
        System.out.println(nums);

        videoSize = nums.get(0);
        endPointsNum = nums.get(1);
        requestsNums = nums.get(2);
        cacheNums = nums.get(3);
        cacheCapacity = nums.get(4);

        String videosList = fileLines.get(1);

        videos = parseVideos(videosList);

        int endPointRow = 2;


        endpoints = new HashMap<>();

        for (int i = 0; i< this.endPointsNum; i++)
        {
            List<Integer> endpointDesc = lineToNums(fileLines.get(endPointRow));

            int dbLatency = endpointDesc.get(0);
            int numOfCaches = endpointDesc.get(1);
            int latencyRowStart = endPointRow + 1;
            int latencyRowEnd = endPointRow + 1 + numOfCaches;
            List<String> cacheLatency = fileLines.subList(latencyRowStart, latencyRowEnd);
            Map<Integer, Integer> cacheToLatency = cacheLatency.stream()
                    .map(s -> s.split(" "))
                    .collect(Collectors.toMap(s -> Integer.parseInt(s[0]),
                                              s -> Integer.parseInt(s[1])));
            endpoints.put(i,new Endpoint(i, cacheToLatency, dbLatency));
            endPointRow = latencyRowEnd + 1;
        }

        int requestRow = endPointRow;
        List<String> requestsLines = fileLines.subList(endPointRow, fileLines.size());



    }

    static class Requests{

    }
    static class Endpoint
    {
        int index;
        Map<Integer, Integer> cacheToLatency;
        int datacenterLatency;

        public Endpoint(int index, Map<Integer, Integer> cacheToLatency, int datacenterLatency)
        {
            this.index = index;
            this.cacheToLatency = cacheToLatency;
            this.datacenterLatency = datacenterLatency;
        }
    }

    private List<Integer> lineToNums(String firstRow)
    {
        return Stream.of(firstRow.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private Map<Integer, Video> parseVideos(String videosList)
    {
        Map<Integer, Video> videos = new HashMap<>();
        String[] sizes = videosList.split(" ");
        for (int i = 0; i < sizes.length; i++)
        {
            videos.put(i, new Video(i, Integer.parseInt(sizes[i])));
        }

        return videos;
    }

    static class Video
    {
        final int index;
        final int size;

        public Video(int index, int size)
        {
            this.index = index;
            this.size = size;
        }
    }

    public static void main(String[] args) throws IOException
    {
        CacheParser cacheParser = new CacheParser("me_at_the_zoo.in");
    }
}
