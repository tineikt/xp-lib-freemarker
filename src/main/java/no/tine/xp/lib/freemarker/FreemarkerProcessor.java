package no.tine.xp.lib.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.xp.resource.Resource;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.resource.ResourceProblemException;
import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.ScriptValue;
import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;

public final class FreemarkerProcessor
{
    private String viewString = null;
    private ResourceKey view;
    private ResourceService resourceService;
    private ScriptValue model;
    private Map<String, PortalViewFunction> viewFunctions;
    private final static Logger log = LoggerFactory.getLogger(FreemarkerProcessor.class);
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_25);

    public static void setupFreemarker(final FreemarkerConfig config) {
    	CONFIGURATION.setDefaultEncoding(config.encoding());
    }
    
    public static void setupFreemarker(ResourceService resourceService) {
    	CONFIGURATION.setLogTemplateExceptions(false);
        CONFIGURATION.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);

        CONFIGURATION.setSharedVariable("component", new ComponentDirective());

    	// Let's load resources using our custom Enonic-based Resource Loader
    	CONFIGURATION.setTemplateLoader(new ResourceTemplateLoader(resourceService));

        //CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);		// Throws exceptions to log file
    	CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);		// Shows exceptions on screen
    }

    public FreemarkerProcessor(Map<String, PortalViewFunction> viewFunctions)
    {
    	this.viewFunctions = viewFunctions;
    }

    public void setViewString( final String viewString )
    {
        this.viewString = viewString;
    }

    public void setView( final Object view )
    {
        if(view instanceof String){
            this.viewString = (String) view;
        }else{
            this.view = (ResourceKey) view;
        }
    }

    public void setModel( final ScriptValue model )
    {
        this.model = model;
    }

    public String process()
    {
        try {
            if(this.viewString != null){
                return doProcessViewString();
            }else{
                return doProcessView();
            }
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

    private String doProcessView() throws IOException, TemplateException
    {
        final Resource resource = resourceService.getResource( this.view );

        String key = resource.getKey().toString();
        Template template = CONFIGURATION.getTemplate(key);

        return doProcessTemplate(template);
    }


    private String doProcessViewString() throws IOException, TemplateException
    {
        return doProcessTemplate(new Template( "tmplName_" + System.currentTimeMillis(),  viewString , CONFIGURATION));
    }


    private String doProcessTemplate(Template template) throws IOException, TemplateException
    {
        final Map<String, Object> map = this.model != null ? this.model.getMap() : Maps.newHashMap();

        map.putAll(this.viewFunctions);

        StringWriter sw = new StringWriter();
        template.process(map, sw);

        return sw.toString();
    }


    private RuntimeException handleError( final TemplateException e )
    {
    	final ResourceKey resource = e.getTemplateSourceName() != null ? ResourceKey.from( e.getTemplateSourceName() ) : null;
    	
    	return ResourceProblemException.create()
    			.lineNumber(e.getLineNumber())
    			.resource(resource)
    			.cause(e)
    			.message(e.getMessageWithoutStackTop())
    			.build();
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
