package org.resegerdb.jrdbc.driver;

import org.resegerdb.jrdbc.command.Command;
import org.resegerdb.jrdbc.command.Commands;

import java.util.*;

public class Session {


    private Commands commands;

    Connection connection;

    public Commands commands() {
        return new Commands(this);
    }

    public Result send() {
        connection.flush(this);
        return new Result();
    }


}
