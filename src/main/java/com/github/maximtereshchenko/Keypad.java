package com.github.maximtereshchenko;

import com.diozero.api.DigitalInputDevice;
import com.diozero.api.DigitalOutputDevice;
import com.diozero.api.GpioEventTrigger;
import com.diozero.api.GpioPullUpDown;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

final class Keypad implements AutoCloseable {

    private static final int SIZE = 4;
    private final List<DigitalInputDevice> rows;
    private final List<DigitalOutputDevice> columns;

    private Keypad(List<DigitalInputDevice> rows, List<DigitalOutputDevice> columns) {
        this.rows = rows;
        this.columns = columns;
    }

    static Keypad from(List<Integer> rowPins, List<Integer> columnPins) {
        return new Keypad(
            mapped(rowPins, pin -> new DigitalInputDevice(pin, GpioPullUpDown.PULL_DOWN, GpioEventTrigger.NONE)),
            mapped(columnPins, DigitalOutputDevice::new)
        );
    }

    private static <T> List<T> mapped(List<Integer> pins, Function<Integer, T> mapper) {
        if (pins.size() != SIZE) {
            throw new IllegalArgumentException(String.valueOf(pins.size()));
        }
        return pins.stream()
            .map(mapper)
            .toList();
    }

    @Override
    public void close() {
        rows.forEach(DigitalInputDevice::close);
        columns.forEach(DigitalOutputDevice::close);
    }

    Optional<Key> pressed() {
        for (var column = 0; column < columns.size(); column++) {
            var output = columns.get(column);
            output.on();
            for (var row = 0; row < rows.size(); row++) {
                if (rows.get(row).isActive()) {
                    output.off();
                    return Optional.of(Key.from(row, column));
                }
            }
            output.off();
        }
        return Optional.empty();
    }

    enum Key {
        ONE, TWO, THREE, A,
        FOUR, FIVE, SIX, B,
        SEVEN, EIGHT, NINE, C,
        STAR, ZERO, HASH, D;

        static Key from(int row, int column) {
            return Key.values()[row * SIZE + column];
        }
    }
}
