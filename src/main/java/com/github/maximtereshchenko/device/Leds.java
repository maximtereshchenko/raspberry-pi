package com.github.maximtereshchenko.device;

import com.diozero.devices.LED;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class Leds implements AutoCloseable {

    private List<LED> all;

    private Leds(List<LED> all) {
        this.all = all;
    }

    public static Leds from(Integer... pins) {
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

    public void flow() throws InterruptedException {
        for (var led : all) {
            led.on();
            TimeUnit.MILLISECONDS.sleep(100);
            led.off();
        }
        all = all.reversed();
    }

    public void lightUp(boolean isEven) {
        for (var i = 0; i < all.size(); i++) {
            var isEvenIndex = (i + 1) % 2 == 0;
            all.get(i).setOn(isEvenIndex == isEven);
        }
    }
}
