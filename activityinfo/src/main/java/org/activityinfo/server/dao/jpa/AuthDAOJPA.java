package org.activityinfo.server.dao.jpa;

import com.google.inject.Inject;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.service.PasswordGenerator;
import org.activityinfo.server.mail.Mailer;
import org.apache.commons.mail.SimpleEmail;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.ResourceBundle;
import java.text.MessageFormat;


public class AuthDAOJPA implements AuthDAO {

    private final EntityManager em;
    private final PasswordGenerator pgen;
    private final Mailer mailer;


    @Inject
    public AuthDAOJPA(EntityManager em, PasswordGenerator pgen, Mailer mailer) {
        this.em = em;
        this.pgen = pgen;
        this.mailer = mailer;
    }

    @Override
    public User getUserByEmail(String email) {
        List<User> list = em.createQuery("select u from User u where u.email = ?1")
                .setParameter(1, email).getResultList();

        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    @Override
    public User createUser(String email, String name, String locale, User invitingUser) {

        String password = pgen.generate();

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setNewUser(true);
        user.setLocale("fr");
        user.changePassword(password);

        try {
            ResourceBundle mailMessages =
                    ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages", user.getLocaleObject());

            SimpleEmail mail = new SimpleEmail();
            mail.addTo(email,name);
            mail.setSubject(mailMessages.getString("newUserSubject"));

            StringBuilder sb = new StringBuilder();
            sb.append(MessageFormat.format(mailMessages.getString("greeting"), user.getName())).append("\n\n");
            sb.append(MessageFormat.format(mailMessages.getString("newUserIntro"), invitingUser.getName(), invitingUser.getEmail())).append("\n\n");
            sb.append(MessageFormat.format(mailMessages.getString("newUserPassword"), user.getName(), password)).append("\n\n");
            sb.append(mailMessages.getString("signature"));
            mail.setMsg(sb.toString());

            mailer.send(mail);
        } catch (Exception e) {
            // don't let an email failure rollback the creation of the user.
            e.printStackTrace();
        }

        em.persist(user);

        return user;

    }

    @Override
    public Authentication createSession(User user) {

        Authentication session = new Authentication(user);
        em.persist(session);

        return session;
    }

    @Override
    public Authentication getSession(String sessionId) {

        return em.find(Authentication.class, sessionId);
    }

}


