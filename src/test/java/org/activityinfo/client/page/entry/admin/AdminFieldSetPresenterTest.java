package org.activityinfo.client.page.entry.admin;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.activityinfo.shared.dto.DTOs.BENI;
import static org.activityinfo.shared.dto.DTOs.MASISI;
import static org.activityinfo.shared.dto.DTOs.NORD_KIVU_TERRITOIRES;
import static org.activityinfo.shared.dto.DTOs.NORD_KIVU;
import static org.activityinfo.shared.dto.DTOs.PROVINCE;
import static org.activityinfo.shared.dto.DTOs.PROVINCES;
import static org.activityinfo.shared.dto.DTOs.RUIZI;
import static org.activityinfo.shared.dto.DTOs.SECTEUR;
import static org.activityinfo.shared.dto.DTOs.SUD_KIVU_TERRITOIRES;
import static org.activityinfo.shared.dto.DTOs.SUD_KIVU;
import static org.activityinfo.shared.dto.DTOs.TERRITOIRE;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.resetToDefault;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.activityinfo.client.dispatch.DispatcherStub;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.DTOs.PEAR;
import org.activityinfo.shared.util.mapping.Extents;
import org.junit.Before;
import org.junit.Test;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;

public class AdminFieldSetPresenterTest {

    private DispatcherStub dispatcher = new DispatcherStub();
    private AdminFieldSetPresenter presenter;
    private Listener<AdminLevelSelectionEvent> selectionListener;
    private Listener<LevelStateChangeEvent> levelStateChangeListener;
    private Listener<BoundsChangedEvent> boundsListener;

    @Before
    public void setupDispatcher() {
        dispatcher.setResult(new GetAdminEntities(PROVINCE.getId()), PROVINCES);
        dispatcher.setResult(
            new GetAdminEntities(TERRITOIRE.getId(), NORD_KIVU.getId()),
            NORD_KIVU_TERRITOIRES);
        dispatcher.setResult(
            new GetAdminEntities(TERRITOIRE.getId(), SUD_KIVU.getId()),
            SUD_KIVU_TERRITOIRES);
    }

    @Before
    public void setupListeners() {
        selectionListener = createNiceMock("selectionListener", Listener.class);
        levelStateChangeListener = createNiceMock("levelStateChangeListener",
            Listener.class);
        boundsListener = createNiceMock("boundsListener", Listener.class);

        replay(selectionListener, levelStateChangeListener, boundsListener);
    }

    @Test
    public void testSetSite() throws Exception {
        expectSelections(PEAR.WATALINA_CENTER_IN_BENI.getAdminEntities()
            .values());
        expectEnabledEvents(TERRITOIRE, SECTEUR);

        presenterForActivity(PEAR.NFI_DISTRIBUTION);

        presenter.setSelection(PEAR.WATALINA_CENTER_IN_BENI);

        // Verify that setting a sites results in the correct values being
        // sent to the view, and that the correct combos are enabled
        verify(selectionListener, levelStateChangeListener);

        assertSelected(NORD_KIVU);
        assertLevelIsEnabled(PROVINCE);
        assertLevelIsEnabled(TERRITOIRE);
        assertLevelIsEnabled(SECTEUR);

        // VERIFY that the correct command has been set for combos
        verifyLoad(TERRITOIRE, NORD_KIVU_TERRITOIRES);
    }

    @Test
    public void testInitBlank() throws Exception {

        presenterForActivity(PEAR.NFI_DISTRIBUTION);
        presenter.setSelection(PEAR.SITE_WITH_NO_ADMIN_LEVELS);

        assertLevelIsEnabled(PROVINCE);
        assertLevelIsDisabled(TERRITOIRE);
        assertLevelIsDisabled(SECTEUR);
    }

    @Test
    public void testCascade() throws Exception {

        presenterForActivity(PEAR.NFI_DISTRIBUTION);
        presenter.setSelection(PEAR.SITE_WITH_NO_ADMIN_LEVELS);

        verifyLoad(PROVINCE, PROVINCES);

        // VERIFY that a change to the province reconfigures the territory
        // loader
        presenter.setSelection(1, NORD_KIVU);

        verifyLoad(TERRITOIRE, NORD_KIVU_TERRITOIRES);
    }

    @Test
    public void testCascadeReplace() throws Exception {

        presenterForActivity(PEAR.NFI_DISTRIBUTION);

        // SETUP selection
        presenter.setSelection(PEAR.WATALINA_CENTER_IN_BENI);

        // VERIFY that change to province correctly cascades
        presenter.setSelection(PROVINCE.getId(), SUD_KIVU);

        assertLevelIsEmpty(TERRITOIRE);
        assertLevelIsEmpty(SECTEUR);
        assertLevelIsDisabled(SECTEUR);

        verifyLoad(TERRITOIRE, SUD_KIVU_TERRITOIRES);
    }

    @Test
    public void testBounds() {

        expectBounds(BENI.getBounds(), BENI.getName());

        presenterForActivity(PEAR.NFI_DISTRIBUTION);

        presenter.setSelection(PEAR.WATALINA_CENTER_IN_BENI);

        verify(boundsListener);

        assertThat(presenter.getBounds(), equalTo(BENI.getBounds()));
        assertThat(presenter.getBoundsName(), equalTo(BENI.getName()));
    }

    @Test
    public void testBoundsChange() {

        presenterForActivity(PEAR.NFI_DISTRIBUTION);

        presenter.setSelection(PEAR.WATALINA_CENTER_IN_BENI);

        expectBounds(MASISI.getBounds(), MASISI.getName());

        presenter.setSelection(TERRITOIRE.getId(), MASISI);

        verify(boundsListener);
    }

    /**
     * Regression test for bug
     */
    @Test
    public void testChange3rdLevelAdmin() {

        presenterForActivity(PEAR.NFI_DISTRIBUTION);

        // VERIFY: changing one adminlevel works properlty
        presenter.setSelection(PEAR.WATALINA_CENTER_IN_BENI);
        presenter.setSelection(SECTEUR.getId(), RUIZI);

        assertThat(presenter.getAdminEntity(PROVINCE), equalTo(NORD_KIVU));
        assertThat(presenter.getAdminEntity(TERRITOIRE), equalTo(BENI));
        assertThat(presenter.getAdminEntity(SECTEUR), equalTo(RUIZI));
    }

    private void expectSelections(Collection<AdminEntityDTO> values) {
        resetToDefault(selectionListener);
        for (AdminEntityDTO entity : values) {
            selectionListener.handleEvent(eq(new AdminLevelSelectionEvent(
                entity.getLevelId(), entity)));
        }
        replay(selectionListener);
    }

    private void expectEnabledEvents(AdminLevelDTO... levels) {
        resetToDefault(levelStateChangeListener);
        for (AdminLevelDTO level : levels) {
            levelStateChangeListener.handleEvent(eq(new LevelStateChangeEvent(
                level.getId(), true)));
        }
        replay(levelStateChangeListener);
    }

    private void expectBounds(Extents bounds, String name) {
        resetToDefault(boundsListener);
        boundsListener.handleEvent(eq(new BoundsChangedEvent(bounds, name)));
        replay(boundsListener);
    }

    private void assertSelected(AdminEntityDTO entity) {
        assertThat("levelId=" + entity.getLevelId() + " selection",
            presenter.getAdminEntity(entity.getLevelId()), equalTo(entity));
    }

    private void assertLevelIsEmpty(AdminLevelDTO level) {
        assertThat(level.getName() + " is empty",
            presenter.getAdminEntity(level.getId()), is(nullValue()));
    }

    private void assertLevelIsEnabled(AdminLevelDTO level) {
        assertTrue(level.getName() + "is enabled",
            presenter.isLevelEnabled(level));
    }

    private void assertLevelIsDisabled(AdminLevelDTO level) {
        assertFalse(level.getName() + "is disabled",
            presenter.isLevelEnabled(level));
    }

    private void verifyLoad(AdminLevelDTO level,
        AdminEntityResult expectedEntities) {
        ListStore<AdminEntityDTO> store = presenter.getStore(level.getId());
        store.getLoader().load();

        assertThat("number of entities loaded for " + level.getName()
            + " combo", store.getModels(), equalTo(expectedEntities.getData()));
    }

    private void presenterForActivity(ActivityDTO activity) {
        CountryDTO country = activity.getDatabase().getCountry();
        presenter = new AdminFieldSetPresenter(dispatcher, country,
            country.getAdminLevels());
        presenter.addListener(AdminLevelSelectionEvent.TYPE, selectionListener);
        presenter.addListener(LevelStateChangeEvent.TYPE,
            levelStateChangeListener);
        presenter.addListener(BoundsChangedEvent.TYPE, boundsListener);
    }
}
