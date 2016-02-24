package org.mercycorps.translationcards.porting;

/**
 * Exception for when there is an error exporting a .txc file.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class ExportException extends Exception {

    public enum ExportProblem {
        TARGET_FILE_NOT_FOUND,
        WRITE_ERROR,
        TOO_MANY_DUPLICATE_FILENAMES
    }

    private final ExportProblem problem;

    public ExportException(ExportProblem problem, Throwable cause) {
        super(cause);
        this.problem = problem;
    }

    public ExportProblem getProblem() {
        return problem;
    }
}
