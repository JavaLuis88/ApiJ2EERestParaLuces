/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmeralda.proyectos.exceptions;

import org.jboss.resteasy.logging.Logger;

/**
 *
 * @author lingmoy
 */
import org.jboss.resteasy.logging.impl.Log4jLogger;

public class ServerException extends Exception {

    public ServerException() {

        super("Server Exception");
        Logger log;
        log = Logger.getLogger(this.getClass());
        log.error("Server Exception", this);

    }

    public ServerException(String msg) {

        super(msg);
        Logger log;
        log = Logger.getLogger(this.getClass());
        log.error(msg, this);

    }

    public ServerException(Exception exception) {

        super("Server Exception");
        Logger log = Logger.getLogger(exception.getClass());
        log.error(exception.getMessage(), exception);
    }
    
    public ServerException(String msg,Exception exception) {

        super(msg);
        Logger log = Logger.getLogger(exception.getClass());
        log.error(exception.getMessage(), exception);
    }
  
}
