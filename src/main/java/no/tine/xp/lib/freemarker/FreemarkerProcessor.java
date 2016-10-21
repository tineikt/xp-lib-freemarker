package no.tine.xp.lib.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.enonic.xp.resource.Resource;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.ScriptValue;
import com.google.common.collect.Maps;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public final class FreemarkerProcessor
{
    private ResourceKey view;
    private ResourceService resourceService;
    private ScriptValue model;
    
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_25);
    private static final StringTemplateLoader STRING_LOADER = new StringTemplateLoader();
    
    // Static init
    {
    	CONFIGURATION.setDefaultEncoding("UTF-8");
    	CONFIGURATION.setLogTemplateExceptions(false);
    	CONFIGURATION.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/");			// Let's try to load templates from the class path
    	
    	CONFIGURATION.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[] { 
    			new ClassTemplateLoader(getClass().getClassLoader(), ""), 
    			STRING_LOADER 
    	}));
    	
    	//CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);		// Throws exceptions to log file
    	CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);		// Shows exceptions on screen
    }

    public FreemarkerProcessor()
    { }

    public void setView( final ResourceKey view )
    {
        this.view = view;
    }

    public void setModel( final ScriptValue model )
    {
        this.model = model;
    }

    public String process()
    {
        try {
            return doProcess();
        }
        catch ( final TemplateException e ) {
        	throw handleError( e );
        }
        catch ( final IOException e) {
        	throw handleError( e );
        }
        catch ( final RuntimeException e )
        {
            throw handleError( e );
        }
    }

    public void setResourceService( final ResourceService resourceService )
    {
        this.resourceService = resourceService;
    }

    private String doProcess() throws IOException, TemplateException
    {
        final Resource resource = resourceService.getResource( this.view );
        final Map<String, Object> map = this.model != null ? this.model.getMap() : Maps.newHashMap();

        STRING_LOADER.putTemplate(resource.getKey().toString(), resource.readString());
        
        Template template = CONFIGURATION.getTemplate(resource.getKey().toString());
        
        StringWriter sw = new StringWriter();
        template.process(map, sw);
        
        return sw.toString();
    }
    
    private RuntimeException handleError( final TemplateException e )
    {
        return new RuntimeException("Script error", e);
    }
    
    private RuntimeException handleError( final IOException e )
    {
        return new RuntimeException("Script error", e);
    }
    
    private RuntimeException handleError( final RuntimeException e )
    {
        return e;
    }
}