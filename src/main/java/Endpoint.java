import java.util.Map;

/**
 * Created by amenahem on 2/23/17.
 */
public class Endpoint
{
    public final int index;
    public final Map<Integer, Integer> cacheToLatency;
    public final int datacenterLatency;


    public Endpoint(int index, Map<Integer, Integer> cacheToLatency, int datacenterLatency)
    {
        this.index = index;
        this.cacheToLatency = cacheToLatency;
        this.datacenterLatency = datacenterLatency;
    }
}
