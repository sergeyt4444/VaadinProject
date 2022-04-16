package com.project.views.components;


import com.project.tools.AttributeTool;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import feign.template.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CourseFilterDetails extends HorizontalLayout {

    private MenuBar difficultyBar;
    private MenuBar languageBar;
    private MenuBar formatBar;
    private CheckboxGroup<String> difficultyGroup;
    private CheckboxGroup<String> languageGroup;
    private CheckboxGroup<String> formatGroup;
    private Button filterButton;

    public CourseFilterDetails(String courseName, Map<String, List<String>> queryParamList) {

        List<String> preselectedDif, preselectedLang, preselectedFormat;
        preselectedDif = AttributeTool.getDifficulties();
        preselectedLang = AttributeTool.getLanguages();
        preselectedFormat = AttributeTool.getFormats();

        if (queryParamList.containsKey("difficulty")) {
            preselectedDif = queryParamList.get("difficulty");
        }
        if (queryParamList.containsKey("language")) {
            preselectedLang = queryParamList.get("language");
        }
        if (queryParamList.containsKey("format")) {
            preselectedFormat = queryParamList.get("format");
        }

        this.setAlignItems(Alignment.START);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.addClassName("course-filter-details");

        difficultyGroup = new CheckboxGroup<>();
        difficultyGroup.setItems(AttributeTool.getDifficulties());
        difficultyGroup.select(preselectedDif);
        difficultyGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        difficultyGroup.addClassName("filter-checkbox-group");

        difficultyBar = new MenuBar();
        MenuItem difItem = difficultyBar.addItem("Difficulty");
        SubMenu difSubMenu = difItem.getSubMenu();
        difSubMenu.addItem(difficultyGroup);

        languageGroup = new CheckboxGroup<>();
        languageGroup.setItems(AttributeTool.getLanguages());
        languageGroup.select(preselectedLang);
        languageGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        languageGroup.addClassName("filter-checkbox-group");

        languageBar = new MenuBar();
        MenuItem langItem = languageBar.addItem("Language");
        SubMenu langSubMenu = langItem.getSubMenu();
        langSubMenu.addItem(languageGroup);

        formatGroup = new CheckboxGroup<>();
        formatGroup.setItems(AttributeTool.getFormats());
        formatGroup.select(preselectedFormat);
        formatGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        formatGroup.addClassName("filter-checkbox-group");

        formatBar = new MenuBar();
        MenuItem formatItem = formatBar.addItem("Format");
        SubMenu formatSubMenu = formatItem.getSubMenu();
        formatSubMenu.addItem(formatGroup);

        filterButton = new Button("Filter");
        filterButton.setClassName("filter-button");
        filterButton.addClickListener(click -> {
            getUI().ifPresent(ui -> {
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("difficulty", difficultyGroup.getSelectedItems().toArray(new String[0]));
                parameters.put("language",
                        Arrays.stream(languageGroup.getSelectedItems().toArray(new String[0]))
                                .map(lang -> UriUtils.encode(lang, StandardCharsets.UTF_8)).toArray(String[]::new));
                parameters.put("format", formatGroup.getSelectedItems().toArray(new String[0]));
                ui.navigate("vaadin_project/"+courseName, QueryParameters.full(parameters));
            });
        });

        add(difficultyBar, languageBar, formatBar, filterButton);

    }

}
