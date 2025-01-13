package com.github.maximtereshchenko.tutorial;

import com.diozero.devices.LED;

import java.util.concurrent.TimeUnit;

final class Blink {

    public static void main(String[] args) throws InterruptedException {
        try (var led = new LED(17)) {
            for (var i = 0; i < 10; i++) {
                led.toggle();
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
