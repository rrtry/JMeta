public class Metadata {

    public final String title;
    public final String releaseDate;
    public final String albumName;
    public final String artistName;
    public final String trackNumber;
    public final String trackCount;

    private AlbumCover coverArt;

    public Metadata(
            String title,
            String releaseDate,
            String albumName,
            String artistName,
            String trackNumber,
            String trackCount)
    {
        this.title       = title;
        this.releaseDate = releaseDate;
        this.albumName   = albumName;
        this.artistName  = artistName;
        this.trackNumber = trackNumber;
        this.trackCount  = trackCount;
    }

    public void setCoverArt(AlbumCover coverArt) {
        this.coverArt = coverArt;
    }

    public AlbumCover getCoverArt() {
        return coverArt;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n%s\n", title, artistName, albumName, releaseDate);
    }
}
