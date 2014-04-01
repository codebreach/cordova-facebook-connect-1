//
//  FacebookConnect.java
//
// Created by Olivier Louvignes on 2012-07-20.
//
// Copyright 2012 Olivier Louvignes. All rights reserved.
// MIT Licensed

package org.apache.cordova.plugins.FacebookConnect;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.drinklynk.app.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class FacebookConnect extends CordovaPlugin {
    private final String CLASS = "FacebookConnect";

    @Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Boolean success = false;

        try {
            if (action.equals("init")) {
                success = this.init(args, callbackContext);
            } else if (action.equals("login")) {
                success = this.login(args, callbackContext);
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
        
        this.cordova.setActivityResultCallback(this);
        
        Activity cordovaActivity = cordova.getActivity();
        Parse.initialize(
        		cordovaActivity,
        		cordovaActivity.getString(R.string.parse_app_id), 
        		cordovaActivity.getString(R.string.parse_key));
        ParseFacebookUtils.initialize(
        		cordovaActivity.getString(R.string.fb_app_id));

        ParseUser parseUser = ParseUser.getCurrentUser();
        
        JSONObject result = new JSONObject();
        result.put("state", "unknown");
        
        if (parseUser != null && parseUser.getSessionToken() != null) {
        	Log.d(CLASS, "existing user!: " + parseUser.getObjectId());
        	result.put("state", "connected");
        	result.put("session_token", parseUser.getSessionToken());
        	result.put("object_id", parseUser.getObjectId());
        }
        
        context.success(result);
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
        Log.d(CLASS, "login(): " + args.toString());
        
        List<String> permissions = new ArrayList<>();
        
        for (int i = 0; i < args.length(); i++) {
        	permissions.add(args.getString(i));
        }
        
        ParseFacebookUtils.logIn(permissions, cordova.getActivity(), new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException ex) {
				JSONObject result = new JSONObject();
				try {
					if (ex != null) {
						result.put("state", "error");
						result.put("message", ex.getMessage());
						ex.printStackTrace();
						context.error(result);
						return;
					}
					
					if (user == null) {
						Log.v(CLASS, "Uh oh. The user cancelled the Facebook login.");
						result.put("state", "failed");
						context.error(result);
					} else {
						if (user.isNew()) {
							user.saveInBackground();
							Log.v(CLASS, "User signed up and logged in through Facebook!");
						} else {
							Log.v(CLASS, "User logged in through Facebook!");
						}
						result.put("state", "connected");
						result.put("session_token", user.getSessionToken());
						result.put("object_id", user.getObjectId());
						context.success(result);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					context.error("JSON EXCEPTION");
				}
			}
		});
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
        Log.d(CLASS, "logout():");
        ParseUser.logOut();
        
        context.success();
        return true;
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
