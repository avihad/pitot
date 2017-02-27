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

    public void updateRequests(Integer cache)
    {
        this.requests.forEach(r -> r.updateRequestTimeSaving(this, cache));
    }

    public long overallSaving(){
        return this.requests.stream()
                .mapToLong(r -> r.timeSaved)
                .sum();
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

        Video video = (Video) o;

        if (index != video.index)
        {
            return false;
        }
        return size == video.size;
    }

    @Override
    public int hashCode()
    {
        int result = index;
        result = 31 * result + size;
        return result;
    }
}
