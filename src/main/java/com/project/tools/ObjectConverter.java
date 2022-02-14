package com.project.tools;

import com.project.entity.Obj;
import com.project.entity.ObjAttr;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectConverter {

    public static Map<Integer, String> convertObject(Obj obj) {
        Map<Integer,String> result = new HashMap<>();
        for(ObjAttr objAttr: obj.getObjAttrs()) {
            result.put(objAttr.getAttribute().getAttrId(), objAttr.getValue());
        }
        return result;
    }

    public static List<Map<Integer, String>> convertListOfObjects(List<Obj> list) {
        List<Map<Integer, String>> result = new ArrayList<>();
        for (Obj obj: list) {
            Map<Integer, String> mappedObj = convertObject(obj);
            result.add(mappedObj);
        }
        return result;
    }

    public static boolean validateMappedObject(Map<Integer, String> mappedObj) {
        for (String attrValue: mappedObj.values()) {
            if (attrValue == null || attrValue.length() <= 2 || attrValue.length() > 250) {
                return false;
            }
        }
        return true;
    }

}
