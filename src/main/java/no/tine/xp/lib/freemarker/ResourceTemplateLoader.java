package no.tine.xp.lib.freemarker;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.resource.Resource;
import com.enonic.xp.resource.ResourceKeys;
import com.enonic.xp.resource.ResourceService;

import freemarker.cache.TemplateLoader;

public class ResourceTemplateLoader implements TemplateLoader {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private ResourceService resourceService; 
	
	public ResourceTemplateLoader(ResourceService rs) {
		this.resourceService = rs;
	}
	
	@Override
	public Object findTemplateSource(String name) throws IOException {
		logger.trace("Looking for file: [" + name + "]");
		
		String[] parts = StringUtils.split(name, ':');
		if(parts.length != 2) {
			return null;
		}
		
		ResourceKeys keys = resourceService.findFiles(ApplicationKey.from(parts[0]), parts[1]);
		if(keys.isEmpty()) {
			return null;
		}
		
		logger.warn("Found " + keys.getSize() + " file(s) matching " + name);
		
		final Resource resource = this.resourceService.getResource(keys.get(0));
		return new ResourceTemplateSource(resource);
	}

	@Override
	public long getLastModified(Object templateSource) {
		ResourceTemplateSource source = (ResourceTemplateSource)templateSource;
		return source.getLastModified();
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		ResourceTemplateSource source = (ResourceTemplateSource)templateSource;
		return source.getReader();
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		ResourceTemplateSource source = (ResourceTemplateSource)templateSource;
		source.close();
	}

}
