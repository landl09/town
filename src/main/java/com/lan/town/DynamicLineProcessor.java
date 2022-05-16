package com.lan.town;

import java.util.*;
import java.util.function.Function;

public class DynamicLineProcessor<I, O> extends LineProcessor<I, O> {

    private final int poolSize;

    private final int threshold;

    private LineProcessor<O, ?> next;

    private final Function<List<I>, List<O>> processFunc;

    public DynamicLineProcessor(int poolSize, int threshold, LineProcessor<O, ?> next, Function<List<I>, List<O>> processFunc) {

        this.poolSize = poolSize;

        this.threshold = threshold;

        this.next = next;

        this.processFunc = processFunc;

    }

    @Override

    public Pack<O> process(Pack<I> input) {

        List<O> result = processFunc.apply(input.sourceList);
        //assumption: all process results have the same size.
        return new Pack<>(result, input.offset / threshold() * result.size());

    }

    @Override

    protected int poolSize() {

        return poolSize;

    }

    @Override

    protected int threshold() {

        return threshold;

    }

    @Override

    protected LineProcessor<O, ?> next() {

        return next;

    }

    public void setNext(LineProcessor<O, ?> next) {

        this.next = next;

    }

}
