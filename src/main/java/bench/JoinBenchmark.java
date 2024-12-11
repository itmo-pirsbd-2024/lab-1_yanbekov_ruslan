package bench;

import JoinStrategy.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JoinBenchmark {

    @Param({"1000", "10000", "100000", "1000000", "10000000"})
    private int tableSize;

    private List<Row> table1;
    private List<Row> table2;

    @Setup
    public void setup() {
        table1 = new ArrayList<>(tableSize);
        table2 = new ArrayList<>(tableSize);
        Random random = new Random();

        for (int i = 0; i < tableSize; i++) {
            int id = random.nextInt(tableSize);
            String value = "Value1_" + i;
            table1.add(new Row(id, value));
        }

        for (int i = 0; i < tableSize; i++) {
            int id = random.nextInt(tableSize);
            String value = "Value2_" + i;
            table2.add(new Row(id, value));
        }
    }

    @Benchmark
    public List<Row> hashJoinBenchmark() {
        return HashJoin.hashJoin(table1, table2);
    }

    @Benchmark
    public List<Row> mergeJoinBenchmark() {
        return MergeJoin.mergeJoin(table1, table2);
    }

    @Benchmark
    public List<Row> mergeJoinBenchmarkIterativeBenchmark() {
        return MergeJoinIterative.mergeJoin(table1, table2);
    }

    @Benchmark
    public List<Row> nestedLoopJoinBenchmark() {
        return NestedLoopJoin.nestedLoopJoin(table1, table2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JoinBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
