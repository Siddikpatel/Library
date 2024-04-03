package com.librayapp.springBootLibrary.Utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {

    public static String payloadJWTExtraction(String token, String extraction) {

        token.replace("Bearer ", "");

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        String[] entries = payload.split(",");
        Map<String, String> map = new HashMap<>();

        for(String e : entries) {
            String[] keyVal = e.split(":");

            if(keyVal[0].equals(extraction)) {
                int remove = 1;
                if (keyVal[1].endsWith("}")) {
                    remove = 2;
                }
                keyVal[1] = keyVal[1].substring(0, keyVal[1].length() - remove);
                keyVal[1] = keyVal[1].substring(1);

                map.put(keyVal[0], keyVal[1]);
            }
        }

        if(map.containsKey(extraction)) {
            return map.get(extraction);
        }
        return null;
    }
}
