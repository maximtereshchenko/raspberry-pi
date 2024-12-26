package com.github.maximtereshchenko;

import com.diozero.api.DigitalInputDevice;
import com.diozero.api.GpioPullUpDown;
import com.diozero.devices.Button;

final class RotaryEncoder implements AutoCloseable {

    private final Button button;
    private final DigitalInputDevice rotation;
    private final DigitalInputDevice direction;

    RotaryEncoder(int button, int rotation, int direction) {
        this.button = new Button(button, GpioPullUpDown.PULL_UP);
        this.rotation = new DigitalInputDevice(rotation);
        this.direction = new DigitalInputDevice(direction);
    }

    @Override
    public void close() {
        button.close();
        rotation.close();
        direction.close();
    }

    void onButtonPress(Runnable runnable) {
        button.whenPressed(time -> runnable.run());
    }

    void onClockwiseRotation(Runnable runnable) {
        direction.whenActivated(time -> onDirection(runnable).run());
    }

    void onCounterclockwiseRotation(Runnable runnable) {
        direction.whenDeactivated(time -> onDirection(runnable).run());
    }

    private Runnable onDirection(Runnable runnable) {
        return () -> {
            if (rotation.isActive()) {
                runnable.run();
            }
        };
    }
}
