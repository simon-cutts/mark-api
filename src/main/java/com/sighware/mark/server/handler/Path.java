package com.sighware.mark.server.handler;

public class Path {

    private Path() {
    }

    public static String getRegistrationNumber(String path) {
        int s = path.lastIndexOf("/");
        int e = path.length();
        return path.substring(s + 1, e);
    }
}
