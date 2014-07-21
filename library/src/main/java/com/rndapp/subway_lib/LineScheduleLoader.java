package com.rndapp.subway_lib;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by kevin on 7/16/14.
 */
public class LineScheduleLoader {

    public interface OnLineScheduleLoadedListener {
        public void onLineScheduleLoaded(JSONObject jsonObject);
        public void onFailure(VolleyError volleyError);
    }

    public static void load(final OnLineScheduleLoadedListener callback, final String url) {

        // TODO SubwayApplication.getRequestQueue().cancelAll(line);
        // to prevent against spamming

        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        callback.onLineScheduleLoaded(jsonObject);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onFailure(volleyError);
                    }
                }
        );
        request.setTag(url);
        SubwayApplication.getRequestQueue().add(request);
    }

}
