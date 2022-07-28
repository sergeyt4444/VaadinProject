import com.project.tools.AttributeTool;
import org.junit.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttributeToolTest {

    @Test
    public void ConvertObjAttr() {
        String objAttrName = "test name";
        String objAttrValue = "test value";
        int objId = 123;
        Map<String, String> convertedObjAttr = AttributeTool.convertObjAttr(objAttrName, objAttrValue, objId);
        assertEquals(convertedObjAttr.get("name"), objAttrName);
        assertEquals(convertedObjAttr.get("value"), objAttrValue);
        assertEquals(convertedObjAttr.get("objId"), "123");;
    }

}
