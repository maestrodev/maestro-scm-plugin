/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maestrodev;

import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.manager.BasicScmManager;

/**
 *
 * @author kelly
 */
public class MaestroScmMananager extends BasicScmManager implements ScmLogger{

  private MaestroWorker worker;
  
  public MaestroScmMananager(MaestroWorker worker){
    this.worker = worker;
  }
  
  @Override
  protected ScmLogger getScmLogger(){
    return this;
  }
  
  public boolean isDebugEnabled() {
    return true;
  }

  public void debug(String string) {
    worker.writeOutput(string);
  }

  public void debug(String string, Throwable thrwbl) {
    worker.writeOutput(string);
  }

  public void debug(Throwable thrwbl) {
    worker.writeOutput(thrwbl.getMessage());
  }

  public boolean isInfoEnabled() {
    return true;
  }

  public void info(String string) {
    worker.writeOutput(string);
  }

  public void info(String string, Throwable thrwbl) {
    worker.writeOutput(string);
  }

  public void info(Throwable thrwbl) {
    worker.writeOutput(thrwbl.getMessage());
  }

  public boolean isWarnEnabled() {
    return true;
  }

  public void warn(String string) {
    worker.writeOutput(string);
  }

  public void warn(String string, Throwable thrwbl) {
    worker.writeOutput(string);
  }

  public void warn(Throwable thrwbl) {
    worker.writeOutput(thrwbl.getMessage());
  }

  public boolean isErrorEnabled() {
    return true;
  }

  public void error(String string) {
    worker.writeOutput(string);
  }

  public void error(String string, Throwable thrwbl) {
    worker.writeOutput(string);
  }

  public void error(Throwable thrwbl) {
    worker.writeOutput(thrwbl.getMessage());
  }
  
}
