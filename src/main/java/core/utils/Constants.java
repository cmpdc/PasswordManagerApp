package core.utils;

public class Constants {
    public static final String GITHUB_PAGE_URL = "https://cmpdc.me/";

    public static final String APP_NAME = "URIFY PASSWORD MANAGER";
    public static final int[] APP_LOGIN_SIZE = {520, 520};
    public static final int[] APP_MAIN_SIZE = {920, 620};

    public static final String UPPERCASES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWERCASES = UPPERCASES.toLowerCase();
    public static final String DIGITS = "01233456789";
    public static final String PUNCS = "!@#$%&*()_[]|,./?";

    public static final String ADMIN_MASTER_KEY = "6CJFM9QYX8M815FWB9526NM3YUCFTO3YMG71K96XLLLLL4"; // randomStrings(46)
    public static final String PASSWORD_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA512";
    public static final String PASSWORD_KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static final int PASSWORD_KEY_ITERATIONS = 10000;
    public static final int PASSWORD_KEY_LENGTH = 256;

    public static final int PASSWORD_MINIMUM_LENGTH = 10;
    public static final int PASSWORD_MAXIMUM_LENGTH = 30;

    public static final int USER_NAME_MIN_LENGTH = 5;
}
