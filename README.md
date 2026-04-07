# Spring Alibaba AI 调用本地 Claude Skills 示例

## 项目说明

本项目演示如何使用 Spring Alibaba AI 框架调用本地 Claude 创建的技能（Skills）。

## 项目结构

```
src/main/java/com/example/skills/
├── SkillsApplication.java          # Spring Boot 主应用类
├── client/
│   ├── LocalClaudeSkills.java      # 定义本地技能（@Tool 注解）
│   └── AiChatService.java          # AI 聊天服务，集成技能调用
└── controller/
    └── SkillsController.java       # REST API 控制器
```

## 主要功能

### 1. 定义技能 (LocalClaudeSkills.java)

使用 `@Tool` 注解定义可以被 AI 调用的技能方法：

```java
@Service
public class LocalClaudeSkills {
    
    @Tool(description = "计算两个数字的和")
    public double add(double a, double b) {
        return a + b;
    }
    
    @Tool(description = "查询指定城市的天气信息")
    public String getWeather(String city) {
        // 实现天气查询逻辑
        return "天气信息...";
    }
}
```

### 2. 配置 AI 服务 (AiChatService.java)

将技能注册到 ChatClient 中：

```java
@Service
public class AiChatService {
    
    private final ChatClient chatClient;
    
    public AiChatService(ChatModel chatModel, LocalClaudeSkills localClaudeSkills) {
        // 创建工具回调提供者
        ToolCallbackProvider toolCallbackProvider = MethodToolCallbackProvider.builder()
                .toolObjects(localClaudeSkills)
                .build();
        
        // 构建带工具功能的 ChatClient
        this.chatClient = ChatClient.builder(chatModel)
                .defaultTools(toolCallbackProvider)
                .build();
    }
    
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
```

### 3. 提供 REST API (SkillsController.java)

```java
@RestController
@RequestMapping("/api/skills")
public class SkillsController {
    
    @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        return aiChatService.chat(message);
    }
}
```

## 使用方法

### 1. 配置 API Key

在 `application.yml` 或环境变量中设置：

```bash
export DASHSCOPE_API_KEY=your-api-key
```

### 2. 运行应用

```bash
mvn spring-boot:run
```

### 3. 调用 API

#### 聊天接口（AI 自动选择技能）

```bash
curl -X POST "http://localhost:8080/api/skills/chat?message=请帮我计算 25 加 37 等于多少"
```

#### 健康检查

```bash
curl http://localhost:8080/api/skills/health
```

## 连接本地 Claude

如果要连接本地部署的 Claude，需要：

1. **配置自定义 ChatModel**：创建配置类指向本地 Claude 服务地址

```java
@Configuration
public class ClaudeConfig {
    
    @Bean
    public ChatModel chatModel() {
        // 配置本地 Claude 的连接
        // 可以使用 RestTemplate 或 WebClient 调用本地 Claude API
        return new CustomClaudeChatModel("http://localhost:11434");
    }
}
```

2. **实现 HTTP 客户端调用本地 Claude**：

```java
@Service
public class LocalClaudeSkills {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CLAUDE_URL = "http://localhost:11434/api/generate";
    
    @Tool(description = "使用本地 Claude 进行文本分析")
    public String analyzeText(String text) {
        Map<String, Object> request = new HashMap<>();
        request.put("prompt", text);
        request.put("model", "claude");
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
            CLAUDE_URL, 
            request, 
            Map.class
        );
        
        return response.getBody().get("response").toString();
    }
}
```

## 依赖说明

- **Spring Boot 3.2.5**: 基础框架
- **Spring AI Alibaba 1.0.0-M3**: AI 集成框架
- **Spring AI Tool**: 工具/技能调用支持

## 注意事项

1. 确保本地 Claude 服务已启动并可访问
2. 根据实际部署地址修改配置中的 URL
3. 技能方法的参数和返回值需要是 JSON 可序列化的类型
4. 每个 `@Tool` 方法都应该有清晰的 description，帮助 AI 理解何时调用该技能

## 扩展建议

- 添加更多实用技能（文件处理、数据库查询等）
- 实现技能执行日志和监控
- 添加技能权限控制
- 支持技能组合和链式调用