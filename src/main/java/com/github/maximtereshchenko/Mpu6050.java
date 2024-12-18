package com.github.maximtereshchenko;

import com.diozero.api.I2CConstants;
import com.diozero.internal.provider.builtin.i2c.NativeI2CDeviceSMBus;
import com.diozero.sbc.DeviceFactoryHelper;

import java.util.BitSet;
import java.util.concurrent.TimeUnit;

final class Mpu6050 {

    private final NativeI2CDeviceSMBus bus = new NativeI2CDeviceSMBus(
        DeviceFactoryHelper.getNativeDeviceFactory(),
        "MPU6050",
        I2CConstants.CONTROLLER_1,
        0x68,
        I2CConstants.AddressSize.SIZE_7,
        false
    );

    static Mpu6050 newInstance() throws InterruptedException {
        var mpu6050 = new Mpu6050();
        mpu6050.reset();
        TimeUnit.MILLISECONDS.sleep(1);
        mpu6050.selectInternalOscillatorClockSource();
        mpu6050.setGyroscopeFullScaleRange250();
        mpu6050.setAccelerometerFullScaleRange2g();
        mpu6050.wakeUp();
        TimeUnit.MILLISECONDS.sleep(1);
        return mpu6050;
    }

    double temperatureCelsius() {
        return readInteger(0x41) / 340d + 36.53;
    }

    int accelerometerValue(Axis axis) {
        return readInteger(0x3B, axis) / 16384;
    }

    int gyroscopeValue(Axis axis) {
        return readInteger(0x43, axis) / 131;
    }

    private int readInteger(int xAxisHighBitsRegister, Axis axis) {
        return readInteger(xAxisHighBitsRegister + axis.ordinal() * 2);
    }

    private void write(int register, int data, int from, int to) {
        var bitSet = read(register);
        bitSet.clear(from, to);
        bitSet(data)
            .stream()
            .filter(index -> index < to - from)
            .forEach(index -> bitSet.set(index + from));
        bus.writeByteData(register, toByte(bitSet));
    }

    private void setGyroscopeFullScaleRange250() {
        write(0x1B, 0, 3, 5);
    }

    private void setAccelerometerFullScaleRange2g() {
        write(0x1C, 0, 3, 5);
    }

    private void selectInternalOscillatorClockSource() {
        writeToPowerManagement1(0, 0, 3);
    }

    private void reset() {
        writeToPowerManagement1(1, 7, 8);
    }

    private void wakeUp() {
        writeToPowerManagement1(0, 6, 7);
    }

    private void writeToPowerManagement1(int data, int from, int to) {
        write(0x6B, data, from, to);
    }

    private int readInteger(int highBitsRegister) {
        var bitSet = read(highBitsRegister + 1);
        read(highBitsRegister)
            .stream()
            .map(index -> index + 8)
            .forEach(bitSet::set);
        var array = bitSet.toLongArray();
        if (array.length == 0) {
            return 0;
        }
        return (int) array[0];
    }

    private BitSet read(int register) {
        return bitSet(bus.readByteData(register));
    }

    private BitSet bitSet(int value) {
        return BitSet.valueOf(new byte[]{(byte) (value & 0xFF)});
    }

    private byte toByte(BitSet bitSet) {
        var array = bitSet.toByteArray();
        if (array.length == 0) {
            return 0;
        }
        return array[0];
    }

    enum Axis {
        X, Y, Z
    }
}
