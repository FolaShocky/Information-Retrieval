package analysistext;
import java.util.Map;
import java.util.Map.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
public class AnalysisTextSerialiser implements Serializer<AnalysisText> {
    public void configure(Map<String,?> configs, boolean isKey){

    }

    public byte[] serialize(String topic, AnalysisText analysisText){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(analysisText).getBytes();
        }
        catch (JsonProcessingException ex){
            System.out.println("A JsonProcessingException occurred: " +ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public void close(){

    }
}
