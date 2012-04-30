/*
 * 2012 copyright Buongiorno SpA
 */
package com.lumata.os.groovy.jsonschema

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 *
 */
class JsonSchema {

	static boolean conformsSchema(Object instance, Object schema){
		schema = schema ?: instance.getSchema()

		if(!schema){
			return true
		}

		if(schema instanceof String){
			schema = resolve(schema)
		}

		if(schema.$ref){
			return conformsSchema(instance, JsonSchemaResolver.resolveSchema(schema.$ref, schema.getParent()))
		}

		if(isNull(instance)){
			return !schema.required
		}

		def type = schema.type

		if(type == 'array'){
			return validateArray(instance, schema)
		}
		if(type == 'string'){
			return isString(instance);
		}
		if(type == 'number'){
			return isNumber(instance);
		}
		if(type == 'integer'){
			return isInteger(instance);
		}
		if(type == 'boolean'){
			return isBoolean(instance);
		}

		if(type == 'object' || schema.properties != null){
			return isObject(instance) &&
			schema.properties.every { name, property ->
				def value = instance."$name"
				setParentIfNotNull(property, schema)
				return conformsSchema(value, property)
			}
		}

		return true
	}

	static boolean validateArray(Object value, Object schema){
		return isArray(value) && value.every { item ->
			def items = schema.items
			setParentIfNotNull(items, schema)
			return conformsSchema(item, items)
		}
	}


	static void setSchema(Object obj, Object jsonSchema){
		obj.getMetaClass().getSchema = { -> jsonSchema }
	}

	static boolean isString(value){
		return value == null || value instanceof String
	}

	static boolean isNumber(value){
		return value == null || value instanceof Number
	}

	static boolean isInteger(value){
		return value == null || value instanceof Integer
	}

	static boolean isBoolean(value){
		return value == null || value instanceof Boolean
	}

	static boolean isArray(value){
		return value == null || value.getClass().isArray() || value instanceof List;
	}

	static boolean isObject(value){
		return value == null || (!isString(value) && !isNull(value) && !isInteger(value) && !isBoolean(value) && !isArray(value));
	}

	static boolean isNull(value){
		return value == null
	}

	static Object resolve(String id){
		return JsonSchemaResolver.resolveSchema(id);
	}

	private static setParentIfNotNull(schema, parent){
		if(schema){
			schema.getMetaClass().getParent  = { -> parent }
		}
	}
}
