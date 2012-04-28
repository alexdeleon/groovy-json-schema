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

		//must be an object then
		boolean valid = isObject(instance)
		if(valid){
			schema.properties.each{ name, property ->
				def value =  instance."$name"
				if(property){
					property.getMetaClass().getParent = { -> instance.getSchema() }
				}
				valid &= conformsSchema(value, property)
			}
		}
		return valid
	}

	static boolean validateArray(Object value, Object schema){
		boolean valid = true;
		valid &= isArray(value);
		if(valid && value != null){
			for(def item : value){
				def items = schema.items
				if(items){
					items.getParent = { -> schema }
				}
				valid &= conformsSchema(item, items)
			}
		}
		return valid;
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
}
