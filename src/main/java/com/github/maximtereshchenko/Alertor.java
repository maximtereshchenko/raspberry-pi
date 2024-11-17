package com.github.maximtereshchenko;

import com.diozero.api.PwmOutputDevice;
import com.diozero.devices.Button;

import java.util.concurrent.TimeUnit;

final class Alertor {

    public static void main(String[] args) throws InterruptedException {
        try (
            var buzzer = new PwmOutputDevice(17);
            var button = new Button(18)
        ) {
            button.whenDeactivated(time -> buzzer.setValue(0.9f));
            button.whenActivated(time -> buzzer.setValue(0));
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
