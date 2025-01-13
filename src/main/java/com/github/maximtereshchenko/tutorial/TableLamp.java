package com.github.maximtereshchenko.tutorial;

import com.diozero.devices.Button;
import com.diozero.devices.LED;

import java.util.concurrent.TimeUnit;

final class TableLamp {

    public static void main(String[] args) throws InterruptedException {
        try (var led = new LED(17); var button = new Button(18)) {
            button.whenPressed(time -> led.toggle());
            TimeUnit.SECONDS.sleep(30);
        }
    }
}
