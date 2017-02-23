import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    List<Video> videos;

    public CacheParser(String fileName) throws IOException
    {
        List<String> fileLines = Files.readAllLines(Paths.get(fileName));

        List<Integer> nums = Stream.of(fileLines.get(0).split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        System.out.println(nums);

        videoSize = nums.get(0);
        endPointsNum = nums.get(1);
        requestsNums = nums.get(2);
        cacheNums = nums.get(3);
        cacheCapacity = nums.get(4);

        String videosList = fileLines.get(1);

        videos = parseVideos(videosList);


    }

    private List<Video> parseVideos(String videosList)
    {
        List<Video> videos = new ArrayList<>();
        String[] sizes = videosList.split(" ");
        for (int i = 0; i < sizes.length; i++)
        {
            videos.add(new Video(i, Integer.parseInt(sizes[i])));
        }

        return videos;
    }

    static class Video{
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
