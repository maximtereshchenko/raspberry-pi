package com.github.maximtereshchenko;

import com.diozero.api.DigitalOutputDevice;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

final class StopWatch {

    public static void main(String[] args) {
        try (
            var chip = new Chip74HC595(23, 18, 24);
            var scheduler = Executors.newSingleThreadScheduledExecutor()
        ) {
            var counter = new AtomicInteger();
            scheduler.scheduleAtFixedRate(
                counter::incrementAndGet,
                0,
                1,
                TimeUnit.SECONDS
            );
            var deadline = Instant.now().plusSeconds(30);
            var display = new Display(chip);
            while (Instant.now().isBefore(deadline)) {
                display.output(counter.get());
            }
        }
    }

    private static final class Display {

        private final List<DigitalOutputDevice> digits;
        private final Chip74HC595 chip;

        Display(Chip74HC595 chip) {
            this.digits = Stream.of(17, 27, 22, 10)
                .map(DigitalOutputDevice::new)
                .toList();
            this.chip = chip;
        }

        void output(int value) {
            try {
                for (var i = 0; i < digits.size(); i++) {
                    chip.output(bitMask(Character.digit("%04d".formatted(value).charAt(i), 10)));
                    var digit = digits.get(i);
                    digit.on();
                    TimeUnit.MILLISECONDS.sleep(3);
                    digit.off();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private int bitMask(int value) {
            return switch (value) {
                case 0 -> 0b00000010;
                case 1 -> 0b10011110;
                case 2 -> 0b00100100;
                case 3 -> 0b00001100;
                case 4 -> 0b10011000;
                case 5 -> 0b01001000;
                case 6 -> 0b11000000;
                case 7 -> 0b00011110;
                case 8 -> 0b00000000;
                case 9 -> 0b00011000;
                default -> throw new IllegalArgumentException(String.valueOf(value));
            };
        }
    }
}
