package org.reseger.jrdbc.driver.session;

import org.riseger.protoctl.message.BasicMessage;

public interface Session {


    BasicMessage send() throws InterruptedException;


}
