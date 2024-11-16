package com.github.maximtereshchenko;

import com.diozero.devices.LED;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

final class FlowingWaterLight {

    public static void main(String[] args) throws InterruptedException {
        try (var leds = Leds.from(17, 18, 27, 22, 23, 24, 25, 2, 3, 8)) {
            for (var i = 0; i < 10; i++) {
                leds.flow();
            }
        }
    }

    private static final class Leds implements AutoCloseable {

        private List<LED> all;

        private Leds(List<LED> all) {
            this.all = all;
        }

        static Leds from(Integer... pins) {
            return new Leds(
                Stream.of(pins)
                    .map(LED::new)
                    .toList()
            );
        }

        @Override
        public void close() {
            all.forEach(LED::close);
        }

        void flow() throws InterruptedException {
            for (var led : all) {
                led.on();
                TimeUnit.MILLISECONDS.sleep(100);
                led.off();
            }
            all = all.reversed();
        }
    }
}
