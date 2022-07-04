#import "LibertyNfcPlugin.h"
#if __has_include(<liberty_nfc/liberty_nfc-Swift.h>)
#import <liberty_nfc/liberty_nfc-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "liberty_nfc-Swift.h"
#endif

@implementation LibertyNfcPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftLibertyNfcPlugin registerWithRegistrar:registrar];
}
@end
