import java.util.ArrayList;
import java.io.IOException;

public interface MetadataProvider <T extends Metadata> {

    ArrayList<T> getMetadataList(String artist, String title) throws IOException;
    void setAlbumCover(T metadata) throws IOException;
}
