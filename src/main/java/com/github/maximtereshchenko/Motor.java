package com.github.maximtereshchenko;

import com.diozero.api.DigitalOutputDevice;
import com.diozero.api.PwmOutputDevice;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class Motor {

    public static void main(String[] args) throws InterruptedException {
        var adc = new ADS7830Device();
        try (
            var first = new DigitalOutputDevice(27);
            var second = new DigitalOutputDevice(17);
            var speedPwm = new PwmOutputDevice(22, 1000, 0)
        ) {
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                var max = 128f;
                var value = adc.analogRead(0) - max;
                if (value >= 0) {
                    first.on();
                    second.off();
                } else {
                    first.off();
                    second.on();
                }
                speedPwm.setValue(Math.abs(value) / max);
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }
    }
}
