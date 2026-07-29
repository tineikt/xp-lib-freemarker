package no.tine.xp.lib.freemarker.view;

public interface ViewFunction
{
    String getName();

    Object execute( ViewFunctionParams params );
}
