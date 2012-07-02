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

  private ScmWorker worker;
  
  public MaestroScmMananager(ScmWorker worker){
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
    worker.bufferOutput(string + "\n", false);
  }

  public void debug(String string, Throwable thrwbl) {
    worker.bufferOutput(string + "\n", false);
  }

  public void debug(Throwable thrwbl) {
    worker.bufferOutput(thrwbl.getMessage() + "\n", false);
  }

  public boolean isInfoEnabled() {
    return true;
  }

  public void info(String string) {
    worker.bufferOutput(string + "\n", false);
  }

  public void info(String string, Throwable thrwbl) {
    worker.bufferOutput(string + "\n", false);
  }

  public void info(Throwable thrwbl) {
    worker.bufferOutput(thrwbl.getMessage() + "\n", false);
  }

  public boolean isWarnEnabled() {
    return true;
  }

  public void warn(String string) {
    worker.bufferOutput(string + "\n", false);
  }

  public void warn(String string, Throwable thrwbl) {
    worker.bufferOutput(string + "\n", false);
  }

  public void warn(Throwable thrwbl) {
    worker.bufferOutput(thrwbl.getMessage() + "\n", false);
  }

  public boolean isErrorEnabled() {
    return true;
  }

  public void error(String string) {
    worker.bufferOutput(string + "\n", false);
  }

  public void error(String string, Throwable thrwbl) {
    worker.bufferOutput(string + "\n", false);
  }

  public void error(Throwable thrwbl) {
    worker.bufferOutput(thrwbl.getMessage() + "\n", false);
  }
  
}
