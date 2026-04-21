package com.example.bracesaligner.core.network;

/**
 * Minimal JWT payload reader (no signature verification).
 * Used when backend returns only access_token and user id lives in "sub".
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\f\u0010\u0006\u001a\u00020\u0004*\u00020\u0004H\u0002\u00a8\u0006\u0007"}, d2 = {"Lcom/example/bracesaligner/core/network/JwtPayloadParser;", "", "()V", "parseSub", "", "jwt", "padPayloadForBase64Url", "app_debug"})
public final class JwtPayloadParser {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.bracesaligner.core.network.JwtPayloadParser INSTANCE = null;
    
    private JwtPayloadParser() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String parseSub(@org.jetbrains.annotations.NotNull()
    java.lang.String jwt) {
        return null;
    }
    
    private final java.lang.String padPayloadForBase64Url(java.lang.String $this$padPayloadForBase64Url) {
        return null;
    }
}