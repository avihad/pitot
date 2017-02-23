import java.util.Map;

/**
 * Created by amenahem on 2/23/17.
 */
public class Endpoint
{
    public int index;
    public Map<Integer, Integer> cacheToLatency;
    public int datacenterLatency;

    public Endpoint(int index, Map<Integer, Integer> cacheToLatency, int datacenterLatency)
    {
        this.index = index;
        this.cacheToLatency = cacheToLatency;
        this.datacenterLatency = datacenterLatency;
    }
}
