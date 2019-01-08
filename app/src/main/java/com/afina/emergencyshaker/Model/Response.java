package com.afina.emergencyshaker.Model;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="message")
class Message {

    @Element(name = "to")
    private String to;

    @Element(name = "status")
    private String status;

    @Element(name = "text")
    private String text;

    @Element(name = "balance")
    private String balance;


    // Getter Methods

    public String getTo() {
        return to;
    }

    public String getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    public String getBalance() {
        return balance;
    }

    // Setter Methods

    public void setTo(String to) {
        this.to = to;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
@Root(name = "response")
public class Response {
    @Element(name = "message")
    Message MessageObject;


    // Getter Methods

    public Message getMessage() {
        return MessageObject;
    }

    // Setter Methods

    public void setMessage(Message messageObject) {
        this.MessageObject = messageObject;
    }
}