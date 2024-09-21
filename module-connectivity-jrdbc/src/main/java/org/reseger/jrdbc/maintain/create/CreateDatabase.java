package org.reseger.jrdbc.maintain.create;

import org.reseger.jrdbc.maintain.command.Command;
import org.reseger.jrdbc.maintain.command.Commands;

public class CreateDatabase extends Command {

    public CreateDatabase(Commands commands) {
        super(commands);
    }

    public CreateDatabase name(String name) {
        super.addArgument(name);
        return this;
    }
}
