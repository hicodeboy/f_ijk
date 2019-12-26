//
//  PlayerViewController.h
//  flutter_live
//
//  Created by 杜甲 on 2019/7/30.
//

#import <UIKit/UIKit.h>
#import <Flutter/Flutter.h>
NS_ASSUME_NONNULL_BEGIN

@interface PlayerViewFactory : NSObject<FlutterPlatformViewFactory>
- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger> *)messager;

@end

@interface PlayerViewController : NSObject<FlutterPlatformView>
- (instancetype)initWithWithFrame:(CGRect)frame
                   viewIdentifier:(int64_t)viewId
                        arguments:(id _Nullable)args
                  binaryMessenger:(NSObject<FlutterBinaryMessenger>*)messenger;
@end

NS_ASSUME_NONNULL_END
