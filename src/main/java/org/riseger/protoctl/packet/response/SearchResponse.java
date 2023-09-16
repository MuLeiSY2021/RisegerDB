package org.riseger.protoctl.packet.response;

import lombok.Getter;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.protoctl.packet.PacketType;

import java.util.List;
import java.util.Map;

@Getter
public class SearchResponse extends BasicResponse<Map<String, List<Element_c>>> {

    public SearchResponse() {
        super(PacketType.SEARCH_RESPONSE);
    }

    @Override
    public PacketType getType() {
        return super.getType();
    }

}
