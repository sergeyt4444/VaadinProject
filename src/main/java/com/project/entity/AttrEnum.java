package com.project.entity;

public enum AttrEnum {
    COURSE_NAME(1), COURSE_DESCRIPTION(2), PARENT_ID(3), TEST_ATTRIBUTE(4), CREATOR(5), CREATION_DATE(6);

    private final int attrId;

    private AttrEnum(int attrId) {
        this.attrId = attrId;
    }

    public int getValue() {
        return attrId;
    }
}
