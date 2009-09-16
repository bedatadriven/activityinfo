package org.activityinfo.server.domain;

import javax.persistence.*;
import java.io.Serializable;

/*
 * @author Alex Bertram
 */
@Entity
public class ReportSubscription implements Serializable {

    private ReportSubscriptionId id;
    private ReportTemplate template;
    private User user;
    private int frequency;
    private int day;
    private User invitingUser;

    protected ReportSubscription() {
    }

    public ReportSubscription(ReportTemplate template, User user) {
        id = new ReportSubscriptionId(template.getId(), user.getId());
        this.template = template;
        this.user = user;
    }

    @EmbeddedId
    @AttributeOverrides( {                                                          
            @AttributeOverride(name = "reportTemplateId", column = @Column(name = "reportTemplateId", nullable = false)),
            @AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false)) })
    public ReportSubscriptionId getId() {
        return this.id;
    }

    public void setId(ReportSubscriptionId id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reportTemplateId", nullable = false, insertable = false, updatable = false)
	public ReportTemplate getTemplate() {
		return this.template;
	}

    public void setTemplate(ReportTemplate template) {
        this.template = template;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", nullable=false, insertable=false, updatable=false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    /**
     *
     * @return The second user who has invited <code>user</code> to
     * subscribe to this report.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="invitingUserId", nullable=true)
    public User getInvitingUser() {
        return invitingUser;
    }

    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }
}
