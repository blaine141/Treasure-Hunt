package com.example.treasurehunt;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

interface Consumer<T> {
    void accept(T t);
}

public class Cache implements Serializable {
    public static final String BASE_URL = "https://www.opencaching.us/okapi/services/";
    public static final String CONSUMER_KEY = "7Q7WcQ9QECgUYgwX5AGH";

    public String name;
    public String code;
    public double lat;
    public double lng;
    public String type;
    public String status;
    public double distance;              // Distance in meters
    public double bearing;
    public double terrain;               // Terrain rating from 1-5
    public double difficulty;            // Difficulty rating from 1-5
    public Double rating;                // Rating from 1-5. Can be Null
    public String shortDescription;     // Short description
    public String description;          // Long description with HTML
    public ArrayList<CacheImage> images = new ArrayList<>();
    public static LocationListener listener;

    public Cache(JSONObject cacheInfo) {
        updateFields(cacheInfo);
    }

    public void updateFields(JSONObject cacheInfo) {
        try {
            this.code = cacheInfo.getString("code");
            this.name = cacheInfo.getString("name");
            this.type = cacheInfo.getString("type");
            this.status = cacheInfo.getString("status");
            String location = cacheInfo.getString("location");
            String latStr = location.split("\\|")[0];
            String lngStr = location.split("\\|")[1];
            this.lat = Double.parseDouble(latStr);
            this.lng = Double.parseDouble(lngStr);
            this.distance = Double.parseDouble(cacheInfo.getString("distance"));
            this.bearing = Double.parseDouble(cacheInfo.getString("bearing"));
            this.terrain = Double.parseDouble(cacheInfo.getString("terrain"));
            this.difficulty = Double.parseDouble(cacheInfo.getString("difficulty"));
            this.rating = parseDouble(cacheInfo.getString("rating"));
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

    public void refresh(final Context context, final Runnable onReload) {
        getLocation(context, 5, new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                refresh(context, onReload, location.getLatitude(), location.getLongitude());
            }
        });
    }

    public void refresh(final Context context, final Runnable onReload, double lat, double lon) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BASE_URL + "caches/geocache?consumer_key=" + CONSUMER_KEY + "&cache_code="+ code +"&my_location=" + lat + "|" + lon + "&fields=code|name|location|type|status|distance|bearing|difficulty|terrain|rating|short_description|description|last_found|images";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = new JSONObject(response);
                            updateFields(obj);
                            onReload.run();

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
    }

    public static Double parseDouble(String s) {
        if (s.equals("null"))
            return null;
        return Double.parseDouble(s);
    }

    public static void getCaches(final Context context, final int numCaches, final Consumer<ArrayList<Cache>> callback) {
        getLocation(context, 500, new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                getCaches(context, location.getLatitude(), location.getLongitude(), numCaches, callback);
            }
        });
    }

    public static void getCaches(final Context context, final double lat, final double lon, final int numCaches, final Consumer<ArrayList<Cache>> callback) {
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
                                                callback.accept(caches);
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

    public static void getLocation(Context context, final float accuracy, final Consumer<Location> consumer) {
        final LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location.getAccuracy() < accuracy) {
                    locationManager.removeUpdates(this);
                    consumer.accept(location);
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) { }
            @Override
            public void onProviderDisabled(@NonNull String provider) { }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        } catch (SecurityException ex) {
            System.out.println("Location permission rejected.");
        }
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

    public static class CacheImage implements Serializable{
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
