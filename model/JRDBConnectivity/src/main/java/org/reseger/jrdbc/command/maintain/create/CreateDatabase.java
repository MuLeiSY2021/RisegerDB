package org.reseger.jrdbc.command.maintain.create;

import org.reseger.jrdbc.command.Command;
import org.reseger.jrdbc.command.Commands;

public class CreateDatabase extends Command {

    public CreateDatabase(Commands commands) {
        super(commands);
    }

    public CreateDatabase name(String name) {
        super.addArgument(name);
        return this;
    }
}
