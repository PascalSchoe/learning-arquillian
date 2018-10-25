package unit;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pschoe.tutorials.Greeter;
import org.pschoe.tutorials.PhraseBuilder;

@RunWith(Arquillian.class)
public class GreeterTest {
	
	@Inject
	Greeter greeter;

	@Deployment
	public static JavaArchive createDeployment(){
		return ShrinkWrap
				.create(JavaArchive.class)
				.addClasses(Greeter.class, PhraseBuilder.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
 
	@Test
	public void should_create_greeting(){
		Assert.assertEquals(
				"Hello, Earthling!",
				greeter.createGreeting("Earthling")
		);
		
		
		System.out.println("==============================");
		greeter.greet(System.out, "Earthling");
		System.out.println("==============================");
	}
}
