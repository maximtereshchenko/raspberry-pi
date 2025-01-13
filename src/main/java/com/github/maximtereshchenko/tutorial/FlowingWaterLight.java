package com.github.maximtereshchenko.tutorial;

import com.github.maximtereshchenko.device.Leds;

final class FlowingWaterLight {

    public static void main(String[] args) throws InterruptedException {
        try (var leds = Leds.from(17, 18, 27, 22, 23, 24, 25, 2, 3, 8)) {
            for (var i = 0; i < 10; i++) {
                leds.flow();
            }
        }
    }
}
