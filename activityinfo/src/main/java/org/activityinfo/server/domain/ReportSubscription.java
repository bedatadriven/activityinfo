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

package org.activityinfo.server.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines a subscription to a given report.
 *
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

    public ReportSubscription() {
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


    /**
     * Gets the ReportTemplate to which the user is subscribed
     *
     * @return the ReportTemplate to which the user is subscribed
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reportTemplateId", nullable = false, insertable = false, updatable = false)
	public ReportTemplate getTemplate() {
		return this.template;
	}

    /**
     * Sets the Report Template to which the user is subscribed.
     *
     * @param template
     */
    public void setTemplate(ReportTemplate template) {
        this.template = template;
    }

    /**
     * Get the user who will receive the report by mail.
     *
     * @return The user to whom the report will be mailed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", nullable=false, insertable=false, updatable=false)
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who will receive the report by mail.
     *
     * @param user  The user who will receive the report by mail.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the frequency with which this report should be mailed. See constants in
     * {@link org.activityinfo.shared.domain.Subscription}.
     *
     * @return The frequency with which this report should be mailed.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency with which the report should be mailed.
     *
     * @param frequency A constant from {@link org.activityinfo.shared.domain.Subscription} :
     * <code>DAILY, WEEKLY, MONTHLY</code>
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Gets the day on which the report is to be mailed.
     *
     * If the subscription frequency is
     * <code>WEEKLY</code>, then this value refers to the
     * day of the week (0=Sunday, 6=Saturday). If the frequency is <code>MONTHLY</code> this refers to the
     * day of the month (1..28). A value of {@link org.activityinfo.shared.domain.Subscription.LAST_DAY_OF_MONTH} (28)
     * indicats that the report will be mailed on the last day of the month, whether this the 28th, the 30th, or the 31st.
     *
     * @return the day on which the report is to be mailed.
     *
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day on which the report should be mailed.
     *
     * @param day For <code>WEEKLY</code> subscriptions, 0=Sunday, 6=Saturday. For <code>MONTHLY</code>,
     * subscriptions, 1=first day of month,2,3,4...28=<code>LAST_DAY_OF_MONTH</code>
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Gets the inviting user
     *
     * @return The second user who has invited <code>user</code> to
     * subscribe to this report. NULL if the user has set their
     * own preferences.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="invitingUserId", nullable=true)
    public User getInvitingUser() {
        return invitingUser;
    }

    /**
     * Sets the inviting user
     *
     * @param invitingUser A second user who has invited the <code>user</code> to subscribe to the
     * report.
     */
    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }
}
