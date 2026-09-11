package no.tine.xp.lib.freemarker.view.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import no.tine.xp.lib.freemarker.view.ViewFunction;
import no.tine.xp.lib.freemarker.view.ViewFunctionParams;
import com.enonic.xp.portal.url.AssetUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;

import static no.tine.xp.lib.freemarker.view.ParamsHelper.singleValue;

@Component(immediate = true)
public final class AssetUrlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public AssetUrlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "assetUrl";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final AssetUrlParams urlParams = new AssetUrlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.path( singleValue( arguments, "_path" ) );
        urlParams.application( singleValue( arguments, "_application" ) );
        urlParams.type( singleValue( arguments, "_type" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.assetUrl( urlParams );
    }
}
