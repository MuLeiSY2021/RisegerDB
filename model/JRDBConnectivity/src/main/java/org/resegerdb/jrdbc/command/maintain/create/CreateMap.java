package org.resegerdb.jrdbc.command.maintain.create;

import org.resegerdb.jrdbc.command.Command;
import org.resegerdb.jrdbc.command.Commands;

public class CreateMap extends Command {

    public CreateMap(Commands commands) {
        super(commands);
    }

    public CreateMap name(String name) {
        super.addArgument(name);
        return this;
    }

    public CreateMap database(String database) {
        super.addArgument(database);
        return this;
    }


}
