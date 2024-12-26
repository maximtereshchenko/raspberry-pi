package com.github.maximtereshchenko;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

final class RotaryEncoderCounter {

    public static void main(String[] args) {
        try (var encoder = new RotaryEncoder(27, 17, 18)) {
            var counter = new AtomicInteger();
            encoder.onButtonPress(() -> counter.set(0));
            encoder.onClockwiseRotation(counter::incrementAndGet);
            encoder.onCounterclockwiseRotation(counter::decrementAndGet);
            var deadline = Instant.now().plusSeconds(30);
            var last = 0;
            while (Instant.now().isBefore(deadline)) {
                var current = counter.get();
                if (current != last) {
                    System.out.println(current);
                    last = current;
                }
            }
        }
    }
}
