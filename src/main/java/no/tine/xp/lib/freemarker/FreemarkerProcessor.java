package no.tine.xp.lib.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import no.api.freemarker.java8.Java8ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.xp.resource.Resource;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.resource.ResourceProblemException;
import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.ScriptValue;
import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public final class FreemarkerProcessor {
	private ResourceKey view;
	private ResourceService resourceService;
	private ScriptValue model;
	private Map<String, PortalViewFunction> viewFunctions;
	private final static Logger log = LoggerFactory.getLogger(FreemarkerProcessor.class);

	private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_25);

	public static void setupFreemarker(final FreemarkerConfig config) {
		CONFIGURATION.setDefaultEncoding(config.encoding());
	}

	public static void setupFreemarker(ResourceService resourceService) {
		CONFIGURATION.setLogTemplateExceptions(false);
		CONFIGURATION.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);

		CONFIGURATION.setSharedVariable("component", new ComponentDirective());

		// Remove lookup for localized files (template.ftl => template_en_EN.ftl, template_en.ftl, template.ftl)
		// Should improve performance in dev mode, where some machines have slow file lookup
		CONFIGURATION.setLocalizedLookup(false);

		// Let's load resources using our custom Enonic-based Resource Loader
		CONFIGURATION.setTemplateLoader(new ResourceTemplateLoader(resourceService));

		//CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);		// Throws exceptions to log file
		CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);      // Shows exceptions on screen
		CONFIGURATION.setObjectWrapper(new Java8ObjectWrapper(Configuration.VERSION_2_3_25));
	}

	public FreemarkerProcessor(Map<String, PortalViewFunction> viewFunctions) {
		this.viewFunctions = viewFunctions;
	}

	public void setView(final ResourceKey view) {
		this.view = view;
	}

	public void setModel(final ScriptValue model) {
		this.model = model;
	}

	public String process() {
		try {
			return doProcess();
		} catch (final TemplateException e) {
			throw handleError(e);
		} catch (final IOException e) {
			throw handleError(e);
		} catch (final RuntimeException e) {
			throw handleError(e);
		}
	}

	public void setResourceService(final ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	private String doProcess() throws IOException, TemplateException {
		final Resource resource = resourceService.getResource(this.view);
		final Map<String, Object> map = this.model != null ? this.model.getMap() : Maps.newHashMap();

		map.putAll(this.viewFunctions);

		String key = resource.getKey().toString();
		Template template = CONFIGURATION.getTemplate(key);

		StringWriter sw = new StringWriter();
		template.process(map, sw);
		return sw.toString();
	}

	private RuntimeException handleError(final TemplateException e) {
		final ResourceKey resource = e.getTemplateSourceName() != null ? ResourceKey.from(e.getTemplateSourceName()) : null;

		return ResourceProblemException.create()
			.lineNumber(e.getLineNumber())
			.resource(resource)
			.cause(e)
			.message(e.getMessageWithoutStackTop())
			.build();
	}

	private RuntimeException handleError(final IOException e) {
		String error = "IO with the script.";
		log.error(error, e);
		return new RuntimeException(error, e);
	}

	private RuntimeException handleError(final RuntimeException e) {
		return e;
	}
}
