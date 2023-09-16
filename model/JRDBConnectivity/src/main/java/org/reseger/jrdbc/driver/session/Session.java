package org.reseger.jrdbc.driver.session;

import org.riseger.protoctl.packet.response.BasicResponse;

public interface Session<P extends BasicResponse<?>> {


    P send() throws InterruptedException;


}
