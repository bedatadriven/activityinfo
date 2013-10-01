package org.activityinfo.client.page.entry.form;

import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Locale;

import org.activityinfo.server.event.sitechange.JreIndicatorValueFormatter;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.junit.Before;
import org.junit.Test;

import com.teklabs.gwt.i18n.server.LocaleProxy;

public class SiteRendererTest {

    private SiteRenderer siteRenderer;

    @Before
    public void setup() {
        LocaleProxy.setLocale(Locale.ENGLISH);

        siteRenderer = new SiteRenderer(new JreIndicatorValueFormatter());

    }

    @Test
    public void multipleGroupsRender() {


        IndicatorDTO indicator1 = new IndicatorDTO();
        indicator1.setId(1);
        indicator1.setAggregation(IndicatorDTO.AGGREGATE_SUM);
        indicator1.setName("First indicator");
        indicator1.setCategory("First group");

        IndicatorDTO indicator2 = new IndicatorDTO();
        indicator2.setAggregation(IndicatorDTO.AGGREGATE_SUM);
        indicator2.setId(2);
        indicator2.setName("Second indicator");
        indicator2.setCategory("Second group");

        ActivityDTO activity = new ActivityDTO();
        activity.setId(1);
        activity.getIndicators().add(indicator1);
        activity.getIndicators().add(indicator2);

        SiteDTO site = new SiteDTO();
        site.setIndicatorValue(1, 1000d);
        site.setIndicatorValue(2, 2000d);

        String html = siteRenderer.renderSite(site, activity, false, true);

        assertTrue(html.contains(indicator1.getName()));
        assertTrue(html.contains(indicator2.getName()));

    }

}
