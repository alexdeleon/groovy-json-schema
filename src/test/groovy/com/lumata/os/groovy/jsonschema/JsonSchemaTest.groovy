/*
 * 2012 copyright Buongiorno SpA
 */
package com.lumata.os.groovy.jsonschema

import static org.junit.Assert.*
import groovy.json.JsonSlurper

import org.junit.Ignore
import org.junit.Test



/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 *
 */
class JsonSchemaTest {

	@Test
	void testJson(){
		//create json object to validate
		def valid = new JsonSlurper().parseText('{"a":1}')
		def invalid = new JsonSlurper().parseText('{"a":"a"}')

		//create json schema
		def jsonSchema = new JsonSlurper().parseText('{"properties":{"a":{"type":"number"}}}')

		use(JsonSchema){
			valid.setSchema(jsonSchema)
			invalid.setSchema(jsonSchema)
			assert valid.conformsSchema() : "Json is not valid"
			assert !invalid.conformsSchema() : "Json is  valid"
		}
	}

	@Test
	void testValidObject(){
		//create json object to validate
		def jsonDoc = new TestObj()
		jsonDoc.a = 1

		//create json schema
		def jsonSchema = new JsonSlurper().parseText('{"properties":{"a":{"type":"number"}}}')

		use(JsonSchema){
			jsonDoc.setSchema(jsonSchema)
			assert jsonDoc.conformsSchema() : "Json is not valid"
		}
	}


	@Test
	void testNestedProperties(){
		//create json object to validate
		def valid = new JsonSlurper().parseText('{"a":{"b":1}}')
		def invalid = new JsonSlurper().parseText('{"a":1}')

		//create json schema
		def jsonSchema = new JsonSlurper().parseText('{"properties":{"a":{"type":"object", "properties":{"b":{"type":"number"}}}}}')

		use(JsonSchema){
			valid.setSchema(jsonSchema)
			invalid.setSchema(jsonSchema)
			assert valid.conformsSchema() : "Json is not valid"
			assert !invalid.conformsSchema() : "Json is valid"
		}
	}

	@Test
	void testArrays(){
		//create json object to validate
		def valid = new JsonSlurper().parseText('{"a":[1,2]}')
		def invalid = new JsonSlurper().parseText('{"a":1}')

		//create json schema
		def jsonSchema = new JsonSlurper().parseText('{"properties":{"a":{"type":"array", "items":{"type":"number"}}}}')

		use(JsonSchema){
			valid.setSchema(jsonSchema)
			invalid.setSchema(jsonSchema)
			assert valid.conformsSchema() : "Json is not valid"
			assert !invalid.conformsSchema() : "Json is valid"
		}
	}

	@Test
	void testRef(){
		def json = new JsonSlurper().parseText('{"b":{ "a" : 1 }}')
		use(JsonSchema){
			json.schema  = 'classpath:///testSchemaWithRef.json'.resolve()
			assert json.conformsSchema()
		}
	}

	@Test
	void testNullSchema(){
		def json = new JsonSlurper().parseText('{"b":{ "a" : 1 }}')
		use(JsonSchema){
			json.schema  = null
			assert json.conformsSchema()
		}
	}

	@Ignore("The http://json-schema.org/card schema has a syntaxt error. Disabling the test until it is fixed.")
	@Test
	void testReadmeExample1(){
		def json = new JsonSlurper().parseText("""\
			{
				"givenName" : "Alexander",
				"familyName" : "De Leon"
			}
		""")

		use(JsonSchema){
			json.schema  = 'http://json-schema.org/card'.resolve()
			assert json.conformsSchema()
		}
	}


	class TestObj{
		int a
	}


}
