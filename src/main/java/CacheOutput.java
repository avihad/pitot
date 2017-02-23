import java.util.Arrays;
import java.util.List;

/**
 * Created by testuser on 23/02/2017.
 */
public class CacheOutput {
    public int cache;
    public List<Integer> videos;

    public CacheOutput(int cache, List<Integer> videos)
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
