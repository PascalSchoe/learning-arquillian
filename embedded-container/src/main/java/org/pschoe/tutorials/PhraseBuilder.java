package org.pschoe.tutorials;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jms.Message;

public class PhraseBuilder {
	private Map<String, String> templates;
	
	public String buildPhrase(String id, Object... objects){
		return MessageFormat.format(templates.get(id), objects);
	}
	
	@PostConstruct
	public void initialize(){
		templates = new HashMap<String, String>();
		templates.put("hello", "Hello, {0}!");
	}

}
