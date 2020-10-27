package com.example.treasurehunt;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

interface CacheListener {
    public void cachesLoaded(ArrayList<Cache> caches);
}

public class Cache {
    public static final String BASE_URL = "https://www.opencaching.us/okapi/services/";
    public static final String CONSUMER_KEY = "7Q7WcQ9QECgUYgwX5AGH";

    public String name;
    public String code;
    public float lat;
    public float lng;
    public String type;
    public String status;
    public float distance;              // Distance in meters
    public float bearing;
    public float terrain;               // Terrain rating from 1-5
    public float difficulty;            // Difficulty rating from 1-5
    public Float rating;                // Rating from 1-5. Can be Null
    public String shortDescription;     // Short description
    public String description;          // Long description with HTML
    public ArrayList<CacheImage> images = new ArrayList<>();

    public Cache(JSONObject cacheInfo) {
        try {
            this.code = cacheInfo.getString("code");
            this.name = cacheInfo.getString("name");
            this.type = cacheInfo.getString("type");
            this.status = cacheInfo.getString("status");
            String location = cacheInfo.getString("location");
            String latStr = location.split("\\|")[0];
            String lngStr = location.split("\\|")[1];
            this.lat = Float.parseFloat(latStr);
            this.lng = Float.parseFloat(lngStr);
            this.distance = Float.parseFloat(cacheInfo.getString("distance"));
            this.bearing = Float.parseFloat(cacheInfo.getString("bearing"));
            this.terrain = Float.parseFloat(cacheInfo.getString("terrain"));
            this.difficulty = Float.parseFloat(cacheInfo.getString("difficulty"));
            this.rating = parseFloat(cacheInfo.getString("rating"));
            this.shortDescription = cacheInfo.getString("short_description");
            this.description = cacheInfo.getString("description");
            JSONArray imageArray = cacheInfo.getJSONArray("images");
            for (int i = 0; i < imageArray.length(); i++) {
                this.images.add(new CacheImage(imageArray.getJSONObject(i)));
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static Float parseFloat(String s) {
        if (s.equals("null"))
            return null;
        return Float.parseFloat(s);
    }

    public static void getCaches(final Context context, final float lat, final float lon, final int numCaches, final CacheListener callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BASE_URL + "caches/search/nearest?consumer_key=" + CONSUMER_KEY + "&center=" + lat + "|" + lon + "&limit=" + numCaches + "&type=Traditional";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            RequestQueue queue = Volley.newRequestQueue(context);
                            JSONObject obj = new JSONObject(response);
                            JSONArray results = obj.getJSONArray("results");
                            String codes = jsonArrayToString(results, "|");
                            String url = BASE_URL + "caches/geocaches?consumer_key=" + CONSUMER_KEY + "&cache_codes="+codes+"&my_location=" + lat + "|" + lon + "&fields=code|name|location|type|status|distance|bearing|difficulty|terrain|rating|short_description|description|last_found|images";

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                ArrayList<Cache> caches = new ArrayList<>();
                                                JSONObject obj = new JSONObject(response);
                                                for (Iterator<String> i = obj.keys(); i.hasNext(); ) {
                                                    String key = i.next();
                                                    Cache cache = new Cache(obj.getJSONObject(key));
                                                    caches.add(cache);
                                                }
                                                callback.cachesLoaded(caches);
                                            } catch (JSONException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    httpError(error);
                                }
                            });
                            queue.add(stringRequest);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                httpError(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void httpError(VolleyError error) {
        error.printStackTrace();
    }

    public static String jsonArrayToString(JSONArray array, String separator) {
        String result = "";
        for (int i=0; i < array.length(); i++) {
            if (i != 0)
                result += separator;
            try {
                result += array.getString(i);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public class CacheImage {
        public String uuid;
        public String url;
        public String thumbUrl;
        public String caption;
        public boolean isSpoiler;

        public CacheImage(JSONObject cacheInfo) {
            try {
                this.uuid = cacheInfo.getString("uuid");
                this.url = cacheInfo.getString("url");
                this.thumbUrl = cacheInfo.getString("thumb_url");
                this.caption = cacheInfo.getString("caption");
                this.isSpoiler = cacheInfo.getBoolean("is_spoiler");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
