package com.lan.town;

import java.util.Stack;

public abstract class LocalStoredPackUnit<I, O> implements Unit<Pack<I>, Pack<O>> {
    protected Pool<I> pool;

    private Pool<Integer> signal;

    protected Stack<Integer> stack;

    private int stackSize;

    protected abstract int poolSize();

    protected abstract int threshold();

    private void checkAndInit() {

        if (pool == null) {

            initPool();

        }

        if (signal == null) {

            initSignal();

        }

        if (stack == null) {

            initStack();

        }

    }

    private synchronized void initPool() {

        if (pool == null) {

            pool = new Pool<>(poolSize() > 0 ? poolSize() : Integer.MAX_VALUE);

        }

    }

    private synchronized void initSignal() {

        if (signal == null) {

            stackSize = poolSize() > 0 && threshold() > 0 ? (poolSize() / threshold() + (poolSize() % threshold() == 0 ? 0 : 1)) : Integer.MAX_VALUE;

            signal = new Pool<>(stackSize);

        }

    }

    private synchronized void initStack() {

        if (stack == null) {

            stack = new Stack<>();

        }

    }

    private void mark(int revOffset, int revSize) {

        int startIndex = revOffset;

        int endIndex = startIndex + revSize - 1;

        int poolStartPage = startIndex / threshold();

        int poolEndPage = endIndex / threshold();

        for (int page = poolStartPage; page <= poolEndPage; page++) {

            int pageEndIndexNext = (page + 1) * threshold();

            if (pageEndIndexNext > endIndex) {

                pageEndIndexNext = endIndex + 1;

            }

            int inc = pageEndIndexNext - startIndex;

            writeSignal(page, inc);

            if (signal.getOrDefault(page, 0) >= threshold()) {

                stack.add(page);

                writeSignal(page, -threshold());

            }

            startIndex = pageEndIndexNext;

        }

    }

    private synchronized void writeSignal(int i, int inc) {

        signal.set(i, signal.getOrDefault(i, 0) + inc);

    }

    @Override

    public Pack<I> receive(Pack<I> input) {

        checkAndInit();

        pool.addAll(input.offset, input.sourceList);

        mark(input.offset, input.sourceList.size());

        return input;
    }

    @Override
    public void call(Pack<I> input) {
        receive(input);
        while (!stack.isEmpty()) {
            int page = stack.pop();
            processAndSend(page * threshold(), (page + 1) * threshold());
        }
    }

    public synchronized void clear() {

        for (int i = 0; i < stackSize - 1; i++) {
            int leftAmount = signal.getOrDefault(i, 0);
            if (leftAmount > 0) {
                processAndSend(i * threshold(), i * threshold() + leftAmount);
            }

        }

    }

    protected void processAndSend(int start, int end) {
        send(process(new Pack<I>(pool.subList(start, end), start)));
    }
}
