package com.project.views.components;

import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.entity.ObjAttr;
import com.project.tools.AttributeTool;
import com.project.tools.MiscTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@CssImport("./styles/styles.css")
public class CoursePanel extends VerticalLayout {

    private H1 nameHeader;
    private HorizontalLayout creationInfoLayout;
    private Label creatorLabel;
    private Label creationDateLabel;
    private HorizontalLayout courseDataLayout;
    private VerticalLayout headerInfoLayout;
    private VerticalLayout courseDescrLayout;
    private H2 descriptionHeader;
    private TextArea descriptionTA;
    private HorizontalLayout primAttrLayout;
    private Label difficultyLabel;
    private Label languageLabel;
    private Label formatLabel;
    private VerticalLayout attributeSubLayout;
    private VerticalLayout attributeLayout;
    private Scroller scroller;
    private Label attributeLabel;
    private Label participantsLabel;
    private Button joinCourseButton;
    private Button cancelCourseButton;
    private VerticalLayout participationLayout;
    private Label attributeManagementLabel;
    private MenuBar attributeManagementMenu;

    public CoursePanel(UserControllerInterface controllerInterface,
                       ModeratorControllerInterface moderatorControllerInterface, Obj obj) {
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.START);
        this.addClassName("course-panel");

        Map<Integer, String> mappedObj = ObjectConverter.convertObject(obj);
        ComponentUtil.setData(UI.getCurrent(), "course", mappedObj);

        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = ((KeycloakPrincipal) userAuthentication.getPrincipal());
        String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();

        Obj user = controllerInterface.getUser(username).getBody();
        Map<Integer, String> mappedUser = ObjectConverter.convertObject(user);

        nameHeader = new H1(mappedObj.get(AttrEnum.COURSE_NAME.getValue()));
        nameHeader.setClassName("name-header");

        creatorLabel = new Label("Created by: " + mappedObj.get(AttrEnum.CREATOR.getValue()));
        creatorLabel.setClassName("course-info-label");

        creationDateLabel = new Label("Starts on " + mappedObj.get(AttrEnum.START_DATE.getValue()));
        creationDateLabel.setClassName("course-info-label");

        creationInfoLayout = new HorizontalLayout(creatorLabel, creationDateLabel);
        creationInfoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        creationInfoLayout.setClassName("creation-info-layout");

        headerInfoLayout = new VerticalLayout(nameHeader, creationInfoLayout);
        headerInfoLayout.setAlignItems(Alignment.CENTER);
        headerInfoLayout.setJustifyContentMode(JustifyContentMode.START);
        headerInfoLayout.setWidthFull();
        headerInfoLayout.addClassName("header-info-layout");

        descriptionHeader = new H2("Course description");
        descriptionHeader.setClassName("description-header");

        descriptionTA = new TextArea();
        descriptionTA.setValue(mappedObj.get(AttrEnum.COURSE_DESCRIPTION.getValue()));
        descriptionTA.setClassName("description-ta");
        descriptionTA.setEnabled(false);

        difficultyLabel = new Label("Difficulty: " + mappedObj.get(AttrEnum.DIFFICULTY.getValue()));
        difficultyLabel.addClassName("prim-attr-label");

        languageLabel = new Label("Language: " + mappedObj.get(AttrEnum.LANGUAGE.getValue()));
        languageLabel.addClassName("prim-attr-label");

        formatLabel = new Label("Format: " + mappedObj.get(AttrEnum.FORMAT.getValue()));
        formatLabel.addClassName("prim-attr-label");

        primAttrLayout = new HorizontalLayout(difficultyLabel, languageLabel, formatLabel);
        primAttrLayout.setAlignItems(Alignment.CENTER);
        primAttrLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        primAttrLayout.setClassName("prim-attr-layout");

        courseDescrLayout = new VerticalLayout(descriptionHeader, descriptionTA, primAttrLayout);
        courseDescrLayout.setClassName("course-descr-layout");
        courseDescrLayout.setAlignItems(Alignment.CENTER);

        attributeLayout = new VerticalLayout();
        attributeLayout.setClassName("attribute-layout");
        attributeLayout.setJustifyContentMode(JustifyContentMode.START);
        attributeLayout.setAlignItems(Alignment.START);

        attributeSubLayout = new VerticalLayout();
        attributeSubLayout.setClassName("attrubute-sublayout");
        attributeSubLayout.setJustifyContentMode(JustifyContentMode.START);
        attributeSubLayout.setAlignItems(Alignment.START);

        attributeLabel = new Label("Other attributes: ");
        attributeLabel.setClassName("attribute-label");
        attributeSubLayout.add(attributeLabel);

        for (ObjAttr objAttr: obj.getObjAttrs()) {
            if (objAttr.getAttribute().getAttrId() > AttributeTool.PRIMARY_ATTRIBUTE_ID_SPACE) {
                attributeSubLayout.add(new Label(objAttr.getAttribute().getAttrName() + ": " +
                        objAttr.getValue()));
            }
        }

        scroller = new Scroller(attributeSubLayout);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.addClassName("scroller");
        attributeLayout.add(scroller);

        if (userAuthentication != null && userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR"))) {
            attributeManagementLabel = new Label("Manage attributes");
            attributeManagementMenu = new MenuBar();
            MenuItem menuItem = attributeManagementMenu.addItem(attributeManagementLabel);
            SubMenu subMenu = menuItem.getSubMenu();

            ComponentEventListener<ClickEvent<MenuItem>> attrDeleteListener = click -> {
                Dialog dialog = new Dialog();

                DeleteAttributePanel deleteAttributePanel = new DeleteAttributePanel(controllerInterface, moderatorControllerInterface, obj, dialog);
                dialog.setModal(false);
                dialog.setDraggable(true);

                dialog.add(deleteAttributePanel);
                dialog.open();
            };


            ComponentEventListener<ClickEvent<MenuItem>> optionalAttrCreateListener = click -> {
                Dialog dialog = new Dialog();

                CreateOptionalAttributePanel changeOptionalAttributePanel =
                        new CreateOptionalAttributePanel(controllerInterface, moderatorControllerInterface, mappedObj, dialog);
                dialog.setModal(false);
                dialog.setDraggable(true);

                dialog.add(changeOptionalAttributePanel);
                dialog.open();
            };

            ComponentEventListener<ClickEvent<MenuItem>> optionalAttrChangeListener = click -> {
                Dialog dialog = new Dialog();

                ChangeOptionalAttributePanel changeOptionalAttributePanel =
                        new ChangeOptionalAttributePanel(controllerInterface, moderatorControllerInterface, obj, dialog);
                dialog.setModal(false);
                dialog.setDraggable(true);

                dialog.add(changeOptionalAttributePanel);
                dialog.open();
            };

            subMenu.addItem("Delete attribute", attrDeleteListener);
            subMenu.addItem("Create optional attribute", optionalAttrCreateListener);
            subMenu.addItem("Change optional attribute", optionalAttrChangeListener);
            attributeLayout.add(attributeManagementMenu);

        }
        courseDataLayout = new HorizontalLayout(courseDescrLayout, attributeLayout);
        courseDataLayout.setClassName("course-data-layout");
        courseDataLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        courseDataLayout.setAlignItems(Alignment.STRETCH);

        participantsLabel = new Label(mappedObj.get(AttrEnum.CURRENT_PARTICIPANTS.getValue()) + "/" +
                mappedObj.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue())+ " participants");
        

        joinCourseButton = new Button("Join course");
        joinCourseButton.setClassName("big-button");
        joinCourseButton.addClickListener(click -> {
            addUserCourse(controllerInterface, mappedObj, mappedUser);
        });

        cancelCourseButton = new Button("Cancel course");
        cancelCourseButton.setClassName("big-button");
        cancelCourseButton.addClickListener(click -> {
            cancelUserCourse(controllerInterface, mappedObj, mappedUser);
        });

        if (Integer.parseInt(mappedObj.get(AttrEnum.CURRENT_PARTICIPANTS.getValue()))
                >= Integer.parseInt(mappedObj.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue()))) {
            cancelCourseButton.setEnabled(false);
            joinCourseButton.setEnabled(false);
        }
        participationLayout = new VerticalLayout(participantsLabel);
        if (mappedUser.get(AttrEnum.USER_COURSES.getValue()).contains(Integer.toString(ObjectConverter.getIdFromMappedObj(mappedObj)) +";")) {
            participationLayout.add(cancelCourseButton);
        }
        else {
            participationLayout.add(joinCourseButton);
        }
        participationLayout.setClassName("participation-layout");
        participationLayout.setJustifyContentMode(JustifyContentMode.START);
        participationLayout.setAlignItems(Alignment.CENTER);

        this.add(headerInfoLayout, new Hr(), courseDataLayout, new Hr(),participationLayout);

    }

    private void cancelUserCourse(UserControllerInterface controllerInterface, Map<Integer, String> mappedObj,
                                  Map<Integer, String> mappedUser) {
        Obj upToDateCourse = controllerInterface.getObjectById(ObjectConverter.getIdFromMappedObj(mappedObj)).getBody();
        Map<Integer, String> mappedUpToDateCourse = ObjectConverter.convertObject(upToDateCourse);
        if (Integer.parseInt(mappedUpToDateCourse.get(AttrEnum.CURRENT_PARTICIPANTS.getValue())) < Integer.parseInt(
                mappedUpToDateCourse.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue())
        )) {
            String updatedParticipants = Integer.toString (Integer.parseInt(mappedUpToDateCourse.get(AttrEnum.CURRENT_PARTICIPANTS.getValue())) - 1);
            String updatedUserCourses;
            if (mappedUser.containsKey(AttrEnum.USER_COURSES.getValue())) {
                updatedUserCourses = MiscTool.removeNumFromStringList(mappedUser.get(AttrEnum.USER_COURSES.getValue()),
                        ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse));
            }
            else {
                return;
            }

            Map<String, String> mappedObjAttr = AttributeTool.convertObjAttr("current participants",
                    updatedParticipants, ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse));
            controllerInterface.addUserCourse(mappedObjAttr);
            Map<String, String> mappedUserCourses = AttributeTool.convertObjAttr("user courses",
                    updatedUserCourses, ObjectConverter.getIdFromMappedObj(mappedUser));
            controllerInterface.addUserCourse(mappedUserCourses);


            String updatedCourse;
            if (mappedUpToDateCourse.containsKey(AttrEnum.SUBSCRIBERS.getValue())) {
                updatedCourse = MiscTool.removeNumFromStringList(mappedUpToDateCourse.get(AttrEnum.SUBSCRIBERS.getValue()),
                                ObjectConverter.getIdFromMappedObj(mappedUser));
            }
            else {
                return;
            }

            Map<String, String> mappedCourse = AttributeTool.convertObjAttr("subscribers",
                    updatedCourse, ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse));
            controllerInterface.addUserCourse(mappedCourse);
        }
        UI.getCurrent().getPage().reload();
    }

    private void addUserCourse(UserControllerInterface controllerInterface, Map<Integer, String> mappedObj,
                               Map<Integer, String> mappedUser) {
        Obj upToDateCourse = controllerInterface.getObjectById(ObjectConverter.getIdFromMappedObj(mappedObj)).getBody();
        Map<Integer, String> mappedUpToDateCourse = ObjectConverter.convertObject(upToDateCourse);
        if (Integer.parseInt(mappedUpToDateCourse.get(AttrEnum.CURRENT_PARTICIPANTS.getValue())) < Integer.parseInt(
                mappedUpToDateCourse.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue())
        )) {
            String updatedParticipants = Integer.toString(Integer.parseInt(mappedUpToDateCourse.get(AttrEnum.CURRENT_PARTICIPANTS.getValue())) + 1);
            Map<String, String> mappedObjAttr = AttributeTool.convertObjAttr("current participants",
                    updatedParticipants, ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse));
            controllerInterface.addUserCourse(mappedObjAttr);

            String updatedUserCourses;
            if (mappedUser.containsKey(AttrEnum.USER_COURSES.getValue())) {
                updatedUserCourses = mappedUser.get(AttrEnum.USER_COURSES.getValue()) +
                        Integer.toString(ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse)) + ";";
            } else {
                updatedUserCourses = mappedUpToDateCourse.get(Integer.toString(ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse))) + ";";
            }
            Map<String, String> mappedUserCourses = AttributeTool.convertObjAttr("user courses",
                    updatedUserCourses, ObjectConverter.getIdFromMappedObj(mappedUser));
            controllerInterface.addUserCourse(mappedUserCourses);

            String updatedCourse;
            if (mappedUpToDateCourse.containsKey(AttrEnum.SUBSCRIBERS.getValue())
                    && mappedUpToDateCourse.get(AttrEnum.SUBSCRIBERS.getValue()) != null) {
                updatedCourse = mappedUpToDateCourse.get(AttrEnum.SUBSCRIBERS.getValue()) +
                        Integer.toString(ObjectConverter.getIdFromMappedObj(mappedUser)) + ";";
            } else {
                updatedCourse = Integer.toString(ObjectConverter.getIdFromMappedObj(mappedUser)) + ";";
            }
            Map<String, String> mappedCourse = AttributeTool.convertObjAttr("subscribers",
                    updatedCourse, ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse));
            controllerInterface.addUserCourse(mappedCourse);

            if (Integer.parseInt(updatedParticipants) == Integer.parseInt(mappedUpToDateCourse.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue()))) {
                controllerInterface.sendMailNotifications(ObjectConverter.getIdFromMappedObj(mappedUpToDateCourse));
            }
        }

        UI.getCurrent().getPage().reload();
    }



}
