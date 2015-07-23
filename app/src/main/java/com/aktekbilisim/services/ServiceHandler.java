package com.aktekbilisim.services;

/**
 * Created by berkan.kahyaoglu on 31.05.2015.
 */

import android.content.Context;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    InputStream is = null;
    private Context ctx;

    public ServiceHandler(Context ctx) {
        this.ctx = ctx;

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    public String makeServiceCall(String url, int method, String params) {

        try {

            // http client
            DefaultHttpClient httpClient = null;
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            httpClient = new DefaultHttpClient();
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                // adding post params
                if (params != null) {
                    // Log.v("params", params);
                    httpPost.setEntity(new StringEntity(params, HTTP.UTF_8));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {

                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            Log.v("responseCode", String.valueOf(responseCode));
            if (responseCode == 500)
                return "500";



            httpEntity = httpResponse.getEntity();

            is = httpEntity.getContent();

            response = convertInputStreamToString(is);

            // response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log.v("test", response);

        return response;

    }


    public static String convertInputStreamToString(InputStream is) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader tmp = new BufferedReader(new InputStreamReader(is),
                    65728);
            String line = null;

            while ((line = tmp.readLine()) != null) {

                sb.append(line);
            }

            // close stream
            is.close();

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}