//
//  ImageConverter.m
//  CameraTest
//
//  Created by Shnoudi on 10/20/14.
//
//

#import "ImageConverterPlugin.h"

@implementation ImageConverterPlugin
-(void)getBase64Image:(CDVInvokedUrlCommand*)command{
    NSString *path = [command.arguments objectAtIndex:0];
    path = [path lastPathComponent];
    path = [NSTemporaryDirectory() stringByAppendingPathComponent:[NSString stringWithFormat:@"%@",path]];
    
    UIImage *theImage = [UIImage imageWithContentsOfFile:path];
    NSData *imageData = UIImageJPEGRepresentation(theImage, 0.0);
    NSString *base64 = [imageData base64Encoding];

    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NSString stringWithFormat:@"data:image/png;base64,%@",base64]];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}
@end
