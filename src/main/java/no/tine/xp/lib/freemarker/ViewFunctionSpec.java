package no.tine.xp.lib.freemarker;

import java.util.Arrays;
import java.util.List;

public class ViewFunctionSpec {
	private String name;
	private List<String> predefinedParameters;
	
	public ViewFunctionSpec(String name, String ...predef) {
		super();
		this.name = name;
		this.predefinedParameters = Arrays.asList(predef);
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getPredefinedParameters() {
		return predefinedParameters;
	}
	public void setPredefinedParameters(List<String> predefinedParameters) {
		this.predefinedParameters = predefinedParameters;
	}
	
	
}
