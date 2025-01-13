package com.github.maximtereshchenko.tutorial;

import com.github.maximtereshchenko.device.Lcd1602;
import com.github.maximtereshchenko.device.Leds;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final class MerryChristmas {

    public static void main(String[] args) throws InterruptedException {
        try (
            var leds = Leds.from(17, 27, 22, 10, 9, 11, 0, 5, 6, 13, 19, 26);
            var lcd = Lcd1602.newInstance();
            var scheduler = Executors.newScheduledThreadPool(0)
        ) {
            scheduler.submit(() -> {
                while (true) {
                    try {
                        leds.lightUp(true);
                        TimeUnit.MILLISECONDS.sleep(500);
                        leds.lightUp(false);
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            var deadline = Instant.now().plus(5, ChronoUnit.MINUTES);
            while (Instant.now().isBefore(deadline)) {
                lcd.write(Lcd1602.Position.FIRST_LINE, centered("MERRY"));
                lcd.write(Lcd1602.Position.SECOND_LINE, centered("CHRISTMAS!"));
                TimeUnit.SECONDS.sleep(5);
                lcd.clearScreen();
                hoHo(lcd, Lcd1602.Position.FIRST_LINE);
                hoHo(lcd, Lcd1602.Position.SECOND_LINE);
                TimeUnit.SECONDS.sleep(2);
            }
            scheduler.shutdownNow();
        }
    }

    private static void hoHo(Lcd1602 lcd, Lcd1602.Position position) throws InterruptedException {
        lcd.write(position, centered("HO"));
        TimeUnit.SECONDS.sleep(1);
        lcd.write(position, centered("HO HO"));
        TimeUnit.SECONDS.sleep(1);
    }

    private static String centered(String original) {
        return " ".repeat((16 - original.length()) / 2) + original;
    }
}
