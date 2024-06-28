package cn.chimera.command.impl;

import cn.chimera.command.Command;
import org.union4dev.base.Access;
import org.union4dev.base.util.ChatUtil;

/**
 * @author chimera
 */
public class HelpCommand implements Command {

    @Override
    public boolean run(String[] args) {
        ChatUtil.info("Here are the list of commands:");
        for (Command c : Access.getInstance().getCommandManager().getCommands().values()) {
            ChatUtil.info(c.usage());
        }
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: .help";
    }
}