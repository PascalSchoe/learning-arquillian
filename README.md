# Arquillian Tutorial
Sobald du komplexere Projekte testen möchtest die zum Beispiel mit *Dependency Injection* arbeiten oder auf Datenbank operieren dann ist es nur mit viel Aufwand und [Mocks](https://site.mockito.org/) möglich diese zu testen. Und dort kommt *Arquillian* ins Spiel! Seine Aufgaben sind mitunter:
- Container Management
- Anreicherung der Test Klassen
- Resultate zurückgeben

## Testrunner
- Test runner sind die die 'Brücke' zwischen Arquillian und Test-Frameworks 
- zb. für : *JUnit*, *TestNG*
- Verwendung:
	- JUnit --> `@RunWith(Arquillian.class)` 
	- TestNG --> Testklasse muss von `Arquillian` erben.
## Container 
- Ort an in dem die Laufzeitumgebung sich befindet.
-> zb. Wildfly
- Arquillian kann von hause aus schon mit vielen Containern kommunizieren
- Sollte ein Container nicht unterstützt werden so liefert Arquillian ein SPI.
- Arquillian kann von hause aus schon mit vielen Containern kommunizieren
- Sollte ein Container nicht unterstützt werden so liefert Arquillian ein SPI.

### embedded
- in der selben *JVM* wie der *Testrunner*
- Container wird von Arquillian gemanaged
- Tests werden mit einem lokalen Protokoll aufgerufen
### remote
- *Container* *JVM* separiert von Testrunner
- Arquillian bindet sich an den *Container* und deployed das von *ShrinkWrap* erstellte Archiv. 
- Tests werden mit einem Remote Protokoll (zb. *Servlet*, *JMX*) aufgerufen. 

### managed
- sehr ähnich zu *remote containern*
- Unterschied ist das die Lebenszyklen des Containers von Arquillian gehandelt werden

## ShrinkWrap
- Java API zum Erstellen von Archiven 
- Erstellte Microdeployments werden von Arquillian genutzt um die Testumgebung zu generieren.
- Liefert ein virtuelles Dateiensystem

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
1. Test-Archiv wird ohne weitere Vorbereitungen in den Container deployed
2. Test wird in der gleichen JVM wie Testrunner ausgeführt

Hierbei sind die einzelnen Testcases die Klienten des Containers. Ideal für Web UI testing..