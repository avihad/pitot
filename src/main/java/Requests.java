/**
 * Created by amenahem on 2/23/17.
 */
public class Requests{

    public Endpoint endpoint;
    public Video video;
    public int numOfRequests;

    public Requests(Endpoint endpoint, Video video, int numOfRequests)
    {
        this.endpoint = endpoint;
        this.video = video;
        this.numOfRequests = numOfRequests;
    }
}