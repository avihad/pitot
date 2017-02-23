import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amenahem on 2/23/17.
 */
public class Algos
{
    public static List<CacheOutput> simple(VideoCache videoCache)
    {
        int cacheCapacity = videoCache.cacheCapacity;

        List<CacheOutput> outputs = new ArrayList<>();
        for (int i = 0; i < videoCache.cacheNums; i++)
        {
            int currentCapacity = cacheCapacity;
            List<Integer> currentCache = new ArrayList<>();
            for (Video video : videoCache.videos.values())
            {
                if (video.size < currentCapacity){
                    currentCache.add(video.index);
                    currentCapacity -= video.size;
                }
            }
            outputs.add(new CacheOutput(i, currentCache));

        }


        return outputs;
    }


    public static void main(String[] args) throws IOException
    {
        VideoCache videoCache = new VideoCache("kittens.in");
        List<CacheOutput> output = simple(videoCache);
        outputToFile(output);
    }

    public static void outputToFile(List<CacheOutput> outputs) throws IOException
    {
        List<String> lines = new ArrayList<>();
        lines.add("" + outputs.size());
        outputs.stream().map(CacheOutput::toString).forEach(lines::add);
        Files.write(Paths.get("output.out"), lines);
    }

}
