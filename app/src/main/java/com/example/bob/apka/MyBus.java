package com.example.bob.apka;

import com.squareup.otto.Bus;

/**
 * Created by Bob on 2015-02-04.
 */
public class MyBus {

    private static final Bus BUS=new Bus();
    public static Bus getInstance() {
        return BUS;
    }
}
