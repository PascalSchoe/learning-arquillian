package org.pschoe.tutorials;

import java.io.PrintStream;


/*
 * Bei der Verwendung als CDI-Bean wird so getan als würde dies Klasse
 * weitere JavaEE Konstrukte verwendet wie zb. DI usw.
 */

public class Greeter {
	public void greet(PrintStream to, String name) {
		to.println(createGreeting(name));
	}
	
	public String createGreeting(String name) {
		return "Hello, " + name + "!";
	}
}
