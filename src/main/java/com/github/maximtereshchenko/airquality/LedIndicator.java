package com.github.maximtereshchenko.airquality;

import com.diozero.devices.LED;

final class LedIndicator implements Indicator, AutoCloseable {

    private final LED greenLed;
    private final LED yellowLed;
    private final LED redLed;

    LedIndicator(int greenPin, int yellowPin, int redPin) {
        this.greenLed = new LED(greenPin);
        this.yellowLed = new LED(yellowPin);
        this.redLed = new LED(redPin);
    }

    @Override
    public void turnOff() {
        greenLed.off();
        yellowLed.off();
        redLed.off();
    }

    @Override
    public void lightUp(Level level) {
        turnOff();
        switch (level) {
            case GOOD -> greenLed.on();
            case FAIR -> {
                greenLed.on();
                yellowLed.on();
            }
            case MODERATE -> yellowLed.on();
            case POOR -> {
                yellowLed.on();
                redLed.on();
            }
            case VERY_POOR -> redLed.on();
        }
    }

    @Override
    public void close() {
        greenLed.close();
        yellowLed.close();
        redLed.close();
    }
}
