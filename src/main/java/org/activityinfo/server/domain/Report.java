package org.activityinfo.server.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
@Entity
public class Report {

    private int id;

    private User owner;

    private UserDatabase database;

    private ReportTemplate templateReference;

    private int templateVersion;

    private String template;

    private Date date1;

    private Date date2;

    private boolean finalized;

    private String content;

    private List<ReportReviewers> reviewers = new ArrayList<ReportReviewers>();

    private List<User> recipients = new ArrayList<User>();

    private Date dateFinalized;

    private Date dateDistributed;


    @Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return The owner of this report, who has the authority to identify
     * reviewers and finalize and disseminate the report
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="OwnerUserId", nullable=false)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     *
     * @return  The database to which this report relates.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DatabaseId", nullable=false)
    public UserDatabase getDatabase() {
        return database;
    }

    public void setDatabase(UserDatabase database) {
        this.database = database;
    }

    /**
     *
     * @return The template which was initially used to generate this report.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public ReportTemplate getTemplateReference() {
        return templateReference;
    }

    public void setTemplateReference(ReportTemplate templateReference) {
        this.templateReference = templateReference;
    }


    /**
     *
     * @return The start date of the time period covered by this report
     */
    @Temporal(value = TemporalType.DATE)
    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    /**
     *
     * @return The end date of the time period covered by this report
     */
    @Temporal(value= TemporalType.DATE)
    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    /**
     *
     * @return True if this report has been finalized. Until a report is finalized,
     * the data and template fields should remain NULL
     */
    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;

        if(!finalized) {
            template = null;
            templateVersion = 0;
            content = null;
        }
    }

    /**
     *
     * @return The serialized content of this report
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
    public List<ReportReviewers> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<ReportReviewers> reviewers) {
        this.reviewers = reviewers;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    public List<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<User> recipients) {
        this.recipients = recipients;
    }

    @Temporal(value= TemporalType.TIMESTAMP)
    public Date getDateFinalized() {
        return dateFinalized;
    }

    public void setDateFinalized(Date dateFinalized) {
        this.dateFinalized = dateFinalized;
    }

    @Temporal(value=TemporalType.TIMESTAMP)
    public Date getDateDistributed() {
        return dateDistributed;
    }

    public void setDateDistributed(Date dateDistributed) {
        this.dateDistributed = dateDistributed;
    }
}
