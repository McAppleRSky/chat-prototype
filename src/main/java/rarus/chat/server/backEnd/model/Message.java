package rarus.chat.server.backEnd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String name;
    @JsonProperty("date_time")
    private String dateTime;
    private String text;

}

