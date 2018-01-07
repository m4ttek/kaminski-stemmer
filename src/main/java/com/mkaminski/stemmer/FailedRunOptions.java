package com.mkaminski.stemmer;

public class FailedRunOptions extends RunOptions {

    public FailedRunOptions() {
        super(null, null, null, null);
    }

    @Override
    public String toString() {
        return "run options failure";
    }
}
