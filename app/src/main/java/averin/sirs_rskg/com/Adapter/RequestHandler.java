package averin.sirs_rskg.com.Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestHandler {

//    public static final String APIdev = "http://192.168.38.32:9000/api/v1/";
   public static final String APIdev = "https://api-dev.averin.co.id/api/v1/";
//   public static final String APIdev = "https://demo-pluit.sirs.co.id/api/v1/";

    public String reqToken(String postUrl, HashMap<String, String> param) {
        Response response;
        StringBuilder sb = new StringBuilder();
        MediaType mt = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = new JSONObject(param);
        RequestBody body = RequestBody.create(mt, parameter.toString());
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(postUrl)
                    .method("GET", null)
                    .post(body)
                    .addHeader("token", "")
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Cookie", "PHPSESSID=oqd1qsi4p3c61acr911qu4reat")
                    .build();
            sb = new StringBuilder();
            response = client.newCall(request).execute();
            JSONObject obj1 = new JSONObject(response.body().string());
//            return response.body().string();
            sb.append(obj1);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String postRequest(String postUrl, String method, String var_header, String val_header, HashMap<String, String> param) {
        Response response;
        StringBuilder sb = new StringBuilder();
        MediaType mt = MediaType.parse("text/plain");
        JSONObject parameter = new JSONObject(param);
        RequestBody body = RequestBody.create(mt, parameter.toString());
//        RequestBody body = RequestBody.create(mt, "{\r\n    \"kode_klinik\":\"\",\r\n    \"url_site\":\"\"\r\n}");

        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(postUrl)
                    .method(method, body)
//                    .post(body)
                    .addHeader(var_header, val_header)
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Cookie", "PHPSESSID=oqd1qsi4p3c61acr911qu4reat")
                    .build();
            sb = new StringBuilder();
            response = client.newCall(request).execute();
            JSONObject obj1 = new JSONObject(response.body().string());
//            return response.body().string();
            sb.append(obj1);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String requestData(String postUrl, String method, String tipe, String var_header, String val_header, String var_header2, String val_header2, HashMap<String, String> param) {
        Response response;
        StringBuilder sb = new StringBuilder();
        StringBuilder sbkosong = new StringBuilder();
        MediaType mt = MediaType.parse(tipe);
        JSONObject parameter = new JSONObject(param);
        RequestBody body = RequestBody.create(mt, parameter.toString());
//        RequestBody body = RequestBody.create(mt, "{\r\n    \"kode_klinik\":\"\",\r\n    \"url_site\":\"\"\r\n}");

        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                                                                                                                                                                                                                                              .url(postUrl)
                    .method(method, body)
//                    .post(body)
                    .addHeader(var_header, val_header)
                    .addHeader(var_header2, val_header2)
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Cookie", "PHPSESSID=oqd1qsi4p3c61acr911qu4reat")
                    .build();
            sb = new StringBuilder();
            response = client.newCall(request).execute();
            JSONObject obj1 = new JSONObject(response.body().string());
//            return response.body().string();
            sb.append(obj1);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(sb==null) {
            String kosong = "200";
            sb.append(kosong);
            return sbkosong.toString();
        }else{
            return sb.toString();
        }
    }

    public String requestSpinner(String postUrl, String method, String tipe, String var_header, String val_header, String var_header2, String val_header2, HashMap<String, String> param) {
        Response response;
        StringBuilder sb = new StringBuilder();
        StringBuilder sbkosong = new StringBuilder();
        MediaType mt = MediaType.parse(tipe);
        JSONObject parameter = new JSONObject(param);
        RequestBody body = RequestBody.create(mt, parameter.toString());
//        RequestBody body = RequestBody.create(mt, "{\r\n    \"kode_klinik\":\"\",\r\n    \"url_site\":\"\"\r\n}");

        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(postUrl)
                    .method(method, body)
//                    .post(body)
                    .addHeader(var_header, val_header)
                    .addHeader(var_header2, val_header2)
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Cookie", "PHPSESSID=oqd1qsi4p3c61acr911qu4reat")
                    .build();
            sb = new StringBuilder();
            response = client.newCall(request).execute();
//            JSONObject obj1 = new JSONObject(response.body().string());
            JSONArray jr = new JSONArray(response.body().string());

            sb.append(jr);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    //this method is converting keyvalue pairs data into a query string as needed to send to the server
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
