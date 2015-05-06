package com.lexicalscope.svm.examples.router.broken;

public class ExampleServing {
    public static void main(String[] args) {
        main(0);
    }

    private static void main(int a) {
        new ExampleServing().serve(a);
    }

    private void serve(int a) {
        Router router = new Router();
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
        String requestUrl = result;
        if (requestUrl.equals("/applications/id/12") ||
                requestUrl.equals("/applications") ||
                requestUrl.equals("/")) {
            router.matchRoute(requestUrl);
        }
    }
}
