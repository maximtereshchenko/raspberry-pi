package com.github.maximtereshchenko.tutorial;

import com.diozero.api.GpioPullUpDown;
import com.diozero.api.SmoothedInputDevice;
import com.diozero.devices.LED;
import com.diozero.util.DiozeroScheduler;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class VoiceLamp {

    public static void main(String[] args) throws InterruptedException {
        try (
            var microphone = new SmoothedInputDevice(18, GpioPullUpDown.PULL_DOWN, 5, 10, 20);
            var led = new LED(17)
        ) {
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                if (microphone.isActive()) {
                    led.on();
                    TimeUnit.MILLISECONDS.sleep(500);
                } else {
                    led.off();
                }
            }
        } finally {
            DiozeroScheduler.shutdownAll();
        }
    }
}
