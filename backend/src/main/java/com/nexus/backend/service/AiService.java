package com.nexus.backend.service;

import com.nexus.backend.dto.UserTender;
import com.nexus.backend.entity.Act;
import com.nexus.backend.entity.Tender;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

@Service
public class AiService {

    public void testGpt(){
        PromptTemplate promptTemplate = PromptTemplate
                .from("Tell me a {{adjective}} joke about {{content}}..");
        Map<String, Object> variables = new HashMap<>();
        variables.put("adjective", "funny");
        variables.put("content", "computers");
        Prompt prompt = promptTemplate.apply(variables);

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey("KEY").modelName(GPT_3_5_TURBO)
                .temperature(0.3)
                .build();
        String response = model.generate(prompt.text());
        System.out.println(response);
    }

    public String checkIfCompliant(Act act, UserTender userTender) {
        PromptTemplate promptTemplate = PromptTemplate
                .from("This is a government act with a set of compliances {{act}}, With keeping this above act in mind, tell me if my tender/plan seems broadly compliant or not. " +
                        "Consider this tender/plan: {{tender}}" +
                        "Let me know if there are any shortcomings and where the tender/plan is not compliant. Also tell me about penalties.");

        Map<String, Object> variables = new HashMap<>();
        variables.put("act", act);
        variables.put("tender", userTender);

        Prompt prompt = promptTemplate.apply(variables);

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey("API_KEY")
                .modelName(GPT_3_5_TURBO)
                .temperature(0.3)
                .build();
        String response = model.generate(prompt.text());
        System.out.println(response);

        return response;
    }

    public void Summarise(){

    }

    public String checkIfTenderIsCompliant(Tender tender, String userTender) {
        PromptTemplate promptTemplate = PromptTemplate
                .from("This is a government Tender with a set of compliances {{tender}}. With keeping this above act in mind, tell me if my tender seems broadly compliant or not. " +
                        "Consider this tender/plan: {{userTender}}" +
                        "Let me know if there are any shortcomings and where the tender is not compliant. Also tell me about penalties.");

        Map<String, Object> variables = new HashMap<>();
        variables.put("tender", tender.toString());
        variables.put("userTender", userTender.toString());

        Prompt prompt = promptTemplate.apply(variables);

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey("KEY")
                .modelName(GPT_3_5_TURBO)
                .temperature(0.3)
                .build();
        String response = model.generate(prompt.text());
        System.out.println(response);

        return response;
    }
}
