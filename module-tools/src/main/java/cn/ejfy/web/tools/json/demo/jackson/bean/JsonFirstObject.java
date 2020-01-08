package cn.ejfy.web.tools.json.demo.jackson.bean;

import java.util.ArrayList;
import java.util.List;

public class JsonFirstObject {

 

          private Integer age = 76;

          private String name = "Morgan Freeman";

          private JsonSecondObject jsnSO = new JsonSecondObject();

          private List<String> messages;

 

          public JsonFirstObject() {

            this.messages = new ArrayList<String>() {

                    {

                              add("I once heard a wise man say..");

                              add("Well, what is it today? More..");

                              add("Bruce... I'm God. Circumstances have..");

                              }

                    };

          }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonSecondObject getJsnSO() {
        return jsnSO;
    }

    public void setJsnSO(JsonSecondObject jsnSO) {
        this.jsnSO = jsnSO;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}