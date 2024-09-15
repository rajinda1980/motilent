package com.motlient.notification.util;

public final class AppConstants {
    private AppConstants(){ }

    public static final String COMMON_URL_CHARACTERS = "[^a-zA-Z0-9:/?&@=._~-]";
    public static final String VALID_URL_PATTERN = "^(https?|ftp)://[a-zA-Z0-9.-]+(:\\\\d+)?(/[a-zA-Z0-9._~:/?#\\\\[\\\\]@!$&'()*+,;=%-]*)?$";

    public static final String MESSAGE_FILE_PATH_NOT_PROVIDED = "File path is not provided";
    public static final String MESSAGE_TOO_MANY_ARGUMENTS_ARE_PROVIDED = "Too many arguments provided. Multiple file paths are not allowed";
    public static final String MESSAGE_INVALID_FILE_PATH = "Invalid file path";
    public static final String MESSAGE_FILE_PATH_LENGTH_EXCEEDED = "File path length cannot exceed 200 characters";
    public static final String MESSAGE_INVALID_URL_FORMAT = "Invalid URL format";
}
