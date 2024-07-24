package com.skyfork.api.chimera.command.impl;

import com.skyfork.api.chimera.command.Command;
import com.skyfork.client.Access;
import com.skyfork.client.util.ChatUtil;

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