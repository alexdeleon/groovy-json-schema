/*
 * 2012 copyright Buongiorno SpA
 */
package com.lumata.os.groovy.jsonschema

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 *
 */
class JsonSchemaValidator {

	static boolean conformsSchema(Object obj){
		def schema =  obj.getJsonSchema()
		if(!schema){
			return true
		}

		if(schema.$ref){
			setSchema(obj, JsonSchemaResolver.resolveSchema(schema.$ref, schema.parent))
			return conformsSchema(obj)
		}

		if(schema.required && isNull(obj)){
			return false
		}

		def type = schema.type

		if(type == 'array'){
			return validateArray(obj, schema)
		}
		if(type == 'string'){
			return isString(obj);
		}
		if(type == 'number'){
			return isNumber(obj);
		}
		if(type == 'integer'){
			return isInteger(obj);
		}
		if(type == 'boolean'){
			return isBoolean(obj);
		}

		//must be an object then
		boolean valid = isObject(obj)
		if(valid){
			schema.properties.each{ name, property ->
				def value =  obj."$name"
				if(property){
					property.parent = obj.getJsonSchema()
				}
				setSchema(value, property)
				valid &= conformsSchema(value)
			}
		}
		return valid
	}

	static boolean validateArray(Object value, schema){
		boolean valid = true;
		valid &= isArray(value);
		if(valid && value != null){
			for(def item : value){
				def items = schema.items
				if(items){
					items.parent = schema
				}
				setSchema(item, items)
				valid &= conformsSchema(item)
			}
		}
		return valid;
	}

	static void setSchema(Object obj, Object schema){
		obj.getMetaClass().getJsonSchema = { -> schema }
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
