package cn.chimera.command.impl;

import cn.chimera.command.Command;
import org.union4dev.base.Access;
import org.union4dev.base.util.ChatUtil;
import org.lwjgl.input.Keyboard;

/**
 * @author chimera
 */
public class BindCommand implements Command {
    @Override
    public boolean run(String[] args) {
        if (args.length == 3) {
            Class<?> m = Access.getInstance().getModuleManager().getModuleClass(args[1]);
            if (m == null) {
                ChatUtil.info("The module with the name " + args[1] + " does not exist.");
                return false;
            }
            Access.getInstance().getModuleManager().setKey(m,Keyboard.getKeyIndex(args[2].toUpperCase()));
            ChatUtil.info(m.getName() + " has been bound to " + args[2] + ".");
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: .bind [模块] [按键]";
    }
}