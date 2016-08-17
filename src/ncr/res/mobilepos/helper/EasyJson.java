package ncr.res.mobilepos.helper;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class EasyJson {
    public static Map<String, String> toMap(String jsonString) {
        try {
            return new ObjectMapper().readValue(jsonString,
                         new TypeReference<HashMap<String, String>>(){});
        } catch (IOException e) {
            Logger.getInstance().logSnapException("EasyJson", "E1",
                                  "failed to convert:" + jsonString, e);
        }
        return Collections.emptyMap();
    }
}
