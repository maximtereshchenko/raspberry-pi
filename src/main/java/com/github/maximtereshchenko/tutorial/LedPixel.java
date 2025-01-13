package com.github.maximtereshchenko.tutorial;

import com.diozero.ws281xj.rpiws281x.WS281x;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

final class LedPixel {

    public static void main(String[] args) throws InterruptedException {
        try (var leds = new WS281x(18, 15, 8)) {
            var colors = List.of(Color.RED, Color.GREEN, Color.BLUE);
            for (var i = 0; i < 5; i++) {
                for (var color : colors) {
                    for (var pixel = 0; pixel < leds.getNumPixels(); pixel++) {
                        leds.setPixelColour(pixel, color.getRGB());
                        leds.render();
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                }
            }
        }
    }
}
