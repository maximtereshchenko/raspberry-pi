package com.github.maximtereshchenko.tutorial;

import com.diozero.devices.HCSR04;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class UltrasonicRanging {

    public static void main(String[] args) throws InterruptedException {
        try (var sensor = new HCSR04(23, 24)) {
            var deadline = Instant.now().plusSeconds(15);
            while (Instant.now().isBefore(deadline)) {
                System.out.printf("%2f cm%n", sensor.getDistanceCm());
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }
    }
}
