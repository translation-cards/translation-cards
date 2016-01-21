package org.mercycorps.translationcards;

/**
 * Exception for when there is an error importing a .txc file.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class ImportException extends Exception {

    public enum ImportProblem {
        FILE_NOT_FOUND,
        READ_ERROR,
        NO_INDEX_FILE,
        INVALID_INDEX_FILE
    }

    private final ImportProblem problem;

    public ImportException(ImportProblem problem, Throwable cause) {
        super(cause);
        this.problem = problem;
    }

    public ImportProblem getProblem() {
        return problem;
    }
}
