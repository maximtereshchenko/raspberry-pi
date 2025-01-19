package com.github.maximtereshchenko.airquality;

import com.github.maximtereshchenko.device.Lcd1602;

import java.time.format.DateTimeFormatter;

final class Lcd1602Display implements Display, AutoCloseable {

    private final Lcd1602 lcd1602 = Lcd1602.newInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    Lcd1602Display() throws InterruptedException {}

    @Override
    public void write(AirQuality airQuality) throws InterruptedException {
        lcd1602.switchDisplay(true);
        lcd1602.write(
            Lcd1602.Position.FIRST_LINE,
            "%s, %.0f PM2.5".formatted(
                formatter.format(airQuality.time()),
                airQuality.fineParticlesMatter25()
            )
        );
        lcd1602.write(
            Lcd1602.Position.SECOND_LINE,
            "%.0f N02, %.0f CO".formatted(airQuality.nitrogenDioxide(), airQuality.carbonMonoxide())
        );
    }

    @Override
    public void turnOff() throws InterruptedException {
        lcd1602.clearScreen();
        lcd1602.switchDisplay(false);
    }

    @Override
    public void close() throws InterruptedException {
        lcd1602.close();
    }
}
