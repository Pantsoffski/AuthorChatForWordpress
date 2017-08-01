package pl.ordin.authorchatforwordpress;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class {@link ChatCreator} is responsible processing JSON object.
 */
class ChatCreator {

    ArrayList<CustomArrayList> readJsonStream(InputStreamReader streamReader) throws IOException {

        ArrayList<String> nick = new ArrayList<>();
        ArrayList<String> chat = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();

        ArrayList<CustomArrayList> chatToShowList = new ArrayList<>();

        JsonReader jsonReader = new JsonReader(streamReader);

        try {
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all names
                String name = jsonReader.nextName(); // Fetch the next name
                if (name.equals("nick")) { // Check if desired name
                    jsonReader.beginArray(); //consume the array's opening bracket
                    while (jsonReader.hasNext()) {
                        nick.add(jsonReader.nextString());
                    }
                    jsonReader.endArray(); //read the array's closing bracket
                } else if (name.equals("msg")) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        chat.add(jsonReader.nextString());
                    }
                    jsonReader.endArray();
                } else if (name.equals("date")) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        date.add(jsonReader.nextString());
                    }
                    jsonReader.endArray();
                } else {
                    jsonReader.skipValue(); // Skip values of other names
                }
            }
            jsonReader.endObject();

            //put all values to CustomArrayList
            for (int i = 0; i < nick.size(); i++) {
                chatToShowList.add(new CustomArrayList(nick.get(i), chat.get(i), date.get(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatToShowList;
    }

}