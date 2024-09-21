package org.riseger.protocol.packet.request;

import lombok.Setter;
import org.riseger.protocol.compiler.CommandTree;
import org.riseger.protocol.job.CommandSQLJob;
import org.riseger.protocol.job.Job;
import org.riseger.protocol.packet.PacketType;
import org.riseger.protocol.packet.RequestType;

@Setter
public class CommandSQLRequest extends TranspondRequest {
    private final CommandTree commandTree;

    private String ipAddress;

    public CommandSQLRequest(CommandTree commandTree, String ipAddress, RequestType type) {
        super(PacketType.COMMAND_SQL, type);
        this.commandTree = commandTree;
        this.ipAddress = ipAddress;
    }

    @Override
    public Job warp() {
        return new CommandSQLJob(super.getTransponder(), commandTree, ipAddress);
    }
}
