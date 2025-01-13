package com.github.maximtereshchenko.tutorial;

import com.github.maximtereshchenko.device.Dht11;

import java.util.concurrent.TimeUnit;

final class Dht11Measurements {

    public static void main(String[] args) throws InterruptedException {
        var dht11 = new Dht11(17);
        for (var i = 0; i < 10; i++) {
            System.out.println(dht11.measurement());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}