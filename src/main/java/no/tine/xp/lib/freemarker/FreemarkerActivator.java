package no.tine.xp.lib.freemarker;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, configurationPid = "no.tine.xp.freemarker")
public final class FreemarkerActivator {
	private final static Logger log = LoggerFactory.getLogger(FreemarkerActivator.class);

	@Activate void activate ( final BundleContext context, final FreemarkerConfig config) {
		log.info("Configuring freemarker");
		FreemarkerProcessor.setupFreemarker(config);
	}





}
