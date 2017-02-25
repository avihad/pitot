import java.util.ArrayList;
import java.util.List;

/**
 * Created by amenahem on 2/23/17.
 */
public class Video
{
    public final int index;
    public final int size;

    public List<Requests> requests;
    public Video(int index, int size)
    {
        this.index = index;
        this.size = size;
        this.requests = new ArrayList<>();
    }

    public void addRequests(Requests requests)
    {
        this.requests.add(requests);
    }
}
