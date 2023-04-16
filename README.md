# ChatGPT
a Chat APP by ChatGPT API.

一个基于 ChatGPT API 的Android 语音助手（暂时这个需求方向）。

# 技术点

## 1.语音识别


### 目前考虑的方案：

1. 科大讯飞

- 免费的话，语音听写和在线语音合成一年5万次交互量。

2. 微软Azure（https://azure.microsoft.com/zh-cn/products/cognitive-services/speech-services/）

- 免费版本：每月 5 小时免费音频，永久免费。
- 注册需要VISA卡。

3. 百度云、阿里云、腾讯云等云平台提供的功能



### 废弃的方案：

1. 谷歌原生的语音识别

优点：代码集成简单，维护方便。

缺点：使用需要科学魔法，可能会影响ChatGPT的沟通效率。后续扩展或者加入个性化语音需求比较麻烦。

2. PocketSphinx on Android （https://cmusphinx.github.io/wiki/tutorialandroid/）

优点：离线语音识别，网上教程多。

缺点：集成有点复杂，并且没有个性化语音功能，很多教程都比较久远，维护难度大。
