public class MusicBrainzMetadata extends Metadata {

    public final String releaseId;
    public final String mediaTitle;

    public MusicBrainzMetadata(String releaseId,
                               String title,
                               String releaseDate,
                               String albumName,
                               String artistName,
                               String mediaTitle,
                               String trackNumber,
                               String trackCount)
    {
        super(title, releaseDate, albumName, artistName, trackNumber, trackCount);
        this.releaseId  = releaseId;
        this.mediaTitle = mediaTitle;
    }
}
