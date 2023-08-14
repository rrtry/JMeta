import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractMetadataProvider<T extends Metadata> implements MetadataProvider<T> {

    public static final String DEFAULT_USER_AGENT = "JMeta/1.0 (fedormocalov36@gmail.com)";
    private String clientUserAgent = DEFAULT_USER_AGENT;

    public AbstractMetadataProvider(String clientUserAgent) {
        this.clientUserAgent = clientUserAgent;
    }

    public AbstractMetadataProvider() {

    }

    protected byte[] getResponse(URL url) throws IOException {
        HttpURLConnection connection = null;
        try {

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", clientUserAgent);
            connection.setRequestProperty("Accept", "application/json");

            byte[] bytes;
            try (InputStream in = connection.getInputStream()) {
                bytes = in.readAllBytes();
            }
            return bytes;

        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
