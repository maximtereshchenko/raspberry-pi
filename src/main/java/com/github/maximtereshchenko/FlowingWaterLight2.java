package com.github.maximtereshchenko;

import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;

final class FlowingWaterLight2 {

    public static void main(String[] args) throws InterruptedException {
        try (var chip = new Chip74HC595(27, 22, 17)) {
            for (var i = 0; i < 10; i++) {
                flow(chip, 128, value -> value >> 1);
                flow(chip, 1, value -> value << 1);
            }
        }
    }

    private static void flow(Chip74HC595 chip, int first, IntUnaryOperator next) throws InterruptedException {
        var current = first;
        for (var i = 0; i < 8; i++) {
            chip.shift(current);
            chip.output();
            TimeUnit.MILLISECONDS.sleep(100);
            current = next.applyAsInt(current);
        }
    }
}
