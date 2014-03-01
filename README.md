# Cordova Facebook Connect Plugin #
updated by [Madhav Bhagat](http://twitter.com/codebreach)
based on work by [Olivier Louvignes](http://olouv.com)

## DESCRIPTION ##

* This plugin provides a simple way to use Facebook Graph API in Cordova.

* This plugin is built for Cordova >= v3.4.0 with ARC. Both iOS & Android are supported with the same javascript interface.

* For iOS, this plugin relies on the [Facebook iOS SDK](https://github.com/facebook/facebook-ios-sdk) that is bundled in the `FacebookSDK` folder (licensed under the Apache License, Version 2.0).

* For Android, this plugin relies on the [Facebook Android SDK](https://github.com/facebook/facebook-android-sdk) that is packaged as a .jar in the `libs` folder (licensed under the Apache License, Version 2.0).

## UPDATES ##

* Supports cordova CLI
* Generates Android and iOS config files (xml/plist)

## SAMPLE PROJECT GENERATION ##

You can generate a sample XCode project by running `samples/ios/create.sh` from the root of the repository.

## PLUGIN SETUP ##

    cordova plugin add https://github.com/codebreach/cordova-facebook-connect-simple.git --variable APP_ID="$facebookAppId" --variable APP_NAME="$facebookAppName"

All plist and xml files are updated as necessary.

## JAVASCRIPT INTERFACE (IOS/ANDROID) ##

    // After device ready, create a local alias
    var facebookConnect = window.plugins.facebookConnect;

    facebookConnect.login({permissions: ["email", "user_about_me"], appId: "YOUR_APP_ID"}, function(result) {
        console.log("FacebookConnect.login:" + JSON.stringify(result));

        // Check for cancellation/error
        if(result.cancelled || result.error) {
            console.log("FacebookConnect.login:failedWithError:" + result.message);
            return;
        }

        // Basic graph request example
        facebookConnect.requestWithGraphPath("/me/friends", {limit: 100}, function(result) {
            console.log("FacebookConnect.requestWithGraphPath:" + JSON.stringify(result));
        });

        // Feed dialog example
        var dialogOptions = {
            link: 'https://developers.facebook.com/docs/reference/dialogs/',
            picture: 'http://fbrell.com/f8.jpg',
            name: 'Facebook Dialogs',
            caption: 'Reference Documentation',
            description: 'Using Dialogs to interact with users.'
        };
        facebookConnect.dialog('feed', dialogOptions, function(response) {
            console.log("FacebookConnect.dialog:" + JSON.stringify(response));
        });

    });

* Check [source](https://github.com/mgcrea/cordova-facebook-connect/tree/master/FacebookConnect.js) for additional configuration.

## BUGS AND CONTRIBUTIONS ##

Patches welcome! Send a pull request. Since this is not a part of Cordova Core (which requires a CLA), this should be easier.

Post issues on [Github](https://github.com/mgcrea/cordova-facebook-connect/issues)

The latest code (my fork) will always be [here](https://github.com/mgcrea/cordova-facebook-connect/tree/master)

## LICENSE ##

Copyright 2012 Olivier Louvignes. All rights reserved.

The MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## CREDITS ##

Contributors :

* [Jon Buffington](http://blog.jon.buffington.name/) added dialog support for iOS.

Inspired by :

* [phonegap-plugin-facebook-connect](https://github.com/davejohnson/phonegap-plugin-facebook-connect) built by Dave Johnson.

* [Facebook iOS Tutorial](https://developers.facebook.com/docs/mobile/ios/build/)

* [Facebook iOS SDK Reference](https://developers.facebook.com/docs/reference/iossdk/)
