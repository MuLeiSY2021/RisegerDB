package org.reseger.jrdbc.command.maintain.create;

import org.reseger.jrdbc.command.CommandId;
import org.reseger.jrdbc.command.Commands;
import org.reseger.jrdbc.driver.session.Session;

@CommandId("create")
public class Create {


    private final Session session;

    private final Commands commands;

    public Create(Session session, Commands commands) {
        this.session = session;
        this.commands = commands;
    }

    @CommandId("database")
    public CreateDatabase database() {
        return (CreateDatabase) commands.addCommand(new CreateDatabase(commands));
    }

    @CommandId("map")
    public CreateMap map() {
        return (CreateMap) commands.addCommand(new CreateMap(commands));
    }

    @CommandId("model")
    public CreateModel model() {
        return (CreateModel) commands.addCommand(new CreateModel(commands));
    }
}
