package org.sigmah.shared.domain.reminder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "reminder_list")
public class ReminderList implements Serializable {

    private static final long serialVersionUID = -3989146795758040919L;

    private Integer id;
    private List<Reminder> reminders = new ArrayList<Reminder>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_reminder_list")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "parentList", cascade = CascadeType.ALL)
    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void addReminder(Reminder reminder) {
        if (reminder == null) {
            return;
        }
        reminder.setParentList(this);
        reminders.add(reminder);
    }
}
