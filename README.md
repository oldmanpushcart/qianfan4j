# erniebot4j：文心一言Java客户端

`erniebot4j`是一个开源的文心一言非官方Java客户端，基于`JDK17`构建。它旨在提供一个功能丰富、易于集成和使用的Java库，以便开发者能够通过文心一言API轻松实现对话、续写、向量嵌入和图像处理等功能。

> 请注意：在使用`erniebot4j`时，你需要遵守文心一言API的使用条款和条件。

## 一、主要功能

erniebot4j支持以下文心一言API功能：

- **对话（Chat）**
  - 提供用户与文心一言模型进行自然语言对话。
  - 支持用户在一次对话中触发多个函数调用。

- **续写（Completions）**
  - 提供文本续写功能，可以根据给定的文本片段生成后续内容。

- **向量（Embeddings）**
  - 将文本转换为向量表示，用于文本相似度比较、聚类等任务。

- **图像（Images）**
  - **图生文：** 根据提供的图像生成描述性文本。
  - **文生图：** 将文本描述转换为相应的图像。

## 二、系统要求

1. **JDK17**或更高版本

## 三、跑通测试

1. 到[百度智能云](https://cloud.baidu.com/)上注册一个账号
2. 在百度智能云上[创建一个应用](https://console.bce.baidu.com/qianfan/ais/console/applicationConsole/application)，你将会得到一个API Key和一个Secret Key
3. 在本地创建一个文件：`$HOME/erniebot-test.properties`，内容如下：

   ```properties
   erniebot.identity=<你的API Key>
   erniebot.secret=<你的Secret Key>
   ```
4. 运行测试用例：`mvn test`

## 四、依赖使用

项目仓库托管在GitHub上，可以通过以下方式引入依赖

首先在`pom.xml`中配置erniebot4j所在的GitHub仓库地址
```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/oldmanpushcart/erniebot4j</url>
</repository>
```

然后在`pom.xml`中引入erniebot4j
```xml
<dependency>
    <groupId>io.github.ompc.erniebot4j</groupId>
    <artifactId>erniebot4j</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 创建客户端

```java

// 百度的Token
final var refresher = new TokenRefresher(
    "***", // API Key
    "***"  // Secret Key
);

// 线程池
final var executor = Executors.newFixedThreadPool(10);

// 文心一言客户端
final var client = new ErnieBotClient.Builder()
    .refresher(refresher)
    .executor(executor)
    .connectTimeout(Duration.ofSeconds(30))
    .build();
```

### 对话示例

```java
// 对话请求
final var request = new ChatRequest.Builder()
        .model(ChatModel.ERNIEBOT_8K)
        .message(Message.human("你是谁?"))
        .option(ChatOptions.IS_STREAM, true)
        .build();

// 对话响应
final var response = client.chat(request)
        .async()
        .join();
```
输出结果
```text
2024-02-07 02:47:15 DEBUG erniebot://chat/ernie-bot-8k/http => {"stream":true,"messages":[{"role":"user","content":"你是谁?"}]}
2024-02-07 02:47:15 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-414fqwb8p3","object":"chat.completion","created":1707245236,"sentence_id":0,"is_end":false,"is_truncated":false,"result":"你好，","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":2,"completion_tokens":0,"total_tokens":2}}
2024-02-07 02:47:16 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-414fqwb8p3","object":"chat.completion","created":1707245237,"sentence_id":1,"is_end":false,"is_truncated":false,"result":"我是一个虚拟语言模型，旨在通过机器学习和自然语言处理技术来回答用户的问题和提供相关信息。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":2,"completion_tokens":0,"total_tokens":2}}
2024-02-07 02:47:17 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-414fqwb8p3","object":"chat.completion","created":1707245237,"sentence_id":2,"is_end":false,"is_truncated":false,"result":"如果你有任何问题或需要帮助，请随时向我提问。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":2,"completion_tokens":0,"total_tokens":2}}
2024-02-07 02:47:17 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-414fqwb8p3","object":"chat.completion","created":1707245238,"sentence_id":3,"is_end":true,"is_truncated":false,"result":"","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":2,"completion_tokens":36,"total_tokens":38}}
ChatResponse[id=as-414fqwb8p3, type=chat.completion, timestamp=1707245236000, usage=Usage[prompt=2, completion=36, total=38], sentence=Sentence[index=0, isLast=true, content=你好，我是一个虚拟语言模型，旨在通过机器学习和自然语言处理技术来回答用户的问题和提供相关信息。如果你有任何问题或需要帮助，请随时向我提问。], call=null, search=Search[items=[]]]
```

## 五、参与贡献

如果你对erniebot4j感兴趣并希望为其做出贡献，请遵循以下步骤：

1. Fork本项目到你的GitHub账户。
2. 克隆项目到你的本地环境。
3. 创建一个新的分支用于你的修改。
4. 提交你的更改并通过`Pull Request`请求合并到主分支。

在提交Pull Request之前，请确保你的代码符合项目的编码规范和最佳实践，并且已经通过了相关的测试。

## 六、特别致谢

首先，我要向百度千帆大模型团队的同学们表达我最深切地感谢。正是他们不懈的努力和卓越的工作成果，使得我们能够如此便捷地利用百度文心一言的API进行开发。他们为整个开发者社区树立了榜样，推动了技术的进步。

### 关于文心一言
  
作为个人使用者，我对文心一言这个产品怀有极高地评价。相较于OpenAi的GPT-4，虽然在某些功能上还有待完善，但文心一言在稳定性方面展现出了显著的优势。在实际应用中，它的可靠和稳定让我倍感信赖，这也是我选择它作为开发基础的重要原因之一。

### 缘起与动机

当我得知千帆大模型发布了SDK时，我迫不及待地想要集成到我的项目中。然而，我遗憾地发现他们的SDK当时并不支持Java。作为一个Java开发者，我深知Java在开发者社区中的普及程度和重要性。因此，我决定自己动手，填补这一空白，为Java开发者提供一个方便、易用的文心一言客户端。

正是在这样的背景下，我发起了`erniebot4j`项目。它旨在成为文心一言的Java开发者最佳伴侣，提供简洁明了的API接口，帮助开发者快速集成和使用文心一言的功能。通过`erniebot4j`，Java开发者可以轻松地实现对话、续写、向量嵌入和图像处理等功能，极大地提升了开发效率和用户体验。

### 展望与呼吁

展望未来，我希望`erniebot4j`能够成为Java开发者与文心一言之间的桥梁，推动文心一言在更多领域的应用和发展。同时，我也呼吁更多的开发者加入到`erniebot4j`的开源社区中来，共同完善和优化这个项目，让它更好地服务于整个开发者社区。

## 七、相关链接

- [千帆大模型平台](https://console.bce.baidu.com/qianfan/overview)