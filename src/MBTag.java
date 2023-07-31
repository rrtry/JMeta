public class MBTag {

    public final String releaseName;
    public final String releaseDate;
    public final String albumName;
    public final String artistName;
    public final String mediaTitle;
    public final String trackNumber;
    public final String trackCount;
    public final String releaseId;

    private MBCoverArt coverArt;

    public MBTag(
            String releaseId,
            String releaseName,
            String releaseDate,
            String albumName,
            String artistName,
            String mediaTitle,
            String trackNumber,
            String trackCount)
    {
        this.releaseId   = releaseId;
        this.releaseName = releaseName;
        this.releaseDate = releaseDate;
        this.albumName   = albumName;
        this.artistName  = artistName;
        this.mediaTitle  = mediaTitle;
        this.trackNumber = trackNumber;
        this.trackCount  = trackCount;
    }

    public void setCoverArt(MBCoverArt coverArt) {
        this.coverArt = coverArt;
    }

    public MBCoverArt getCoverArt() {
        return coverArt;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n%s\n", mediaTitle, artistName, albumName, releaseDate);
    }
}
