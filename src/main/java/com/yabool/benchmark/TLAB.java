package com.yabool.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Warmup(iterations = 5)
//@Fork(value = 1, jvmArgsAppend = {"-XX:-UseTLAB"})
@Fork(value = 1, jvmArgsAppend = {"-XX:+UseTLAB"})
@Measurement(iterations = 10)
@State(Scope.Thread)
@Threads(16)
public class TLAB {
    private static final int PRIME = 179426491;

    private byte[] arr;


    @Benchmark
    public byte[] test(Data data) {
        byte[] arr = new byte[data.size];
        data.size = (data.size + PRIME) % data.maxSize;
        return arr;
    }

    @State(Scope.Benchmark)
    public static class Data {

        @Param({"8", "32", "128", "256", "512", "1024", "4096", "32768", "131072", "524288", "1048576"})
        private int maxSize;

        private int size;

        @Setup
        public void setup() {
            size = (size + PRIME) % maxSize;
        }
    }

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + TLAB.class.getSimpleName() + ".*")
//                .addProfiler(HotspotMemoryProfiler.class)
                .build();
        new Runner(opt).run();
    }
}
