package com.lan.town;

public class PackPool<E> extends Pool<Pack<E>> {

    public PackPool(int size) {
        super(size);
    }

    public void add(Pack<E> pack){
        array[pack.offset] = pack;
    }

}