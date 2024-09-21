package org.reseger.jrdbc.maintain.command;

import java.util.LinkedList;
import java.util.List;

public class Command {

    private final Commands commands;

    private final List<String> arguments = new LinkedList<>();

    private String command;

    public Command(Commands commands) {
        this.commands = commands;
    }

    public void addArgument(String argument) {
        String name = commands.getName(argument);
        this.arguments.add(name);
    }
}
