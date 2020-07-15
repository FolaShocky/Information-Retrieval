package controller;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.*;
import analysistext.AnalysisText;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class AnalysisTextController {
    @PostMapping(value = "/sendLatestAnalysisText")
    public String sendLatestAnalysisText(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "title")String title,
            @RequestParam(name = "content")String content,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "sentLocalDateTime")
            @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss") String sentLocalDateTime){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(
                new AnalysisText(title)
                .setId(UUID.fromString(id))
                .setContent(content)
                .setType(type)
                .setSentLocalDateTime(LocalDateTime.parse(sentLocalDateTime))
            );
        }
        catch (JsonProcessingException ex){
            System.out.println("An exception occurred: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }



}
