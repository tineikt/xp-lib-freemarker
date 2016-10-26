package no.tine.xp.lib.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class ComponentDirective implements TemplateDirectiveModel {

    private static final String PARAM_PATH = "path";

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final String componentPath = (params.containsKey(PARAM_PATH) ? params.get(PARAM_PATH).toString() : "" );

        Writer out = env.getOut();
        out.append("<!--# COMPONENT " + componentPath + " -->");
    }
}
