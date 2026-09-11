package no.tine.xp.lib.freemarker.view.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import no.tine.xp.lib.freemarker.view.ViewFunction;
import no.tine.xp.lib.freemarker.view.ViewFunctionParams;
import com.enonic.xp.portal.url.AttachmentUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;

import static no.tine.xp.lib.freemarker.view.ParamsHelper.singleValue;

@Component(immediate = true)
public final class AttachmentUrlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public AttachmentUrlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "attachmentUrl";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final AttachmentUrlParams urlParams = new AttachmentUrlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.type( singleValue( arguments, "_type" ) );
        urlParams.id( singleValue( arguments, "_id" ) );
        urlParams.path( singleValue( arguments, "_path" ) );
        urlParams.name( singleValue( arguments, "_name" ) );
        urlParams.label( singleValue( arguments, "_label" ) );
        urlParams.download( singleValue( arguments, "_download" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.attachmentUrl( urlParams );
    }
}
