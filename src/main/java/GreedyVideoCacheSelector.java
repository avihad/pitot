import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by amenahem on 2/26/17.
 */
public class GreedyVideoCacheSelector implements Algo
{

    public PriorityQueue<VideoCacheLatency> videoQueue;

    private VideoCache videoCache;

    public Map<Video, Set<VideoCacheLatency>> videoToCacheLatencies;

    public Map<Integer, CacheOutput> indexToCache;

    public GreedyVideoCacheSelector(VideoCache videoCache)
    {
        this.videoCache = videoCache;
        videoToCacheLatencies = new HashMap<>();
        indexToCache = new HashMap<>();

        videoQueue = new PriorityQueue<>(
                Comparator.comparing((VideoCacheLatency v) -> v.latencyToSize, Comparator.reverseOrder())
                        .thenComparing(v -> v.cache)
        );
    }

    @Override
    public List<CacheOutput> calculate()
    {

        for (Video video : videoCache.videos.values())
        {
            Set<VideoCacheLatency> videoCacheLatencies = calculateCacheLatencies(video);

            this.videoToCacheLatencies.put(video, videoCacheLatencies);
            this.videoQueue.addAll(videoCacheLatencies);

        }

        boolean isChanged = true;

        while (isChanged)
        {
            isChanged = iterate();
        }

        return indexToCache.values().stream().collect(Collectors.toList());
    }

    private boolean iterate()
    {
        int cacheCapacity = videoCache.cacheCapacity;

        while (this.videoQueue.size() > 0)
        {
            VideoCacheLatency cacheLatency = extractBestVideoCache();
            Video video = cacheLatency.video;
            int cache = cacheLatency.cache;

            CacheOutput cacheOut = indexToCache.computeIfAbsent(cache, c -> new CacheOutput(cache, cacheCapacity));
            if (cacheOut.capacity >= video.size && !cacheOut.isContained(video))
            {
                cacheOut.addVideo(video);
                Set<VideoCacheLatency> oldCacheLatencies = videoToCacheLatencies.get(video);
                video.updateRequests(cache);
                System.out.println("video assigned (" + video.index + " => " + cache + ") rel " + cacheLatency.latencyToSize + " saving " + cacheLatency.latency);
                Set<VideoCacheLatency> newCacheLatencies = calculateCacheLatencies(video);
                this.videoQueue.removeAll(oldCacheLatencies);
                this.videoQueue.addAll(newCacheLatencies);
                this.videoToCacheLatencies.put(video, newCacheLatencies);
                return true;
            }
        }
        return false;
    }

    private VideoCacheLatency extractBestVideoCache()
    {
        VideoCacheLatency candidate = this.videoQueue.poll();
        /*int candidateCapacity = this.indexToCache
                .computeIfAbsent(candidate.cache,
                                 c -> new CacheOutput(c, this.videoCache.cacheCapacity)).capacity;
        while (!this.videoQueue.isEmpty() && this.videoQueue.peek().video.equals(candidate.video))
        {
            VideoCacheLatency nextValue = this.videoQueue.poll();
            int nexyCapacity = this.indexToCache
                    .computeIfAbsent(nextValue.cache,
                                     c -> new CacheOutput(c, this.videoCache.cacheCapacity)).capacity;
            if (candidate.latencyToSize == nextValue.latencyToSize &&
                    candidateCapacity < nexyCapacity)
            {
                candidate = nextValue;
                candidateCapacity = nexyCapacity;
            }
        }*/
        return candidate;
    }

    private Set<VideoCacheLatency> calculateCacheLatencies(Video video)
    {
        Map<Integer, Integer> cacheToImprovement = new HashMap<>();
        video.requests.forEach(request ->
                               {
                                   int numOfRequests = request.numOfRequests;
                                   int timeSaved = request.timeSaved;
                                   Endpoint endpoint = request.endpoint;
                                   int datacenterLatency = endpoint.datacenterLatency;
                                   Map<Integer, Integer> cacheToDiffSum = endpoint.cacheToLatency.entrySet()
                                           .stream()
                                           .collect(Collectors.toMap(Map.Entry::getKey, e ->
                                                   Math.max(0, ((datacenterLatency - e.getValue()) * numOfRequests) - timeSaved)));

                                   cacheToDiffSum.entrySet().forEach(
                                           e -> cacheToImprovement.put(e.getKey(),
                                                                       cacheToImprovement.getOrDefault(e.getKey(), 0) + e.getValue()));
                               });

        return cacheToImprovement.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(e -> new VideoCacheLatency(video, e.getKey(), e.getValue()))
                .collect(Collectors.toSet());
    }


    static class VideoCacheLatency
    {
        Video video;
        int cache;
        double latencyToSize;
        int latency;

        public VideoCacheLatency(Video video, int cache, int latency)
        {
            this.video = video;
            this.cache = cache;
            this.latency = latency;
            this.latencyToSize = latency / (1.0 * video.size);
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

            VideoCacheLatency that = (VideoCacheLatency) o;

            if (cache != that.cache)
            {
                return false;
            }
            return video != null ? video.equals(that.video) : that.video == null;
        }

        @Override
        public int hashCode()
        {
            int result = video != null ? video.hashCode() : 0;
            result = 31 * result + cache;
            return result;
        }
    }
}
