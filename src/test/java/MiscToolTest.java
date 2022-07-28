import com.project.tools.MiscTool;
import feign.template.UriUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MiscToolTest {

    @Test
    public void TestRemoveNumFromList() {
        String testString = "1;2;3;";
        String updatedString = MiscTool.removeNumFromStringList(testString, 1);
        assertEquals(updatedString, "2;3;");
    }

    @Test
    public void TestRemoveUnexistingNumFromList() {
        String testString = "11;12;13;";
        String updatedString = MiscTool.removeNumFromStringList(testString, 1);
        assertEquals(updatedString, "11;12;13;");
    }

    @Test
    public void TestRemoveDuplicateNumFromList() {
        String testString = "1;1;2;3;";
        String updatedString = MiscTool.removeNumFromStringList(testString, 1);
        assertEquals(updatedString, "1;2;3;");
    }

    @Test
    public void TestRemoveNumFromEmptyList() {
        String testString = ";";
        String updatedString = MiscTool.removeNumFromStringList(testString, 1);
        assertEquals(updatedString, "");
    }

    @Test
    public void TestConvertMap() {
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("1");
        map.put("id", list);
        Map<String, String[]> convertedMap = MiscTool.convertMaptoQueryParamMap(map);
        String[] arr = new String[] {"1"};
        assertTrue(Arrays.equals(convertedMap.get("id"), arr));
    }

    @Test
    public void TestConvertMapWithSymbols() {
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("C++");
        map.put("id", list);
        Map<String, String[]> convertedMap = MiscTool.convertMaptoQueryParamMap(map);
        String[] arr = new String[1];
        arr[0] = UriUtils.encode("C++", StandardCharsets.UTF_8);
        assertTrue(Arrays.equals(arr, convertedMap.get("id")));
    }

}
