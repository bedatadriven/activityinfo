package org.activityinfo.client.icon;

import java.util.Map;

import org.activityinfo.shared.report.model.DimensionType;

import com.google.common.collect.Maps;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class FromEntities {
	/*
	 * Returns an icon from given DimensionType or defaults to unknown icon if 
	 * DimensionType is not supported
	 */
	private static Map<String, AbstractImagePrototype> iconsByExtension = Maps.newHashMap();
	static {
		iconsByExtension.put("doc", IconImageBundle.ICONS.msword());
		iconsByExtension.put("docx", IconImageBundle.ICONS.msword());
		iconsByExtension.put("ppt", IconImageBundle.ICONS.ppt());
		iconsByExtension.put("pptx", IconImageBundle.ICONS.ppt());
		iconsByExtension.put("xls", IconImageBundle.ICONS.excel());
		iconsByExtension.put("xlx", IconImageBundle.ICONS.excel());
		iconsByExtension.put("pdf", IconImageBundle.ICONS.pdf());
		iconsByExtension.put("png", IconImageBundle.ICONS.image());
		iconsByExtension.put("gif", IconImageBundle.ICONS.image());
		iconsByExtension.put("jpg", IconImageBundle.ICONS.image());
		iconsByExtension.put("jpeg", IconImageBundle.ICONS.image());
	}
	public AbstractImagePrototype fromDimension(DimensionType dimension) {
		switch(dimension) {
		case AdminLevel:
			return IconImageBundle.ICONS.adminlevel1();
		case Database:
			return IconImageBundle.ICONS.database();
		case Activity:
			return IconImageBundle.ICONS.activity();
		case Project:
			return IconImageBundle.ICONS.project();
		case Partner:
			return IconImageBundle.ICONS.partner();
		case Indicator:
			return IconImageBundle.ICONS.indicator();
		case Site:
			return IconImageBundle.ICONS.site();
		case AttributeGroup:
			return IconImageBundle.ICONS.group();
		case Location:
			return IconImageBundle.ICONS.location(); 
		}
		
		return IconImageBundle.ICONS.none();
	}
	
	public AbstractImagePrototype fromExtension(String extension) {
		return iconsByExtension.get(extension.trim().toLowerCase());
	}
}
