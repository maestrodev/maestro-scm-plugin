/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maestrodev;

import java.io.*;
import org.apache.commons.exec.*;
import org.codehaus.plexus.util.StringUtils;

/**
 *
 * @author kelly
 */
public class PerforceWorker extends ScmWorker {
    
//    hostname, port, path, user and password
    
    public void validatePerforceInputs() throws Exception
    {
        if(StringUtils.isEmpty(getField("host")))
            throw new Exception("Missing Host Field");
        if(StringUtils.isEmpty(getField("port")))
            throw new Exception("Missing Port Field");
        if(StringUtils.isEmpty(getField("path")))
            throw new Exception("Missing path Field");
        if(StringUtils.isEmpty(getField("depot")))
            throw new Exception("Missing depot Field");
        if(StringUtils.isEmpty(getField("auto_login")))
            throw new Exception("Missing Auto Login Field");
        if(StringUtils.isEmpty(getField("project_path"))){
            setField("project_path", "");
        } else {
            setField("project_path", String.format("/%s", getField("project_path")));
        }

        String url = String.format("scm:perforce:%s:%s://%s%s",
                getField("host"),
                getField("port"),
                getField("depot"),
                getField("project_path"));
        
        setField("url", url);
    }
    
    public void tryToLogin() throws ExecuteException, InterruptedException, IOException, Exception
    {       
            CommandLine commandLine = CommandLine.parse(String.format("p4 -d %s -p %s:%s -u %s -P %s login -p",
                    getField("path"),
                    getField("host"),
                    getField("port"),
                    getField("username"), 
                    getField("password")));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(getField("password").getBytes());
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(os, os ,is);
            DefaultExecutor executor = new DefaultExecutor(); 
            executor.setStreamHandler(pumpStreamHandler);

            DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
            executor.execute(commandLine, handler);
            while(!handler.hasResult())
              Thread.sleep(100);
            String result = os.toString();
            if(handler.getExitValue() == 0){
              String ticket = result.split("Enter password:")[1];
              setField("password", ticket.replace("\n", "").replace("\r", "").replace(" ", ""));
            } else {
              throw new Exception("Unable To Login, " + result.replace("Enter password: ", ""));
            }
    }
    
    @Override
    public void scm()
    {
      
        try {
            this.resetBuffer();
            getLogger().info("Running task perforce");

            writeOutput("Validating Inputs For Perforce Tasking\n");
            
            validatePerforceInputs();
            
            if(!getField("auto_login").isEmpty() && getField("auto_login").equals("true"))
              tryToLogin();
            
            super.scm();
            
            bufferOutput("Perforce Task Completed Successfully For Command " + getField("command") + "\n", true);
        } catch (Exception e) {
           e.printStackTrace();
            setError("Maestro Detected Error In Perforce Task " + e.getMessage() + " Make Sure P4 Is Installed And All Input Fields Are Correct.");
        }
        
    }
    
}
