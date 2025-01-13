package com.github.maximtereshchenko.tutorial;

import com.diozero.devices.Button;
import com.diozero.devices.Buzzer;

import java.util.concurrent.TimeUnit;

final class Doorbell {

    public static void main(String[] args) throws InterruptedException {
        try (
            var buzzer = new Buzzer(17);
            var button = new Button(18)
        ) {
            button.whenDeactivated(time -> buzzer.on());
            button.whenActivated(time -> buzzer.off());
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
