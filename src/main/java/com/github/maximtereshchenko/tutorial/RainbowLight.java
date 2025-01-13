package com.github.maximtereshchenko.tutorial;

import com.diozero.ws281xj.rpiws281x.WS281x;
import com.github.maximtereshchenko.device.ADS7830Device;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class RainbowLight {

    public static void main(String[] args) throws InterruptedException {
        var adc = new ADS7830Device();
        try (var leds = new WS281x(18, 15, 8)) {
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                for (var pixel = 0; pixel < leds.getNumPixels(); pixel++) {
                    var read = adc.analogRead(2);
                    var value = Math.round(read / 255f * 360 + pixel * 45) % 360;
                    leds.setPixelColour(pixel, color(value).getRGB());
                    leds.render();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }
    }

    private static Color color(int initial) {
        var value = initial / 360f * 255;
        if (value < 85) {
            var temp = (int) (value * 3);
            return new Color(255 - temp, temp, 0);
        }
        if (value < 170) {
            value = value - 85;
            var temp = (int) (value * 3);
            return new Color(0, 255 - temp, temp);
        }
        value = value - 170;
        var temp = (int) (value * 3);
        return new Color(temp, 0, 255 - temp);
    }
}
