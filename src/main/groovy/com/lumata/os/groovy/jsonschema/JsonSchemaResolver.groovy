/*
 * 2012 copyright Buongiorno SpA
 */
package com.lumata.os.groovy.jsonschema


import groovy.json.JsonSlurper

import java.util.HashMap

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 *
 */
class JsonSchemaResolver {

	private static Map<String, Object> loadedSchemas = new HashMap<String, Object>()


	static Object resolveSchema(uri, parentSchema = null){
		uri = resolveUri(uri, parentSchema);
		if(!loadedSchemas.containsKey(uri)){
			tryToLoad(uri);
		}
		return loadedSchemas.get(uri);
	}

	protected static String resolveUri(uri, parent){
		if(!parent){
			return uri
		}
		def baseUri =  parent.resolvedId
		if(!baseUri){
			baseUri = parent.id
		}
		return resolveRelativeUri(uri, baseUri);
	}


	protected static void tryToLoad(String uri) {
		InputStream input = null;
		if(uri ==~ /^classpath:\/\/.*/){
			input = JsonSchemaResolver.class.getResourceAsStream(uri.substring(12))
		}else if(uri ==~ /^file:\/\/.*/){
			input = new FileInputStream(uri.substring(7))
		}else if(uri ==~ /^http(?:s)?:\/\/.*/){
			input = new URL(uri).openStream()
		}else{
			//TODO: other protocols?
			return
		}

		try{
			JsonSlurper sluper = new JsonSlurper()
			Object schema = sluper.parse(new InputStreamReader(input))
			if(schema){
				schema.resolvedId = uri
				loadedSchemas.put(uri, schema)
			}
		}catch(Exception e){
			//it's not here
		}
	}

	private static String resolveRelativeUri(String uri, String base){
		if(!base || isAbsolute(uri)){
			return uri
		}
		if(base.endsWith("/")){
			return base+uri;
		}
		return base.substring(0, base.lastIndexOf("/")+1)+uri;
	}

	private static boolean isAbsolute(uri){
		return uri ==~ /^.*:\/\/.*/
	}
}
