package spotify;

public class Artist {
    private String href;
    private String id;
    private String name;
    private String type;
    private String uri;
    private External_url external_url;

    public External_url getExternal_url() {
        return external_url;
    }

    public void setExternal_url(External_url external_url) {
        this.external_url = external_url;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
