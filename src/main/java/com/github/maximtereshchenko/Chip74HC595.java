package com.github.maximtereshchenko;

import com.diozero.api.DigitalOutputDevice;

final class Chip74HC595 implements AutoCloseable {

    private final DigitalOutputDevice latch;
    private final DigitalOutputDevice clock;
    private final DigitalOutputDevice data;

    Chip74HC595(int latch, int clock, int data) {
        this.latch = new DigitalOutputDevice(latch);
        this.clock = new DigitalOutputDevice(clock);
        this.data = new DigitalOutputDevice(data);
    }

    @Override
    public void close() {
        latch.close();
        clock.close();
        data.close();
    }

    void shift(int bitMask) {
        latch.off();
        int remaining = bitMask;
        for (var i = 0; i < 8; i++) {
            clock.off();
            data.setOn((remaining & 1) == 1);
            clock.on();
            remaining = remaining >> 1;
        }
    }

    void output() {
        latch.on();
    }
}
