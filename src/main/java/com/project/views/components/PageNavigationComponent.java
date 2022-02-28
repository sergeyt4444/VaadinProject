package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.tools.MiscTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageNavigationComponent extends HorizontalLayout {

    private Button firstPageButton;
    private Button prevPageButton;
    private Label currentPageLabel;
    private Label miscLabel;
    private Label pagesCountLabel;
    private Button nextPageButton;
    private Button lastPageButton;


    public PageNavigationComponent(int currentPage, int pagesCount, BeforeEvent event) {
        setAlignItems(Alignment.CENTER);

        this.setClassName("page-nav-component");
        Location location = event.getLocation();

        Icon firstIcon = new Icon("fast-backward");
        firstPageButton = new Button(firstIcon);
        firstPageButton.addClickListener(click -> {
            getUI().ifPresent(ui -> {
                Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();
                if (parameters == null) {
                    parameters = new HashMap<>();
                }
                Map<String, String[]> convertedParams = MiscTool.convertMaptoQueryParamMap(parameters);
                convertedParams.put("page", new String[] {"1"});
                ui.navigate(location.getPath(), QueryParameters.full(convertedParams));
            });
        });
        firstPageButton.setClassName("page-nav-button");

        Icon prevIcon = new Icon("step-backward");
        prevPageButton = new Button(prevIcon);
        int finalCurrentPage = currentPage;
        prevPageButton.addClickListener(click -> {
            getUI().ifPresent(ui -> {
                Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();
                if (parameters == null) {
                    parameters = new HashMap<>();
                }
                Map<String, String[]> convertedParams = MiscTool.convertMaptoQueryParamMap(parameters);
                convertedParams.put("page", new String[] {Integer.toString(finalCurrentPage - 1)});
                ui.navigate(location.getPath(), QueryParameters.full(convertedParams));
            });
        });
        prevPageButton.setClassName("page-nav-button");

        currentPageLabel = new Label(Integer.toString(currentPage));
        currentPageLabel.setClassName("page-nav-label");

        miscLabel = new Label("/");
        miscLabel.setClassName("page-nav-label");

        pagesCountLabel = new Label(Integer.toString(pagesCount));
        pagesCountLabel.setClassName("page-nav-label;");

        Icon nextIcon = new Icon("step-forward");
        nextPageButton = new Button(nextIcon);
        nextPageButton.addClickListener(click -> {
            getUI().ifPresent(ui -> {
                Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();
                if (parameters == null) {
                    parameters = new HashMap<>();
                }
                Map<String, String[]> convertedParams = MiscTool.convertMaptoQueryParamMap(parameters);
                convertedParams.put("page", new String[] {Integer.toString(finalCurrentPage + 1)});
                ui.navigate(location.getPath(), QueryParameters.full(convertedParams));
            });
        });
        nextPageButton.setClassName("page-nav-button");

        Icon lastIcon = new Icon("fast-forward");
        lastPageButton = new Button(lastIcon);
        lastPageButton.addClickListener(click -> {
            getUI().ifPresent(ui -> {
                Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();
                if (parameters == null) {
                    parameters = new HashMap<>();
                }
                Map<String, String[]> convertedParams = MiscTool.convertMaptoQueryParamMap(parameters);
                convertedParams.put("page", new String[] {Integer.toString(pagesCount)});
                ui.navigate(location.getPath(), QueryParameters.full(convertedParams));
            });
        });
        lastPageButton.setClassName("page-nav-button");

        if (currentPage == 1) {
            firstPageButton.setEnabled(false);
            prevPageButton.setEnabled(false);
        }
        if (currentPage == pagesCount) {
            lastPageButton.setEnabled(false);
            nextPageButton.setEnabled(false);
        }

        add(firstPageButton, prevPageButton, currentPageLabel, miscLabel, pagesCountLabel, nextPageButton, lastPageButton);

    }

}
