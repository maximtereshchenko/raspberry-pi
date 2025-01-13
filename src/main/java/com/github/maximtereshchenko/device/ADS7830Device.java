package com.github.maximtereshchenko.device;

import com.diozero.api.I2CConstants;
import com.diozero.internal.provider.builtin.i2c.NativeI2CDeviceSMBus;
import com.diozero.sbc.DeviceFactoryHelper;

public final class ADS7830Device {

    private final NativeI2CDeviceSMBus bus = new NativeI2CDeviceSMBus(
        DeviceFactoryHelper.getNativeDeviceFactory(),
        "ADS7830",
        I2CConstants.CONTROLLER_1,
        0x4b,
        I2CConstants.AddressSize.SIZE_7,
        false
    );

    public int analogRead(int inputPin) {
        return bus.readByteData(0x84 | (((inputPin << 2 | inputPin >> 1) & 0x07) << 4)) & 0xFF;
    }
}
