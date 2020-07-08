# Google Firebase Cloud Messaging Cordova Push Plugin
> Extremely easy plug&play push notification plugin for Cordova applications with Google Firebase FCM.


## How it works
Send a push notification to a single device or topic.
- 1.a Application is in foreground:
 - The notification data is received in the JavaScript callback without notification bar message (this is the normal behaviour of mobile push notifications).
- 1.b Application is in background or closed:
 - The device displays the notification message in the device notification bar.
 - If the user taps the notification, the application comes to foreground and the notification data is received in the JavaScript callback.
 - If the user does not tap the notification but opens the applicacion, nothing happens until the notification is tapped.

## License
```
The MIT License

Copyright (c) 2017 Felipe Echanique Torres (felipe.echanique in the gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
