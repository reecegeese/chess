package client.model;

public record RegisterResult(String username, String authToken) {
    public RegisterResult {
        authToken = clean(authToken);
        String[] parts = authToken.split(", ");
        authToken = parts[1];
    }

    private static String clean(String s) {
        return s == null ? null : s.trim().replaceAll("[\":]", "");
    }
}