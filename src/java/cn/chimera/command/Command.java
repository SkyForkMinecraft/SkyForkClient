package cn.chimera.command;

public interface Command {
    boolean run(String[] var1);

    String usage();
}