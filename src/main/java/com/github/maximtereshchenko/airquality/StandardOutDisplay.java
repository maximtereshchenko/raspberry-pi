package com.github.maximtereshchenko.airquality;

final class StandardOutDisplay implements Display {

    @Override
    public void write(AirQuality airQuality) {
        System.out.println(airQuality);
    }

    @Override
    public void turnOff() {
        System.out.println("Turned off");
    }
}
