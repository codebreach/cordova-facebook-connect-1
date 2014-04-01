//
//  FacebookConnect.m
//
// Created by Olivier Louvignes on 2012-06-25.
//
// Copyright 2012 Olivier Louvignes. All rights reserved.
// MIT Licensed

#import "FacebookConnect.h"

@implementation FacebookConnect

#pragma mark - Overloads

/* This overrides CDVPlugin's method, which receives a notification when handleOpenURL is called on the main app delegate */
- (void) handleOpenURL:(NSNotification*)notification
{
    NSURL* url = [notification object];
    
    if (![url isKindOfClass:[NSURL class]]) {
        return;
    }
    
    [FBAppCall handleOpenURL:url sourceApplication:@"com.facebook" withSession:[PFFacebookUtils session]];
}

- (void)pluginInitialize
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onAppDidBecomeActive:)
                                                 name:UIApplicationDidBecomeActiveNotification object:nil];
}

/*
 * This method is called to let your application know that it moved from the inactive to active state.
 */
- (void)onAppDidBecomeActive:(NSNotification*)notification
{
    [FBAppCall handleDidBecomeActiveWithSession:[PFFacebookUtils session]];
}

#pragma mark - Cordova plugin interface

- (void)init:(CDVInvokedUrlCommand*)command {
    DLog(@"init:%@\n", command);

    [Parse setApplicationId:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"ParseAppId"]
                  clientKey:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"ParseKey"]];
    [PFFacebookUtils initializeFacebook];
    
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)login:(CDVInvokedUrlCommand*)command {
    ALog(@"login:%@\n", command);

    NSArray *permissions = nil;
    if ([command.arguments count] > 0) {
        permissions = command.arguments;
    }
    
    [PFFacebookUtils logInWithPermissions:permissions block:^(PFUser *user, NSError *error) {
        CDVPluginResult* pluginResult;
        if (error) {
            NSDictionary* result = [NSDictionary init];
            [result setValue:@"connected" forKey:@"state"];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
        } else {
            if (user.isNew) {
                [user saveInBackground];
                ALog(@"new user");
            } else {
                ALog(@"old user");
            }
            NSDictionary* result = [NSDictionary init];
            [result setValue:user.sessionToken forKey:@"session_token"];
            [result setValue:user.objectId forKey:@"object_id"];
            [result setValue:@"connected" forKey:@"state"];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)logout:(CDVInvokedUrlCommand*)command {
    DLog(@"logout:%@\n", command);

    [PFUser logOut];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
