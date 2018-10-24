# Arquillian Tutorial
## Motivation
Im javaEE bereich werden komplexere Konstrukte verwendet wie zb. CDI
## Testrunner
- Test runner sind die die 'Brücke' zwischen Arquillian und Test-Frameworks 
- zb. für : Junit, testNG
- Verwendung:
	- JUnit --> `@RunWith(Arquillian.class)` 
	- TestNG --> Testklasse muss von `Arquillian` erben.
## Deployment
## Container 
- Ort an in dem die Laufzeitumgebung sich befindet.
-> zb. Wildfly
- Arquillian kann von hause aus schon mit vielen Containern kommunizieren
- Sollte ein Container nicht unterstützt werden so liefert Arquillian ein SPI.
- Arquillian kann von hause aus schon mit vielen Containern kommunizieren
- Sollte ein Container nicht unterstützt werden so liefert Arquillian ein SPI.

### embedded
- in der selben JVM wie der Testrunner
- Container wird von Arquillian gemanaged
- Tests werden mit einem lokalen Protokoll aufgerufen
### remote
- container jvm separiert von Testrunner
- arquillian bindet sich an den Container und deployed das von ShrinkWrap erstellte Archiv. 
- Tests werden mit einem Remote Protokoll (zb. Servlet, JMX) aufgerufen. 

### managed
- sehr ähnich zu remote containern
- Unterschied ist das die Lebenszyklen des Containers von Arquillian gehandelt werden

## ShrinkWrap
- Java API zum erstellen von Archiven 
- erstellte Microdeployments werden von Arquillian genutzt um die Testumgebung zu generieren.
- liefert ein virtuelles Dateiensystem

```java
JavaArchive myArchive = ShrinkWrap
			.create(JavaArchive.class, "myJavaArchive.jar")
 			.addClasses(Blender.class, PizzaWheel.class)
  			.addResource("blender.properties");
```

Dieses Archiv könnte nun von Arquillian verwendet werden um zu testenden Komponenten zu bündeln.

Die Abhängigkeit zu ShrinkWrap kann über Maven aufgelöst werden:

Innerhalb deines POMS:

```xml
<dependency>
	<groupId>org.jboss.shrinkwrap</groupId>
	<artifactId>shrinkwrap-depchain</artifactId>
	<version>${version.shrinkwarp}</version>
</dependency>
```
Es ist jederzeit möglich die Inhalte des erstellten Archives zu inspiziern:
```java
System.out.println(myArchive.toString(true));
```
Das 'true' bewirkt hierbei eine Rekursive Ausgabe aller Inhalte.


Wechseln zwischen den Archivarten: 
```java
 myArchive.as(WebArchive.class).addWebResource(....)
```

Ebenfalls möglich importieren von schon erzeugten Archiven:

```java
JavaArchive alreadyCreatedArchive = ShrinkWrap
					.create(ZipImporter.class, "alreadyCreatedArchive.jar")
					.importFrom(
						new File("path/to/file/alreadyCreatedArchive.jar")
 					)
					.as(JavaArchive.class);
```

Die andere Richtung ist ebenfalls möglich hierbei wird der *ZipExporter* genutzt.
			
## Modi			
### In-Container
Default Modus in dem Arquillian operiert:
1. Arquillian reichert das Testarchiv an mit der Test-Infrastruktur an
2. Verbindung zum Container, deployment des Archives
3. Ausführung der Tests mittels jeweiligen Testrunner
4. Arquillian erhält Testergebnisse
Hierbei ist der Testrunner sozusagen der Client des Containers
### Client	
1. Test-Archiv wird ohne weitere Vorbereitungen in den COntainer deployed
2. Test wird in der gleichen JVM wie Testrunner ausgeführt

Hierbei sind die einzelnen Testcases die Clienten des Containers. Ideal für Web UI testing..
			
## zusätzlcihes
--> @Test @OperateOnDeployment("werner")... multiple deployments
--> testing persistence
--> Modi: client, in-container