//
//  FacebookConnect.java
//
// Created by Olivier Louvignes on 2012-07-20.
//
// Copyright 2012 Olivier Louvignes. All rights reserved.
// MIT Licensed

package org.apache.cordova.plugins.FacebookConnect;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;

public class FacebookConnect extends CordovaPlugin {

    private final String CLASS = "FacebookConnect";

    private Session session;
    //private final Handler handler = new Handler();

    @Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Boolean success = false;

        try {
            if (action.equals("init")) {
                success = this.init(args, callbackContext);
            } else if (action.equals("login")) {
                success = this.login(args, callbackContext);
            } else if (action.equals("requestWithGraphPath")) {
                success = this.requestWithGraphPath(args, callbackContext);
            } else if (action.equals("dialog")) {
                success = this.dialog(args, callbackContext);
            } else if (action.equals("logout")) {
                success = this.logout(args, callbackContext);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION));
        } catch (IOException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION));
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
        }

        return success;
    }

    /**
     * Cordova interface to initialize the appId
     *
     * @param args JSONArray
     * @param context CallbackContext
     * @return PluginResult
     * @throws JSONException
     */
    public Boolean init(final JSONArray args, CallbackContext context) throws JSONException {
        Log.d(CLASS, "init()");
        JSONObject params = args.getJSONObject(0);

        JSONObject result = new JSONObject();

        session = new Session(cordova.getActivity());
	//
	//        session.openForRead(new OpenRequest(cordova.getActivity()).setPermissions("about_me").set;
        // Check for any stored session update Facebook session information
	//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
	//        String accessToken = prefs.getString("access_token", null);
	//        Long accessExpires = prefs.getLong("access_expires", 0);
	//        if (accessToken != null) {
	//            facebook.setAccessToken(accessToken);
	//        }
	//        if (accessExpires != 0) {
	//            facebook.setAccessExpires(accessExpires);
	//        }
	//
	//        result.put("accessToken", accessToken);
	//        result.put("expirationDate", accessExpires);

        context.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));


        return true;

    }

    /**
     * Cordova interface to perform a login
     *
     * @param args JSONArray
     * @param context CallbackContext
     * @return PluginResult
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IOException
     */
    public Boolean login(final JSONArray args, final CallbackContext context) throws JSONException, IOException {
        Log.d(CLASS, "login() :" + args.toString());
        final JSONObject params = args.getJSONObject(0);
        final PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);

        session.openForRead(new OpenRequest(cordova.getActivity()).setPermissions("about_me").setCallback(new StatusCallback() {
		@Override
		    public void call(Session session, SessionState sessionState, Exception ex) {
		    context.success(session.getAccessToken());
		}
	    }));
	//        final Facebook facebook = this.getFacebook();
	//
	//        // Check for any stored session update Facebook session information
	//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
	//        final String accessToken = prefs.getString("access_token", null);
	//        final Long accessExpires = prefs.getLong("access_expires", 0);
	//
	//        final FacebookConnect me = this;
	//
	//        cordova.getThreadPool().execute(new Runnable() {
	//            public void run() {
	//
	//                if (accessToken != null) {
	//                    facebook.setAccessToken(accessToken);
	//                }
	//                if (accessExpires != 0) {
	//                    facebook.setAccessExpires(accessExpires);
	//                }
	//
	//                if (!me.getFacebook().isSessionValid()) {
	//                    JSONArray permissionsArray = null;
	//                    String[] permissions = new String[0];
	//
	//                    try {
	//                        permissionsArray = (JSONArray) params.get("permissions");
	//                        permissions = new String[permissionsArray.length()];
	//            for (int i = 0; i < permissionsArray.length(); i++) {
	//                permissions[i] = permissionsArray.getString(i);
	//            }
	//                    } catch (JSONException e) {
	//                        context.error("JSON exception: " + e.getMessage());
	//                        e.printStackTrace();
	//                    }
	//
	//                    final String[] finalPermissions = permissions;
	//
	//                    me.authorizeDialogListener = new AuthorizeDialogListener(me, context);
	//                    me.cordova.setActivityResultCallback(me);
	//            Runnable runnable = new Runnable() {
	//                public void run() {
	//                            me.getFacebook().authorize(me.cordova.getActivity(), finalPermissions, me.authorizeDialogListener);
	//                }
	//            };
	//            pluginResult.setKeepCallback(true);
	//                    me.cordova.getActivity().runOnUiThread(runnable);
	//        } else {
	//                    JSONObject result = null;
	//                    try {
	//                        result = new JSONObject(facebook.request("/me"));
	//            result.put("accessToken", accessToken);
	//            result.put("expirationDate", accessExpires);
	//            Log.d(CLASS, "login::result " + result.toString());
	//            context.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
	//                    } catch (JSONException e) {
	//                        context.error("JSON exception: " + e.getMessage());
	//                        e.printStackTrace();
	//                    } catch (IOException e) {
	//                        context.error("IO exception: " + e.getMessage());
	//                        e.printStackTrace();
	//                    }
	//        }
	//        }
	//        });

        return true;
    }

    /**
     * Cordova interface to perfom a graph request
     *
     * @param args JSONArray
     * @param context CallbackContext
     * @return Boolean
     * @throws JSONException
     */
    public Boolean requestWithGraphPath(final JSONArray args, final CallbackContext context) throws JSONException {
        Log.d(CLASS, "requestWithGraphPath() :" + args.toString());
        JSONObject params = args.getJSONObject(0);

        

        return true;
    }

    /**
     * Cordova interface to display a dialog
     *
     * @param args JSONArray
     * @param context CallbackContext
     * @return Boolean
     * @throws JSONException
     * @throws FileNotFoundException
     * @throws MalformedURLException
     * @throws IOException
     */
    public Boolean dialog(final JSONArray args, final CallbackContext context) throws JSONException, IOException {
	//        Log.d(CLASS, "dialog() :" + args.toString());
	//        JSONObject params = args.getJSONObject(0);
	//        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
	//
	//        final String method = params.has("method") ? params.getString("method") : "feed";
	//        JSONObject optionsObject = (JSONObject) params.get("params");
	//        final Bundle options = new Bundle();
	//        Iterator<?> keys = optionsObject.keys();
	//        while (keys.hasNext()) {
	//            String key = (String) keys.next();
	//            options.putString(key, optionsObject.getString(key));
	//            //if(optionsObject.get(key) instanceof JSONObject)
	//        }
	//
	//        final FacebookConnect me = this;
	//        Runnable runnable = new Runnable() {
	//            public void run() {
	//                me.getFacebook().dialog(me.cordova.getActivity(), method, options, new RegularDialogListener(me, context));
	//            }
	//        };
	//        pluginResult.setKeepCallback(true);
	//        this.cordova.getActivity().runOnUiThread(runnable);

        return true;
    }

    /**
     * Cordova interface to logout from Facebook
     *
     * @param args JSONArray
     * @param context CallbackContext
     * @return Boolean
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IOException
     */
    public Boolean logout(final JSONArray args, CallbackContext context) throws JSONException, MalformedURLException, IOException {
	//        Log.d(CLASS, "logout() :" + args.toString());
	//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
	//        prefs.edit().remove("access_expires").commit();
	//        prefs.edit().remove("access_token").commit();
	//        this.getFacebook().logout(this.cordova.getActivity());
	//        context.sendPluginResult(new PluginResult(PluginResult.Status.OK));
        return true;
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.session.onActivityResult(cordova.getActivity(), requestCode, resultCode, data);
        //this.webView.sendJavascript("window.alert('test')"); //@todo not working :(
    }
}
