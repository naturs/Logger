# Logger #

该库主要基于[https://github.com/orhanobut/logger][1]进行了部分修改，主要增加了以下特性：

1. 将项目更改为Java工程，可以在Java平台上输出日志；
2. 支持显示简化的日志信息；
3. 添加了对Collection、Map、多维数组、Intent、Bundle的格式化输出；
4. 支持指定调用方法栈；

部分修改参考了以下开源项目：

[https://github.com/ZhaoKaiQiang/KLog][2]

[https://github.com/JakeWharton/timber][3]

[https://github.com/pengwei1024/LogUtils][4]

## 使用方式 ##

#### 一、添加依赖 ####

#### 二、初始化 ####

```java
// Must
// For Java
Logger.addLogAdapter(new DefaultLogAdapter("Your Global Tag"));
Logger.setLogConverter(new DefaultLogConverter());
```

```java
// Must
// For Android
Logger.addLogAdapter(new AndroidLogAdapter("Your Global Tag"));
Logger.setLogConverter(new AndroidLogConverter());
```

```java
// Optional
// Add your custom ConverterStrategy
Logger.addConverterStrategy(YourCustomConverterStrategy);
```

#### 三、使用 ####

#### 四、效果 ####

![](screenshots/java.png)

![](screenshots/android.png)

## License ##

	Copyright 2017 naturs

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

[1]:https://github.com/orhanobut/logger
[2]:https://github.com/ZhaoKaiQiang/KLog
[3]:https://github.com/JakeWharton/timber
[4]:https://github.com/pengwei1024/LogUtils