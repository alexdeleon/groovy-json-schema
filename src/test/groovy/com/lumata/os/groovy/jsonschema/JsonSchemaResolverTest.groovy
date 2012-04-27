/*
 * 2012 copyright Buongiorno SpA
 */
package com.lumata.os.groovy.jsonschema;

import static org.junit.Assert.*;

import org.junit.Test


/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 *
 */
class JsonSchemaResolverTest {

	@Test
	public void testLoadFromClassPath(){
		def schema = JsonSchemaResolver.resolveSchema("classpath:///testSchema.json")
		assert schema != null
		assert schema.properties.a.type == 'number'
	}

	@Test
	public void testLoadFromWeb(){
		def schema = JsonSchemaResolver.resolveSchema("http://json-schema.org/address")
		assert schema != null
		assert schema.type == 'object'
	}

	@Test
	public void testFromFile(){
		URL fileUrl = JsonSchemaResolverTest.class.getResource("/testSchema.json")
		def schema = JsonSchemaResolver.resolveSchema('file://'+fileUrl.path)
		assert schema != null
		assert schema.properties.a.type == 'number'
	}

	@Test
	public void testRelativeSchemaUris(){
		def schema = JsonSchemaResolver.resolveSchema("classpath:///testSchemaWithRef.json")
		def refSchema = JsonSchemaResolver.resolveSchema(schema.properties.b.$ref, schema)
		assert refSchema != null
		assert refSchema.properties.a.type == 'number'
	}
}
