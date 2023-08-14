import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ItunesProvider extends AbstractMetadataProvider<ItunesMetadata> {

    private int artworkSize = ItunesMetadata.ARTWORK_SIZE_600;

    public ItunesProvider(int artworkSize) {
        this.artworkSize = artworkSize;
    }

    public ItunesProvider() {

    }

    @Override
    public ArrayList<ItunesMetadata> getMetadataList(String artist, String title) throws IOException {
        return parseResponse(new String(getResponse(
                new URL("https://itunes.apple.com/search?" + String.format("term=%s-%s&media=music&entity=song", URLEncoder.encode(artist, UTF_8), URLEncoder.encode(title, UTF_8))
        ))));
    }

    @Override
    public void setAlbumCover(ItunesMetadata metadata) throws IOException {
        metadata.setCoverArt(new AlbumCover(getResponse(new URL(metadata.artworkURL)), "image/jpg"));
    }

    private ArrayList<ItunesMetadata> parseResponse(String response) {

        Gson gson          = new Gson();
        JsonObject json    = gson.fromJson(response, JsonObject.class);
        JsonArray elements = json.getAsJsonArray("results");

        ArrayList<ItunesMetadata> entries = new ArrayList<>();
        for (JsonElement element : elements) {

            JsonObject entry   = element.getAsJsonObject();
            String title       = entry.get("trackName").getAsString();
            String releaseDate = entry.get("releaseDate").getAsString();
            String albumName   = entry.get("collectionName").getAsString();
            String artistName  = entry.get("artistName").getAsString();
            String trackNumber = entry.get("trackNumber").getAsString();
            String trackCount  = entry.get("trackCount").getAsString();
            String discNumber  = entry.get("discNumber").getAsString();
            String discCount   = entry.get("discCount").getAsString();
            String genre       = entry.get("primaryGenreName").getAsString();
            String country     = entry.get("country").getAsString();
            String artworkURL  = entry.get("artworkUrl100").getAsString();
            releaseDate        = releaseDate.substring(0, releaseDate.length() - 1);

            if (artworkSize != 100) {
                String[] parts = artworkURL.split("/");
                parts[parts.length - 1] = String.format("%dx%dbb.jpg", artworkSize, artworkSize);
                artworkURL = String.join("/", parts);
            }

            entries.add(new ItunesMetadata(
                    title,
                    releaseDate,
                    albumName,
                    artistName,
                    trackNumber,
                    trackCount,
                    discNumber,
                    discCount,
                    genre,
                    country,
                    artworkURL
            ));
        }
        return entries;
    }
}
