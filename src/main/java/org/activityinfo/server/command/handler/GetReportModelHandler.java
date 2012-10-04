package org.activityinfo.server.command.handler;

import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import org.activityinfo.server.database.hibernate.entity.ReportDefinition;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.shared.command.GetReportModel;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.ReportDTO;
import org.activityinfo.shared.dto.ReportMetadataDTO;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.report.model.EmailDelivery;
import org.activityinfo.shared.report.model.Report;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class GetReportModelHandler implements CommandHandlerAsync<GetReportModel, ReportDTO> {
	private final EntityManager em;

	@Inject
	public GetReportModelHandler(final EntityManager em) {
		this.em = em;
	}

	@Override
	public void execute(final GetReportModel cmd, final ExecutionContext context,
			final AsyncCallback<ReportDTO> callback) {

		ReportDTO reportDTO = null;
		Integer reportId = cmd.getReportId();

		if (reportId != null) {

			// always load report
			ReportDefinition entity = em.find(ReportDefinition.class, reportId);
			Report report = new Report();

			try {
				report = ReportParserJaxb.parseXml(entity.getXml());
			} catch (JAXBException e) {
				throw new UnexpectedCommandException(e);
			}
			report.setId(reportId);

			reportDTO = new ReportDTO(report);

			if (cmd.isLoadMetadata()) {
				// load metadata if specified
				loadMetadata(reportId, context, reportDTO, callback);
			} else {
				// exit handler with only the report object filled
				callback.onSuccess(reportDTO);
			}
		}
	}

	private void loadMetadata(final Integer reportId, final ExecutionContext context,
			final ReportDTO reportDTO, final AsyncCallback<ReportDTO> callback) {
		
		final int userId = context.getUser().getId();

		SqlQuery mySubscriptions =
				SqlQuery.selectAll()
						.from("reportsubscription")
						.where("userId").equalTo(userId);

		SqlQuery myDatabases =
				SqlQuery.selectSingle("d.databaseid")
						.from("userdatabase", "d")
						.leftJoin(
								SqlQuery.selectAll()
										.from(Tables.USER_PERMISSION, "UserPermission")
										.where("UserPermission.UserId").equalTo(userId), "p")
						.on("p.DatabaseId = d.DatabaseId")
						.where("d.ownerUserId").equalTo(userId)
						.or("p.AllowView").equalTo(1);

		SqlQuery.select()
				.appendColumn("r.reportTemplateId", "reportId")
				.appendColumn("r.title", "title")
				.appendColumn("r.ownerUserId", "ownerUserId")
				.appendColumn("o.name", "ownerName")
				.appendColumn("s.dashboard", "dashboard")
				.appendColumn("s.emaildelivery", "emaildelivery")
				.appendColumn("s.emailday", "emailday")
				.appendColumn(
						SqlQuery.selectSingle("max(defaultDashboard)")
								.from("reportvisibility", "v")
								.where("v.databaseId").in(myDatabases)
								.whereTrue("v.reportid = r.reportTemplateId"),
						"defaultDashboard")
				.from("reporttemplate", "r")
				.leftJoin("userlogin o").on("o.userid = r.ownerUserId")
				.leftJoin(mySubscriptions, "s").on("r.reportTemplateId = s.reportId")
				.where("r.ownerUserId").equalTo(userId)
				.where("r.reportTemplateId").equalTo(reportId)
				.or("r.reportTemplateId").in(
						SqlQuery.select("reportId")
								.from("reportvisibility", "v")
								.where("v.databaseid").in(myDatabases))
				.execute(context.getTransaction(), new SqlResultCallback() {

					@Override
					public void onSuccess(final SqlTransaction tx, final SqlResultSet results) {
						List<ReportMetadataDTO> dtos = Lists.newArrayList();

						for (SqlResultSetRow row : results.getRows()) {
							ReportMetadataDTO dto = new ReportMetadataDTO();
							dto.setId(row.getInt("reportId"));
							dto.setAmOwner(row.getInt("ownerUserId") == userId);
							dto.setOwnerName(row.getString("ownerName"));
							dto.setTitle(row.getString("title"));
							dto.setEditAllowed(dto.getAmOwner());
							if (!row.isNull("emaildelivery")) {
								dto.setEmailDelivery(EmailDelivery.valueOf(row.getString("emaildelivery")));
							}
							if (row.isNull("emailday")) {
								dto.setDay(1);
							} else {
								dto.setDay(row.getInt("emailday"));
							}
							if (row.isNull("dashboard")) {
								// inherited from database-wide visibility
								dto.setDashboard(!row.isNull("defaultDashboard") &&
										row.getBoolean("defaultDashboard"));
							} else {
								dto.setDashboard(row.getBoolean("dashboard"));
							}
							dtos.add(dto);
						}

						if (dtos.size() == 0) {
							ReportMetadataDTO dummy = new ReportMetadataDTO();
							dummy.setId(reportId);
							dtos.add(dummy);
						}

						// there should be only one result
						reportDTO.setReportMetadataDTO(dtos.get(0));

						// exit handler with both the report and metadata objects filled
						callback.onSuccess(reportDTO);
					}
				});
	}
}
