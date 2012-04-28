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