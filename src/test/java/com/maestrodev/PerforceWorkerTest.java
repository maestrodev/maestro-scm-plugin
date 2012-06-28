package com.maestrodev;

import java.io.File;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

/**
 * Tests for Maestro Cloud plugin.
 */
public class PerforceWorkerTest
{
    private static final JSONParser parser = new JSONParser();

    
    /**
     * Test for scm
     */
    @Test
    public void checkout()
        throws Exception
    {
        
        PerforceWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "perforce" ) );

        File wd = new File(worker.getField("path"));
        if(wd.exists())
            FileUtils.deleteDirectory(wd);
        
        worker.scm();

        assertNull( worker.getError() );        
    }

     /**
     * Test for scm
     */
    @Test
    public void badPassword()
        throws Exception
    {
        
        PerforceWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "perforce_bad_pass" ) );
        
        worker.scm();

        assertEquals("Maestro Detected Error In Perforce Task Unable To Login, Password invalid. Make Sure P4 Is Installed And All Input Fields Are Correct.",  worker.getError().replaceAll("\n", "") );        
    }
    
    @Test
    public void generateUrl() throws Exception
    {
        PerforceWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "perforce" ) );
        
        worker.validatePerforceInputs();
        
        assertEquals("scm:perforce:kellyplummer-474.trials.perforce.com:1666://depot/centrepoint", worker.getField("url"));
    }
    
    @Test
    public void generateUrlWithoutProjectPath() throws Exception
    {
        PerforceWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "perforce_no_project" ) );
        
        worker.validatePerforceInputs();
        
        assertEquals("scm:perforce:somehost:1666://Depot", worker.getField("url"));
    }
    
    
    @Test
    public void failOnConnect()
            throws Exception
    {
        ScmWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "failure" ) );

        
        worker.scm();
        
        System.out.println(worker.getError());
        assertNotNull( worker.getError() );        
    }
    
    @Test
    public void failOnMissingCommand()
            throws Exception
    {
        ScmWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "failure_missing_command" ) );

        
        worker.scm();
        
        System.out.println(worker.getError());
        assertNotSame( worker.getError(), "Maestro Detected Error In SCM Task Unsupported Command not real" );        
    }
    
    public JSONObject loadJson( String name )
        throws IOException, ParseException
    {
        InputStream is = null;
        try
        {
            String f = name + ".json";
            is = getClass().getClassLoader().getResourceAsStream( f );
            if ( is == null )
            {
                throw new IllegalStateException( "File not found " + f );
            }
            else
            {
                return (JSONObject) parser.parse( IOUtils.toString( is ) );
            }
        }
        finally
        {
            IOUtils.closeQuietly( is );
        }
    }

}
