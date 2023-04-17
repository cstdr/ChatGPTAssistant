# ChatGPT
a Chat APP by ChatGPT API.

一个基于 ChatGPT API 的Android 语音助手（暂时这个需求方向）。
## 功能
1. 基于 ChatGPT 3.5 的聊天功能，温度设置为 0.5 ，其他默认设置，没有初始角色设定。
2. 可以语音输入，默认GPT语音播报回答。语音播报可以停止。
3. 支持上下文理解，可以多轮对话，比如扮演面试官角色进行面试沟通。

# 技术点

## 1.语音识别

### 目前考虑的方案：

1. 科大讯飞（目前使用中）

- 免费的话，语音听写和在线语音合成一年5万次交互量。

2. 百度云、阿里云、腾讯云等云平台提供的功能


### 废弃的方案：

1. 谷歌原生的语音识别

优点：代码集成简单，维护方便。

缺点：使用需要科学魔法，可能会影响ChatGPT的沟通效率。后续扩展或者加入个性化语音需求比较麻烦。

2. PocketSphinx on Android （https://cmusphinx.github.io/wiki/tutorialandroid/）

优点：离线语音识别，网上教程多。

缺点：集成有点复杂，并且没有个性化语音功能，很多教程都比较久远，维护难度大。

3. 微软Azure（https://azure.microsoft.com/zh-cn/products/cognitive-services/speech-services/）

- 免费版本：每月 5 小时免费音频，永久免费。
- 注册需要VISA卡。
最后卡在VISA注册上，卡激活没问题，持卡人签名那边尝试各种办法怎么都过不去。。。

