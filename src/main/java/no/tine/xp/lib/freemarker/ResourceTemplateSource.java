package no.tine.xp.lib.freemarker;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import com.enonic.xp.resource.Resource;

public class ResourceTemplateSource implements Closeable {
	private Resource resource;
	private Reader reader = null;

	public ResourceTemplateSource(Resource resource) {
		super();
		this.resource = resource;
	}

	public long getLastModified() {
		return this.resource.getTimestamp();
	}

	public Reader getReader() throws FileNotFoundException {
		this.reader = this.resource.openReader();
		return this.reader;
	}

	@Override
	public void close() throws IOException {
		if(this.reader != null) {
			this.reader.close();
		}
	}
}
