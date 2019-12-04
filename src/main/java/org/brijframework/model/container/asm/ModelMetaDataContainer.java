package org.brijframework.model.container.asm;

import org.brijframework.container.impl.module.AbstractModuleContainer;
import org.brijframework.group.Group;
import org.brijframework.model.container.ModelContainer;
import org.brijframework.model.factories.metadata.TypeModelMetaDataFactory;
import org.brijframework.model.group.ModelMetaDataGroup;
import org.brijframework.support.config.DepandOn;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=ModelMetaResourceContainer.class)
public class ModelMetaDataContainer extends AbstractModuleContainer implements ModelContainer{

	private static ModelMetaDataContainer container;

	@SingletonFactory
	public static ModelMetaDataContainer getContainer() {
		if (container == null) {
			container = InstanceUtil.getSingletonInstance(ModelMetaDataContainer.class);
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (TypeModelMetaDataFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends TypeModelMetaDataFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (TypeModelMetaDataFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends TypeModelMetaDataFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Group load(Object groupKey) {
		Group group = getCache().get(groupKey);
		if (group == null) {
			group = new ModelMetaDataGroup(groupKey);
			getCache().put(groupKey, group);
		}
		return group;
	}
}
