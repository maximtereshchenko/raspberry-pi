package com.github.maximtereshchenko.tutorial;

import com.github.maximtereshchenko.device.Lcd1602;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

final class I2cLcd1602 {

    public static void main(String[] args) throws IOException, InterruptedException {
        try (var lcd = Lcd1602.newInstance()) {
            var deadline = Instant.now().plusSeconds(15);
            var formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            while (Instant.now().isBefore(deadline)) {
                lcd.write(Lcd1602.Position.FIRST_LINE, "CPU: %.2f C".formatted(cpuTemperature()));
                lcd.write(Lcd1602.Position.SECOND_LINE, formatter.format(LocalTime.now()));
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private static double cpuTemperature() throws IOException {
        return Integer.parseInt(Files.readString(Paths.get("/sys/class/thermal/thermal_zone0/temp")).trim()) / 1000d;
    }
}
