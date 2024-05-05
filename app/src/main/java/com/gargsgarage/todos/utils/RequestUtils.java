package com.gargsgarage.todos.utils;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RequestUtils {
    // parses through the input stream to create key-->value pairs for each of the
    // fields of a task
    public static Map<String, String> getFormParams(InputStream inputStream) {

        // first get the request body, which is the user's input
        // still need to split and decode the body
        Scanner sc = new Scanner(inputStream);
        String requestBody;

        if (sc.hasNext()) {
            requestBody = sc.useDelimiter("\\A").next();
        } else {
            requestBody = "";
        }
        sc.close();

        // split and decode the pairs
        // (ex current requestBody: id=0&taskTitle=say+hello&status=IN_PROGRESS)
        String[] pairs = requestBody.toString().split("&");
        Map<String, String> params = new HashMap<>();
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            }
        }
        // after splitting and decoding, params Map looks like this:
        // params: id-->0, taskTitle-->say hello, status--> IN_PROGRESS

        return params;

    }
}
