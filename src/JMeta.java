import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JMeta {

    private static final String USER_AGENT = "JMeta/1.0 (fedormocalov36@gmail.com)";

    private static byte[] getResponse(URL url) throws IOException {

        HttpURLConnection connection = openConnection(url);
        InputStream in = connection.getInputStream();

        return in.readAllBytes();
    }

    private static HttpURLConnection openConnection(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "application/json");

        return connection;
    }

    public static void fetchCoverArt(MBTag tag) throws IOException {

        final String urlString = String.format("https://coverartarchive.org/release/%s", tag.releaseId)
                .replace(" ", "%20");

        byte[] buffer = getResponse(new URL(urlString));
        tag.setCoverArt(parseCoverArt(new String(buffer)));
    }

    public static ArrayList<MBTag> fetchTagList(String artist, String title, File outFile) throws IOException {

        final String urlString = String.format("https://musicbrainz.org/ws/2/recording/?query=\"%s\" AND artist:\"%s\"?inc=genres", title, artist)
                .replace(" ", "%20");

        byte[] buffer = getResponse(new URL(urlString));
        if (outFile != null) {
            FileOutputStream out = new FileOutputStream(outFile.getAbsolutePath());
            out.write(buffer);
            out.close();
        }

        return parseRecordingMetadata(new String(buffer));
    }

    public static ArrayList<MBTag> parseRecordingMetadata(File fileObj) throws IOException {

        if (!fileObj.isFile()) {
            System.err.println(fileObj.getName() + " is not a regular file");
            return null;
        }

        FileInputStream in = new FileInputStream(fileObj);
        byte[] buffer      = in.readAllBytes();
        in.close();

        return parseRecordingMetadata(new String(buffer));
    }

    private static MBCoverArt parseCoverArt(String jsonString) throws IOException {

        Gson gson             = new Gson();
        JsonObject json       = gson.fromJson(jsonString, JsonObject.class);
        JsonObject imageEntry = json.getAsJsonArray("images")
                .get(0).getAsJsonObject();

        String imageUrl = imageEntry.get("image").getAsString();
        String mimeType = "image/" + imageUrl.substring(imageUrl.lastIndexOf(".") + 1);

        URL url        = new URL(imageUrl);
        InputStream in = url.openStream();
        byte[] bytes   = in.readAllBytes();
        in.close();

        return new MBCoverArt(bytes, mimeType);
    }

    private static ArrayList<MBTag> parseRecordingMetadata(String jsonString) {

        Gson gson             = new Gson();
        JsonObject json       = gson.fromJson(jsonString, JsonObject.class);
        JsonArray recordings  = json.getAsJsonArray("recordings");
        ArrayList<MBTag> tags = new ArrayList<>();

        for (JsonElement e : recordings) {

            JsonObject recording = e.getAsJsonObject();
            JsonArray releases   = recording.getAsJsonArray("releases");
            if (releases == null) continue;

            for (JsonElement f : releases) {
                try {

                    JsonObject release = f.getAsJsonObject();
                    String releaseName = release.get("title").getAsString();
                    String releaseId   = release.get("id").getAsString();
                    String releaseDate = release.get("date").getAsString();
                    String albumName   = release.get("release-group")
                            .getAsJsonObject().get("title").getAsString();

                    String artistName = release.get("artist-credit")
                            .getAsJsonArray().get(0).getAsJsonObject()
                            .get("artist").getAsJsonObject()
                            .get("name").getAsString();

                    String mediaTitle = release.get("media")
                            .getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("track").getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("title").getAsString();

                    String trackNumber = release.get("media")
                            .getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("track").getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("number").getAsString();

                    String trackCount = release.get("media")
                            .getAsJsonArray().get(0)
                            .getAsJsonObject().get("track-count")
                            .getAsString();

                    if (artistName.equals("Various Artists")) {
                        continue;
                    }

                    MBTag tag = new MBTag(
                            releaseId,
                            releaseName,
                            releaseDate,
                            albumName,
                            artistName,
                            mediaTitle,
                            trackNumber,
                            trackCount
                    );
                    tags.add(tag);

                } catch (NullPointerException ignored) {
                    // some field missing
                }
            }
        }
        return tags;
    }
}
