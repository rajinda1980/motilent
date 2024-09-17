package com.motlient.notification.util;

import com.motlient.notification.exceptions.AppValidationException;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation class
 */
public class AppValidator {

    public static final Logger LOGGER = Logger.getLogger(AppValidator.class.getName());

    /**
     * Validates the command-line arguments to ensure that a valid JSON file path is provided
     * <p>This method checks the following conditions:
     * <ul>
     *   <li>Throws an {@link AppValidationException} if no arguments are provided or the first argument is empty.</li>
     *   <li>Throws an {@link AppValidationException} if more than one argument is passed.</li>
     * </ul>
     *
     * @param args the array of command-line arguments
     * @throws AppValidationException if the arguments are invalid (missing, empty, or too many)
     */
    public void validateJsonFilePath(String[] args) throws AppValidationException {
        if (null == args || args.length == 0 || args[0].trim().equals("")) {
            throw new AppValidationException(AppConstants.MESSAGE_FILE_PATH_NOT_PROVIDED);
        } else if (args.length > 1) {
            throw new AppValidationException(AppConstants.MESSAGE_TOO_MANY_ARGUMENTS_ARE_PROVIDED);
        }
    }

    /**
     * Validates if the given URL follows a valid URL pattern
     *
     * @param url - input URL
     * @return {@code true} if the URL matches the valid URL pattern defined in {@link AppConstants#VALID_URL_PATTERN}, {@code false} otherwise
     */
    public boolean validateUrlForValidUrlPatten(String url) {
        if (null == url || url.trim().equals("")) return false;

        Pattern pattern = Pattern.compile(AppConstants.VALID_URL_PATTERN);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
