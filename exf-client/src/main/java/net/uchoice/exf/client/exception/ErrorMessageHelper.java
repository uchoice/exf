package net.uchoice.exf.client.exception;

class ErrorMessageHelper {

    private static final ErrorMessage message = ErrorMessage.code("C-EXF-01-01-000");

    static ErrorMessage defaultOne() {
        return message;
    }

}
