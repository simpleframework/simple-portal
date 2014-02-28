package net.simpleframework.mvc.component.portal.module;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.ctx.common.xml.XmlAttri;
import net.simpleframework.ctx.common.xml.XmlDocument;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.component.portal.PageletBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PortalModuleRegistryFactory extends ObjectEx {
	public static PortalModuleRegistryFactory get() {
		return singleton(PortalModuleRegistryFactory.class);
	}

	public static final String DEFAULT_MODULE_NAME = "rss";

	static final String MODULE_FILE = "portal_module.xml";

	static final String WEB_MODULE_PATH = "/WEB-INF/portal_module.xml";

	public void init() {
		InputStream inStream = null;
		try {
			try {
				inStream = new FileInputStream(MVCUtils.getRealPath(WEB_MODULE_PATH));
			} catch (final FileNotFoundException e) {
				inStream = ClassUtils.getResourceAsStream(PortalModuleRegistryFactory.class,
						MODULE_FILE);
			}
			if (inStream != null) {
				new ModuleDocument(inStream, get());
			}
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	static class ModuleDocument extends XmlDocument {
		protected ModuleDocument(final InputStream inputStream,
				final PortalModuleRegistryFactory factory) {
			super(inputStream);
			final Iterator<?> it = getRoot().elementIterator("module");
			while (it.hasNext()) {
				final XmlElement element = (XmlElement) it.next();
				final PortalModule layoutModule = new PortalModule();
				final Iterator<?> attris = element.attributeIterator();
				while (attris.hasNext()) {
					final XmlAttri attri = (XmlAttri) attris.next();
					try {
						final String value = attri.getValue();
						if (value != null) {
							BeanUtils.setProperty(layoutModule, attri.getName(), value);
						}
						final String desc = element.elementText("description");
						if (StringUtils.hasText(desc)) {
							layoutModule.setDescription(desc);
						}
					} catch (final Exception ex) {
						log.warn(ex);
					}
				}
				factory.registered(layoutModule);
			}
		}
	}

	private final Map<String, Collection<PortalModule>> layoutModules = new LinkedHashMap<String, Collection<PortalModule>>();

	public PortalModule getModule(final String name) {
		if (StringUtils.hasText(name)) {
			for (final Collection<PortalModule> coll : layoutModules.values()) {
				for (final PortalModule layoutModule : coll) {
					if (name.equals(layoutModule.getName())) {
						return layoutModule;
					}
				}
			}
		}
		return null;
	}

	public Map<String, Collection<PortalModule>> getModulesByCatalog() {
		return layoutModules;
	}

	public void registered(final PortalModule layoutModule) {
		if (layoutModule != null) {
			final String catalog = layoutModule.getCatalog();
			Collection<PortalModule> coll = layoutModules.get(catalog);
			if (coll == null) {
				layoutModules.put(catalog, coll = new ArrayList<PortalModule>());
			}
			coll.add(layoutModule);
		}
	}

	public IPortalModuleHandler getModuleHandler(final PageletBean pagelet) {
		if (pagelet == null) {
			return null;
		}
		return pagelet.getModuleHandler();
	}

	public static void registered(final Class<? extends AbstractPortalModuleHandler> clazz,
			final String name, final String text, final String catalog, final String icon,
			final String description) {
		PortalModuleRegistryFactory.get().registered(
				new PortalModule().setHandlerClass(clazz.getName()).setCatalog(catalog).setIcon(icon)
						.setText(text).setName(name).setDescription(description));
	}
}
