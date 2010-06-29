/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.Indicator;
import org.sigmah.server.domain.IndicatorValue;
import org.sigmah.server.domain.ReportingPeriod;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetMonthlyReports;
import org.sigmah.shared.command.Month;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.MonthlyReportResult;
import org.sigmah.shared.dto.IndicatorRowDTO;
import org.sigmah.shared.exception.CommandException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * See GetMonthlyReports
 *
 * @author Alex Bertram
 */
public class GetMonthlyReportsHandler implements CommandHandler<GetMonthlyReports> {


    private final EntityManager em;

    @Inject
    public GetMonthlyReportsHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(GetMonthlyReports cmd, User user) throws CommandException {


        List<ReportingPeriod> periods = em.createQuery("select p from ReportingPeriod p where p.site.id = ?1")
                .setParameter(1, cmd.getSiteId())
                .getResultList();

        List<Indicator> indicators = em.createQuery("select i from Indicator i where i.activity.id =" +
                "(select s.activity.id from Site s where s.id = ?1)")
                .setParameter(1, cmd.getSiteId())
                .getResultList();


        List<IndicatorRowDTO> list = new ArrayList<IndicatorRowDTO>();

        for (Indicator indicator : indicators) {

            IndicatorRowDTO dto = new IndicatorRowDTO();
            dto.setIndicatorId(indicator.getId());
            dto.setSiteId(cmd.getSiteId());
            dto.setIndicatorName(indicator.getName());

            for (ReportingPeriod period : periods) {

                Month month = HandlerUtil.monthFromRange(period.getDate1(), period.getDate2());
                if (month != null &&
                        month.compareTo(cmd.getStartMonth()) >= 0 &&
                        month.compareTo(cmd.getEndMonth()) <= 0) {

                    for (IndicatorValue value : period.getIndicatorValues()) {
                        if (value.getIndicator().getId() == indicator.getId()) {
                            dto.setValue(month, value.getValue());
                        }
                    }
                }
            }

            list.add(dto);
        }

        return new MonthlyReportResult(list);

    }

}
