import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JMeta {

    private final MetadataProvider provider;

    public JMeta(MetadataProvider provider) {
        this.provider = provider;
    }

    public <T extends Metadata> T getMetadata(String artist, String title) throws IOException {

        ArrayList<T> entries = provider.getMetadataList(artist, title);
        if (entries.isEmpty()) return null;

        T metadata = entries.get(0);
        provider.setAlbumCover(metadata);
        return metadata;
    }

    public <T extends Metadata> ArrayList<T> getMetadataList(String artist, String title) throws IOException {
        return provider.getMetadataList(artist, title);
    }
}
