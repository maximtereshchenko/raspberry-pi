package com.github.maximtereshchenko;

import com.diozero.api.GpioPullUpDown;
import com.diozero.api.SmoothedInputDevice;
import com.diozero.devices.PwmLed;
import com.diozero.util.DiozeroScheduler;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class TouchSensor {

    public static void main(String[] args) throws InterruptedException {
        try (
            var touchSensor = new SmoothedInputDevice(18, GpioPullUpDown.PULL_DOWN, 5, 10, 20);
            var led = new PwmLed(17)
        ) {
            var counter = 1;
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                if (touchSensor.isActive()) {
                    if (counter > 3) {
                        counter = 0;
                    }
                    led.setValue(1f / 3 * counter++);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        } finally {
            DiozeroScheduler.shutdownAll();
        }
    }
}
