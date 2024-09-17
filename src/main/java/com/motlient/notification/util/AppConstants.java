package com.motlient.notification.util;

public final class AppConstants {
    private AppConstants(){ }

    //public static final String VALID_URL_PATTERN = "^(https?|ftp)://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,6}(/[\\w\\-\\.\\?\\=\\&]*)?$";
    public static final String VALID_URL_PATTERN = "^https://webhook\\.site/[0-9a-fA-F\\-]+$";

    public static final String MESSAGE_FILE_PATH_NOT_PROVIDED = "File path is not provided";
    public static final String MESSAGE_TOO_MANY_ARGUMENTS_ARE_PROVIDED = "Too many arguments provided. Multiple file paths are not allowed";
    public static final String MESSAGE_INVALID_NOTIFICATION_URL = "An invalid notification URL is provided";

    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
}
