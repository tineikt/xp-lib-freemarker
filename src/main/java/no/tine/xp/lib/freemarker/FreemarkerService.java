package no.tine.xp.lib.freemarker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enonic.xp.portal.PortalRequest;
import com.enonic.xp.portal.PortalRequestAccessor;
import com.enonic.xp.portal.view.ViewFunctionService;
import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public final class FreemarkerService implements ScriptBean
{
    private ResourceService resourceService;
    private BeanContext context;

    private List<ViewFunctionSpec> viewFunctions;
    
    public FreemarkerService()
    { 
    	viewFunctions = new ArrayList<>();
    	
    	// Make these portal functions available as user-defined functions. Should now be possible to do: <@imageUrl id="11" scale="width(200)" customParam="123" />
    	viewFunctions.add(new ViewFunctionSpec("pageUrl", "id", "path", "type"));
    	viewFunctions.add(new ViewFunctionSpec("imageUrl", "id", "path", "format", "scale", "quality", "background", "filter", "type"));
    	viewFunctions.add(new ViewFunctionSpec("assetUrl", "path", "application", "type"));
    	viewFunctions.add(new ViewFunctionSpec("attachmentUrl", "id", "path", "name", "label", "download", "type"));
    	viewFunctions.add(new ViewFunctionSpec("componentUrl", "id", "path", "component", "type"));
    	viewFunctions.add(new ViewFunctionSpec("serviceUrl", "service", "application", "type"));
    	viewFunctions.add(new ViewFunctionSpec("processHtml", "value", "type"));
    	viewFunctions.add(new ViewFunctionSpec("imagePlaceholder"));
    }

    @Override
    public void initialize( final BeanContext context ) {
        this.context = context;
        this.resourceService = context.getService( ResourceService.class ).get();
        FreemarkerProcessor.setupFreemarker(this.resourceService);
    }

    public Object newProcessor() {
        FreemarkerProcessor freemarkerProcessor = new FreemarkerProcessor(createViewFunctions());
        freemarkerProcessor.setResourceService( resourceService );
        return freemarkerProcessor;
    }

    private Map<String, PortalViewFunction> createViewFunctions() {
    	ViewFunctionService vfs = this.context.getService( ViewFunctionService.class ).get();
    	PortalRequest pr = PortalRequestAccessor.get();

    	Map<String, PortalViewFunction> functions = new HashMap<>();
    	    	
    	viewFunctions.forEach(fn ->
    		functions.put(fn.getName(), new PortalViewFunction(fn, vfs, pr))
    	);

        return functions;
    }

}
