package com.github.maximtereshchenko;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class Mpu6050Measurements {

    public static void main(String[] args) throws InterruptedException {
        var mpu6050 = Mpu6050.newInstance();
        var deadline = Instant.now().plusSeconds(30);
        while (Instant.now().isBefore(deadline)) {
            System.out.printf(
                "%.2f C%nAccelerometer(g) %d X, %d Y, %d Z%nGyroscope(o/s) %d X, %d Y, %d Z%n",
                mpu6050.temperatureCelsius(),
                mpu6050.accelerometerValue(Mpu6050.Axis.X),
                mpu6050.accelerometerValue(Mpu6050.Axis.Y),
                mpu6050.accelerometerValue(Mpu6050.Axis.Z),
                mpu6050.gyroscopeValue(Mpu6050.Axis.X),
                mpu6050.gyroscopeValue(Mpu6050.Axis.Y),
                mpu6050.gyroscopeValue(Mpu6050.Axis.Z)
            );
            TimeUnit.MILLISECONDS.sleep(200);
        }
    }
}
