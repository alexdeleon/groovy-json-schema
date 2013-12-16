Groovy JSON Schema
==================

An implemetation of the json schema specification : http://json-schema.org/

Basic Usage
-----------

		def json = new JsonSlurper().parseText("""\
			{
				"givenName" : "Alexander",
				"familyName" : "De Leon"
			}
		""")

		use(JsonSchema){
			json.schema  = 'http://json-schema.org/card'
			assert json.conformsSchema()
		}

Loading Schemas
----------------

Schemas can be loaded from

* URL: 
```json.schema  = 'http://json-schema.org/card'```
* Classpath: 
```json.schema  = 'classpath://schemas/card.json'``
* File:
```json.schema  = 'file://opt/schemas/card.json'``

Install using Maven
--------------------

Add the following to your pom.xml

```
<repositories>
	<repository>
		<id>alexdeleon.release.repository</id>
		<url>https://repository-alexdeleon.forge.cloudbees.com/release</url>
	</repository>
</repositories>
```

```
<dependency>
	<groupId>groovy-json-schema</groupId>
	<artifactId>groovy-json-schema</artifactId>
	<version>0.0.1</version>
</dependency>
```