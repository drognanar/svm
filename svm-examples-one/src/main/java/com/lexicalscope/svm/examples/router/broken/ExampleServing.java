package com.lexicalscope.svm.examples.router.broken;

public class ExampleServing {
    public static void main(final String[] args) {
        main(0);
    }

    private static void main(final int a) {
        new ExampleServing().serve(a);
    }

    private void serve(final int a) {
        final Router router = new Router();
        String result;
        if (a < 0) {
            result = "/";
        } else if (a < 15) {
            result = "/applications";
        } else if (a < 55) {
            result = "/applications/id/12";
        } else {
            result = "/applications/name";
        }
        final String requestUrl = result;
        if (requestUrl.equals("/applications/id/12") ||
                requestUrl.equals("/applications") ||
                requestUrl.equals("/")) {
            router.matchRoute(requestUrl);
        }
    }
}
