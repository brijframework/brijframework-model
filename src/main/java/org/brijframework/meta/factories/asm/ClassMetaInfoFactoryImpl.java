package org.brijframework.meta.factories.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.brijframework.group.Group;
import org.brijframework.meta.factories.ClassMetaInfoFactory;
import org.brijframework.meta.helper.MetaBuilderHelper;
import org.brijframework.meta.info.ClassMetaInfo;
import org.brijframework.meta.info.FieldMetaInfo;
import org.brijframework.meta.setup.ClassMetaSetup;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.Model;

public class ClassMetaInfoFactoryImpl extends MetaInfoFactoryImpl<ClassMetaInfo> implements ClassMetaInfoFactory {
	
	protected ClassMetaInfoFactoryImpl() {
	}

	protected static ClassMetaInfoFactoryImpl factory;

	@Assignable
	public static ClassMetaInfoFactoryImpl getFactory() {
		if (factory == null) {
			factory = new ClassMetaInfoFactoryImpl();
		}
		return factory;
	}

	@Override
	public ClassMetaInfoFactoryImpl clear() {
		if (getCache() != null) {
			getCache().clear();
		}
		return this;
	}

	public ClassMetaInfo getClassInfo(String id) {
		for(Entry<String, ClassMetaInfo> entry:getCache().entrySet()) {
			if(entry.getKey().equals(id)) {
				return entry.getValue();
			}
		}
		return getContainer(id);
	}

	public void register(Class<?> target, ClassMetaInfo metaInfo) {
		this.getCache().put(metaInfo.getId(), metaInfo);
		loadContainer(metaInfo);
	}
	
	public void loadContainer(ClassMetaInfo metaInfo) {
		if (getContainer() == null) {
			return;
		}
		Group group = getContainer().load(metaInfo.getOwner().getName());
		if(!group.containsKey(metaInfo.getId())) {
			group.add(metaInfo.getId(), metaInfo);
		}else {
			group.update(metaInfo.getId(), metaInfo);
		}
		getContainer().merge(metaInfo.getOwner().getName(), group);
	}

	public ClassMetaInfo getContainer(String modelKey) {
		if (getContainer() == null) {
			return null;
		}
		return  getContainer().find(modelKey);
	}

	@Override
	public List<ClassMetaInfo> getClassInfoList(Class<?> cls) {
		return null;
	}

	@Override
	public List<ClassMetaInfo> getClassInfoList(Class<?> target, String parentID) {
		return null;
	}

	public FieldMetaInfo getFieldMeta(String simpleName, String _keyPath) {
		ClassMetaInfo classMeta=getClassInfo(simpleName);
		if(classMeta==null) {
			return null;
		}
		return classMeta.getPropertyInfo(_keyPath);
	}

	public void register(Class<?> target, Model metaSetup) {
		ClassMetaInfo metaInfo = MetaBuilderHelper.getModelInfo(getContainer(),target, metaSetup);
		this.getCache().put(metaInfo.getId(), metaInfo);
		System.err.println("Meta Info    : "+metaInfo.getId());
		loadContainer(metaInfo);
	}
	
	public void register(Class<?> target, ClassMetaSetup metaSetup) {
		ClassMetaInfo metaInfo = MetaBuilderHelper.getModelInfo(getContainer(),target, metaSetup);
		this.getCache().put(metaInfo.getId(), metaInfo);
		System.err.println("Meta Info    : "+metaInfo.getId());
		loadContainer( metaInfo);
	}
	

	public List<ClassMetaInfo> getMetaList(Class<?> model) {
		List<ClassMetaInfo> list=new ArrayList<>();
		for(ClassMetaInfo meta:getCache().values()) {
			if(model.isAssignableFrom(meta.getTarget())) {
				list.add(meta);
			}
		}
		return list;
	}
	
	@Override
	public ClassMetaInfoFactoryImpl loadFactory() {
		return this;
	}

}