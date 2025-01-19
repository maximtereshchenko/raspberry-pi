package com.github.maximtereshchenko.airquality;

interface Display {

    void write(AirQuality airQuality) throws InterruptedException;

    void turnOff() throws InterruptedException;
}
