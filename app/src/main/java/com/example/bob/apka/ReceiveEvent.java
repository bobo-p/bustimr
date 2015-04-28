package com.example.bob.apka;

import java.util.List;

/**
 * Created by Bob on 2015-02-04.
 */
public class ReceiveEvent {

    private List<Data> res;
    public ReceiveEvent(List<Data> r) {
        res=r;
    }
    public List<Data> getResult() {
        return res;
    }
}
