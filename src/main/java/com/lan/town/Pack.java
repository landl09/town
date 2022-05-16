package com.lan.town;

import java.util.List;

public class Pack<T> {

    List<T> sourceList;

    int offset;


    public Pack(List<T> sourceList, int offset) {

        this.sourceList = sourceList;

        this.offset = offset;

    }

}
