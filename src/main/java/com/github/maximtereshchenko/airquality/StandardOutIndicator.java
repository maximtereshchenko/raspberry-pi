package com.github.maximtereshchenko.airquality;

final class StandardOutIndicator implements Indicator {

    private final String name;

    StandardOutIndicator(String name) {
        this.name = name;
    }

    @Override
    public void turnOff() {
        System.out.println(name + " turned off");
    }

    @Override
    public void lightUp(Level level) {
        System.out.println(name + " " + level);
    }
}
