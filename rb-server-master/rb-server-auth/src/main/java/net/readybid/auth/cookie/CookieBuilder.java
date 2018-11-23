package net.readybid.auth.cookie;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Created by DejanK on 11/30/2016.
 *
 */
public class CookieBuilder {

    private final String name;
    private final String value;

    private String domain;
    private String path;
    private boolean httpOnly;
    private boolean secure;
    private long maxAge;
    private long expires;

    private static final String FIELD_FORMAT = "%s=%s; ";
    private static final String BOOLEAN_FIELD_FORMAT = "%s; ";

    public CookieBuilder(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public CookieBuilder setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public CookieBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder setHttpOnly() {
        this.httpOnly = true;
        return this;
    }

    public CookieBuilder setSecure() {
        this.secure = true;
        return this;
    }

    public CookieBuilder setMaxAge(long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public String build() {
        String cookie = String.format(FIELD_FORMAT, name, value);

        if (domain != null && !domain.isEmpty()) cookie += String.format(FIELD_FORMAT, "Domain", domain);
        if (path != null && !path.isEmpty()) cookie += String.format(FIELD_FORMAT, "Path", path);
        if (httpOnly) cookie += String.format(BOOLEAN_FIELD_FORMAT, "HttpOnly");
        if (secure) cookie += String.format(BOOLEAN_FIELD_FORMAT, "Secure");

        if (expires > 0) {
            cookie += String.format(FIELD_FORMAT, "Expires", formatExpires(expires));
        }

        if (maxAge > 0) {
            cookie += String.format(FIELD_FORMAT, "MaxAge", maxAge);
            if (expires == 0) {
                cookie += String.format(FIELD_FORMAT, "Expires", toExpires(maxAge));
            }
        }

        return cookie;
    }

    private String toExpires(long maxAge) {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now(ZoneOffset.UTC)
                .plus(Duration.ofSeconds(maxAge)));
    }

    private String formatExpires(long expires) {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.ofInstant(Instant.ofEpochMilli(expires), ZoneOffset.UTC));
    }

    public CookieBuilder setExpires(long expiresTimestamp) {
        this.expires = expiresTimestamp;
        return this;
    }
}
