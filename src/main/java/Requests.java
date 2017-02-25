/**
 * Created by amenahem on 2/23/17.
 */
public class Requests
{

    public Endpoint endpoint;
    public Video video;
    public int numOfRequests;

    public int timeSaved;

    public Requests(Endpoint endpoint, Video video, int numOfRequests)
    {
        this.endpoint = endpoint;
        this.video = video;
        this.numOfRequests = numOfRequests;
        this.timeSaved = 0;
    }

    public static Requests create(Endpoint endpoint, Video video, int numOfRequests)
    {
        Requests requests = new Requests(endpoint, video, numOfRequests);
        video.addRequests(requests);
        return requests;
    }


    public void updateRequestTimeSaving(Video video, int cache)
    {
        if (this.video.equals(video))
        {
            int datacenterLatency = endpoint.datacenterLatency;
            timeSaved = Math.max(timeSaved, numOfRequests * endpoint.cacheToLatency.getOrDefault(cache, 0) - datacenterLatency);
        }
    }
}