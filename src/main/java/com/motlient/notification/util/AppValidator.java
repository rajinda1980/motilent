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
     * Validates the argument based on the following criteria:
     * 1. The argument must contain only one value.
     * 2. The argument must be a valid URL.
     *
     * @param args - input URL
     * @throws AppValidationException if validation fails
     */
    public void validateJsonFilePath(String[] args) throws AppValidationException {
        if (null == args || args.length == 0 || args[0].equals("")) {
            throw new AppValidationException(AppConstants.MESSAGE_FILE_PATH_NOT_PROVIDED);
        } else if (args.length > 1) {
            throw new AppValidationException(AppConstants.MESSAGE_TOO_MANY_ARGUMENTS_ARE_PROVIDED);
        }
    }

    private boolean validateUrlForSpecialCharacters(String url) {
        return validateUrlWithRegExpression(AppConstants.COMMON_URL_CHARACTERS, url);
    }

    private boolean validateUrlForValidUrlPatten(String url) {
        return validateUrlWithRegExpression(AppConstants.VALID_URL_PATTERN, url);
    }

    private boolean validateUrlWithRegExpression(String exp, String url) {
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }
}
