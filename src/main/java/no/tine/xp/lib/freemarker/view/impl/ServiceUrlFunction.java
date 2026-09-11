package no.tine.xp.lib.freemarker.view.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import no.tine.xp.lib.freemarker.view.ViewFunction;
import no.tine.xp.lib.freemarker.view.ViewFunctionParams;
import com.enonic.xp.portal.url.PortalUrlService;
import com.enonic.xp.portal.url.ServiceUrlParams;

import static no.tine.xp.lib.freemarker.view.ParamsHelper.singleValue;

@Component(immediate = true)
public final class ServiceUrlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public ServiceUrlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "serviceUrl";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final ServiceUrlParams urlParams = new ServiceUrlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.type( singleValue( arguments, "_type" ) );
        urlParams.service( singleValue( arguments, "_service" ) );
        urlParams.application( singleValue( arguments, "_application" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.serviceUrl( urlParams );
    }
}
