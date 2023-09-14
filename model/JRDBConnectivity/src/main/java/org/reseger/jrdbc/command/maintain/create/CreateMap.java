package org.reseger.jrdbc.command.maintain.create;

import org.reseger.jrdbc.command.Command;
import org.reseger.jrdbc.command.Commands;

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
