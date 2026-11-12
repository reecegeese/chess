package client.model;

import com.google.gson.Gson;

public record LogInRequest(String username, String password, String email) {
    public String toString() {
        return new Gson().toJson(this);
    }
}