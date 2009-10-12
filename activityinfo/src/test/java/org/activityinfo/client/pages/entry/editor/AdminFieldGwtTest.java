package org.activityinfo.client.pages.entry.editor;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.mock.MockCommandService;
import org.activityinfo.client.page.entry.editor.AdminFieldSet;
import org.activityinfo.client.page.entry.editor.AdminFieldSetPresenter;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Schema;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AdminFieldGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.activityinfo.ApplicationTest";
    }

    public void testSetSite() {


        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        MockCommandService service = new MockCommandService();
        service.setResult(new GetAdminEntities(1), DummyData.getProvinces());
        service.setResult(new GetAdminEntities(2, 101), DummyData.getTerritoires(100));

        final AdminFieldSet fieldSet = new AdminFieldSet(nfi);

        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        assertEquals(101, fieldSet.getCombo(2).getValue().getId());

        RootPanel.get().add(fieldSet);
        fieldSet.layout();

        assertTrue(fieldSet.getCombo(2).isRendered());
        assertEquals("Combo value", "Beni", fieldSet.getCombo(2).getRawValue());



    }
}
