package org.activityinfo.client.page.app;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.Log;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.GetUserProfile;
import org.activityinfo.shared.command.UpdateUserProfile;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.UserProfileDTO;
import org.activityinfo.shared.util.ObjectUtil;

import com.bedatadriven.rebar.async.NullCallback;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class UserProfilePage extends FormPanel implements Page {
    public static final PageId PAGE_ID = new PageId("userprofile");

    private final Dispatcher dispatcher;
    private FormBinding binding;

    private UserProfileDTO userProfile;

    @Inject
    public UserProfilePage(final Dispatcher dispatcher) {
        super();
        this.dispatcher = dispatcher;

        binding = new FormBinding(this);

        TextField<String> nameField = new TextField<String>();
        nameField.setAllowBlank(false);
        nameField.setFieldLabel(I18N.CONSTANTS.name());
        nameField.setMaxLength(50);
        nameField.addListener(Events.Change, new ProfileChangeListener("name"));
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
        this.add(nameField);

        TextField<String> organizationField = new TextField<String>();
        organizationField.setFieldLabel(I18N.CONSTANTS.organization());
        organizationField.setMaxLength(100);
        organizationField.addListener(Events.Change, new ProfileChangeListener("organization"));
        binding.addFieldBinding(new FieldBinding(organizationField, "organization"));
        this.add(organizationField);

        TextField<String> jobtitleField = new TextField<String>();
        jobtitleField.setFieldLabel(I18N.CONSTANTS.jobtitle());
        jobtitleField.setMaxLength(100);
        jobtitleField.addListener(Events.Change, new ProfileChangeListener("jobtitle"));
        binding.addFieldBinding(new FieldBinding(jobtitleField, "jobtitle"));
        this.add(jobtitleField);
        
        CheckBox emailNotification = new CheckBox();
        emailNotification.setFieldLabel(I18N.CONSTANTS.emailNotification());
        emailNotification.addListener(Events.Change, new ProfileChangeListener("emailNotification"));
        binding.addFieldBinding(new FieldBinding(emailNotification, "emailNotification"));
        this.add(emailNotification);

        bindProfile();
    }

    private void bindProfile() {
        userProfile = new UserProfileDTO();
         AuthenticatedUser user = new ClientSideAuthProvider().get();
         dispatcher.execute(new GetUserProfile(user.getUserId()),
            new AsyncCallback<UserProfileDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                    Log.error("error binding profile", caught);
                }

                @Override
                public void onSuccess(UserProfileDTO userProfileDTO) {
                    userProfile = userProfileDTO;
                    binding.bind(userProfile);
                }
            });
    }

    private void saveProfile() {
        AuthenticatedUser user = new ClientSideAuthProvider().get();
        if (user != null && user.getUserId() == userProfile.getUserId()) {
            dispatcher.execute(new UpdateUserProfile(userProfile), new NullCallback<VoidResult>());
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return this;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        return true;
    }

    private class ProfileChangeListener implements Listener<FieldEvent> {
        private String fieldname;

        public ProfileChangeListener(String fieldname) {
            this.fieldname = fieldname;
        }

        @Override
        public void handleEvent(FieldEvent fe) {
            // check if we need to save (change-event is also called on init)
            Object modelValue = userProfile.get(fieldname);
            Object fieldValue = fe.getField().getValue();
            if (ObjectUtil.notEquals(modelValue, fieldValue)) {
                // this eventtype fires before the form->model binding occurs, so we need to
                // set the model-value manually before save..
                userProfile.set(fieldname, fieldValue);
                saveProfile();
            }
        }
    }

    public static class State implements PageState {
        @Override
        public PageId getPageId() {
            return UserProfilePage.PAGE_ID;
        }

        @Override
        public String serializeAsHistoryToken() {
            return null;
        }

        @Override
        public List<PageId> getEnclosingFrames() {
            return Arrays.asList(UserProfilePage.PAGE_ID);
        }

        @Override
        public Section getSection() {
            return null;
        }

    }

    public static class StateParser implements PageStateParser {
        @Override
        public PageState parse(String token) {
            return new State();
        }
    }
}
