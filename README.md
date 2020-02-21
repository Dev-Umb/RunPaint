# 									"绘跑"APP

 

### **"绘跑"是一款针对爱好户外跑步的人群设计的用于增加户外跑步趣味性的运动类APP。**

## 主要功能：

| 功能名称                 | 功能描述                                                     | 实现方法                                                     |
| ------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 登录注册功能             | 用户通过注册功能可以拥有属于自己的账号密码以及个人应用信息，并通过登录进入应用 | 预想使用服务端实现用户数据的云存储，但因技术方面原因（菜）转而暂时使用sharePreference这种较为低端的方式来本地存储用户数据，后期登陆注册会转向服务端 |
| 第三方登录               | 用户除了手动注册账号外还可以使用自己的QQ账号直接登录应用，一步完成登录注册的过程 | 注册了腾讯开发者获得了腾讯的第三方登录和腾讯应用数据分析的SDK以及对应应用密钥，参考官方文档与CSDN相关技术文档，最终调用了QQloginManager库和实现了QQ的第三方登录 |
| 实时天气提醒             | 在app主界面顶部设置了每日天气提醒，实时更新天气，为户外运动者提供户外最新的天气情况 | 注册了和风天气开发者，调取了和风天气SDK，结合百度SDK的Location监听器实现自动定位，再将定位的信息传递给和风天气的API，获得天气的JSON数据，使用安卓自带的JSONObject类去解析获得数据并在主界面中展示出来，因网络请求较多，因此本app中所有网络请求皆使用了单独的线程 |
| 每日签到                 | 在天气推送下方添加了每日签到按钮，点击按钮会获得“每日一句”的励志短语以及加载每日好图，签到在每日的24点会清零，次日可再次签到 | 签到按钮是使用TextView实现的，并添加了点击开关，在签到后会触发队金山词霸的网络请求，获得返回的每日一句的JSON文件并解析出每日一句以及每日好图并加载到主界面。另外对金山词霸的网络爬取使用了Jsoup库来解析请求到的html文件在从中找出JSON文件的请求api |
| 好友功能                 | 用户可自定义添加好友，因为服务端未建成的原因，这里暂时使用了读取手机本地联系人的方式生成用户的好友列表。当然用户也可以点击 + 号手动添加好友。点击好友会进入短信界面，以方便用户与好友通信 | 通过参考CSDN上的教程，在获取读取手机联系人的权限后，使用getContentResolver().query来实例化Cursor类，并使用Cursor的实例对象进行对联系人的读取。读取后存入ListView中并显示出来。在点击时发生活动跳转，点击的listView卡片会向短信界面传递改卡片所携带的联系人电话信息，从而达到自动跳转到联系人的短信界面。 |
| 跑步百科                 | 用户可以通过底部栏导航进入“发现”模块去学习跑步的各种注意事项 | 这里是直接使用了webView做了套壳浏览器，因为安卓原生的浏览器内核有无法读取加载进度的bug（也可能是因为我菜），借由注册了腾讯开发者的机会，将安卓原生的浏览器内核替换为了腾讯x5浏览器内核，使用方法与原生webView没有区别，但是没有了无法读取加载进度的bug |
| 主题更改                 | 用户可以在设置界面进行应用主体颜色的更改，我为用户提供了天依蓝，活力橙，阳光黄，乐正红，极简白，酷炫黑六种主体 | 通过在主界面设置全局静态变量red，green，blue三个颜色参数实现对主体控件颜色的更改，因为安卓setBackgroundColor方法无法直接使用R.color进行颜色更改（默认是透明色且无法更改），因此使用了直接设置rgb色值的方法进行颜色更改 |
| 足迹跟踪                 | 用户在地图中可以点击绘跑按钮开启足迹追踪达到在地图上”绘画''的趣味活动 | 使用了百度地图的SDK实现实时定位，再结合百度SDK中绘制直线的方法实时绘制用户的轨迹并在用户选择关闭绘制后停止绘制并标记终止点 |
| 地图展示                 | 用户可以在地图中查看当前路况信息和人流密集分布情况，以选择最方便的出行路线 | 调用百度地图的热力图和交通图以及卫星图实现                   |
| 轨迹保存以及历史轨迹查询 | 用户在绘制轨迹结束之后可以选择保存轨迹并自定义轨迹名字，将此存储到用户数据文件中方便日后随时查看 | 预想功能，本计划使用安卓自带的sqlite数据库实现，但bug太多加之对数据库了解很少，暂时无法实现 |

第三方库的使用：（没有包括直接手动导入的百度地图SDK，腾讯QQSDK，腾讯浏览器x5内核，和风天气SDK）

```java
    implementation 'de.hdodenhof:circleimageview:2.1.0'
    implementation 'com.makeramen:roundedimageview:2.2.1'
    //noinspection GradleCompatible
    implementation 'com.android.support:appcompat-v7:26.0.0'
    //noinspection GradleCompatible
    implementation 'com.android.support:support-v4:26.0.0'
    implementation 'com.baidu.lbs:trace:3.1.5'
    implementation("com.squareup.okio:okio:2.4.3")
    implementation 'com.squareup.okhttp3:okhttp:3.4.1'
    implementation 'com.google.code.gson:gson:2.6.2'
    implementation 'org.jsoup:jsoup:1.9.2'
    implementation 'com.tencent.tac:tac-core:1.1.0'
    implementation 'com.qq.mta:mta:3.4.2'
    //MID基础包
    implementation 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
    implementation 'com.tencent.mid:mid:3.73-release'
```



截图



