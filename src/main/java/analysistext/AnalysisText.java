package analysistext;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class AnalysisText {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "sentLocalDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddThh:mm:ss")
    private LocalDateTime sentLocalDateTime;

    public AnalysisText(){

    }

    public AnalysisText(String title){
        this.title = title;
    }
    public UUID getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getType(){
        return type;
    }

    public LocalDateTime getSentLocalDateTime(){
        return sentLocalDateTime;
    }

    public AnalysisText setId(UUID id){
        this.id = id;
        return this;
    }

    public AnalysisText setTitle(String title){
        this.title = title;
        return this;
    }

    public AnalysisText setContent(String content){
        this.content = content;
        return this;
    }

    public AnalysisText setType(String type){
        this.type = type;
        return this;
    }

    public AnalysisText setSentLocalDateTime(LocalDateTime sentLocalDateTime) {
        this.sentLocalDateTime = sentLocalDateTime;
        return this;
    }

    public boolean equals(Object obj){
        if(!(obj instanceof AnalysisText))
            return false;
        AnalysisText analysisText = (AnalysisText)obj;
        return id.equals(analysisText.id)
                && title.equals(analysisText.title)
                && content.equals(analysisText.content)
                && type.equals(analysisText.type)
                && sentLocalDateTime.equals(analysisText.sentLocalDateTime);
    }
}
