package org.resegerdb.jrdbc.driver.session;

import org.resegerdb.jrdbc.driver.result.Result;

public interface Session {


    Result send() throws InterruptedException;


}
