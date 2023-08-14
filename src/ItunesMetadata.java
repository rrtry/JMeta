public class ItunesMetadata extends Metadata {

    public static final int ARTWORK_DIM_30    = 30;
    public static final int ARTWORK_SIZE_40   = 40;
    public static final int ARTWORK_SIZE_60   = 60;
    public static final int ARTWORK_SIZE_100  = 100;
    public static final int ARTWORK_SIZE_110  = 110;
    public static final int ARTWORK_SIZE_130  = 130;
    public static final int ARTWORK_SIZE_150  = 150;
    public static final int ARTWORK_SIZE_160  = 160;
    public static final int ARTWORK_SIZE_170  = 170;
    public static final int ARTWORK_SIZE_200  = 200;
    public static final int ARTWORK_SIZE_220  = 220;
    public static final int ARTWORK_SIZE_230  = 230;
    public static final int ARTWORK_SIZE_250  = 250;
    public static final int ARTWORK_SIZE_340  = 340;
    public static final int ARTWORK_SIZE_400  = 400;
    public static final int ARTWORK_SIZE_440  = 440;
    public static final int ARTWORK_SIZE_450  = 450;
    public static final int ARTWORK_SIZE_460  = 460;
    public static final int ARTWORK_SIZE_600  = 600;
    public static final int ARTWORK_SIZE_1200 = 1200;
    public static final int ARTWORK_SIZE_1400 = 1400;

    public final String discCount;
    public final String discNumber;
    public final String genre;
    public final String country;
    public final String artworkURL;

    public ItunesMetadata(String title,
                          String releaseDate,
                          String albumName,
                          String artistName,
                          String trackNumber,
                          String trackCount,
                          String discNumber,
                          String discCount,
                          String genre,
                          String country,
                          String artworkURL)
    {
        super(title, releaseDate, albumName, artistName, trackNumber, trackCount);
        this.discNumber = discNumber;
        this.discCount  = discCount;
        this.genre      = genre;
        this.country    = country;
        this.artworkURL = artworkURL;
    }
}
