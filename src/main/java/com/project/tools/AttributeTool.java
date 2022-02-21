package com.project.tools;

import java.util.*;

public class AttributeTool {

    public static final List<String> DIFFICULTIES = Collections.unmodifiableList(
            Arrays.asList("Easy", "Moderate", "Hard"));

    public static final List<String> LANGUAGES = Collections.unmodifiableList(
            Arrays.asList("C++", "Java", "JavaScript", "Python"));

    public static final List<String> FORMATS = Collections.unmodifiableList(
            Arrays.asList("In-person", "Remote"));

    public static final List<String> REQ_ATTRIBUTES = Collections.unmodifiableList(
            Arrays.asList("course name", "course description", "parent id", "creator", "start date", "difficulty",
                    "language", "format", "participants required", "current participants"));

    public static final int PRIMARY_ATTRIBUTE_ID_SPACE = 11;

    public static List<String> getDifficulties() {
        return DIFFICULTIES;
    }

    public static List<String> getLanguages() {
        return LANGUAGES;
    }

    public static List<String> getFormats() {
        return FORMATS;
    }

    public static List<String> getReqAttributes() {
        return REQ_ATTRIBUTES;
    }

    public static Map<String, String> convertObjAttr(String name, String value, int objId) {
        Map<String, String> mappedObjAttr = new HashMap<>();
        mappedObjAttr.put("name", name);
        mappedObjAttr.put("value", value);
        mappedObjAttr.put("isHidden", "false");
        mappedObjAttr.put("isMultiple", "false");
        mappedObjAttr.put("type", "text");
        mappedObjAttr.put("objId", Integer.toString(objId));
        return mappedObjAttr;
    }

}
