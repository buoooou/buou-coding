package com.github.buoooou.test.javabetter.exception1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 3/26/23
 */
public class TryCatchTest1 {
    @Benchmark
    public void fornotry(Blackhole blackhole) {
        for (int i = 0; i < 1000; i++) {
            blackhole.consume(i);
        }
    }

    @Benchmark
    public void tryfor(Blackhole blackhole) {
        for (int i = 0; i < 1000; i++) {
            try {
                blackhole.consume(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
