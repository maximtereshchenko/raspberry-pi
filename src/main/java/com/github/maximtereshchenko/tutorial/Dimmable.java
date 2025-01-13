package com.github.maximtereshchenko.tutorial;

import com.diozero.devices.PwmLed;
import com.github.maximtereshchenko.device.RotaryEncoder;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

final class Dimmable {

    public static void main(String[] args) throws InterruptedException {
        try (
            var encoder = new RotaryEncoder(27, 17, 18);
            var led = new PwmLed(22)
        ) {
            var counter = new AtomicInteger();
            encoder.onButtonPress(() -> counter.set(0));
            encoder.onClockwiseRotation(() -> counter.set(Math.min(counter.get() + 1, 100)));
            encoder.onCounterclockwiseRotation(() -> counter.set(Math.max(counter.get() - 1, 0)));
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                led.setValue(counter.get() / 100f);
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }
    }
}
