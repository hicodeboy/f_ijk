//
//  PlayerViewController.m
//  flutter_live
//
//  Created by 杜甲 on 2019/7/30.
//

#import "PlayerViewController.h"
#import <IJKMediaFramework/IJKMediaFramework.h>
@implementation PlayerViewFactory{
    NSObject<FlutterBinaryMessenger>*_messenger;
}

- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger> *)messager {
    self = [super init];
    if (self) {
        _messenger = messager;
    }
    return self;
}

-(NSObject<FlutterMessageCodec> *)createArgsCodec{
    return [FlutterStandardMessageCodec sharedInstance];
}

- (NSObject<FlutterPlatformView>*)createWithFrame:(CGRect)frame
                                   viewIdentifier:(int64_t)viewId
                                        arguments:(id _Nullable)args {

    PlayerViewController *view = [[PlayerViewController alloc] initWithWithFrame:frame viewIdentifier:viewId arguments:args binaryMessenger:(NSObject<FlutterBinaryMessenger> *)_messenger];
    return view;
}

@end

@interface PlayerViewController ()
@property (nonatomic, strong) NSString *url;
@property (nonatomic , strong)IJKFFMoviePlayerController *playback;
@property (nonatomic , strong) UIView *view;
@end

@implementation PlayerViewController{
    int64_t _viewId;
    FlutterMethodChannel *_channel;
    
}

- (instancetype)initWithWithFrame:(CGRect)frame
                   viewIdentifier:(int64_t)viewId
                        arguments:(id)args
                  binaryMessenger:(NSObject<FlutterBinaryMessenger> *)messenger
{
    if (self = [super init]) {
        
        _viewId = viewId;
        
        NSDictionary *dic = args;
        _url = dic[@"url"];
        BOOL shouldAutoPlayer = [dic[@"shouldAutoPlayer"] boolValue];
        IJKFFOptions *options = [IJKFFOptions optionsByDefault];
        
        NSLog(@"_url = %@",_url);
        
        _view = [[UIView alloc] initWithFrame:frame];
        _view.backgroundColor = [UIColor redColor];
        
        _playback = [[IJKFFMoviePlayerController alloc] initWithContentURL:[NSURL URLWithString:_url] withOptions:options];

        _playback.view.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight;

        _playback.scalingMode = IJKMPMovieScalingModeAspectFit;
        _playback.shouldAutoplay = shouldAutoPlayer;
        _playback.view.frame = frame;
        [_playback prepareToPlay];
        [_view addSubview:_playback.view];
        
        NSString *channelName = [NSString stringWithFormat:@"plugins/live_player_%lld",viewId];
        _channel = [FlutterMethodChannel methodChannelWithName:channelName binaryMessenger:messenger];
        
        __weak __typeof__(self) weakSelf = self;
        [_channel setMethodCallHandler:^(FlutterMethodCall * _Nonnull call, FlutterResult  _Nonnull result) {
            [weakSelf onMethodCall:call result:result];
        }];
    }
    
    
    return self;
}

- (void)dealloc {
    NSLog(@"player dealloc");
}

- (nonnull UIView *)view {
    return _view;
}

-(void)onMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result{
    
    NSString *method = call.method;
    if ([method isEqualToString:@"play"]) {
        NSLog(@"play");
        [_playback play];
    } else if ([method isEqualToString:@"shutdown"]) {
        NSLog(@"shutdown");
       [_playback shutdown];
    } else if ([method isEqualToString:@"id"]) {
        result(@(_viewId));
    } else {
        result(FlutterMethodNotImplemented);
    }
}

@end
