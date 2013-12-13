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
