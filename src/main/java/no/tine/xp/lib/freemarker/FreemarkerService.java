package no.tine.xp.lib.freemarker;

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

    public FreemarkerService()
    { }

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

    	// Make these portal functions available as user-defined functions. Should now be possible to do: <@imageUrl id="11" scale="width(200)"/>
    	List<String> functionNames = Arrays.asList(
    		new String[] { "pageUrl", "imageUrl", "assetUrl", "attachmentUrl", "componentUrl", "serviceUrl", "processHtml", "imagePlaceholder" }
    	);

    	Map<String, PortalViewFunction> functions = new HashMap<>();
    	functionNames.forEach(fnName ->
    		functions.put(fnName, new PortalViewFunction(fnName, vfs, pr))
    	);

        return functions;
    }

}
