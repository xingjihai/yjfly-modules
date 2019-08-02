package cn.ejfy.web.tools.personality.string;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 * 1、将内容分割，更直观
 * 2、参数为空则不加进内容
 */
public class MessageTools {
    private List<Message> msgList=new ArrayList<>();

    public void addMsgMap(String template,Object params){
        Message message=new Message(template, params);
        msgList.add(message);
    }

    public  String getMsgString(){
        String content="";
        for (Message message : msgList) {
            if(message.getParams()!=null){
                content+=String.format(message.getTemplate() , message.getParams() );
            }
        }
        return content;
    }


    class Message{
        String template;
        Object params;

        Message(String template,Object params){
            this.template=template;
            this.params=params;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public Object getParams() {
            return params;
        }

        public void setParams(Object params) {
            this.params = params;
        }
    }


    public static void main(String[] args) {
        MessageTools messageTools=new MessageTools();
//        messageTools.addMsgMap("【预立案通过】您诉%s","李某某");
        messageTools.addMsgMap("【预立案通过】","");
        messageTools.addMsgMap("您诉%s,",null);
        messageTools.addMsgMap(" %s","(2017)闽0203民初329号");
//        messageTools.addMsgMap(" 的%s","民家借贷纠纷");
        messageTools.addMsgMap(" 的%s",null);
        messageTools.addMsgMap("一案已进入诉前程序，并已组织送达工作，您应于收到收件通知书之日起%s日内进行举证",30);
        System.out.println( messageTools.getMsgString() );
    }
}


