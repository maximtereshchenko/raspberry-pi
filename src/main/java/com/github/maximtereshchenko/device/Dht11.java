package com.github.maximtereshchenko.device;

import com.diozero.api.DigitalInputDevice;
import com.diozero.api.DigitalOutputDevice;
import com.diozero.api.GpioEventTrigger;
import com.diozero.api.GpioPullUpDown;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class Dht11 {

    private final int pin;

    public Dht11(int pin) {
        this.pin = pin;
    }

    public Measurement measurement() throws InterruptedException {
        while (true) {
            var measurement = timedResponses()
                .map(TimedResponses::data)
                .filter(Data::hasValidChecksum)
                .map(Data::measurement);
            if (measurement.isEmpty()) {
                continue;
            }
            return measurement.get();
        }
    }

    private Optional<TimedResponses> timedResponses() throws InterruptedException {
        sendStartSignal();
        return TimedResponses.from(durations());
    }

    private List<Duration> durations() {
        try (var input = new DigitalInputDevice(pin, GpioPullUpDown.PULL_DOWN, GpioEventTrigger.NONE)) {
            var last = input.isActive();
            var durations = new ArrayList<Duration>();
            var start = Instant.now();
            while (durations.size() < TimedResponses.MAX_SIZE) {
                var current = input.isActive();
                var now = Instant.now();
                var duration = Duration.between(start, now);
                if (duration.compareTo(Duration.ofMillis(1)) >= 0) {
                    break;
                }
                if (current != last) {
                    if (current) {
                        start = now;
                    } else {
                        durations.add(duration);
                    }
                    last = current;
                }
            }
            return durations;
        }
    }

    private void sendStartSignal() throws InterruptedException {
        try (var output = new DigitalOutputDevice(pin)) {
            output.off();
            TimeUnit.MILLISECONDS.sleep(18);
        }
    }

    public record Measurement(double humidity, double temperature) {}

    private static final class TimedResponses {

        static final int MAX_SIZE = Data.SIZE + 1;
        private final List<Duration> durations;

        private TimedResponses(List<Duration> durations) {
            this.durations = durations;
        }

        static Optional<TimedResponses> from(List<Duration> durations) {
            return switch (durations.size()) {
                case MAX_SIZE -> from(durations.subList(1, durations.size()));
                case Data.SIZE -> Optional.of(new TimedResponses(durations));
                default -> Optional.empty();
            };
        }

        Data data() {
            var bits = new BitSet();
            for (var i = 0; i < durations.size(); i++) {
                if (durations.get(i).compareTo(Duration.ofNanos(30000)) > 0) {
                    bits.set(i);
                }
            }
            return Data.from(bits);
        }
    }

    private static final class Data {

        static final int SIZE = 40;
        private final List<Integer> bytes;
        private final int checksum;

        private Data(List<Integer> bytes, int checksum) {
            this.bytes = bytes;
            this.checksum = checksum;
        }

        static Data from(BitSet bits) {
            return new Data(
                List.of(
                    integer(bits, 0),
                    integer(bits, 8),
                    integer(bits, 16),
                    integer(bits, 24)
                ),
                integer(bits, 32)
            );
        }

        private static int integer(BitSet bits, int start) {
            int result = 0;
            for (var i = 0; i < 8; i++) {
                if (bits.get(i + start)) {
                    result |= 1 << 7 - i;
                }
            }
            return result;
        }

        boolean hasValidChecksum() {
            return bytes.stream()
                       .mapToInt(x -> x)
                       .sum() == checksum;
        }

        Measurement measurement() {
            return new Measurement(value(0), value(2));
        }

        private double value(int index) {
            return bytes.get(index) + 0.1 * bytes.get(index + 1);
        }
    }
}