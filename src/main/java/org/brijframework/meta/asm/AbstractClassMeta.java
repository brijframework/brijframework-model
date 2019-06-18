package org.brijframework.meta.asm;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.meta.reflect.FieldGroup;

public abstract class AbstractClassMeta extends AbstractMetaInfo<Class<?>> implements ClassMeta{
	
	private Map<String,FieldGroup>  properties;
	
	private Class<?> target;
	private String type;
	
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String getType() {
		return type;
	}
	
	public void setTarget(Class<?> target) {
		this.target = target;
	}

	@Override
	public Class<?> getTarget() {
		return target;
	}

	@Override
	public Map<String, FieldGroup> getProperties() {
		if(properties==null) {
			properties=new HashMap<String, FieldGroup>();
		}
		return properties;
	}
}
