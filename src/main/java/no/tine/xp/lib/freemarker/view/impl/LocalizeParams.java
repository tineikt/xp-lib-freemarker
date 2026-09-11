package no.tine.xp.lib.freemarker.view.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.portal.PortalRequest;

import static com.google.common.base.Strings.isNullOrEmpty;

class LocalizeParams
{
    private String key;

    private Locale locale;

    private ApplicationKey application;

    private Object[] params;

    private static final Pattern VALUES_PATTERN = Pattern.compile( "^\\{.*\\}$" );

    LocalizeParams( final PortalRequest request )
    {
        if ( request != null )
        {
            if ( request.getSite() != null && request.getSite().getLanguage() != null )
            {
                this.locale = request.getSite().getLanguage();
            }
            this.application = request.getApplicationKey();
        }
    }

    public void key( final String key )
    {
        this.key = key;
    }

    public ApplicationKey getApplicationKey()
    {
        return this.application;
    }

    public void locale( final String value )
    {
        final String locale = Strings.emptyToNull( value );
        if ( locale != null )
        {
            this.locale = Locale.forLanguageTag( value );
        }
    }

    public void values( final Collection<String> values )
    {
        if ( values.size() == 1 && VALUES_PATTERN.matcher( values.iterator().next() ).find() )
        {
            parseValues( values.iterator().next() );
        }
        else
        {
            handleArray( values );
        }
    }

    private void handleArray( final Collection<String> values )
    {
        final List<Object> params = new ArrayList<>( values );

        this.params = params.toArray();
    }

    private void parseValues( final String valuesAsString )
    {
        if ( isNullOrEmpty( valuesAsString ) )
        {
            this.params = new ArrayList<>().toArray();
            return;
        }

        if ( !VALUES_PATTERN.matcher( valuesAsString ).find() )
        {
            throw new IllegalArgumentException( "Wrong format on values-parameter: " + valuesAsString );
        }

        final String argumentList = valuesAsString.substring( 1, valuesAsString.length() - 1 );

        this.params = Arrays.asList( argumentList.split( "," ) ).toArray();
    }

    public void application( final String value )
    {
        final String application = Strings.emptyToNull( value );
        if ( application != null )
        {
            this.application = ApplicationKey.from( application );
        }
    }

    public String getKey()
    {
        return key;
    }


    public Locale getLocale()
    {
        return locale;
    }

    public Object[] getParams()
    {
        return params;
    }
}
