package org.pschoe.tutorials;

import java.io.PrintStream;

import javax.inject.Inject;


/*
 * Bei der Verwendung als CDI-Bean wird so getan als würde dies Klasse
 * weitere JavaEE Konstrukte verwendet wie zb. DI usw.
 */

public class Greeter {
	private PhraseBuilder phraseBuilder;
	
	@Inject
	public Greeter(PhraseBuilder phraseBuilder){
		this.phraseBuilder = phraseBuilder;
	}

	public void greet(PrintStream to, String name) {
		to.println(createGreeting(name));
	}
	
	public String createGreeting(String name) {
		return phraseBuilder.buildPhrase("hello", name);
	}
}
