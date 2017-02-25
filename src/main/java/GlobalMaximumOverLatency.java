import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by amenahem on 2/23/17.
 */
public class GlobalMaximumOverLatency implements Algo
{

    public List<CacheOutput> calculate(VideoCache videoCache)
    {

        List<Requests> requests = videoCache.requests;

        Map<Video, Map<Integer, Integer>> videoToCacheTimes = calculateVideoSumToCacheLatency(requests);

        Map<Integer, CacheOutput> indexToCache = new HashMap<>();

        boolean isChanged = true;
        while(isChanged)
        {
            isChanged = iterateVideos(indexToCache, videoCache, videoToCacheTimes);
            System.out.println("Iteration");
        }
        System.out.println("Finish one");

        return indexToCache.values().stream().collect(Collectors.toList());
    }

    private Map<Video, Map<Integer, Integer>> calculateVideoSumToCacheLatency(List<Requests> requests)
    {
        Map<Video, Map<Integer, Integer>> videoToCacheTimes = new HashMap<>();
        for (Requests request : requests)
        {
            int numOfRequests = request.numOfRequests;
            Video video = request.video;
            Map<Integer, Integer> cacheToImprovement = videoToCacheTimes.computeIfAbsent(video, v -> new HashMap<>());
            Endpoint endpoint = request.endpoint;
            int datacenterLatency = endpoint.datacenterLatency;

            Map<Integer, Integer> cacheToDiffSum = endpoint.cacheToLatency.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (datacenterLatency - e.getValue()) * numOfRequests));

            cacheToDiffSum.entrySet().forEach(
                    e -> cacheToImprovement.put(e.getKey(),
                                                cacheToImprovement.getOrDefault(e.getKey(), 0) + e.getValue())
            );

        }
        return videoToCacheTimes;
    }

    private boolean iterateVideos(Map<Integer, CacheOutput> indexToCache,
                                         VideoCache videoCache,
                                         Map<Video, Map<Integer, Integer>> videoToCacheTimes)
    {
        boolean isChanged = false;

        for (Video video : videoCache.videos.values())
        {
            Map<Integer, Integer> cacheToSum = videoToCacheTimes.get(video);
            if (cacheToSum == null)
            {
                continue;
            }
            List<Map.Entry<Integer, Integer>> sortedEntries = cacheToSum.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue, Collections.reverseOrder()))
                    .collect(Collectors.toList());
            for (Map.Entry<Integer, Integer> sortedEntry : sortedEntries)
            {
                Integer cache = sortedEntry.getKey();
                CacheOutput cout = indexToCache.computeIfAbsent(cache,
                                                                c -> new CacheOutput(c, videoCache.cacheCapacity));
                if (!cout.isContained(video) &&
                        cout.capacity > video.size )
                {
                    cout.addVideo(video);
                    isChanged = true;
                    break;
                }
            }
        }
        return isChanged;
    }

}
