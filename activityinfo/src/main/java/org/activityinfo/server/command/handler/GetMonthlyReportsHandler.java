package org.activityinfo.server.command.handler;

import org.activityinfo.server.domain.Indicator;
import org.activityinfo.server.domain.IndicatorValue;
import org.activityinfo.server.domain.ReportingPeriod;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetMonthlyReports;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.MonthlyReportResult;
import org.activityinfo.shared.dto.IndicatorRow;
import org.activityinfo.shared.exception.CommandException;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

import java.util.List;
import java.util.ArrayList;
/*
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


        List<IndicatorRow> list = new ArrayList<IndicatorRow>();

        for(Indicator indicator : indicators) {

            IndicatorRow dto = new IndicatorRow();
            dto.setIndicatorId(indicator.getId());
            dto.setSiteId(cmd.getSiteId());
            dto.setIndicatorName(indicator.getName());

            for(ReportingPeriod period : periods) {

                Month month = HandlerUtil.monthFromRange(period.getDate1(), period.getDate2());
                if(month != null &&
                   month.compareTo(cmd.getStartMonth()) >= 0 &&
                   month.compareTo(cmd.getEndMonth()) <= 0) {

                    for(IndicatorValue value : period.getIndicatorValues()) {
                        if(value.getIndicator().getId() == indicator.getId()) {
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
