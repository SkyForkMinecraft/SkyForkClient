package com.skyfork.api.chimera.command.impl;

import com.skyfork.api.chimera.command.Command;
import com.skyfork.client.Access;
import com.skyfork.client.util.ChatUtil;

/**
 * @author chimera
 */
public class ToggleCommand implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 2) {
            Class<?> m = Access.getInstance().getModuleManager().getModuleClass(args[1]);
            if (m == null) {
                ChatUtil.info("The module with the name " + args[1] + " does not exist.");
                return false;
            }
            Access.getInstance().getModuleManager().setEnable(m,!Access.getInstance().getModuleManager().isEnabled(m));
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: .t [模块]";
    }
}