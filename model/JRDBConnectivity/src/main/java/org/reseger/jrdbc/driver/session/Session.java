package org.reseger.jrdbc.driver.session;

import org.reseger.jrdbc.driver.result.Result;

public interface Session {


    Result send() throws InterruptedException;


}
