package no.tine.xp.lib.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.enonic.xp.portal.PortalRequest;
import com.enonic.xp.portal.view.ViewFunctionParams;
import com.enonic.xp.portal.view.ViewFunctionService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PortalViewFunction implements TemplateDirectiveModel {

	protected ViewFunctionService viewFunctionService;
    protected PortalRequest portalRequest;
    protected String name;
    
    public PortalViewFunction(String name, ViewFunctionService vfs, PortalRequest pr) {
		this.name = name;
		this.viewFunctionService = vfs;
		this.portalRequest = pr;
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	executeGenerics(env, (Map<String, TemplateModel>)params, loopVars, body);
    }
    
	public void executeGenerics(Environment env, Map<String, TemplateModel> params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		List<String> args = params.entrySet().stream()
				.map(e -> "_" + e.getKey() + "=" + e.getValue().toString())
				.collect(Collectors.toList());
		
		final ViewFunctionParams vfParams = new ViewFunctionParams().name( name ).args( args ).portalRequest( this.portalRequest );
		
		Writer out = env.getOut();
		out.append(this.viewFunctionService.execute( vfParams ).toString());
		out.close();
	}

	
}
