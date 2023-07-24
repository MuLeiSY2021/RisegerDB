package org.resegerdb.jrdbc.command.create;

import org.resegerdb.jrdbc.command.Command;
import org.resegerdb.jrdbc.command.Commands;

public class CreateDatabase extends Command {

    public CreateDatabase(Commands commands) {
        super(commands);
    }

    public CreateDatabase name(String name) {
        super.addArgument(name);
        return this;
    }
}
