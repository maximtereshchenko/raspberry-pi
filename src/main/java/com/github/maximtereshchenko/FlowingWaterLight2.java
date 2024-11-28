package com.github.maximtereshchenko;

import com.diozero.api.DigitalOutputDevice;

import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;

final class FlowingWaterLight2 {

    public static void main(String[] args) throws InterruptedException {
        try (
            var latch = new DigitalOutputDevice(27);
            var clock = new DigitalOutputDevice(22);
            var data = new DigitalOutputDevice(17)
        ) {
            for (var i = 0; i < 10; i++) {
                flow(latch, clock, data, 128, value -> value >> 1);
                flow(latch, clock, data, 1, value -> value << 1);
            }
        }
    }

    private static void flow(DigitalOutputDevice latch, DigitalOutputDevice clock, DigitalOutputDevice data, int first, IntUnaryOperator next) throws InterruptedException {
        var current = first;
        for (var i = 0; i < 8; i++) {
            latch.off();
            inputBits(clock, data, current);
            latch.on();
            current = next.applyAsInt(current);
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    private static void inputBits(DigitalOutputDevice clock, DigitalOutputDevice data, int value) {
        int remaining = value;
        for (var i = 0; i < 8; i++) {
            clock.off();
            data.setOn((remaining & 1) == 1);
            clock.on();
            remaining = remaining >> 1;
        }
    }
}
