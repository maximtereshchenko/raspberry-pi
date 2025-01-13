package com.github.maximtereshchenko.tutorial;

import com.diozero.api.ServoDevice;
import com.diozero.api.ServoTrim;

import java.util.concurrent.TimeUnit;

final class Sweep {

    public static void main(String[] args) throws InterruptedException {
        try (
            var servo = ServoDevice.Builder.builder(18)
                .setTrim(new ServoTrim(500, 2500, 180L))
                .build()
        ) {
            for (var i = 0; i < 10; i++) {
                servo.max();
                TimeUnit.MILLISECONDS.sleep(400);
                servo.min();
                TimeUnit.MILLISECONDS.sleep(400);
            }
        }
    }
}
