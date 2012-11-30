package com.maestrodev;

import static org.junit.Assert.*;

import java.io.File;
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
    public void sync()
        throws Exception
    {
        
        PerforceWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "perforce" ) );

        worker.sync();

        assertNull( worker.getError(), worker.getError() );
    }
 
      /**
     * Test for scm
     */
    @Test
    public void label()
        throws Exception
    {
        
        PerforceWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "perforce_label" ) );
        
        worker.label();
        
        assertNull( worker.getError(), worker.getError() );
    }

    /**
     * Test for scm
     */
    @Test
    public void edit()
        throws Exception
    {
        
        PerforceWorker worker = new PerforceWorker();

        worker.setWorkitem( loadJson( "perforce_edit" ) );
        
        worker.edit();

        assertNull( worker.getError(), worker.getError() );
    }
    
     /**
     * Test for scm
     */
    @Test
    public void submit()
        throws Exception
    {
        PerforceWorker worker = new PerforceWorker();
        FileUtils.write(new File("/tmp/p4/Centrepoint/README"), "Hello ");
        worker.setWorkitem( loadJson( "perforce_submit" ) );
        
        worker.submit();

        assertNull( worker.getError(), worker.getError() );
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
        
        worker.sync();

        assertEquals("Maestro Detected Error In Perforce Task Password invalid. Make Sure All Input Fields Are Valid.",  worker.getError().replaceAll("\n", "") );        
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
