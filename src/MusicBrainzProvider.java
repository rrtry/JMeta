import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MusicBrainzProvider extends AbstractMetadataProvider<MusicBrainzMetadata> {

    @Override
    public ArrayList<MusicBrainzMetadata> getMetadataList(String artist, String title) throws IOException {
        return parseRecordingMetadata(new String(
                getResponse(new URL("https://musicbrainz.org/ws/2/recording/?query=" + URLEncoder.encode(String.format("\"%s\" AND artist:\"%s\"?inc=genres", title, artist), UTF_8)))
        ));
    }

    @Override
    public void setAlbumCover(MusicBrainzMetadata metadata) throws IOException {
        metadata.setCoverArt(parseCoverArt(new String(
                getResponse(new URL("https://coverartarchive.org/release/" + URLEncoder.encode(metadata.releaseId, UTF_8)))
        )));
    }

    private AlbumCover parseCoverArt(String jsonString) throws IOException {

        Gson gson             = new Gson();
        JsonObject json       = gson.fromJson(jsonString, JsonObject.class);
        JsonArray imagesArray = json.getAsJsonArray("images");
        JsonObject imageEntry = json.getAsJsonArray("images")
                .get(0).getAsJsonObject();

        // set front cover
        for (JsonElement element : imagesArray) {

            JsonObject image   = element.getAsJsonObject();
            boolean isFront    = image.get("front").getAsBoolean();
            boolean isApproved = image.get("approved").getAsBoolean();

            if (isFront && isApproved) {
                imageEntry = element.getAsJsonObject();
                break;
            }
        }

        String imageUrl = imageEntry.get("image").getAsString();
        String mimeType = "image/" + imageUrl.substring(imageUrl.lastIndexOf(".") + 1);

        URL url = new URL(imageUrl);
        byte[] bytes;

        try (InputStream in = url.openStream()) {
            bytes = in.readAllBytes();
        }
        return new AlbumCover(bytes, mimeType);
    }

    private ArrayList<MusicBrainzMetadata> parseRecordingMetadata(String jsonString) {

        Gson gson             = new Gson();
        JsonObject json       = gson.fromJson(jsonString, JsonObject.class);
        JsonArray recordings  = json.getAsJsonArray("recordings");

        ArrayList<MusicBrainzMetadata> entries = new ArrayList<>();
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

                    MusicBrainzMetadata metadata = new MusicBrainzMetadata(
                            releaseId,
                            releaseName,
                            releaseDate,
                            albumName,
                            mediaTitle,
                            artistName,
                            trackNumber,
                            trackCount
                    );
                    entries.add(metadata);

                } catch (NullPointerException ignored) {
                    // some field missing
                }
            }
        }
        return entries;
    }
}
