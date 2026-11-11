package client.model;

import com.google.gson.Gson;

public record RegisterResult(Type type, String message) {
    public enum Type {
        ARRIVAL,
        NOISE,
        DEPARTURE
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}