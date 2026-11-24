# Spring Boot Multi-Model AI POC

A Proof of Concept (POC) demonstrating **multi-model AI integration using Spring Boot and Spring AI**, supporting **OpenAI APIs and locally hosted Ollama (Llama 3.2:1b)**, featuring RESTful APIs, dedicated AI model configurations, prompt handling, and AI-generated responses.

This project demonstrates how to integrate cloud-based and locally hosted Large Language Models (LLMs) into a single Spring Boot application using Spring AI's `ChatClient` abstraction.

The application provides **separate REST API endpoints for OpenAI and Ollama**, allowing clients to choose the AI provider through the requested endpoint.

## 🚀 Features

- **Multi-Model AI Integration:** Integrates OpenAI and Ollama within one Spring Boot application.
- **OpenAI Integration:** Communicates with OpenAI's cloud-hosted language models.
- **Ollama Integration:** Connects to locally hosted Llama 3.2:1b using Ollama.
- **Spring AI Framework:** Uses Spring AI's `ChatClient` abstraction for LLM communication.
- **Dedicated ChatClient Beans:** Configures separate Spring-managed `ChatClient` instances for OpenAI and Ollama.
- **Provider-Specific REST APIs:** Exposes independent endpoints for each AI provider.
- **Prompt Handling:** Accepts prompts through HTTP request parameters.
- **AI-Generated Responses:** Returns generated natural-language responses.
- **Service-Layer Architecture:** Separates REST controller, service, and AI configuration responsibilities.
- **Extensible Design:** Provides a foundation for additional AI models and providers.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 17+ | Backend development |
| Spring Boot | Application framework |
| Spring AI | AI integration framework |
| OpenAI API | Cloud-hosted LLM integration |
| Ollama | Local LLM runtime |
| Llama 3.2:1b | Locally hosted language model |
| Spring Web MVC | RESTful API development |
| Maven | Dependency and build management |
| Postman / cURL | REST API testing |

## 🏗️ Application Architecture

The application follows a layered architecture with separate AI client configurations for OpenAI and Ollama.

```text
                    Client / Postman
                           |
                           | HTTP GET
                           v
                Spring Boot Application
                      Port: 9292
                           |
                           v
              AIMutiModelChatController
                           |
              +------------+------------+
              |                         |
              v                         v
    /multi-model/chat/openai  /multi-model/chat/ollama
              |                         |
              +------------+------------+
                           |
                           v
                AIMutiModelChatService
                           |
              +------------+------------+
              |                         |
              v                         v
       openAIChatClient          ollamaChatClient
              |                         |
              v                         v
        OpenAiChatModel          OllamaChatModel
              |                         |
              v                         v
          OpenAI API             Ollama Server
          (Cloud)               localhost:11434
              |                         |
              v                         v
        OpenAI Model              Llama 3.2:1b
              |                         |
              +------------+------------+
                           |
                           v
                     AI Response
                           |
                           v
                       REST Client
```

### Request Processing Flow

1. The client sends an HTTP GET request with a `message` parameter.
2. `AIMutiModelChatController` receives the request.
3. The controller invokes the corresponding method in `AIMutiModelChatService`.
4. The service selects the provider-specific `ChatClient`.
5. Spring AI sends the prompt through the corresponding `ChatModel`.
6. OpenAI or Ollama processes the prompt and generates a response.
7. The generated response is returned to the client.

## 📋 Prerequisites

Ensure the following tools are installed or available:

- JDK 17 or higher, compatible with the selected Spring Boot version
- Apache Maven
- IntelliJ IDEA or Spring Tool Suite
- Ollama installed locally
- Llama 3.2:1b model downloaded through Ollama
- Valid OpenAI API key
- Postman or cURL
- Internet connectivity for OpenAI requests and initial model downloads

## ⚙️ Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/springboot-multimodel-poc.git

cd springboot-multimodel-poc
```

Replace `<your-username>` with your GitHub username.

### 2. Install Ollama

Download and install Ollama from:

https://ollama.com/download

Verify the installation:

```bash
ollama --version
```

### 3. Download Llama 3.2:1b

Pull the Llama 3.2:1b model:

```bash
ollama pull llama3.2:1b
```

Verify the downloaded model:

```bash
ollama list
```

Test the model:

```bash
ollama run llama3.2:1b
```

Example prompt:

```text
Explain Spring Boot in simple terms.
```

Exit the interactive session:

```text
/bye
```

### 4. Start the Ollama Server

Ensure the Ollama service is running.

If it is not already running as a background service, start it using:

```bash
ollama serve
```

The default Ollama API endpoint is:

```text
http://localhost:11434
```

Verify that the server is accessible:

```bash
curl http://localhost:11434/api/tags
```

### 5. Configure the OpenAI API Key

Generate an API key from:

https://platform.openai.com/api-keys

Set the API key as an environment variable.

**macOS / Linux**

```bash
export OPENAI_API_KEY="your-openai-api-key"
```

**Windows PowerShell**

```powershell
$env:OPENAI_API_KEY="your-openai-api-key"
```

**Security Note:** Never hardcode API keys in application source files or commit them to GitHub.

### 6. Maven Dependencies

The application uses Spring Web and Spring AI integrations for OpenAI and Ollama.

Example Maven dependencies:

```xml
<!-- Spring Boot Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring AI OpenAI -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>

<!-- Spring AI Ollama -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
```

Use a compatible Spring AI BOM in `pom.xml` to manage Spring AI dependency versions. Ensure both `OpenAiChatModel` and `OllamaChatModel` are available as Spring beans.

### 7. Application Configuration

The application uses `application.yml` to configure the AI providers and server port.

**File:** `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: springboot-multimodel-poc

  ai:
    ollama:
      base-url: http://localhost:11434/
      chat:
        model: llama3.2:1b

    openai:
      api-key: ${OPENAI_API_KEY}

server:
  port: 9292
```

### Configuration Details

| Property | Description |
|---|---|
| `spring.application.name` | Spring Boot application name |
| `spring.ai.ollama.base-url` | Local Ollama server URL |
| `spring.ai.ollama.chat.model` | Ollama language model |
| `spring.ai.openai.api-key` | OpenAI API key from environment |
| `server.port` | Application HTTP server port |

**Note:** The OpenAI model is not explicitly specified in the supplied configuration. Spring AI will use its configured default unless a model is specified elsewhere. The default may vary by Spring AI version.

## 💻 Implementation

The application contains three main components:

1. `AIChatConfiguration` — Configures the OpenAI and Ollama `ChatClient` beans.
2. `AIMutiModelChatController` — Exposes REST endpoints for both AI providers.
3. `AIMutiModelChatService` — Processes prompts and invokes the corresponding AI client.

### 1. AI Chat Configuration

**Class:** `AIChatConfiguration.java`

```java
@Configuration
public class AIChatConfiguration {

    @Bean
    public ChatClient openAIChatClient(
            OpenAiChatModel openAiChatModel) {

        return ChatClient.builder(openAiChatModel)
                .build();
    }

    @Bean
    public ChatClient ollamaChatClient(
            OllamaChatModel ollamaChatModel) {

        return ChatClient.builder(ollamaChatModel)
                .build();
    }
}
```

#### Explanation

The configuration creates two separate Spring-managed `ChatClient` beans.

**OpenAI ChatClient**

```java
@Bean
public ChatClient openAIChatClient(
        OpenAiChatModel openAiChatModel) {

    return ChatClient.builder(openAiChatModel)
            .build();
}
```

- Uses `OpenAiChatModel` for OpenAI API integration.
- Creates a dedicated `ChatClient` named `openAIChatClient`.
- Supports prompt execution using the configured OpenAI model.

**Ollama ChatClient**

```java
@Bean
public ChatClient ollamaChatClient(
        OllamaChatModel ollamaChatModel) {

    return ChatClient.builder(ollamaChatModel)
            .build();
}
```

- Uses `OllamaChatModel` for local LLM integration.
- Creates a dedicated `ChatClient` named `ollamaChatClient`.
- Communicates with the configured local Llama 3.2:1b model.

This configuration enables both providers to be accessed through independent Spring AI clients.

### 2. Multi-Model Chat Controller

**Class:** `AIMutiModelChatController.java`

```java
@RestController
@RequestMapping("/multi-model/chat")
public class AIMutiModelChatController {

    @Autowired
    private AIMutiModelChatService service;

    @GetMapping("/openai")
    public String chatWithOpenAILLM(
            @RequestParam String message) {

        return service.chatWithOpenAI(message);
    }

    @GetMapping("/ollama")
    public String chatWithOllamaLLM(
            @RequestParam String message) {

        return service.chatWithOllama(message);
    }
}
```

#### Explanation

The controller exposes two independent REST endpoints.

| HTTP Method | Endpoint | AI Provider |
|---|---|---|
| GET | `/multi-model/chat/openai` | OpenAI |
| GET | `/multi-model/chat/ollama` | Ollama |

**OpenAI Endpoint**

```java
@GetMapping("/openai")
public String chatWithOpenAILLM(
        @RequestParam String message) {

    return service.chatWithOpenAI(message);
}
```

Accepts a prompt and forwards it to the OpenAI service method.

**Ollama Endpoint**

```java
@GetMapping("/ollama")
public String chatWithOllamaLLM(
        @RequestParam String message) {

    return service.chatWithOllama(message);
}
```

Accepts a prompt and forwards it to the locally hosted Ollama model.

### 3. Multi-Model Chat Service

**Class:** `AIMutiModelChatService.java`

```java
@Service
public class AIMutiModelChatService {

    private final ChatClient openAIChatClient;
    private final ChatClient ollamaChatClient;

    public AIMutiModelChatService(
            ChatClient openAIChatClient,
            ChatClient ollamaChatClient) {

        this.openAIChatClient = openAIChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    public String chatWithOpenAI(String message) {

        return openAIChatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String chatWithOllama(String message) {

        return ollamaChatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
```

#### Explanation

The service layer contains the application logic for interacting with both LLM providers.

**OpenAI Integration**

```java
public String chatWithOpenAI(String message) {

    return openAIChatClient.prompt()
            .user(message)
            .call()
            .content();
}
```

This method:

- Creates a prompt request through the OpenAI `ChatClient`.
- Passes the user message to the OpenAI model.
- Executes the model request.
- Returns the generated response.

**Ollama Integration**

```java
public String chatWithOllama(String message) {

    return ollamaChatClient.prompt()
            .user(message)
            .call()
            .content();
}
```

This method:

- Creates a prompt request using the Ollama `ChatClient`.
- Sends the user message to Llama 3.2:1b.
- Executes local model inference.
- Returns the generated response.

### Recommended Improvement: Explicit Bean Qualification

Since the application defines two beans of type `ChatClient`, explicitly qualifying their injection is recommended.

The service constructor can be updated as follows:

```java
public AIMutiModelChatService(
        @Qualifier("openAIChatClient")
        ChatClient openAIChatClient,

        @Qualifier("ollamaChatClient")
        ChatClient ollamaChatClient) {

    this.openAIChatClient = openAIChatClient;
    this.ollamaChatClient = ollamaChatClient;
}
```

This removes dependency injection ambiguity and ensures that each service field receives the correct AI client.

Without explicit qualifiers, the existing constructor can rely on parameter-name matching, which requires suitable compiler metadata and Spring dependency resolution.

## 🔌 REST API Documentation

The application exposes two GET endpoints on port `9292`.

### 1. Chat with OpenAI

<text>**Endpoint**</text>

```http
GET http://localhost:9292/multi-model/chat/openai
```

**Query Parameter**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `message` | String | Yes | Prompt sent to OpenAI |

**Example cURL Request**

```bash
curl -G "http://localhost:9292/multi-model/chat/openai" \
  --data-urlencode "message=Explain Spring Boot"
```

**Example Response**

```text
Spring Boot is a Java framework designed to simplify
the development of standalone, production-ready
applications. It provides auto-configuration,
embedded servers, and starter dependencies.
```

### 2. Chat with Ollama

**Endpoint**

```http
GET http://localhost:9292/multi-model/chat/ollama
```

**Query Parameter**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `message` | String | Yes | Prompt sent to Llama 3.2:1b |

**Example cURL Request**

```bash
curl -G "http://localhost:9292/multi-model/chat/ollama" \
  --data-urlencode "message=Explain Spring Boot"
```

**Example Response**

```text
Spring Boot is a Java framework that makes it
easier to build and run applications by reducing
manual configuration and providing built-in tools.
```

The above responses are illustrative. Actual output varies by model and prompt.

## 🔄 OpenAI vs Ollama

| Feature | OpenAI | Ollama |
|---|---|---|
| Deployment | Cloud | Local |
| AI Model | Configured OpenAI model | Llama 3.2:1b |
| Internet Required | Yes | No, after initial setup |
| API Key | Required | Not required locally |
| Inference | Remote | Local |
| Hardware | Cloud-managed | Local CPU/GPU |
| Cost | API usage-based | Local compute costs |
| Endpoint | `/openai` | `/ollama` |
| Spring AI Client | `openAIChatClient` | `ollamaChatClient` |
| Chat Model | `OpenAiChatModel` | `OllamaChatModel` |

## 📁 Project Structure

The following is an example structure based on the provided Java classes and configuration.

```text
springboot-multimodel-poc/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/multimodel/
│   │   │       │
│   │   │       ├── config/
│   │   │       │   └── AIChatConfiguration.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   └── AIMutiModelChatController.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   └── AIMutiModelChatService.java
│   │   │       │
│   │   │       └── SpringbootMultimodelPocApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.yml
│   │
│   └── test/
│       └── java/
│
├── .gitignore
├── pom.xml
└── README.md
```

The package names and application entry-point class are illustrative and should be updated to match the repository.

## ▶️ Build and Run

### Step 1: Verify Ollama

```bash
ollama list
```

Ensure that `llama3.2:1b` appears in the installed model list.

### Step 2: Verify OpenAI Configuration

Ensure that the `OPENAI_API_KEY` environment variable is available to the application.

### Step 3: Build the Project

```bash
mvn clean install
```

### Step 4: Start Spring Boot

```bash
mvn spring-boot:run
```

### Step 5: Access the Application

```text
http://localhost:9292
```

### Step 6: Test OpenAI

```bash
curl -G "http://localhost:9292/multi-model/chat/openai" \
  --data-urlencode "message=What is Spring AI?"
```

### Step 7: Test Ollama

```bash
curl -G "http://localhost:9292/multi-model/chat/ollama" \
  --data-urlencode "message=What is Spring AI?"
```

## 🧪 Testing Scenarios

The following test scenarios can be used to validate the application.

| Test Scenario | Expected Behavior |
|---|---|
| Valid OpenAI prompt | OpenAI-generated response |
| Valid Ollama prompt | Llama 3.2:1b-generated response |
| Missing `message` parameter | HTTP 400 Bad Request |
| Ollama server unavailable | Model request fails |
| Invalid OpenAI API key | Authentication error from OpenAI |
| Configured Ollama model missing | Model invocation error |
| Multiple `ChatClient` beans | Correct clients injected when explicitly qualified |

These are expected scenarios rather than a claim that automated tests have already been implemented.

## 🔐 Security Best Practices

- Store API keys using environment variables or a secrets manager.
- Never expose API credentials in GitHub repositories.
- Restrict direct access to the Ollama API.
- Avoid exposing development AI endpoints publicly without authentication.
- Validate prompts and enforce request-size limits.
- Add appropriate API rate limiting.
- Configure connection and response timeouts.
- Avoid logging sensitive prompts or model outputs.
- Monitor OpenAI token consumption and API costs.

For production applications, using POST requests with JSON request bodies is generally preferable to placing potentially sensitive prompts in GET query parameters, which may be recorded in URLs and access logs.

## ⚡ Performance Considerations

### OpenAI

OpenAI response time depends on factors including:

- Selected model
- Network latency
- Prompt complexity
- Output token generation
- API usage limits

### Ollama

Ollama performance depends on:

- Local CPU and GPU availability
- System memory
- Model size
- Prompt and context length
- Concurrent requests
- Model loading time

Llama 3.2:1b is useful for lightweight local AI experimentation, though its capabilities and output quality may differ from larger models.

## 🛠️ Troubleshooting

### 1. Ollama Connection Refused

Verify the Ollama server is running:

```bash
curl http://localhost:11434/api/tags
```

If required, start Ollama:

```bash
ollama serve
```

### 2. Llama 3.2:1b Model Not Found

Install the configured model:

```bash
ollama pull llama3.2:1b
```

Verify:

```bash
ollama list
```

### 3. OpenAI Authentication Error

Ensure that the environment variable exists:

```bash
echo $OPENAI_API_KEY
```

Avoid displaying or sharing actual API key values in logs, screenshots, or support requests.

### 4. ChatClient Bean Ambiguity

The application registers two `ChatClient` beans.

Use `@Qualifier` when injecting these beans into the service:

```java
@Qualifier("openAIChatClient")
ChatClient openAIChatClient
```

```java
@Qualifier("ollamaChatClient")
ChatClient ollamaChatClient
```

### 5. Ollama or OpenAI ChatModel Bean Not Available

Verify the following:

- The matching Spring AI starter dependency is included.
- The Spring AI version supports the configured properties.
- Both provider-specific `ChatModel` beans are initialized.
- Provider auto-configuration is compatible with the multi-model setup.

If automatic initialization does not create both models, configure them explicitly using the APIs supported by your Spring AI version.

## 🔮 Future Enhancements

Potential enhancements include:

- **Dynamic Provider Selection:** Introduce a single endpoint accepting the selected provider.
- **Additional Models:** Support more OpenAI and Ollama models.
- **Streaming Responses:** Use Spring AI streaming APIs to return incremental output.
- **Conversation Memory:** Maintain multi-turn chat context.
- **Retrieval-Augmented Generation (RAG):** Use external knowledge sources with LLMs.
- **Model Comparison:** Compare results from multiple AI providers for the same prompt.
- **Automatic Model Fallback:** Switch providers when a selected model becomes unavailable.
- **Structured Responses:** Generate typed JSON outputs.
- **Global Exception Handling:** Provide consistent API error responses.
- **Spring Security:** Secure AI endpoints using JWT or OAuth2.
- **Docker Integration:** Containerize the Spring Boot application and Ollama.
- **Observability:** Monitor AI request latency, errors, and token usage.
- **Unit and Integration Testing:** Add tests for controller, service, and provider integration.
- **API Documentation:** Integrate OpenAPI/Swagger UI.

## 🎯 Learning Objectives

This POC demonstrates:

1. Integrating cloud-based and locally hosted LLMs into Spring Boot.
2. Using Spring AI's `ChatClient` abstraction.
3. Configuring separate clients for OpenAI and Ollama.
4. Implementing provider-specific REST API endpoints.
5. Using Spring dependency injection with multiple beans of the same type.
6. Separating REST, service, and AI configuration layers.
7. Handling user prompts and generating AI responses.
8. Exploring a foundation for multi-model enterprise AI applications.

## 📚 References

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring AI ChatClient API](https://docs.spring.io/spring-ai/reference/api/chatclient.html)
- [Spring AI OpenAI Chat](https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html)
- [Spring AI Ollama Chat](https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html)
- [OpenAI Developer Platform](https://platform.openai.com/)
- [Ollama Official Website](https://ollama.com/)
- [Ollama Model Library](https://ollama.com/library)
- [Ollama GitHub Repository](https://github.com/ollama/ollama)

## 📄 License

This project is intended for educational and proof-of-concept purposes.

No open-source license is implied unless a LICENSE file is explicitly added.

---

**Spring Boot Multi-Model AI POC** — Exploring multi-model Generative AI integration using Java, Spring Boot, Spring AI, OpenAI, and locally hosted Ollama (Llama 3.2:1b), with dedicated `ChatClient` configurations and provider-specific REST APIs.
