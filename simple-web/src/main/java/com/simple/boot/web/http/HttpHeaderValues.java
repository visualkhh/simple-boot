package com.simple.boot.web.http;

public enum HttpHeaderValues {


    APPLICATION_JSON("application/json"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_XML("application/xml"),
    ATTACHMENT("attachment"),
    BASE64("base64"),
    BINARY("binary"),
    BOUNDARY("boundary"),
    BYTES("bytes"),
    CHARSET("charset"),
    CHUNKED("chunked"),
    CLOSE("close"),
    COMPRESS("compress"),
    _100CONTINUE("100-continue"),
    DEFLATE("deflate"),
    X_DEFLATE("x-deflate"),
    FILE("file"),
    FILENAME("filename"),
    FORM_DATA("form-data"),
    GZIP("gzip"),
    GZIP_DEFLATE("gzip,deflate"),
    X_GZIP("x-gzip"),
    IDENTITY("identity"),
    KEEP_ALIVE("keep-alive"),
    MAX_AGE("max-age"),
    MAX_STALE("max-stale"),
    MIN_FRESH("min-fresh"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    MULTIPART_MIXED("multipart/mixed"),
    MUST_REVALIDATE("must-revalidate"),
    NAME("name"),
    NO_CACHE("no-cache"),
    NO_STORE("no-store"),
    NO_TRANSFORM("no-transform"),
    NONE("none"),
    _0("0"),
    ONLY_IF_CACHED("only-if-cached"),
    PRIVATE("private"),
    PROXY_REVALIDATE("proxy-revalidate"),
    PUBLIC("public"),
    QUOTED_PRINTABLE("quoted-printable"),
    S_MAXAGE("s-maxage"),
    TEXT_CSS("text/css"),
    TEXT_HTML("text/html"),
    TEXT_EVENT_STREAM("text/event-stream"),
    TEXT_PLAIN("text/plain"),
    TRAILERS("trailers"),
    UPGRADE("upgrade"),
    WEBSOCKET("websocket"),
    XMLHTTPREQUEST("XmlHttpRequest");


    private String value;

    HttpHeaderValues(String value) {
        this.value = value;
    }
    public String value() {
        return this.value;
    }
}
