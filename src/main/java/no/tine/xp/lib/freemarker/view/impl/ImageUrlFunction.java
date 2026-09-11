package no.tine.xp.lib.freemarker.view.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import no.tine.xp.lib.freemarker.view.ViewFunction;
import no.tine.xp.lib.freemarker.view.ViewFunctionParams;
import com.enonic.xp.portal.url.ImageUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;

import static no.tine.xp.lib.freemarker.view.ParamsHelper.singleValue;

@Component(immediate = true)
public final class ImageUrlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public ImageUrlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "imageUrl";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final ImageUrlParams urlParams = new ImageUrlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.type( singleValue( arguments, "_type" ) );
        urlParams.id( singleValue( arguments, "_id" ) );
        urlParams.path( singleValue( arguments, "_path" ) );
        urlParams.format( singleValue( arguments, "_format" ) );
        urlParams.quality( singleValue( arguments, "_quality" ) );
        urlParams.filter( singleValue( arguments, "_filter" ) );
        urlParams.background( singleValue( arguments, "_background" ) );
        urlParams.scale( singleValue( arguments, "_scale" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.imageUrl( urlParams );
    }
}
