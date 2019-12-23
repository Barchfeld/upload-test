package eu.barchfeld;

import java.io.Serializable;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

@Service
public class MessageBean implements Serializable {

    private String msg;

    public String getMessage() {
        return msg + LocalTime.now();
    }
    public void setMessage(String msg) { this.msg = msg; }
}
