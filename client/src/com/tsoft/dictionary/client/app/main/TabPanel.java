package com.tsoft.dictionary.client.app.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.widget.AbstractForm;
import com.tsoft.dictionary.client.app.dictionary.DictionaryForm;
import com.tsoft.dictionary.client.app.library.LibraryForm;
import com.tsoft.dictionary.client.app.settings.SettingsForm;
import com.tsoft.dictionary.client.app.tutorial.TutorialForm;
import com.tsoft.dictionary.client.app.wordtrainer.WordTrainerForm;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import java.util.ArrayList;

public class TabPanel extends AbstractPanel<MainForm> {
    interface Binder extends UiBinder<Widget, TabPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField TabLayoutPanel tabPanel;

    class Tab {
        private AbstractForm form;
        private String elementId;
        private HTMLPanel panel;

        public Tab(AbstractForm form, String elementId, HTMLPanel panel) {
            this.form = form;
            this.elementId = elementId;
            this.panel = panel;
        }

        public AbstractForm getForm() {
            return form;
        }

        public String getElementId() {
            return elementId;
        }

        public HTMLPanel getPanel() {
            return panel;
        }
    }
    
    private ArrayList<Tab> tabList = new ArrayList<Tab>();

    @UiField HTMLPanel wordTrainerTab;
    @UiField HTMLPanel dictionaryTab;
    @UiField HTMLPanel tutorialsTab;
    @UiField HTMLPanel libraryTab;
    @UiField HTMLPanel settingsTab;

    public TabPanel() {
        super(binder);
    }

    @Override
    protected void onLoad() {
        WordTrainerForm wordTrainerForm = new WordTrainerForm(getForm().getServerServlets());
        tabList.add(new Tab(wordTrainerForm, "wordTrainerPanel", wordTrainerTab));

        DictionaryForm dictionaryForm = new DictionaryForm(getForm().getServerServlets());
        tabList.add(new Tab(dictionaryForm, "dictionaryPanel", dictionaryTab));

        TutorialForm tutorialForm = new TutorialForm(getForm().getServerServlets());
        tabList.add(new Tab(tutorialForm, "tutorialsPanel", tutorialsTab));

        LibraryForm libraryForm = new LibraryForm(getForm().getServerServlets());
        tabList.add(new Tab(libraryForm, "libraryPanel", libraryTab));

        SettingsForm settingsForm = new SettingsForm(getForm().getServerServlets());
        tabList.add(new Tab(settingsForm, "settingsPanel", settingsTab));

        tabPanel.addBeforeSelectionHandler(new BeforeTabSelectionHandler());
        tabPanel.selectTab(0);

        openTab(0);
        wordTrainerForm.startLesson();
    }

    private class BeforeTabSelectionHandler implements BeforeSelectionHandler<Integer> {
        @Override
        public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
            openTab(event.getItem());
        }
    }

    private void openTab(int index) {
        Tab currentTab = tabList.get(index);
        AbstractForm form = currentTab.getForm();
        if (!form.isElementsCreated()) {
            form.open();

            HTMLPanel currentPanel = currentTab.getPanel();
            currentPanel.add(form.getFormPanel(), currentTab.getElementId());

            // The default position is Relative (style="position: relative;")
            // and this is prevents the form to get full window size
            Element topElem = currentPanel.getElementById(currentTab.getElementId());
            topElem.getFirstChildElement().getStyle().setPosition(Position.STATIC);
        }
    }
}
