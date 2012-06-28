package com.maestrodev;

import java.io.File;
import java.lang.reflect.Method;
import org.apache.maven.scm.*;

import org.apache.maven.scm.manager.BasicScmManager;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;
import org.apache.maven.scm.provider.bazaar.BazaarScmProvider;
import org.apache.maven.scm.provider.clearcase.ClearCaseScmProvider;
import org.apache.maven.scm.provider.cvslib.cvsjava.CvsJavaScmProvider;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.apache.maven.scm.provider.local.LocalScmProvider;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.starteam.StarteamScmProvider;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.SvnExeScmProvider;
import org.apache.maven.scm.provider.synergy.SynergyScmProvider;
import org.apache.maven.scm.provider.tfs.TfsScmProvider;
import org.apache.maven.scm.provider.vss.VssScmProvider;
import org.apache.maven.scm.repository.ScmRepository;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.LoggerFactory;



public class ScmWorker
    extends MaestroWorker
{

    protected final  org.slf4j.Logger logger =  LoggerFactory.getLogger(this.getClass());
    
//    add, add, addScmProvider, blame, branch, branch, changeLog, changeLog, changeLog, changeLog, checkIn, checkIn, checkOut, checkOut, checkOut, checkOut, cleanScmUrl, diff, edit, export, export, export, export, getProviderByRepository, getProviderByType, getProviderByUrl, list, makeProviderScmRepository, makeScmRepository, mkdir, remove, setScmProvider, setScmProviderImplementation, setScmProviders, status, tag, tag, unedit, update, update, update, update, update, update, update, update, update, update, validateScmRepository
    
    protected final static String [] commands = {
        "branch",
        "validate",
        "add",
        "unedit",
        "export",
        "bootstrap",
        "changeLog",
        "list",
        "checkIn",
        "checkOut",
        "status",
        "update",
        "diff",
        "edit",
        "export",
        "tag"
    };
    
    protected org.slf4j.Logger getLogger() {
        return logger;
    }
    
    protected ScmVersion getScmVersion()
    {
        ScmVersion scmVersion = null;
        if ( "tag".equals( getField("version_type") ) )
        {
            scmVersion = new ScmTag( getField("version") );
        }
        else if ( "branch".equals( getField("version_type") ) )
        {
            scmVersion = new ScmBranch( getField("version") );
        }
        else if ( "revision".equals( getField("version_type") ) )
        {
            scmVersion = new ScmRevision( getField("version") );
        }
        else if (!StringUtils.isEmpty(getField("version_type")))
        {
            throw new IllegalArgumentException( "'" + getField("version_type") + "' Version Type Is Unknown." );
        }
        return scmVersion;
        
    }
    
    public ScmRepository getScmRepository(ScmManager scmManager)
         throws ScmException
     {
         ScmRepository repository;
 
         try
         {
             repository = scmManager.makeScmRepository( getField("url") );
 
             ScmProviderRepository providerRepo = repository.getProviderRepository();
 
             if ( !StringUtils.isEmpty( getField("username") ) )
             {
                 providerRepo.setUser( getField("username") );
             }
 
             if ( !StringUtils.isEmpty( getField("password")) )
             {
                 providerRepo.setPassword( getField("password") );
             }
 
             if ( repository.getProviderRepository() instanceof ScmProviderRepositoryWithHost )
             {
                 ScmProviderRepositoryWithHost repo = (ScmProviderRepositoryWithHost) repository.getProviderRepository();
 
 
                 if ( !StringUtils.isEmpty( getField("username") ) )
                 {
                     repo.setUser( getField("username") );
                 }
 
                 if ( !StringUtils.isEmpty( getField("password") ) )
                 {
                     repo.setPassword( getField("password") );
                 }
 
                 if ( !StringUtils.isEmpty( getField("private_key") ) )
                 {
                     repo.setPrivateKey( getField("private_key") );
                 }
 
                 if ( !StringUtils.isEmpty( getField("passphrase") ) )
                 {
                     repo.setPassphrase( getField("passphrase") );
                 }
             }
 
             if ( !StringUtils.isEmpty( getField("tag_base") ) && repository.getProvider().equals( "svn" ) )
             {
                 SvnScmProviderRepository svnRepo = (SvnScmProviderRepository) repository.getProviderRepository();
 
                 svnRepo.setTagBase( getField("tag_base") );
             }
         }
         catch ( Exception e )
         {
             e.printStackTrace();
             throw new ScmException( "Can't load the scm provider.", e );
         }
 
         return repository;
     }
    
    
     public ScmVersion getScmVersion( String versionType, String version ) throws Exception
     {
         if ( StringUtils.isEmpty(versionType) && StringUtils.isNotEmpty( version ) )
         {
             throw new Exception( "You must specify the version type." );
         }
 
         if ( StringUtils.isEmpty( version ) )
         {
             return null;
         }
 
         if ( "branch".equals( versionType ) )
         {
             return new ScmBranch( version );
         }
 
         if ( "tag".equals(versionType ) )
         {
             return new ScmTag( version );
         }
 
         if ( "revision".equals( versionType ) )
         {
             return new ScmRevision( version );
         }
 
         throw new Exception( "Unknown '" + getField("versionType") + "' version type." );
     }

    protected void setupProviders(ScmManager manager) throws Exception
    {
        manager.setScmProvider("perforce",  new PerforceScmProvider());   
        manager.setScmProvider("p4",  new PerforceScmProvider());   
        manager.setScmProvider("bazaar",  new BazaarScmProvider());
//        manager.setScmProvider("hg",  );
        manager.setScmProvider("clearcase",  new ClearCaseScmProvider());
        manager.setScmProvider("synergy",  new SynergyScmProvider());
        manager.setScmProvider("git",  new GitExeScmProvider());
        manager.setScmProvider("cvs",  new CvsJavaScmProvider());
//        manager.setScmProvider("jazz",  new Jaz);
        manager.setScmProvider("local",  new LocalScmProvider());
        manager.setScmProvider("starteam",  new StarteamScmProvider());
        manager.setScmProvider("svn",  new SvnExeScmProvider());
        manager.setScmProvider("vss",  new VssScmProvider());
        manager.setScmProvider("tfs",  new TfsScmProvider());
        
    }
     
    protected void validateInputs() throws Exception
    {
        if(StringUtils.isEmpty(getField("command")))
            throw new Exception("Missing Command Field");
        
        boolean commandFound = false;
        for(String supportedCommand : commands){
            if(getField("command").replace("scm:", "").toLowerCase().equals(supportedCommand.toLowerCase())){
                commandFound = true;
                setField("command", supportedCommand);
                break;
            }
        }
        
        if(!commandFound)
            throw new Exception("Unsupported Command " + getField("command"));
        
        if(StringUtils.isEmpty(getField("path")))
            throw new Exception("Missing Path Field");
        
        if(StringUtils.isEmpty(getField("url")))
            throw new Exception("Missing Url Field");
      
    }
     

    public void scm()
    {
        try {
            logger.info("Running task scm");

            writeOutput("Validating Inputs For SCM Tasking\n");

            validateInputs();
            
            File checkoutDirectory = new File(getField("path"));
            
            ScmManager scmManager = new MaestroScmMananager(this);
            setupProviders(scmManager);

            ScmRepository scmRepository = getScmRepository(scmManager);
            
            ScmVersion scmVersion = getScmVersion();
            ScmResult result = null;
            if(scmVersion == null){
                Method method = scmManager.getClass().getMethod(getField("command"), new Class<?>[]{ScmRepository.class, ScmFileSet.class});
                result = (ScmResult) method.invoke(scmManager, scmRepository, new ScmFileSet(checkoutDirectory.getAbsoluteFile()));
            } else {
                Method method = scmManager.getClass().getMethod(getField("command"), new Class<?>[]{ScmRepository.class, ScmFileSet.class, ScmVersion.class});
                result = (ScmResult) method.invoke(scmManager, scmRepository, new ScmFileSet(checkoutDirectory.getAbsoluteFile()), scmVersion);
            }

            
            logger.debug(result.getCommandOutput());
            if(!result.isSuccess())
                throw new Exception(result.getProviderMessage() + " " + result.getCommandOutput());
            
            writeOutput("SCM Task Completed Successfully For Command " + getField("command") + "\n");
                
        } catch (Exception e) {
            e.printStackTrace();
            setError("Maestro Detected Error In SCM Task " + e.getMessage());
        }
        
    }

}
