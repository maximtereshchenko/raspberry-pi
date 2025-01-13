package com.github.maximtereshchenko.tutorial;

import com.diozero.api.DigitalInputDevice;
import com.diozero.api.DigitalOutputDevice;
import com.diozero.devices.Buzzer;
import com.diozero.devices.LED;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final class HallSensorAlertor {

    public static void main(String[] args) throws InterruptedException {
        try (
            var sensor = new DigitalInputDevice(18);
            var led = new LED(27);
            var buzzer = new Buzzer(17);
            var executor = Executors.newVirtualThreadPerTaskExecutor()
        ) {
            sensor.whenDeactivated(time -> {
                var latch = new CountDownLatch(1);
                executor.submit(() -> blink(latch, led));
                executor.submit(() -> blink(latch, buzzer));
                latch.countDown();
            });
            TimeUnit.SECONDS.sleep(20);
        }
    }

    private static void blink(CountDownLatch latch, DigitalOutputDevice device) {
        try {
            latch.await();
            for (var i = 0; i < 6; i++) {
                device.toggle();
                TimeUnit.MILLISECONDS.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
