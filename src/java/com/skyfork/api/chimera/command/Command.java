package com.skyfork.api.chimera.command;

public interface Command {
    boolean run(String[] var1);

    String usage();
}