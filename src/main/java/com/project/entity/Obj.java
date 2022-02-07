package com.project.entity;


import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Obj {

    private int objId;

    private List<ObjAttr> objAttrs;

    private ObjectType objectType;

    @Override
    public String toString() {
        return "Obj{" +
                "objId=" + objId +
                ", objAttrs=" + objAttrs +
                ", objectType=" + objectType +
                '}';
    }
}
