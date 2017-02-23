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
public class VideoCache
{
    int videoSize;
    int endPointsNum;
    int requestsNums;
    int cacheNums;
    int cacheCapacity;

    Map<Integer, Video> videos;
    Map<Integer, Endpoint> endpoints;
    List<Requests> requests;

    public VideoCache(String fileName) throws IOException
    {
        List<String> fileLines = Files.readAllLines(Paths.get(fileName));

        String firstRow = fileLines.get(0);
        List<Integer> nums = lineToNums(firstRow);

        videoSize = nums.get(0);
        endPointsNum = nums.get(1);
        requestsNums = nums.get(2);
        cacheNums = nums.get(3);
        cacheCapacity = nums.get(4);

        String videosList = fileLines.get(1);

        videos = parseVideos(videosList);

        int endPointRow = 2;


        endpoints = new HashMap<>();

        for (int i = 0; i < this.endPointsNum; i++)
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
            endPointRow = latencyRowEnd;
        }

        List<String> requestsLines = fileLines.subList(endPointRow, fileLines.size());

        requests = requestsLines.stream()
                .map(this::lineToNums)
                .map(n -> new Requests(endpoints.get(n.get(1)),
                                       videos.get(n.get(0)),
                                       n.get(2)))
                .collect(Collectors.toList());
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

    public void outputSolution(List<CacheOutput> cacheOutputs){
        int size = cacheOutputs.size();

        List<CacheOutput> rows = cacheOutputs.subList(1, cacheOutputs.size());

    }

    @Override
    public String toString()
    {
        return "VideoCache{" +
                "\nvideoSize=" + videoSize +
                "\n, endPointsNum=" + endPointsNum +
                "\n, requestsNums=" + requestsNums +
                "\n, cacheNums=" + cacheNums +
                "\n, cacheCapacity=" + cacheCapacity +
                "\n, videos=" + videos +
                "\n, endpoints=" + endpoints +
                "\n, requests=" + requests +
                "\n}";
    }
}
