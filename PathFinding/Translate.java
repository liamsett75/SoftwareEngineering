package PathFinding;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import org.json.JSONObject;

import java.io.IOException;


public class Translate {
    static String subscriptionKey = "4195da3b490040aeb7c5c1ba83a3d114";

    public static String url = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=";

//    public static String getUrl() {
//        return url;
//    }

//    public static void setUrl(String url) {
//        Translate.url = url;
//    }

    public static String translateDirections(String toTranslate) {
        String finalString = "";
        try {
            Translate translateRequest = new Translate();
            String response = translateRequest.Post(toTranslate);
            //System.out.println(prettify(response).toString());
            String pretty = prettify(response);
            String test[] = pretty.split("\\r?\\n");
            //char[] charArray = test.toCharArray();
            for(String str : test) {
                if(str.contains("text")) {
                    char[] charArray = str.trim().toCharArray();
                    for (int i = 9; i < charArray.length - 2; i++) {
                        if (!(charArray[i] == '\\' && charArray[i + 1] == 'n')) {
                            finalString += charArray[i];
                        }
                        else {
                            i++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Translate returning: " + finalString);
        return finalString;
    }

    public String Post(String bamBitch) throws IOException {
        // Instantiates the OkHttpClient.
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\n\t\"Text\": \""+bamBitch+"\"\n}]");
        Request request = new Request.Builder()
                .url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Content-type", "application/json").build();
        Response response = client.newCall(request).execute();

        //System.out.println(jsonObject.getJSONObject("translations"));
        //return jsonObject.getJSONObject("translations").getString("text");

        return response.body().string();
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(json);
        JSONObject jsonObject = new JSONObject();
        //jsonObject = json;
        return gson.toJson(json);
    }
}
