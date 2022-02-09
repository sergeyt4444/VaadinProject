package com.project.entity;

public enum AttrEnum {
    COURSE_NAME(1), COURSE_DESCRIPTION(2), PARENT_ID(3);

    private final int attrId;

    private AttrEnum(int attrId) {
        this.attrId = attrId;
    }

    public int getValue() {
        return attrId;
    }
}
