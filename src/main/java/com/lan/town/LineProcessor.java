package com.lan.town;

public abstract class LineProcessor<I, O> extends LocalStoredPackUnit<I, O> {

    protected abstract LineProcessor<O, ?> next();

    @Override

    public void send(Pack<O> output) {
        next().call(output);
    }

    @Override

    public synchronized void clear() {

        super.clear();

        if (next() != null) {

            next().clear();

        }

    }

}
