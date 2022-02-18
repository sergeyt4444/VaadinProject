package com.project.entity;

public enum ObjectTypeEnum {
    CATEGORY(1), COURSE(2);

    private final int objTypeId;

    private ObjectTypeEnum(int objTypeId) {
        this.objTypeId = objTypeId;
    }

    public int getValue() {
        return objTypeId;
    }


}
