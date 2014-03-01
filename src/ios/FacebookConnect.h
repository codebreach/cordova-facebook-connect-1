//
//  FacebookConnect.h
//
// Created by Olivier Louvignes on 2012-06-25.
//
// Copyright 2012 Olivier Louvignes. All rights reserved.
// MIT Licensed

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import <FacebookSDK/FacebookSDK.h>

@interface FacebookConnect : CDVPlugin {
}

#pragma mark - Properties

@property (nonatomic, retain) NSMutableDictionary *callbackIds;
@property (nonatomic, retain) NSMutableDictionary *facebookRequests;
@property (nonatomic, retain) NSDateFormatter *dateFormatter;

#pragma mark - Instance methods

- (void)init:(CDVInvokedUrlCommand*)command;
- (void)login:(CDVInvokedUrlCommand*)command;
- (void)logout:(CDVInvokedUrlCommand*)command;
- (void)dialog:(CDVInvokedUrlCommand*)command;

@end

#pragma mark - Logging tools

#ifdef DEBUG
#   define DLog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__);
#else
#   define DLog(...)
#endif
#define ALog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__);
