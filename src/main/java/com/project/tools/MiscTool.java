package com.project.tools;

import feign.template.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class MiscTool {

    public static Map<String, String[]> convertMaptoQueryParamMap(Map<String, List<String>> map) {
        Map<String, String[]> convertedMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry: map.entrySet()) {
            convertedMap.put(entry.getKey(), entry.getValue().stream()
                    .map(lang -> UriUtils.encode(lang, StandardCharsets.UTF_8)).toArray(String[]::new));
        }
        return convertedMap;
    }

    public static String removeNumFromStringList(String stringList, int forRemoval) {
        List<String> list = new ArrayList<>();
        if (stringList != null) {
            list.addAll(Arrays.asList(stringList.split(";")));
        }
        if (list.contains(Integer.toString(forRemoval))) {
            list.remove(Integer.toString(forRemoval));
        }
        StringBuilder result = new StringBuilder();
        for (String elem: list) {
            result.append(elem).append(";");
        }
        return result.toString();
    }

}
