package org.reseger.jrdbc.maintain.create;

import org.reseger.jrdbc.maintain.command.Command;
import org.reseger.jrdbc.maintain.command.Commands;
import org.riseger.protoctl.struct.config.Option;
import org.riseger.protoctl.struct.entity.ParentModel;
import org.riseger.protoctl.struct.entity.Type;

import java.util.LinkedList;
import java.util.List;

public class CreateModel extends Command {

    public CreateModel(Commands commands) {
        super(commands);
    }

    public CreateModel database(String database) {
        super.addArgument(database);
        return this;
    }

    public CreateModel name(String name) {
        super.addArgument(name);

//        this.model = new Model(name);
        return this;
    }

    public CreateModel parent(ParentModel parentModel) {
//        this.parentModel = parentModel;
        return this;
    }

    public CreateModel parameter(String name, Type type) {
//        this.model.addParameter(name, type);
        return this;
    }

    public CreateModel config(Option option, String value) {
//        this.model.addConfig(option,value);
        return this;
    }

    public List<String> build() {
        return new LinkedList<String>();
    }
}
