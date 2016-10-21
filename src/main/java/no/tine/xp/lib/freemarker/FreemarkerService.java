package no.tine.xp.lib.freemarker;

import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public final class FreemarkerService implements ScriptBean
{
    private ResourceService resourceService;

    public FreemarkerService()
    {
    }

    public Object newProcessor()
    {
        FreemarkerProcessor reactProcessor = new FreemarkerProcessor();
        reactProcessor.setResourceService( resourceService );
        return reactProcessor;
    }

    @Override
    public void initialize( final BeanContext context )
    {
        this.resourceService = context.getService( ResourceService.class ).get();
    }
}