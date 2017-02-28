import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by amenahem on 2/25/17.
 */
public class Main
{
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException
    {
        ExecutorService executorService = Executors.newWorkStealingPool(1);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);

        completionService.submit(() -> {calculate("trending_today.in", "trending_today.out"); return null;});
//        completionService.submit(() -> {calculate("videos_worth_spreading.in", "videos_worth_spreading.out"); return null;});
//        completionService.submit(() -> {calculate("me_at_the_zoo.in", "me_at_the_zoo.out"); return null;});
//        completionService.submit(() -> {calculate("kittens.in", "output_kittens.out"); return null;});

        for (int i = 0; i < 1; i++)
        {
            Future<Void> take = completionService.take();
            take.get();
        }

        System.out.println("Finish all");
    }

    private static void calculate(String inputFileName, String outputFileName) throws IOException
    {
        System.out.println("Staring: " + inputFileName);
        long timeTook = System.currentTimeMillis();
        VideoCache videoCache = new VideoCache(inputFileName);

        Algo algo = new GreedyVideoCacheSelector(videoCache);

        List<CacheOutput> output = algo.calculate();
        outputToFile(output, outputFileName);
        long timeSaved = output.stream().map(c -> c.videos)
                .flatMap(Set::stream)
                .map(v -> v.requests)
                .flatMap(List::stream)
                .mapToLong(r -> r.timeSaved)
                .sum();
        System.out.println("Finish: " + inputFileName + " Saved: " + timeSaved + " took: " + (System.currentTimeMillis() - timeTook) / 1000.0);
    }

    public static void outputToFile(List<CacheOutput> outputs, String fileName) throws IOException
    {
        List<String> lines = new ArrayList<>();
        lines.add("" + outputs.size());
        outputs.stream().map(CacheOutput::toString).forEach(lines::add);
        Files.write(Paths.get(fileName), lines);
    }
}
