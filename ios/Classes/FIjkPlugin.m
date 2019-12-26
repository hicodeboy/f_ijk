#import "FIjkPlugin.h"
#import "PlayerViewController.h"

@implementation FIjkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"f_ijk"
            binaryMessenger:[registrar messenger]];
  FIjkPlugin* instance = [[FIjkPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
  [registrar registerViewFactory:[[PlayerViewFactory alloc] initWithMessenger:registrar.messenger] withId:@"plugins/live_player"];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
