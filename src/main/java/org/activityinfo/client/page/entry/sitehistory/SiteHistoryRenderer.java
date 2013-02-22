package org.activityinfo.client.page.entry.sitehistory;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.SiteHistoryDTO;

import com.bedatadriven.rebar.time.calendar.LocalDate;

public class SiteHistoryRenderer {

    private static final LocalDate HISTORY_AVAILABLE_FROM = new LocalDate(2012,
        12, 20);

    public String renderLoading() {
        return new Item(I18N.CONSTANTS.loading()).toString();
    }

    public String renderNotAvailable(final SiteDTO site) {
        return Item.appendAll(
            new Item(I18N.MESSAGES.siteHistoryNotAvailable()),
            availableFrom(site));
    }

    public String render(SchemaDTO schema, List<LocationDTO> locations,
        SiteDTO site, List<SiteHistoryDTO> histories) {
        List<Item> items = new ArrayList<Item>();
        items.addAll(items(schema, locations, site, histories));
        items.add(availableFrom(site));
        return Item.appendAll(items);
    }

    private List<Item> items(SchemaDTO schema, List<LocationDTO> locations,
        SiteDTO site, List<SiteHistoryDTO> histories) {
        List<Item> items = new ArrayList<Item>();

        Map<String, Object> baselineState = histories.get(0).getJsonMap();
        RenderContext ctx = new RenderContext(schema, locations, site,
            baselineState);

        boolean first = true;
        for (SiteHistoryDTO history : histories) {
            ctx.setHistory(history);

            Item item = new Item();
            if (first) {
                if (history.isInitial()) {
                    // only show if the entry was created when the actual site
                    // was created, don't show stub baselines
                    item.setMsg(I18N.MESSAGES.siteHistoryCreated(
                        history.getDateCreated(), history.getUserName(),
                        history.getUserEmail()));
                }
                first = false;
            } else {
                item.setMsg(I18N.MESSAGES.siteHistoryUpdated(
                    history.getDateCreated(), history.getUserName(),
                    history.getUserEmail()));
                item.setDetails(details(ctx));
            }

            items.add(item);
        }

        Collections.reverse(items);

        return items;
    }

    private List<ItemDetail> details(RenderContext ctx) {
        List<ItemDetail> details = new ArrayList<ItemDetail>();
        for (Map.Entry<String, Object> entry : ctx.getHistory().getJsonMap()
            .entrySet()) {
            details.add(ItemDetail.create(ctx, entry));
        }
        return details;
    }

    private Item availableFrom(SiteDTO site) {
        Item item = new Item();
        if (site != null && site.getDateCreated() != null
            && site.getDateCreated().before(HISTORY_AVAILABLE_FROM)) {
            item.setMsg(I18N.MESSAGES
                .siteHistoryAvailableFrom(HISTORY_AVAILABLE_FROM
                    .atMidnightInMyTimezone()));
        }
        return item;
    }
}
