package com.skyfork.api.chimera.command;

import com.skyfork.api.chimera.command.impl.HelpCommand;
import com.skyfork.api.chimera.command.impl.BindCommand;
import com.skyfork.api.chimera.command.impl.ToggleCommand;
import lombok.Getter;
import com.skyfork.client.events.EventManager;

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