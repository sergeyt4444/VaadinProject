package com.project.tools;

import feign.template.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiscTool {

    public static Map<String, String[]> convertMaptoQueryParamMap(Map<String, List<String>> map) {
        Map<String, String[]> convertedMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry: map.entrySet()) {
            convertedMap.put(entry.getKey(), entry.getValue().stream()
                    .map(lang -> UriUtils.encode(lang, StandardCharsets.UTF_8)).toArray(String[]::new));
        }
        return convertedMap;
    }

}
