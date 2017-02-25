import java.util.HashSet;
import java.util.Set;

/**
 * Created by testuser on 23/02/2017.
 */
public class CacheOutput {
    public int cache;
    public int capacity;
    public Set<Integer> videos;

    public CacheOutput(int cache, int capacity)
    {
        this.cache = cache;
        this.capacity = capacity;
        this.videos = new HashSet<>();
    }

    public void addVideo(Video video)
    {
        this.videos.add(video.index);
        this.capacity -= video.size;
    }

    public boolean isContained(Video video)
    {
        return videos.contains(video.index);
    }

    public CacheOutput(int cache, Set<Integer> videos)
    {
        this.cache = cache;
        this.videos = videos;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("");
        this.videos.forEach(v-> sb.append(v).append(" "));
        sb.deleteCharAt(sb.length() - 1);
        return cache + " " + sb.toString();
    }
}
