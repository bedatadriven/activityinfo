package org.activityinfo.client.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import org.activityinfo.client.i18n.UIMessages;
import org.activityinfo.shared.dto.*;

import java.util.*;

public class NarrativeRenderer {

	private SchemaDTO schema;
	private DateTimeFormat dateFormat;
	private NumberFormat indicatorFormat;
	private UIMessages messages = GWT.create(UIMessages.class);
	
	public NarrativeRenderer(SchemaDTO schema) {
		this.schema = schema;
		dateFormat = DateTimeFormat.getLongDateFormat();
		indicatorFormat = NumberFormat.getFormat("#,##0");
	}

	public DateTimeFormat getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(DateTimeFormat format) { 
		this.dateFormat = format;
	}
	
	public String renderHtml(List<SiteDTO> rows) {
		
		StringBuilder sb = new StringBuilder();
			
		for(SiteDTO row : rows) {
			renderHtml(sb, schema.getActivityById(row.getActivityId()), row);
		}
		
		return sb.toString();
	}
	
	public String renderHtml(ActivityDTO activity, SiteDTO row) {
		StringBuilder sb = new StringBuilder();
		renderHtml(sb, activity, row);
		return sb.toString();
	}
	
	public String renderHtml(SiteDTO row) {
				
		StringBuilder sb = new StringBuilder();
		ActivityDTO activity = schema.getActivityById(row.getActivityId());
		
		renderHtml(sb, activity, row);
		return sb.toString();
		
	}
	
	protected void renderHtml(StringBuilder sb, ActivityDTO activity, SiteDTO site) {
	
		Date date = (Date)(site.getDate2() == null ? site.getDate1() : site.getDate2());
		
		sb.append("<div class='activity'>");
	
		sb.append("<div class='date'>");
		if(date != null) {
			sb.append(DateTimeFormat.getLongDateFormat().format(date));
		}
		sb.append("</div>");
		
		sb.append("<div class='act-title'>");
		if(site.getDate2()!=null) {
			sb.append(messages.activityAt(activity.getName(), (String)site.getLocationName()));
		}
		sb.append("</div>");
	
		if(site.getLocationAxe()!=null) {
			sb.append("<div class='axe'>");
			sb.append(site.getLocationAxe());
			sb.append("</div>");
		}

		sb.append("<div class='partner'>");
		sb.append(site.getPartnerName());
		sb.append("</div>");	
		
		sb.append("<table class='admin'>");
	
		for(AdminLevelDTO level : activity.getDatabase().getCountry().getAdminLevels()) {
			if(site.getAdminEntityName(level.getId()) != null) {
				sb.append("<tr class='level" + level.getId() + "'>");
				sb.append("<td class='name'>");
				sb.append(level.getName());
				sb.append("</td><td class='entity'>");
				sb.append(site.getAdminEntityName(level.getId()));
				sb.append("</td></tr>");
			}
		}
		sb.append("</table>");
		

		renderAttributesHtml(sb, activity, site);
	
		renderIndicatorsHtml(sb, activity, site);
	
		renderCommentHtml(sb, site);
		
		sb.append("</div>");
	}


	public void renderAttributesHtml(StringBuilder sb, ActivityDTO activity, SiteDTO site) {

		for(AttributeGroupDTO group : activity.getAttributeGroups()) {
				
			StringBuilder asb = new StringBuilder();
			
			for(AttributeDTO attribute : group.getAttributes()) {
				
				Boolean value = (Boolean) site.get(attribute.getPropertyName());;
				if(value != null && value) {
					if(asb.length() != 0) {
						asb.append(", ");
					}
					asb.append(attribute.getName());
				}
			}
			
			if(asb.length() != 0) {
				sb.append("<div class=\"attrib\"><span class=\"attrib-name\">");
				sb.append(group.getName());
				sb.append(":</span> ");
				sb.append(asb.toString());
				sb.append("</div>");
			}
		}
	}
	
	public void renderIndicatorsHtml(StringBuilder sb, ActivityDTO activity, SiteDTO site) {

		Categories indicators = new Categories("indicator");
		for(IndicatorDTO indicator : activity.getIndicators()) {
	
			Double value = (Double) site.get(indicator.getPropertyName());
			
			if(value!=null && value != 0) {
			
				Category category = indicators.getCategory(indicator.getCategory());
				
				category.addValue(indicator.getName(), 
						indicatorFormat.format(value), indicator.getUnits(), "");
			}
		}
		
		sb.append(indicators.getHtml());
	}
	
	public void renderCommentHtml(StringBuilder sb, SiteDTO site) {
		String comments = (String)site.getComments();
		if(comments!=null && comments.length()!=0) {
			sb.append("<div class='comments'>");
			sb.append(comments);
			sb.append("</div>");
		}
	}	
	
	
	private class Categories {
		
		String styleName;
		Map<String, Category> map;
		List<Category> list;
		
		public Categories(String styleName) {
			styleName = this.styleName;
			map = new HashMap<String, Category>();
			list = new ArrayList<Category>();
		}
		
		public Category getCategory(String name) {
			String key, label;
			if(name == null) {
				key = "_default";
				label = "";
			} else {
				key = label = name;
			}
			
			if(map.containsKey(key)) {
				return map.get(key);
			} else {
				Category category = new Category(label, styleName);
				map.put(key, category);
				list.add(category);
				
				return category;
			}
		}
		
		public String getHtml() {
			StringBuilder sb = new StringBuilder();
			for(Category category : list) {
				if(category.getCount() != 0) {
					sb.append(category.getHtml());
				}
			}
			return sb.toString();
		}
	}
	
	private class Category {
		
		private String name;
		private StringBuilder sb;
		private int count = 0;
		
		public Category(String name, String styleName) {
			this.name = name;
			this.sb = new StringBuilder();
			
			sb.append("<table class='" + styleName + "'>");
			sb.append("<thead><tr><td>");
			sb.append(name);
			sb.append("</td></tr></thead>");
			sb.append("<tbody>");
		}
		
		public void addValue(String name, String value, String units, String styleName) {
			if(count%2!=0) {
                styleName += "alt";
            }
			
			sb.append("<tr class='"+ styleName + "'>");
			sb.append("<td class='name'>");
			sb.append(name);
			sb.append("</td><td class='value'>");
			sb.append(value);
			sb.append("</td><td class=\'unit\'>");
			if(units != null) {
				sb.append(units);
			}
			sb.append("</td></tr>");	
			
			count++;
		}
		
		public String getHtml() {
			sb.append("</tbody></table>");
			return sb.toString();
		}
		
		public int getCount() {
			return count;
		}
		
	}
}
