package com.github.maximtereshchenko;

import com.diozero.devices.PwmLed;

import java.util.concurrent.TimeUnit;

final class BreathingLed {

    public static void main(String[] args) throws InterruptedException {
        try (var led = new PwmLed(18)) {
            for (var i = 0; i < 10; i++) {
                breathe(led, 0, 100, 1);
                breathe(led, 100, 0, -1);
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private static void breathe(PwmLed led, int from, int to, int step) throws InterruptedException {
        for (var i = from; i != to + step; i += step) {
            led.setValue((float) i / 100);
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }
}
