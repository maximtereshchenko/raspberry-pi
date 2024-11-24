package com.github.maximtereshchenko;

import com.diozero.api.DigitalOutputDevice;
import com.diozero.devices.sandpit.motor.BasicStepperController;
import com.diozero.devices.sandpit.motor.BasicStepperMotor;
import com.diozero.devices.sandpit.motor.StepperMotorInterface;

import java.util.concurrent.TimeUnit;

final class SteppingMotor {

    public static void main(String[] args) throws InterruptedException {
        try (
            var motor = new BasicStepperMotor(
                new BasicStepperController.UnipolarBasicController(
                    new BasicStepperController.StepperPin[]{
                        new BasicStepperController.GpioStepperPin(new DigitalOutputDevice(18)),
                        new BasicStepperController.GpioStepperPin(new DigitalOutputDevice(23)),
                        new BasicStepperController.GpioStepperPin(new DigitalOutputDevice(24)),
                        new BasicStepperController.GpioStepperPin(new DigitalOutputDevice(25))
                    }
                )
            )
        ) {
            turn(motor, StepperMotorInterface.Direction.FORWARD);
            turn(motor, StepperMotorInterface.Direction.BACKWARD);
        }
    }

    private static void turn(BasicStepperMotor motor, StepperMotorInterface.Direction direction) throws InterruptedException {
        for (var i = 0; i < 2048; i++) {
            motor.step(direction);
            TimeUnit.MILLISECONDS.sleep(3);
        }
    }
}
