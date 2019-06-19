package org.brijframework.meta.factories.asm;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.group.Group;
import org.brijframework.meta.MetaInfo;
import org.brijframework.meta.factories.MetaFactory;

public abstract class MetaInfoFactoryImpl<T extends MetaInfo<?>> implements MetaFactory<T>{

	private ConcurrentHashMap<String, T> cache = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unchecked")
	public ConcurrentHashMap<String, T> getCache() {
		if(cache==null) {
			return null;
		}
		if(getContainer()!=null) {
			for(Entry<Object, Group>  entry:getContainer().getCache().entrySet()) {
				Group  group=entry.getValue();
				if(group!=null)
				group.getCache().forEach((key,value)->{
					cache.put((String)key, (T)value);
				});
			}
		}
		return cache;
	}

	
	private Container container;
	
	protected MetaInfoFactoryImpl() {
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

	@Override
	public MetaInfoFactoryImpl<T> clear() {
		if (getCache() != null) {
			getCache().clear();
		}
		return this;
	}

	public void loadContainer(T metaInfo) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(metaInfo.getName());
		if(!group.containsKey(metaInfo.getId())) {
			group.add(metaInfo.getId(), metaInfo);
		}else {
			group.update(metaInfo.getId(), metaInfo);
		}
	}

	public T getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return getContainer().find(modelKey);
	}

	public T getMetaInfo(String id) {
		for(Entry<String, T> entry:getCache().entrySet()) {
			if(entry.getKey().equals(id)) {
				return entry.getValue();
			}
		}
		return getContainer(id);
	}

}