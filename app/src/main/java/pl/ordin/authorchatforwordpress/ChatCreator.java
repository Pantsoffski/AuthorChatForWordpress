package pl.ordin.authorchatforwordpress;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;

public class ChatCreator {

    public void readJsonStream(InputStreamReader streamReader) throws IOException { // TODO: 22.06.2017 expects begin_object but its string 
        JsonReader jsonReader = new JsonReader(streamReader);

        try {
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all names
                String name = jsonReader.nextName(); // Fetch the next name
                if (name.equals("msg")) { // Check if desired name
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        read(jsonReader);
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
    }

    public void read(JsonReader reader) throws Exception {

        String message;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("0")) {
                message = reader.nextString();
                System.out.println(message);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}