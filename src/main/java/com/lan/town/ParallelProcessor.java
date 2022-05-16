package com.lan.town;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public class ParallelProcessor<I, O> extends DynamicLineProcessor<I, O> {

    private final ExecutorService executorService;

    public ParallelProcessor(int poolSize, int threshold, LineProcessor<O, ?> next, Function<List<I>, List<O>> processFunc, ExecutorService executorService) {
        super(poolSize, threshold, next, processFunc);
        this.executorService = executorService;
    }

    @Override
    protected void processAndSend(int start, int end) {
        executorService.execute(() -> super.processAndSend(start, end));
    }
}
