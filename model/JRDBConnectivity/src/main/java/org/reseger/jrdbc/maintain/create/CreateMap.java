package org.reseger.jrdbc.maintain.create;

import org.reseger.jrdbc.maintain.command.Command;
import org.reseger.jrdbc.maintain.command.Commands;

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
