/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maestrodev;

import com.perforce.p4java.client.IClient;
import com.perforce.p4java.client.IClientSummary;
import com.perforce.p4java.core.IMapEntry.EntryType;
import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.FileSpecOpStatus;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.*;
import com.perforce.p4java.impl.generic.client.ClientOptions;
import com.perforce.p4java.impl.generic.client.ClientView;
import com.perforce.p4java.impl.generic.client.ClientView.ClientViewMapping;
import com.perforce.p4java.impl.mapbased.client.Client;
import com.perforce.p4java.server.IServer;
import com.perforce.p4java.server.ServerFactory;
import java.io.*;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyledEditorKit;
import org.apache.commons.exec.*;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

/**
 *
 * @author kelly
 */
public class PerforceWorker extends ScmWorker {
    
    
    private static final String UP_TO_DATE = "file(s) up-to-date";
  
    public void validatePerforceInputs() throws Exception
    {
        if(StringUtils.isEmpty(getField("host")))
            throw new Exception("Missing Host Field");
        if(StringUtils.isEmpty(getField("port")))
            throw new Exception("Missing Port Field");
        if(StringUtils.isEmpty(getField("path")))
            throw new Exception("Missing path Field");
        
        if(getField("force_sync") == null)
            throw new Exception("Missing force_sync Field");
        
        if(getField("no_update") == null)
            throw new Exception("Missing no_sync Field");
        if(getField("bypass_server") == null)
            throw new Exception("Missing bypass_server Field");
        if(getField("bypass_client") == null)
            throw new Exception("Missing bypass_client Field");
        if(getField("fail_on_up_to_date") == null)
            throw new Exception("Missing fail_on_up_to_date Field");
        if(getField("delete_client") == null)
            throw new Exception("Missing delete_client Field");
        if(getField("clean_working_copy") == null)
            throw new Exception("Missing clean_working_copy Field");
        
        Map fields = getFields();
        if(getField("remote_paths") == null || !(fields.get("remote_paths") instanceof List))
          throw new Exception("Missing or Empty remote_paths Field");

        String url = String.format("p4java://%s:%s",
                getField("host"),
                getField("port")
                );
        
        
        setField("url", url);
    }
    
    private ClientView getClientView(String name){
      ClientView view = new ClientView();

      for(Object remotePath: (List)getFields().get("remote_paths")){
        ClientViewMapping tempMappingEntry = new ClientViewMapping();

        tempMappingEntry.setLeft("//" + remotePath.toString().replaceAll("\\/\\/", ""));
        tempMappingEntry.setRight("//" + name + remotePath.toString().replaceAll("\\/\\/", "/"));
        tempMappingEntry.setType(EntryType.INCLUDE);

        view.addEntry(tempMappingEntry);
      }
      return view;
    }
    
    private IClient getClient(IServer server, String name) throws ConnectionException, RequestException, AccessException, UnknownHostException{
        IClient client = server.getClient(name);

        if(client == null){
          client = new Client();
        }
        

        // Setting up the name and the root folder
        client.setName(name);
        client.setRoot(getField("path"));
        client.setServer(server);
        // Setting the client as the current one for the server
        server.setCurrentClient(client);
  
        IClientSummary.IClientOptions options = new ClientOptions(false, true, false, false, false, false);
        client.setOptions(options);
        client.setClientView(getClientView(name));

        server.updateClient(client);
        server.setCurrentClient(client);

        return client;
    }
    
    private IServer getServer() throws URISyntaxException, ConnectionException, NoSuchObjectException, ConfigException, ResourceException, AccessException, RequestException{
      IServer server = ServerFactory.getServer(getField("url"), null);
      server.connect();
      server.setUserName(getField("username"));
      server.login(getField("password"));
      
      return server;
    }
    
    private void processSyncList(List<IFileSpec> syncList) throws Exception{
      String error = "";
      for(IFileSpec fileSpec : syncList){
              if(fileSpec.getOpStatus() == FileSpecOpStatus.CLIENT_ERROR || 
                      fileSpec.getOpStatus() == FileSpecOpStatus.ERROR){
                if(fileSpec.getStatusMessage() != null && fileSpec.getStatusMessage().contains(UP_TO_DATE) && !Boolean.parseBoolean(getField("fail_on_up_to_date"))){
                  bufferOutput(fileSpec.getStatusMessage() + "\n", false);
                }else{
                  error += fileSpec.getStatusMessage() + "\n";
                }
              }else{
                if(fileSpec == null || fileSpec.getAction() == null)
                  System.out.println("we got null");
                String action = fileSpec.getAction() == null ? "" : fileSpec.getAction().toString();
                String depotPath = fileSpec.getDepotPathString() == null ? "" : fileSpec.getDepotPathString();
                String label = fileSpec.getLabel() == null ? "" : fileSpec.getLabel();
                String clientPath = fileSpec.getClientPathString() == null ? "" : fileSpec.getClientPathString();
                String message = clientPath + label + " " + action + " " + depotPath;
                bufferOutput(message + "\n", false);
              }
            }
            bufferOutput("\n", true);
            
            if(StringUtils.isNotEmpty(error))
              throw new Exception(error);
    }
    
    public void sync()
    {
        IServer server = null;

        try {
            this.resetBuffer();
            getLogger().info("Running task perforce");

            writeOutput("Validating Inputs For Perforce Tasking\n");
           
            if(Boolean.parseBoolean(getField("clean_working_copy"))){
              String removePath = FileUtils.removePath(getField("path"));
              writeOutput("Cleaning Working Copy At " + getField("path") + "\n" + removePath + "\n");
            }
            
            validatePerforceInputs();

            server = getServer();
                        
            IClient client = getClient(server, java.net.InetAddress.getLocalHost().getHostName());
            

            List<IFileSpec> syncList = client.sync(
                              FileSpecBuilder.makeFileSpecList(getField("path") + "/..."),
                              Boolean.parseBoolean(getField("force_update")),
                              Boolean.parseBoolean(getField("no_update")),
                              Boolean.parseBoolean(getField("client_bypass")),
                              Boolean.parseBoolean(getField("server_bypass")));
            
            processSyncList(syncList);
            writeOutput("\nPerforce Sync Task Completed Successfully\n");
        } catch (Exception e) {
            setError("Maestro Detected Error In Perforce Task " + e.getMessage() + " Make Sure All Input Fields Are Valid.\n");
        } finally {
          try {
          if(server != null && Boolean.parseBoolean(getField("delete_client")))
            server.deleteClient(java.net.InetAddress.getLocalHost().getHostName(), true);
          } catch(Exception e) {
            setError("Unable To remove Temporary Client From Server " + e.getMessage());
          }
        }
        
    }
    
}
