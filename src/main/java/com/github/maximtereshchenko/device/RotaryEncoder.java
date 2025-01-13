package com.github.maximtereshchenko.device;

import com.diozero.api.DigitalInputDevice;
import com.diozero.api.GpioPullUpDown;
import com.diozero.devices.Button;

public final class RotaryEncoder implements AutoCloseable {

    private final Button button;
    private final DigitalInputDevice rotation;
    private final DigitalInputDevice direction;

    public RotaryEncoder(int button, int rotation, int direction) {
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

    public void onButtonPress(Runnable runnable) {
        button.whenPressed(time -> runnable.run());
    }

    public void onClockwiseRotation(Runnable runnable) {
        direction.whenActivated(time -> onDirection(runnable).run());
    }

    public void onCounterclockwiseRotation(Runnable runnable) {
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
