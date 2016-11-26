package net.lisd.lunchseat_server;

/**
 * The book of errors.
 *
 * Created by joshua on 8/19/16, in project Cedar.
 */
enum ErrorsAndTheirMeanings {
    // Chapter 1: your basic username issues.
    USERNAME_FORMAT_ERROR(1),
    USERNAME_NEVER_JOINED(2),
    USERNAME_NOT_ONLINE(3),

    // Chapter 2: authentication related stuff.
    CODE_NOT_ACCEPTABLE(4),

    // End of book 1: a happy ending.
    SUCCESS(200),

    // Here be that stuff under your bed.
    LOLWAT(255);
    private final int id;

    ErrorsAndTheirMeanings(int id) {
        this.id = id;
    }

    public int getId() { return id; }
}
