package analysistext;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class AnalysisTextDeserialiser implements Deserializer<AnalysisText> {

    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    public AnalysisText deserialize(String topic, byte[] dataByteArr) {
        ObjectMapper objectMapper = new ObjectMapper();
        AnalysisText analysisText = null;
        try{
            return objectMapper.readValue(dataByteArr,AnalysisText.class);
        }
        catch (Exception ex){
            System.out.println("An exception occurred:" + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public void close() {

    }
}
