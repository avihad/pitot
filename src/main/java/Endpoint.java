import java.util.HashMap;
import java.util.Map;

/**
 * Created by amenahem on 2/23/17.
 */
public class Endpoint
{
    public final int index;
    public final Map<Integer, Integer> cacheToLatency;
    public final int datacenterLatency;

    public Map<Requests, Integer> requestsToSaving;


    public Endpoint(int index, Map<Integer, Integer> cacheToLatency, int datacenterLatency)
    {
        this.index = index;
        this.cacheToLatency = cacheToLatency;
        this.datacenterLatency = datacenterLatency;
        this.requestsToSaving = new HashMap<>();
    }

    public void addRequest(Requests requests){
        this.requestsToSaving.put(requests, 0);
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Endpoint endpoint = (Endpoint) o;

        if (index != endpoint.index)
        {
            return false;
        }
        if (datacenterLatency != endpoint.datacenterLatency)
        {
            return false;
        }
        return cacheToLatency != null ? cacheToLatency.equals(endpoint.cacheToLatency) : endpoint.cacheToLatency == null;
    }

    @Override
    public int hashCode()
    {
        int result = index;
        result = 31 * result + (cacheToLatency != null ? cacheToLatency.hashCode() : 0);
        result = 31 * result + datacenterLatency;
        return result;
    }

    public int getSaving(Requests request)
    {
        return 0;
    }
}
