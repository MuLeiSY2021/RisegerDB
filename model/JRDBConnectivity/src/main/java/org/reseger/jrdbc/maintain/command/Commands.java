package org.reseger.jrdbc.maintain.command;

import org.reseger.jrdbc.driver.session.Session;
import org.reseger.jrdbc.maintain.create.Create;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Commands {
    public static final String STRING_PREFIX = "STR";

    public static final String NUMBER_PREFIX = "NUM";

    public static final String KEYWORD_PREFIX = "KW";

    public static final String SPLIT_PREFIX = "_";


    private final Session session;

    private final Create create;

    private List<Command> commands = new LinkedList<Command>();

    private Map<String, String> variable = new HashMap<String, String>();

    public Commands(Session session) {
        this.session = session;
        this.create = new Create(session, this);
    }

    public Create create() {
        return create;
    }

    public Command addCommand(Command command) {
        this.commands.add(command);
        return command;
    }

    public String getName(String argument) {
        String name = getN(STRING_PREFIX);
        this.variable.put(name, argument);
        return name;
    }

    private String getN(String prefix) {
        return prefix + SPLIT_PREFIX + variable.size();
    }
}
