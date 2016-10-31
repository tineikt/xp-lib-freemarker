package no.tine.xp.lib.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.xp.resource.Resource;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.ScriptValue;
import com.google.common.collect.Maps;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import no.tine.xp.lib.freemarker.ComponentDirective;

public final class FreemarkerProcessor
{
    private ResourceKey view;
    private ResourceService resourceService;
    private ScriptValue model;
    private Map<String, PortalViewFunction> viewFunctions;
    private final static Logger log = LoggerFactory.getLogger(FreemarkerProcessor.class);
    
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_25);
    //private static final StringTemplateLoader STRING_LOADER = new StringTemplateLoader();
    //private static final Map<String, Long> TEMPLATE_LOADED_TIMESTAMP = new HashMap<>();

    public static void setupFreemarker(final FreemarkerConfig config) {
    	CONFIGURATION.setDefaultEncoding(config.encoding());
    }
    
    public static void setupFreemarker(ResourceService resourceService) {
//    	CONFIGURATION.setDefaultEncoding("UTF-8");
    	CONFIGURATION.setLogTemplateExceptions(false);
        CONFIGURATION.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);

        CONFIGURATION.setSharedVariable("component", new ComponentDirective());

    	// Let's support both loading templates from the class loader and from a local string cache.
    	CONFIGURATION.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[] {
    			new ResourceTemplateLoader(resourceService)//,
    			//new ClassTemplateLoader(FreemarkerProcessor.class.getClassLoader(), ""),
    			//STRING_LOADER
    	}));

    	//CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);		// Throws exceptions to log file
    	CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);		// Shows exceptions on screen
    }

    public FreemarkerProcessor(Map<String, PortalViewFunction> viewFunctions)
    {
    	this.viewFunctions = viewFunctions;
    }

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

        map.putAll(this.viewFunctions);

        String key = resource.getKey().toString();

        // Check if the template has already been loaded earlier. If not, we need to load it into the string loader.
        //if(!TEMPLATE_LOADED_TIMESTAMP.containsKey(key) || TEMPLATE_LOADED_TIMESTAMP.get(key).longValue() != resource.getTimestamp()) {
        //	STRING_LOADER.putTemplate(key, resource.readString());
        //}

        Template template = CONFIGURATION.getTemplate(key);

        StringWriter sw = new StringWriter();
        template.process(map, sw);
        return sw.toString();
    }

    private RuntimeException handleError( final TemplateException e )
    {
    	String error = "Error with the script.";
    	log.error(error, e);
        return new RuntimeException(error, e);
    }

    private RuntimeException handleError( final IOException e )
    {
    	String error = "IO with the script.";
    	log.error(error, e);
        return new RuntimeException(error, e);
    }

    private RuntimeException handleError( final RuntimeException e )
    {
        return e;
    }
}
