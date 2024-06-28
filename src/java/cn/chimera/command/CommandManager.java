package cn.chimera.command;

import cn.chimera.command.impl.*;
import lombok.Getter;
import org.union4dev.base.events.EventManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chimera
 */

public class CommandManager {
    @Getter
    private HashMap<String[], Command> commands = new HashMap();

    public CommandManager() {
        EventManager.register(new CommandHandler());
        this.commands.put(new String[]{"help", "h"}, new HelpCommand());
        this.commands.put(new String[]{"bind", "b"}, new BindCommand());
        this.commands.put(new String[]{"toggle", "t"}, new ToggleCommand());
    }



    public Command getCommand(String name) {
        for (Map.Entry<String[], Command> entry : this.commands.entrySet()) {
            for (String s : entry.getKey()) {
                if (!s.equalsIgnoreCase(name)) continue;
                return entry.getValue();
            }
        }
        return null;
    }


}