# qianfan4j：千帆 Java SDK

`qianfan4j`是一个开源的千帆大模型平台的非官方Java客户端，基于`JDK17`构建。它旨在提供一个功能丰富、易于集成和使用的Java库，以便开发者能够通过千帆API轻松实现对话、续写、向量嵌入和图像处理等功能。

> 请注意：在使用`qianfan4j`时，你需要遵守千帆API的使用条款和条件。

## 一、主要功能

`qianfan4j`支持以下千帆API功能：

- **对话（Chat）**
    - 提供用户与千帆模型进行自然语言对话。
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
    - **智慧图问：** 图片理解识别，并对图片内容进行总结概述，输出用户可理解的句子或段落。
    - **百度搜索：** 百度搜索插件,实时获取新闻、股票信息等
    - **网页解析：** 从任何网页链接获取所需文本信息
    - **天气查询：** 输入地址，给出当前该地址天气；输入地址+时间，给出该地址时间段内的天气

## 二、系统要求

1. **JDK17**或更高版本

## 三、跑通测试

1. 到[百度智能云](https://cloud.baidu.com/)上注册一个账号
2. 在百度智能云上[创建一个应用](https://console.bce.baidu.com/qianfan/ais/console/applicationConsole/application)，你将会得到一个API Key和一个Secret Key
3. 声明环境变量：
   ```shell
   export QIANFAN_AK=<YOUR APP-KEY>
   export QIANFAN_SK=<YOUR SECRET-KEY>
   ```
   注意：[PluginAppTestCase](https://github.com/oldmanpushcart/qianfan4j/blob/main/src/test/java/io/github/oldmanpushcart/test/qianfan4j/pluginapp/PluginAppTestCase.java)
   测试用例如果要跑通，需要在千帆大模型平台创建插件应用，开通`ocr-chat`插件。 并在配置文件中追加一行
   ```shell
   export QIANFAN_PLUGIN_APP_ID=<YOUR PLUGIN-APP ID>
   ```
4. 运行测试用例：`mvn test`

## 四、依赖使用

项目仓库托管在Maven中央仓库，你可以在`pom.xml`中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.oldmanpushcart</groupId>
    <artifactId>qianfan4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 创建客户端

```java
// 线程池
final var executor = Executors.newFixedThreadPool(10);

// 千帆客户端
final var client = QianFanClient.newBuilder()
    .ak("***") // API Key
    .sk("***") // Secret Key
    .executor(executor)
    .connectTimeout(Duration.ofSeconds(30))
    .build();
```

### 对话示例

```java
// 对话请求
final var request = ChatRequest.newBuilder()
        .model(ChatModel.ERNIE_V4)
        .messages(Message.ofUser("hello!"))
        .build();

// 对话响应
final var response = client.chat(request)
        .async()
        .join();

// System.out.println(response);
```

输出结果

```text
2024-03-10 17:53:43 DEBUG qianfan://token/refresh success! expired=1712656423872;
2024-03-10 17:53:43 DEBUG qianfan://chat/ernie-v4.0 => {"messages":[{"role":"user","content":"hello!"}]}
2024-03-10 17:53:45 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-7sntu8vk0p","object":"chat.completion","created":1710064425,"result":"你好！很高兴与你交流。有什么我可以帮助你的吗？请随时告诉我。","is_truncated":false,"need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":2,"completion_tokens":16,"total_tokens":18}}
你好！很高兴与你交流。有什么我可以帮助你的吗？请随时告诉我。
```

### 函数调用示例

在`qianfan4j`中进行函数的声明将会变成一个非常简单的事情。框架自动帮你完成了函数的声明和参数的解析。这样，你就可以专注于函数的实现，而不用再去关心函数的声明和参数的解析了。

函数声明

```java
@ChatFn(name = "echo", description = "echo words", examples = {
    @ChatFn.Example(
        question = "echo: words",
        thoughts = "当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息",
        arguments = """
            {
                "words": "hello, world"
            }
            """
    )
})
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
final var request = ChatRequest.newBuilder()
    .model(ChatModel.ERNIE_V4)
    .functions(new EchoFunction())
    .messages(Message.ofUser("echo: HELLO WORLD!"))
    .build();

final var response = client.chat(request)
    .async()
    .join();

// System.out.println(response.content());
```

输出结果

```text
2024-03-10 17:58:37 DEBUG qianfan://token/refresh success! expired=1712656717750;
2024-03-10 17:58:37 DEBUG qianfan://chat/ernie-v4.0 => {"messages":[{"role":"user","content":"echo: HELLO WORLD!"}],"functions":[{"name":"echo","description":"echo words","parameters":{"type":"object","properties":{"words":{"type":"string"}}},"responses":{"type":"object","properties":{"words":{"type":"string"}}},"examples":[[{"role":"user","content":"echo: words"},{"role":"assistant","function_call":{"name":"echo","arguments":"{\n    \"words\": \"hello, world\"\n}\n","thoughts":"当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息"}}]]}]}
2024-03-10 17:58:40 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-aqte3cvapb","object":"chat.completion","created":1710064720,"result":"","is_truncated":false,"need_clear_history":false,"function_call":{"name":"echo","thoughts":"当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息","arguments":"{\"words\":\"HELLO WORLD!\"}"},"finish_reason":"function_call","usage":{"prompt_tokens":112,"completion_tokens":24,"total_tokens":136}}
2024-03-10 17:58:40 DEBUG qianfan://chat/ernie-v4.0/function <= {"words":"HELLO WORLD!"}
2024-03-10 17:58:40 DEBUG qianfan://chat/ernie-v4.0/function => {"words":"HELLO WORLD!"}
2024-03-10 17:58:40 DEBUG qianfan://chat/ernie-v4.0 => {"messages":[{"role":"user","content":"echo: HELLO WORLD!"},{"role":"assistant","function_call":{"name":"echo","arguments":"{\"words\":\"HELLO WORLD!\"}","thoughts":"当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息"}},{"role":"function","content":"{\"words\":\"HELLO WORLD!\"}","name":"echo"}],"functions":[{"name":"echo","description":"echo words","parameters":{"type":"object","properties":{"words":{"type":"string"}}},"responses":{"type":"object","properties":{"words":{"type":"string"}}},"examples":[[{"role":"user","content":"echo: words"},{"role":"assistant","function_call":{"name":"echo","arguments":"{\n    \"words\": \"hello, world\"\n}\n","thoughts":"当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息"}}]]},{"name":"echo","description":"echo words","parameters":{"type":"object","properties":{"words":{"type":"string"}}},"responses":{"type":"object","properties":{"words":{"type":"string"}}},"examples":[[{"role":"user","content":"echo: words"},{"role":"assistant","function_call":{"name":"echo","arguments":"{\n    \"words\": \"hello, world\"\n}\n","thoughts":"当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息"}}]]}]}
2024-03-10 17:58:42 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-tj5rhmj89g","object":"chat.completion","created":1710064722,"result":"您好，您输入的消息是：HELLO WORLD!，我已原样返回。请问有其他需要吗？","is_truncated":false,"need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":225,"completion_tokens":21,"total_tokens":246}}
您好，您输入的消息是：HELLO WORLD!，我已原样返回。请问有其他需要吗？
```

### 多函数调用示例

`qianfan4j`会根据LLM的推理能力，自动拆解多函数调用的任务，然后按照拆解的任务顺序依次调用函数。这样，你就可以专注于函数的实现，而不用再去关心函数的调用顺序了。
我们假设有两个函数 [QueryScoreFunction](https://github.com/oldmanpushcart/qianfan4j/blob/main/src/test/java/io/github/oldmanpushcart/test/qianfan4j/chat/function/QueryScoreFunction.java)和 [ComputeAvgScoreFunction](https://github.com/oldmanpushcart/qianfan4j/blob/main/src/test/java/io/github/oldmanpushcart/test/qianfan4j/chat/function/ComputeAvgScoreFunction.java)，分别用于查询成绩和计算平均分。我们可以通过以下方式实现多函数调用：

```java
final var request = ChatRequest.newBuilder()
        .model(ChatModel.ERNIE_V4)
        .functions(new QueryScoreFunction(), new ComputeAvgScoreFunction())
        .option(ChatOptions.IS_STREAM, true)
        .option(ChatOptions.IS_ENABLE_SEARCH, false)
        .option(ChatOptions.TEMPERATURE, 0.01f)
        .messages(Message.ofUser("计算李四的语文和数学平均分"))
        .build();

final var response = client.chat(request)
        .async()
        .join();

// System.out.println(response.content());
```

输出结果

```text
2024-03-10 18:02:44 DEBUG qianfan://token/refresh success! expired=1712656964044;
2024-03-10 18:02:44 DEBUG qianfan://chat/ernie-v4.0 => {"messages":[{"role":"user","content":"计算李四的语文和数学平均分"}],"functions":[{"name":"query_score","description":"query student's scores","parameters":{"type":"object","properties":{"name":{"type":"string","description":"the student name to query, example: \"张三\""},"subjects":{"type":"array","description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}}},"required":["name","subjects"]},"responses":{"type":"object","properties":{"message":{"type":"string","description":"message"},"data":{"type":"array","description":"data","items":{"type":"object","properties":{"name":{"type":"string","description":"student name"},"subject":{"type":"string","description":"subject items","enum":["CHINESE","MATH","ENGLISH"]},"value":{"type":"number","description":"score value"}}}},"success":{"type":"boolean","description":"success or not"}}},"examples":[[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\n     \"name\": \"张三\",\n     \"subjects\": [\n         \"MATH\"\n     ]\n }\n","thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩"}}],[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\n     \"name\": \"李四\",\n     \"subjects\": [\n         \"MATH\",\n         \"CHINESE\"\n     ]\n }\n","thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩"}}]]},{"name":"compute_avg_score","description":"计算平均成绩","parameters":{"type":"object","properties":{"scores":{"type":"array","description":"分数集合","items":{"type":"number"}}}},"responses":{"type":"object","properties":{"avg_score":{"type":"number","description":"平均分"}}},"examples":[[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         20,\n         100\n     ]\n }\n","thoughts":"我应该将张三的所有分数传入，计算张三的平均分"}}],[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         90\n     ]\n }\n","thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩"}}]]}],"stream":true,"temperature":0.01,"disable_search":true}
2024-03-10 18:02:47 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-gmjf3vyemg","object":"chat.completion","created":1710064967,"sentence_id":0,"is_end":true,"is_truncated":false,"result":"","need_clear_history":false,"function_call":{"name":"query_score","thoughts":"我需要先查询李四的语文和数学成绩，然后计算平均分。任务拆解：[sub-task1: 使用[query_score]工具查询李四的语文和数学成绩，sub-task2: 使用[compute_avg_score]工具计算平均分]。接下来需要调用[query_score]工具来查询李四的语文和数学成绩。","arguments":"{\"name\":\"李四\",\"subjects\":[\"CHINESE\",\"MATH\"]}"},"finish_reason":"function_call","usage":{"prompt_tokens":676,"completion_tokens":92,"total_tokens":768}}
2024-03-10 18:02:47 DEBUG qianfan://chat/ernie-v4.0/function <= {"name":"李四","subjects":["CHINESE","MATH"]}
2024-03-10 18:02:47 DEBUG qianfan://chat/ernie-v4.0/function => {"message":"查询成功","data":[{"name":"李四","subject":"CHINESE","value":80.0},{"name":"李四","subject":"MATH","value":70.0}],"success":true}
2024-03-10 18:02:47 DEBUG qianfan://chat/ernie-v4.0 => {"messages":[{"role":"user","content":"计算李四的语文和数学平均分"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"CHINESE\",\"MATH\"]}","thoughts":"我需要先查询李四的语文和数学成绩，然后计算平均分。任务拆解：[sub-task1: 使用[query_score]工具查询李四的语文和数学成绩，sub-task2: 使用[compute_avg_score]工具计算平均分]。接下来需要调用[query_score]工具来查询李四的语文和数学成绩。"}},{"role":"function","content":"{\"message\":\"查询成功\",\"data\":[{\"name\":\"李四\",\"subject\":\"CHINESE\",\"value\":80.0},{\"name\":\"李四\",\"subject\":\"MATH\",\"value\":70.0}],\"success\":true}","name":"query_score"}],"functions":[{"name":"query_score","description":"query student's scores","parameters":{"type":"object","properties":{"name":{"type":"string","description":"the student name to query, example: \"张三\""},"subjects":{"type":"array","description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}}},"required":["name","subjects"]},"responses":{"type":"object","properties":{"message":{"type":"string","description":"message"},"data":{"type":"array","description":"data","items":{"type":"object","properties":{"name":{"type":"string","description":"student name"},"subject":{"type":"string","description":"subject items","enum":["CHINESE","MATH","ENGLISH"]},"value":{"type":"number","description":"score value"}}}},"success":{"type":"boolean","description":"success or not"}}},"examples":[[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\n     \"name\": \"张三\",\n     \"subjects\": [\n         \"MATH\"\n     ]\n }\n","thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩"}}],[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\n     \"name\": \"李四\",\n     \"subjects\": [\n         \"MATH\",\n         \"CHINESE\"\n     ]\n }\n","thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩"}}]]},{"name":"query_score","description":"query student's scores","parameters":{"type":"object","properties":{"name":{"type":"string","description":"the student name to query, example: \"张三\""},"subjects":{"type":"array","description":"the subjects to query, example: [\"MATH\", \"CHINESE\"]","items":{"type":"string","enum":["CHINESE","MATH","ENGLISH"]}}},"required":["name","subjects"]},"responses":{"type":"object","properties":{"message":{"type":"string","description":"message"},"data":{"type":"array","description":"data","items":{"type":"object","properties":{"name":{"type":"string","description":"student name"},"subject":{"type":"string","description":"subject items","enum":["CHINESE","MATH","ENGLISH"]},"value":{"type":"number","description":"score value"}}}},"success":{"type":"boolean","description":"success or not"}}},"examples":[[{"role":"user","content":"查询张三、李四的数学成绩"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\n     \"name\": \"张三\",\n     \"subjects\": [\n         \"MATH\"\n     ]\n }\n","thoughts":"用户需要查询张三、李四、王五的数学成绩，但函数一次只能查询一个学生，所以我们先查询张三的成绩，然后再分别查询李四和王五的数学成绩"}}],[{"role":"user","content":"查询李四的数学和语文成绩"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\n     \"name\": \"李四\",\n     \"subjects\": [\n         \"MATH\",\n         \"CHINESE\"\n     ]\n }\n","thoughts":"用户需要查询李四的数学和语文成绩，函数一次可以查询一个学生的多个成绩"}}]]}],"stream":true,"temperature":0.01,"disable_search":true}
2024-03-10 18:02:48 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-mghmq7fq8e","object":"chat.completion","created":1710064968,"sentence_id":0,"is_end":false,"is_truncated":false,"result":"李四","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":812,"completion_tokens":0,"total_tokens":812}}
2024-03-10 18:02:49 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-mghmq7fq8e","object":"chat.completion","created":1710064969,"sentence_id":1,"is_end":false,"is_truncated":false,"result":"的语文成绩是80分，数学成绩是70分。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":812,"completion_tokens":0,"total_tokens":812}}
2024-03-10 18:02:49 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-mghmq7fq8e","object":"chat.completion","created":1710064970,"sentence_id":2,"is_end":false,"is_truncated":false,"result":"他的平均分是75分。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":812,"completion_tokens":0,"total_tokens":812}}
2024-03-10 18:02:50 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-mghmq7fq8e","object":"chat.completion","created":1710064971,"sentence_id":3,"is_end":false,"is_truncated":false,"result":"如果您需要更详细的信息或有其他问题，请随时告诉我。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":812,"completion_tokens":0,"total_tokens":812}}
2024-03-10 18:02:50 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-mghmq7fq8e","object":"chat.completion","created":1710064971,"sentence_id":4,"is_end":true,"is_truncated":false,"result":"","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":812,"completion_tokens":36,"total_tokens":848}}
2024-03-10 18:02:50 DEBUG qianfan://chat/ernie-v4.0 => {"messages":[{"role":"user","content":"计算李四的语文和数学平均分"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"CHINESE\",\"MATH\"]}","thoughts":"我需要先查询李四的语文和数学成绩，然后计算平均分。任务拆解：[sub-task1: 使用[query_score]工具查询李四的语文和数学成绩，sub-task2: 使用[compute_avg_score]工具计算平均分]。接下来需要调用[query_score]工具来查询李四的语文和数学成绩。"}},{"role":"function","content":"{\"message\":\"查询成功\",\"data\":[{\"name\":\"李四\",\"subject\":\"CHINESE\",\"value\":80.0},{\"name\":\"李四\",\"subject\":\"MATH\",\"value\":70.0}],\"success\":true}","name":"query_score"},{"role":"assistant","content":"李四的语文成绩是80分，数学成绩是70分。他的平均分是75分。如果您需要更详细的信息或有其他问题，请随时告诉我。"},{"role":"user","content":" 使用[compute_avg_score]工具计算平均分"}],"functions":[{"name":"compute_avg_score","description":"计算平均成绩","parameters":{"type":"object","properties":{"scores":{"type":"array","description":"分数集合","items":{"type":"number"}}}},"responses":{"type":"object","properties":{"avg_score":{"type":"number","description":"平均分"}}},"examples":[[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         20,\n         100\n     ]\n }\n","thoughts":"我应该将张三的所有分数传入，计算张三的平均分"}}],[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         90\n     ]\n }\n","thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩"}}]]},{"name":"compute_avg_score","description":"计算平均成绩","parameters":{"type":"object","properties":{"scores":{"type":"array","description":"分数集合","items":{"type":"number"}}}},"responses":{"type":"object","properties":{"avg_score":{"type":"number","description":"平均分"}}},"examples":[[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         20,\n         100\n     ]\n }\n","thoughts":"我应该将张三的所有分数传入，计算张三的平均分"}}],[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         90\n     ]\n }\n","thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩"}}]]}],"stream":true,"temperature":0.01,"disable_search":true}
2024-03-10 18:02:53 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-y42i2td7tk","object":"chat.completion","created":1710064973,"sentence_id":0,"is_end":true,"is_truncated":false,"result":"","need_clear_history":false,"function_call":{"name":"compute_avg_score","thoughts":"我需要调用[compute_avg_score]工具来计算李四的平均分。","arguments":"{\"scores\":[80,70]}"},"finish_reason":"function_call","usage":{"prompt_tokens":680,"completion_tokens":30,"total_tokens":710}}
2024-03-10 18:02:53 DEBUG qianfan://chat/ernie-v4.0/function <= {"scores":[80,70]}
2024-03-10 18:02:53 DEBUG qianfan://chat/ernie-v4.0/function => {"avg_score":75.0}
2024-03-10 18:02:53 DEBUG qianfan://chat/ernie-v4.0 => {"messages":[{"role":"user","content":"计算李四的语文和数学平均分"},{"role":"assistant","function_call":{"name":"query_score","arguments":"{\"name\":\"李四\",\"subjects\":[\"CHINESE\",\"MATH\"]}","thoughts":"我需要先查询李四的语文和数学成绩，然后计算平均分。任务拆解：[sub-task1: 使用[query_score]工具查询李四的语文和数学成绩，sub-task2: 使用[compute_avg_score]工具计算平均分]。接下来需要调用[query_score]工具来查询李四的语文和数学成绩。"}},{"role":"function","content":"{\"message\":\"查询成功\",\"data\":[{\"name\":\"李四\",\"subject\":\"CHINESE\",\"value\":80.0},{\"name\":\"李四\",\"subject\":\"MATH\",\"value\":70.0}],\"success\":true}","name":"query_score"},{"role":"assistant","content":"李四的语文成绩是80分，数学成绩是70分。他的平均分是75分。如果您需要更详细的信息或有其他问题，请随时告诉我。"},{"role":"user","content":" 使用[compute_avg_score]工具计算平均分"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\"scores\":[80,70]}","thoughts":"我需要调用[compute_avg_score]工具来计算李四的平均分。"}},{"role":"function","content":"{\"avg_score\":75.0}","name":"compute_avg_score"}],"functions":[{"name":"compute_avg_score","description":"计算平均成绩","parameters":{"type":"object","properties":{"scores":{"type":"array","description":"分数集合","items":{"type":"number"}}}},"responses":{"type":"object","properties":{"avg_score":{"type":"number","description":"平均分"}}},"examples":[[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         20,\n         100\n     ]\n }\n","thoughts":"我应该将张三的所有分数传入，计算张三的平均分"}}],[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         90\n     ]\n }\n","thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩"}}]]},{"name":"compute_avg_score","description":"计算平均成绩","parameters":{"type":"object","properties":{"scores":{"type":"array","description":"分数集合","items":{"type":"number"}}}},"responses":{"type":"object","properties":{"avg_score":{"type":"number","description":"平均分"}}},"examples":[[{"role":"user","content":"张三的语文30分、数学20分、英语100分；\n李四的语文50分、数学90分、英语60分；\n计算张三的平均成绩\n"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         20,\n         100\n     ]\n }\n","thoughts":"我应该将张三的所有分数传入，计算张三的平均分"}}],[{"role":"user","content":"张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩"},{"role":"assistant","function_call":{"name":"compute_avg_score","arguments":"{\n     \"scores\": [\n         30,\n         90\n     ]\n }\n","thoughts":"我应该把所有人的语文分数传入，从而计算出语文的平均成绩"}}]]}],"stream":true,"temperature":0.01,"disable_search":true}
2024-03-10 18:02:53 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-gtbgh1agd9","object":"chat.completion","created":1710064974,"sentence_id":0,"is_end":false,"is_truncated":false,"result":"根据您的要求","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":689,"completion_tokens":0,"total_tokens":689}}
2024-03-10 18:02:55 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-gtbgh1agd9","object":"chat.completion","created":1710064975,"sentence_id":1,"is_end":false,"is_truncated":false,"result":"，我已经使用[compute_avg_score]工具计算了李四的平均分。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":689,"completion_tokens":0,"total_tokens":689}}
2024-03-10 18:02:55 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-gtbgh1agd9","object":"chat.completion","created":1710064976,"sentence_id":2,"is_end":false,"is_truncated":false,"result":"他的平均分是75.0分。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":689,"completion_tokens":29,"total_tokens":718}}
2024-03-10 18:02:56 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-gtbgh1agd9","object":"chat.completion","created":1710064977,"sentence_id":3,"is_end":false,"is_truncated":false,"result":"如果您还有其他问题或需要更详细的信息，请随时告诉我。","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":689,"completion_tokens":29,"total_tokens":718}}
2024-03-10 18:02:56 DEBUG qianfan://chat/ernie-v4.0 <= {"id":"as-gtbgh1agd9","object":"chat.completion","created":1710064977,"sentence_id":4,"is_end":true,"is_truncated":false,"result":"","need_clear_history":false,"finish_reason":"normal","usage":{"prompt_tokens":689,"completion_tokens":42,"total_tokens":731}}
根据您的要求，我已经使用[compute_avg_score]工具计算了李四的平均分。他的平均分是75.0分。如果您还有其他问题或需要更详细的信息，请随时告诉我。
```

在一次对话中，用户首先询问了张三的语文成绩，然后询问了李四的语文成绩，最后询问了王五的语文成绩。在这个过程中，`qianfan4j`根据用户的需求，
使用了`query_score`工具来查询李四的语文和数学成绩，分别是80分和70分，
然后使用`compute_avg_score`工具来计算他们的语文平均分。最终，助手返回了李四的语文和数学平均分为75分。

### 文生图示例

`qianfan4j`会将文心一言返回的BASE64编码封装为`BufferedImage`类型，方便开发者进行后续的图像处理。
下面是一个简单的示例，展示了如何使用`qianfan4j`进行图像处理：

```java
final var request = GenerationImageRequest.newBuilder()
    .model(GenerationImageModel.STABLE_DIFFUSION_XL)
    .prompt("猫")
    .negative("白色")
    .option(GenerationImageOptions.NUMBERS, 2)
    .option(GenerationImageOptions.SIZE, GenerationImageRequest.Size.S_1024_1024)
    .build();

final var response = client.generationImage(request)
    .async()
    .join();
```

然后你就可以通过`response.images().get(0)`拿到生成的图片的`BufferedImage`类型进行后续操作了。

![文生图-猫](https://ompc-images.oss-cn-hangzhou.aliyuncs.com/erniebot4j/gen-image-as-mr0hyfmsix-001.png)

## 五、参与贡献

如果你对`qianfan4j`感兴趣并希望为其做出贡献，请遵循以下步骤：

1. Fork本项目到你的GitHub账户。
2. 克隆项目到你的本地环境。
3. 创建一个新的分支用于你的修改。
4. 提交你的更改并通过`Pull Request`请求合并到主分支。

在提交Pull Request之前，请确保你的代码符合项目的编码规范和最佳实践，并且已经通过了相关的测试。

## 六、特别致谢

首先，我要向百度千帆大模型团队的同学们表达我最深切地感谢。正是他们不懈的努力和卓越的工作成果，使得我们能够如此便捷地利用千帆的API使用文心一言在内的LLM大模型进行开发。
他们为整个开发者社区树立了榜样，推动了技术的进步。

### 关于文心一言

作为个人使用者，我对文心一言这个产品怀有极高地评价。相较于OpenAi的GPT-4，虽然在某些功能上还有待完善，但文心一言在稳定性方面展现出了显著的优势。
在实际应用中，它的可靠和稳定让我倍感信赖，这也是我选择它作为开发基础的重要原因之一。

同时我也希望在多模态的时代，千帆大模型平台和文心一言不要落后。

### 缘起与动机

当我得知千帆大模型发布了SDK时，我迫不及待地想要集成到我的项目中。然而，我遗憾地发现他们的SDK当时并不支持Java。
作为一个Java开发者，我深知Java在开发者社区中的普及程度和重要性。因此，我决定自己动手，填补这一空白，为Java开发者提供一个方便、易用的文心一言客户端。

正是在这样的背景下，我发起了`qianfan4j`项目。它旨在成为文心一言的Java开发者最佳伴侣，提供简洁明了的API接口，帮助开发者快速集成和使用文心一言的功能。
通过`qianfan4j`，Java开发者可以轻松地实现对话、续写、向量嵌入和图像处理等功能，极大地提升了开发效率和用户体验。

### 展望与呼吁

展望未来，我希望`qianfan4j`能够成为Java开发者与千帆大模型平台之间的桥梁，推动千帆和文心一言在更多领域的应用和发展。
同时，我也呼吁更多的开发者加入到`qianfan4j`的开源社区中来，共同完善和优化这个项目，让它更好地服务于整个开发者社区。

## 七、相关链接

- [千帆大模型平台](https://console.bce.baidu.com/qianfan/overview)