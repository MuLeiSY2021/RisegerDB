package org.riseger.protoctl.packet.request;

import lombok.Setter;
import org.riseger.protoctl.compiler.CommandTree;
import org.riseger.protoctl.job.CommandSQLJob;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.packet.RequestType;

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
