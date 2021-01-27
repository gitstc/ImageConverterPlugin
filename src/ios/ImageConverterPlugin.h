//
//  ImageConverter.h
//  CameraTest
//
//  Created by Shnoudi on 10/20/14.
//
//

#import <Cordova/CDVPlugin.h>

@interface ImageConverterPlugin : CDVPlugin
-(void)getBase64Image:(CDVInvokedUrlCommand*)command;
@end
