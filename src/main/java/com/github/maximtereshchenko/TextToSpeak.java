package com.github.maximtereshchenko;

import com.diozero.api.DigitalInputDevice;
import com.diozero.api.GpioEventTrigger;
import com.diozero.api.GpioPullUpDown;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class TextToSpeak {

    public static void main(String[] args) throws InterruptedException {
        try (var sensor = new DigitalInputDevice(18, GpioPullUpDown.PULL_UP, GpioEventTrigger.NONE)) {
            sensor.whenActivated(time -> speak());
            var deadline = Instant.now().plusSeconds(20);
            while (Instant.now().isBefore(deadline)) {
                if (sensor.isActive()) {
                    speak();
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            }
        }
    }

    private static void speak() {
        try {
            new ProcessBuilder("espeak", "Please, stay away!").start().waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
