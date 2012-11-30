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
public class ScmWorkerTest
{
    private static final JSONParser parser = new JSONParser();

    
    /**
     * Test for scm
     */
    @Test
    public void checkout()
        throws Exception
    {
        
        ScmWorker worker = new ScmWorker();

        worker.setWorkitem( loadJson( "checkout" ) );

        File wd = new File(worker.getField("path"));
        if(wd.exists())
            FileUtils.deleteDirectory(wd);
        
        worker.scm();

        assertNull( worker.getError(), worker.getError() );
    }

     /**
     * Test for scm
     */
//    @Test
//    public void update()
//        throws Exception
//    {
//        ScmWorker worker = new ScmWorker();
//
//        worker.setWorkitem( loadJson( "update" ) );
//
//        
//        worker.scm();
//        System.out.println(worker.getError());
//        assertNull( worker.getError() );        
//    }
    
    @Test
    public void failOnConnect()
            throws Exception
    {
        ScmWorker worker = new ScmWorker();

        worker.setWorkitem( loadJson( "failure" ) );

        
        worker.scm();
        
        System.out.println(worker.getError());
        assertNotNull( worker.getError() );        
    }
    
    @Test
    public void failOnMissingCommand()
            throws Exception
    {
        ScmWorker worker = new ScmWorker();

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
