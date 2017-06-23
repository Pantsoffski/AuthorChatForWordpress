package pl.ordin.authorchatforwordpress;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class ChatCreator {

    ArrayList<String> readJsonStream(InputStreamReader streamReader) throws IOException {

        ArrayList<String> chatToShowList = new ArrayList<>();

        JsonReader jsonReader = new JsonReader(streamReader);

        try {
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all names
                String name = jsonReader.nextName(); // Fetch the next name
                if (name.equals("msg")) { // Check if desired name
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        chatToShowList.add(jsonReader.nextString());
                    }
                    jsonReader.endArray();
                } else {
                    jsonReader.skipValue(); // Skip values of other names
                }
            }

            jsonReader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatToShowList;
    }

}