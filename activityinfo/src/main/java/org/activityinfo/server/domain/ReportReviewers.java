package org.activityinfo.server.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
@Entity
public class ReportReviewers {

    private int id;

    private Report report;

    private User user;

    private User delegatingUser;

    private Date dateNotified;

    private Date dateLastReviewed;

    private Date dateApproved;

    private String comments;

    public ReportReviewers() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @ManyToOne
    @JoinColumn(name="ReportId", nullable = false)
    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @ManyToOne
    @JoinColumn(name="UserId", nullable=false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="delegatingUserId")
    public User getDelegatingUser() {
        return delegatingUser;
    }

    public void setDelegatingUser(User delegatingUser) {
        this.delegatingUser = delegatingUser;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getDateNotified() {
        return dateNotified;
    }

    public void setDateNotified(Date dateNotified) {
        this.dateNotified = dateNotified;
    }

    @Temporal(value= TemporalType.TIMESTAMP)
    public Date getDateLastReviewed() {
        return dateLastReviewed;
    }

    public void setDateLastReviewed(Date dateLastReviewed) {
        this.dateLastReviewed = dateLastReviewed;
    }

    @Temporal(value= TemporalType.TIMESTAMP)
    public Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Date dateApproved) {
        this.dateApproved = dateApproved;
    }


    @Column(nullable = true)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
