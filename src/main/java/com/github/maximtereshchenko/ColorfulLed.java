package com.github.maximtereshchenko;

import com.diozero.devices.RgbPwmLed;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

final class ColorfulLed {

    public static void main(String[] args) throws InterruptedException {
        try (var led = new RgbPwmLed(17, 18, 27)) {
            for (var i = 0; i < 10; i++) {
                led.setValues(dutyCycle(), dutyCycle(), dutyCycle());
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private static float dutyCycle() {
        return ThreadLocalRandom.current().nextFloat();
    }
}
