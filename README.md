# erniebot4j：文心一言Java客户端

`erniebot4j`是一个开源的文心一言非官方Java客户端，基于`JDK17`构建。它旨在提供一个功能丰富、易于集成和使用的Java库，以便开发者能够通过文心一言API轻松实现对话、续写、向量嵌入和图像处理等功能。

> 请注意：在使用`erniebot4j`时，你需要遵守文心一言API的使用条款和条件。

## 一、主要功能

`erniebot4j`支持以下文心一言API功能：

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

- **插件应用（Plugin）**
    - **知识库：** 让开发者（甚至非技术人员）以简单的方式管理数据集，包括分片、清洗、向量计算等能力。
    - **智慧图问：** 图片理解与识别，并对图片内容进行总结概述，输出用户可理解的自然语言文本描述句子或段落。
    - **百度搜索：** 百度搜索插件,实时获取新闻、股票信息等
    - **网页解析：** 从任何网页链接获取所需文本信息
    - **天气查询：** 输入地址，给出当前该地址天气；输入地址+时间，给出该地址时间段内的天气

## 二、系统要求

1. **JDK17**或更高版本

## 三、跑通测试

1. 到[百度智能云](https://cloud.baidu.com/)上注册一个账号
2. 在百度智能云上[创建一个应用](https://console.bce.baidu.com/qianfan/ais/console/applicationConsole/application)
   ，你将会得到一个API Key和一个Secret Key
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

### 函数调用示例

在`erniebot4j`中进行函数的声明将会变成一个非常简单的事情。框架自动帮你完成了函数的声明和参数的解析。这样，你就可以专注于函数的实现，而不用再去关心函数的声明和参数的解析了。

函数声明

```java
@ChatFn(name = "echo", description = "echo words")
@ChatFnExample(
    question = "echo: words",
    thoughts = "当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息",
    arguments = """
        {
            "words": "hello, world"
        }
        """
)
public class EchoFunction implements ChatFunction<EchoFunction.Echo, EchoFunction.Echo> {

    @Override
    public CompletableFuture<Echo> call(Echo echo) {
      return CompletableFuture.completedFuture(new Echo(echo.words()));
    }
    
    public record Echo(String words) {
    
    }

}
```

对话触发函数调用

```java
// 对话请求
final var request = new ChatRequest.Builder()
    .model(ChatModel.ERNIEBOT_8K)
    .message(Message.human("echo: hello world!"))
    .function(new EchoFunction())
    .build();

// 对话响应
final var response = client.chat(request)
    .async()
    .join();
```

输出结果

```text
2024-02-07 04:33:33 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"echo: words"},{"role":"assistant","function_call":{"thoughts":"当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息","name":"echo","arguments":"{\"words\":\"hello, world\"}"}}]],"name":"echo","description":"echo words","responses":{"type":"object","properties":{"words":{"type":"string"}}},"parameters":{"type":"object","properties":{"words":{"type":"string"}}}}],"messages":[{"role":"user","content":"echo: hello world!"}]}
2024-02-07 04:33:37 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-j3xwjc6vkm","object":"chat.completion","created":1707251618,"result":"","is_truncated":false,"need_clear_history":false,"function_call":{"name":"echo","thoughts":"用户希望我使用echo工具，并且传递给它的参数是'hello world!'。","arguments":"{\"words\":\"hello world!\"}"},"finish_reason":"function_call","usage":{"prompt_tokens":111,"completion_tokens":26,"total_tokens":137}}
2024-02-07 04:33:37 DEBUG erniebot://chat/ernie-bot-8k/function <= {"words":"hello world!"}
2024-02-07 04:33:37 DEBUG erniebot://chat/ernie-bot-8k/function => {"words":"hello world!"}
2024-02-07 04:33:37 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"echo: words"},{"role":"assistant","function_call":{"thoughts":"当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息","name":"echo","arguments":"{\"words\":\"hello, world\"}"}}]],"name":"echo","description":"echo words","responses":{"type":"object","properties":{"words":{"type":"string"}}},"parameters":{"type":"object","properties":{"words":{"type":"string"}}}}],"messages":[{"role":"user","content":"echo: hello world!"},{"role":"assistant","content":null,"function_call":{"name":"echo","arguments":"{\"words\":\"hello world!\"}"}},{"role":"function","name":"echo","content":"{\"words\":\"hello world!\"}"}]}
2024-02-07 04:33:38 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-nx26cc70ms","object":"chat.completion","created":1707251619,"result":"echo 返回了 \"hello world!\"。","is_truncated":false,"need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":119,"completion_tokens":9,"total_tokens":128}}
ChatResponse[id=as-nx26cc70ms, type=chat.completion, timestamp=1707251619000, usage=Usage[prompt=230, completion=35, total=265], sentence=Sentence[index=0, isLast=true, content=echo 返回了 "hello world!"。], call=null, search=Search[items=[]]]
```

### 多函数调用示例

`erniebot4j`会根据LLM的推理能力，自动拆解多函数调用的任务，然后按照拆解的任务顺序依次调用函数。这样，你就可以专注于函数的实现，而不用再去关心函数的调用顺序了。

我们假设有两个函数 [QueryScoreFunction](src/test/java/io/github/ompc/erniebot4j/test/chat/function/QueryScoreFunction.java)
和 [ComputeAvgScoreFunction](src/test/java/io/github/ompc/erniebot4j/test/chat/function/ComputeAvgScoreFunction.java)
，分别用于查询成绩和计算平均分。我们可以通过以下方式实现多函数调用：

```java
// 对话请求
final var request = new ChatRequest.Builder()
    .model(ChatModel.ERNIEBOT_8K)
    .message(Message.human("计算张三、李四、王五的语文平均分"))
    .function(new QueryScoreFunction())
    .function(new ComputeAvgScoreFunction())
    .option(ChatOptions.IS_STREAM, false)
    .option(ChatOptions.IS_ENABLE_SEARCH, false)
    .option(ChatOptions.TEMPERATURE, 0.01f)
    .build();

// 对话响应
final var response = client.chat(request)
    .async()
    .join();
```

输出结果

```text
2024-02-07 04:12:30 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩","name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"MATH\",\"CHINESE\"]}"}}],[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩","name":"query_score","arguments":"{\"name\":\"张三\",\"subjects\":[\"MATH\"]}"}}]],"name":"query_score","description":"query student's scores","responses":{"type":"object","properties":{"data":{"description":"data","type":"array","items":{"type":"object","properties":{"subject":{"description":"subject items","type":"string","enum":["CHINESE","MATH","ENGLISH"]},"name":{"description":"student name","type":"string"},"value":{"description":"score value","type":"number"}}}},"success":{"description":"success or not","type":"boolean"},"message":{"description":"message","type":"string"}}},"parameters":{"type":"object","properties":{"subjects":{"description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","type":"array","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}},"name":{"description":"the student name to query, example: 张三","type":"string"}},"required":["name","subjects"]}},{"examples":[[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩","name":"compute_avg_score","arguments":"{\"scores\":[30,90]}"}}],[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"thoughts":"我应该将张三的所有分数传入，计算张三的平均分","name":"compute_avg_score","arguments":"{\"scores\":[30,20,100]}"}}]],"name":"compute_avg_score","description":"计算平均成绩","responses":{"type":"object","properties":{"avg_score":{"description":"平均分","type":"number"}}},"parameters":{"type":"object","properties":{"scores":{"description":"分数集合","type":"array","items":{"type":"number"}}},"required":["scores"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"}],"disable_search":true}
2024-02-07 04:12:34 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-7n0hujz2km","object":"chat.completion","created":1707250355,"result":"","is_truncated":false,"need_clear_history":false,"function_call":{"name":"query_score","thoughts":"用户需要计算张三、李四、王五的语文平均分，我需要先查询他们的语文成绩，然后再计算平均分。任务拆解：[sub-task1: 使用[query_score]工具查询张三的语文成绩, sub-task2: 使用[query_score]工具查询李四的语文成绩, sub-task3: 使用[query_score]工具查询王五的语文成绩, sub-task4: 使用[compute_avg_score]工具计算平均分]。接下来需要调用[query_score]来查询张三的语文成绩。","arguments":"{\"name\":\"张三\",\"subjects\":[\"CHINESE\"]}"},"finish_reason":"function_call","usage":{"prompt_tokens":686,"completion_tokens":141,"total_tokens":827}}
2024-02-07 04:12:34 DEBUG erniebot://chat/ernie-bot-8k/function <= {"name":"张三","subjects":["CHINESE"]}
2024-02-07 04:12:34 DEBUG erniebot://chat/ernie-bot-8k/function => {"success":true,"message":"查询成功","data":[{"name":"张三","subject":"CHINESE","value":90.0}]}
2024-02-07 04:12:34 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩","name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"MATH\",\"CHINESE\"]}"}}],[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩","name":"query_score","arguments":"{\"name\":\"张三\",\"subjects\":[\"MATH\"]}"}}]],"name":"query_score","description":"query student's scores","responses":{"type":"object","properties":{"data":{"description":"data","type":"array","items":{"type":"object","properties":{"subject":{"description":"subject items","type":"string","enum":["CHINESE","MATH","ENGLISH"]},"name":{"description":"student name","type":"string"},"value":{"description":"score value","type":"number"}}}},"success":{"description":"success or not","type":"boolean"},"message":{"description":"message","type":"string"}}},"parameters":{"type":"object","properties":{"subjects":{"description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","type":"array","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}},"name":{"description":"the student name to query, example: 张三","type":"string"}},"required":["name","subjects"]}},{"examples":[[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩","name":"compute_avg_score","arguments":"{\"scores\":[30,90]}"}}],[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"thoughts":"我应该将张三的所有分数传入，计算张三的平均分","name":"compute_avg_score","arguments":"{\"scores\":[30,20,100]}"}}]],"name":"compute_avg_score","description":"计算平均成绩","responses":{"type":"object","properties":{"avg_score":{"description":"平均分","type":"number"}}},"parameters":{"type":"object","properties":{"scores":{"description":"分数集合","type":"array","items":{"type":"number"}}},"required":["scores"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"},{"role":"assistant","content":null,"function_call":{"name":"query_score","arguments":"{\"name\":\"张三\",\"subjects\":[\"CHINESE\"]}"}},{"role":"function","name":"query_score","content":"{\"success\":true,\"message\":\"查询成功\",\"data\":[{\"name\":\"张三\",\"subject\":\"CHINESE\",\"value\":90.0}]}"}],"disable_search":true}
2024-02-07 04:12:38 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-jtvz0nbw20","object":"chat.completion","created":1707250359,"result":"根据您提供的信息，张三的语文成绩是90分。由于您需要计算张三、李四、王五三人的语文平均分，但目前只有张三的成绩，所以无法得出准确的平均分。为了得到完整的成绩信息，您需要提供李四和王五的成绩。","is_truncated":false,"need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":717,"completion_tokens":63,"total_tokens":780}}
2024-02-07 04:12:38 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩","name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"MATH\",\"CHINESE\"]}"}}],[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩","name":"query_score","arguments":"{\"name\":\"张三\",\"subjects\":[\"MATH\"]}"}}]],"name":"query_score","description":"query student's scores","responses":{"type":"object","properties":{"data":{"description":"data","type":"array","items":{"type":"object","properties":{"subject":{"description":"subject items","type":"string","enum":["CHINESE","MATH","ENGLISH"]},"name":{"description":"student name","type":"string"},"value":{"description":"score value","type":"number"}}}},"success":{"description":"success or not","type":"boolean"},"message":{"description":"message","type":"string"}}},"parameters":{"type":"object","properties":{"subjects":{"description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","type":"array","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}},"name":{"description":"the student name to query, example: 张三","type":"string"}},"required":["name","subjects"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分。由于您需要计算张三、李四、王五三人的语文平均分，但目前只有张三的成绩，所以无法得出准确的平均分。为了得到完整的成绩信息，您需要提供李四和王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询李四的语文成绩"}],"disable_search":true}
2024-02-07 04:12:40 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-0pt5yrv1tb","object":"chat.completion","created":1707250361,"result":"","is_truncated":false,"need_clear_history":false,"function_call":{"name":"query_score","thoughts":"用户需要查询李四的语文成绩，我可以使用[query_score]工具来查询","arguments":"{\"name\":\"李四\",\"subjects\":[\"CHINESE\"]}"},"finish_reason":"function_call","usage":{"prompt_tokens":473,"completion_tokens":37,"total_tokens":510}}
2024-02-07 04:12:40 DEBUG erniebot://chat/ernie-bot-8k/function <= {"name":"李四","subjects":["CHINESE"]}
2024-02-07 04:12:40 DEBUG erniebot://chat/ernie-bot-8k/function => {"success":true,"message":"查询成功","data":[{"name":"李四","subject":"CHINESE","value":80.0}]}
2024-02-07 04:12:40 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩","name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"MATH\",\"CHINESE\"]}"}}],[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩","name":"query_score","arguments":"{\"name\":\"张三\",\"subjects\":[\"MATH\"]}"}}]],"name":"query_score","description":"query student's scores","responses":{"type":"object","properties":{"data":{"description":"data","type":"array","items":{"type":"object","properties":{"subject":{"description":"subject items","type":"string","enum":["CHINESE","MATH","ENGLISH"]},"name":{"description":"student name","type":"string"},"value":{"description":"score value","type":"number"}}}},"success":{"description":"success or not","type":"boolean"},"message":{"description":"message","type":"string"}}},"parameters":{"type":"object","properties":{"subjects":{"description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","type":"array","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}},"name":{"description":"the student name to query, example: 张三","type":"string"}},"required":["name","subjects"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分。由于您需要计算张三、李四、王五三人的语文平均分，但目前只有张三的成绩，所以无法得出准确的平均分。为了得到完整的成绩信息，您需要提供李四和王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询李四的语文成绩"},{"role":"assistant","content":null,"function_call":{"name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"CHINESE\"]}"}},{"role":"function","name":"query_score","content":"{\"success\":true,\"message\":\"查询成功\",\"data\":[{\"name\":\"李四\",\"subject\":\"CHINESE\",\"value\":80.0}]}"}],"disable_search":true}
2024-02-07 04:12:41 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-prpvfr1cwi","object":"chat.completion","created":1707250362,"result":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分。为了得到完整的成绩信息，您需要提供王五的成绩。","is_truncated":false,"need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":504,"completion_tokens":38,"total_tokens":542}}
2024-02-07 04:12:41 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩","name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"MATH\",\"CHINESE\"]}"}}],[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩","name":"query_score","arguments":"{\"name\":\"张三\",\"subjects\":[\"MATH\"]}"}}]],"name":"query_score","description":"query student's scores","responses":{"type":"object","properties":{"data":{"description":"data","type":"array","items":{"type":"object","properties":{"subject":{"description":"subject items","type":"string","enum":["CHINESE","MATH","ENGLISH"]},"name":{"description":"student name","type":"string"},"value":{"description":"score value","type":"number"}}}},"success":{"description":"success or not","type":"boolean"},"message":{"description":"message","type":"string"}}},"parameters":{"type":"object","properties":{"subjects":{"description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","type":"array","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}},"name":{"description":"the student name to query, example: 张三","type":"string"}},"required":["name","subjects"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分。由于您需要计算张三、李四、王五三人的语文平均分，但目前只有张三的成绩，所以无法得出准确的平均分。为了得到完整的成绩信息，您需要提供李四和王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询李四的语文成绩"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分。为了得到完整的成绩信息，您需要提供王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询王五的语文成绩"}],"disable_search":true}
2024-02-07 04:12:44 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-87iy1t4rew","object":"chat.completion","created":1707250365,"result":"","is_truncated":false,"need_clear_history":false,"function_call":{"name":"query_score","thoughts":"用户需要计算张三、李四、王五的语文平均分，需要调用query_score工具来获取王五的语文成绩。","arguments":"{\"name\":\"王五\",\"subjects\":[\"CHINESE\"]}"},"finish_reason":"function_call","usage":{"prompt_tokens":524,"completion_tokens":48,"total_tokens":572}}
2024-02-07 04:12:44 DEBUG erniebot://chat/ernie-bot-8k/function <= {"name":"王五","subjects":["CHINESE"]}
2024-02-07 04:12:44 DEBUG erniebot://chat/ernie-bot-8k/function => {"success":true,"message":"查询成功","data":[{"name":"王五","subject":"CHINESE","value":70.0}]}
2024-02-07 04:12:44 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩","name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"MATH\",\"CHINESE\"]}"}}],[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩","name":"query_score","arguments":"{\"name\":\"张三\",\"subjects\":[\"MATH\"]}"}}]],"name":"query_score","description":"query student's scores","responses":{"type":"object","properties":{"data":{"description":"data","type":"array","items":{"type":"object","properties":{"subject":{"description":"subject items","type":"string","enum":["CHINESE","MATH","ENGLISH"]},"name":{"description":"student name","type":"string"},"value":{"description":"score value","type":"number"}}}},"success":{"description":"success or not","type":"boolean"},"message":{"description":"message","type":"string"}}},"parameters":{"type":"object","properties":{"subjects":{"description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","type":"array","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}},"name":{"description":"the student name to query, example: 张三","type":"string"}},"required":["name","subjects"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分。由于您需要计算张三、李四、王五三人的语文平均分，但目前只有张三的成绩，所以无法得出准确的平均分。为了得到完整的成绩信息，您需要提供李四和王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询李四的语文成绩"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分。为了得到完整的成绩信息，您需要提供王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询王五的语文成绩"},{"role":"assistant","content":null,"function_call":{"name":"query_score","arguments":"{\"name\":\"王五\",\"subjects\":[\"CHINESE\"]}"}},{"role":"function","name":"query_score","content":"{\"success\":true,\"message\":\"查询成功\",\"data\":[{\"name\":\"王五\",\"subject\":\"CHINESE\",\"value\":70.0}]}"}],"disable_search":true}
2024-02-07 04:12:45 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-xvp39tt808","object":"chat.completion","created":1707250366,"result":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分，王五的语文成绩是70分。因此，张三、李四、王五三人的语文平均分为80分。","is_truncated":false,"need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":555,"completion_tokens":54,"total_tokens":609}}
2024-02-07 04:12:45 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩","name":"compute_avg_score","arguments":"{\"scores\":[30,90]}"}}],[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"thoughts":"我应该将张三的所有分数传入，计算张三的平均分","name":"compute_avg_score","arguments":"{\"scores\":[30,20,100]}"}}]],"name":"compute_avg_score","description":"计算平均成绩","responses":{"type":"object","properties":{"avg_score":{"description":"平均分","type":"number"}}},"parameters":{"type":"object","properties":{"scores":{"description":"分数集合","type":"array","items":{"type":"number"}}},"required":["scores"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分。由于您需要计算张三、李四、王五三人的语文平均分，但目前只有张三的成绩，所以无法得出准确的平均分。为了得到完整的成绩信息，您需要提供李四和王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询李四的语文成绩"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分。为了得到完整的成绩信息，您需要提供王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询王五的语文成绩"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分，王五的语文成绩是70分。因此，张三、李四、王五三人的语文平均分为80分。"},{"role":"user","content":" 使用[compute_avg_score]工具计算平均分"}],"disable_search":true}
2024-02-07 04:12:48 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-a77x7ejv6a","object":"chat.completion","created":1707250369,"result":"","is_truncated":false,"need_clear_history":false,"function_call":{"name":"compute_avg_score","thoughts":"根据用户的需求，我需要调用[compute_avg_score]工具来计算平均分。","arguments":"{\"scores\":[90,80,70]}"},"finish_reason":"function_call","usage":{"prompt_tokens":496,"completion_tokens":36,"total_tokens":532}}
2024-02-07 04:12:48 DEBUG erniebot://chat/ernie-bot-8k/function <= {"scores":[90,80,70]}
2024-02-07 04:12:48 DEBUG erniebot://chat/ernie-bot-8k/function => {"avg_score":80.0}
2024-02-07 04:12:48 DEBUG erniebot://chat/ernie-bot-8k/http => {"functions":[{"examples":[[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩","name":"compute_avg_score","arguments":"{\"scores\":[30,90]}"}}],[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"thoughts":"我应该将张三的所有分数传入，计算张三的平均分","name":"compute_avg_score","arguments":"{\"scores\":[30,20,100]}"}}]],"name":"compute_avg_score","description":"计算平均成绩","responses":{"type":"object","properties":{"avg_score":{"description":"平均分","type":"number"}}},"parameters":{"type":"object","properties":{"scores":{"description":"分数集合","type":"array","items":{"type":"number"}}},"required":["scores"]}}],"stream":false,"temperature":0.01,"messages":[{"role":"user","content":"计算张三、李四、王五的语文平均分"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分。由于您需要计算张三、李四、王五三人的语文平均分，但目前只有张三的成绩，所以无法得出准确的平均分。为了得到完整的成绩信息，您需要提供李四和王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询李四的语文成绩"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分。为了得到完整的成绩信息，您需要提供王五的成绩。"},{"role":"user","content":" 使用[query_score]工具查询王五的语文成绩"},{"role":"assistant","content":"根据您提供的信息，张三的语文成绩是90分，李四的语文成绩是80分，王五的语文成绩是70分。因此，张三、李四、王五三人的语文平均分为80分。"},{"role":"user","content":" 使用[compute_avg_score]工具计算平均分"},{"role":"assistant","content":null,"function_call":{"name":"compute_avg_score","arguments":"{\"scores\":[90,80,70]}"}},{"role":"function","name":"compute_avg_score","content":"{\"avg_score\":80.0}"}],"disable_search":true}
2024-02-07 04:12:49 DEBUG erniebot://chat/ernie-bot-8k/http <= {"id":"as-upp67fpwz2","object":"chat.completion","created":1707250370,"result":"根据您提供的信息，张三、李四、王五三人的语文平均分为80分。","is_truncated":false,"need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":505,"completion_tokens":22,"total_tokens":527}}
ChatResponse[id=as-upp67fpwz2, type=chat.completion, timestamp=1707250370000, usage=Usage[prompt=4460, completion=439, total=4899], sentence=Sentence[index=0, isLast=true, content=根据您提供的信息，张三、李四、王五三人的语文平均分为80分。], call=null, search=Search[items=[]]]
```

在一次对话中，用户首先询问了张三的语文成绩，然后询问了李四的语文成绩，最后询问了王五的语文成绩。在这个过程中，`erniebot4j`
根据用户的需求，分别调用了`query_score`工具来查询张三、李四、王五的语文成绩，并使用`compute_avg_score`
工具来计算他们的语文平均分。最终，助手返回了张三、李四、王五三人的语文平均分为80分。

### 文生图示例

`erniebot4j`会将文心一言返回的BASE64编码封装为`BufferedImage`
类型，方便开发者进行后续的图像处理。下面是一个简单的示例，展示了如何使用`erniebot4j`进行图像处理：

```java
// 对话请求
final var request = new GenImageRequest.Builder()
    .model(GenImageModel.STABLE_DIFFUSION_XL)
    .prompt("猫")
    .option(GenImageOptions.NUMBERS, 1)
    .option(GenImageOptions.SIZE, GenImageRequest.Size.S_1024_1024)
    .build();

// 对话响应
final var response = client.image().generation(request)
    .async()
    .join();
```

然后你就可以通过`response.images()[0]`拿到生成的图片的`BufferedImage`类型进行后续操作了。

![文生图-猫](https://ompc-images.oss-cn-hangzhou.aliyuncs.com/erniebot4j/gen-image-as-mr0hyfmsix-001.png)

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

正是在这样的背景下，我发起了`erniebot4j`
项目。它旨在成为文心一言的Java开发者最佳伴侣，提供简洁明了的API接口，帮助开发者快速集成和使用文心一言的功能。通过`erniebot4j`
，Java开发者可以轻松地实现对话、续写、向量嵌入和图像处理等功能，极大地提升了开发效率和用户体验。

### 展望与呼吁

展望未来，我希望`erniebot4j`
能够成为Java开发者与文心一言之间的桥梁，推动文心一言在更多领域的应用和发展。同时，我也呼吁更多的开发者加入到`erniebot4j`
的开源社区中来，共同完善和优化这个项目，让它更好地服务于整个开发者社区。

## 七、相关链接

- [千帆大模型平台](https://console.bce.baidu.com/qianfan/overview)