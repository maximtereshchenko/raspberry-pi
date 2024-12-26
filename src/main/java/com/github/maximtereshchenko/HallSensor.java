package com.github.maximtereshchenko;

import com.diozero.api.DigitalInputDevice;
import com.diozero.devices.LED;

import java.util.concurrent.TimeUnit;

final class HallSensor {

    public static void main(String[] args) throws InterruptedException {
        try (
            var sensor = new DigitalInputDevice(18);
            var led = new LED(17)
        ) {
            sensor.whenActivated(time -> led.on());
            sensor.whenDeactivated(time -> led.off());
            TimeUnit.SECONDS.sleep(30);
        }
    }
}