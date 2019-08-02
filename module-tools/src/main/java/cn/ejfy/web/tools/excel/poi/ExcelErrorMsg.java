package cn.ejfy.web.tools.excel.poi;

import java.util.ArrayList;
import java.util.List;

/**
 * excel错误信息类
 * @author wuyijia
 */
public class ExcelErrorMsg {
    private List<String> msgList = new ArrayList<>();
    private List<String> errorRowMsg = new ArrayList<>();
    private int successSaveCount=0;
    private int successUpdateCount=0;
    private List<String> updateCodeList = new ArrayList<>();
    private int i;
    private int j;
    private boolean isError=false;
    public void addMsg(String message){
        String countStr=excelCount(j+1,"");
//        msgList.add("<br/><span style='color:red'>第"+(i+1)+"行案件,"+(j+1)+"列："+message+"</span>");
        msgList.add("<br/><span style='color:red'>第"+(i+1)+"行案件,"+countStr+"列："+message+"</span>");
        isError=true;
    }

    /**
     * excel 计数（count从1开始算）
     * @param count
     * @param countStr
     * @return
     */
    private static String excelCount(int count,String countStr){
        if(count<=0){
            return countStr;
        }
        if(countStr==null){
            countStr="";
        }
        int mantissa=count%26;
        int multiple=count/26;
        countStr=excelCount(multiple,countStr);
        char countChar=(char)(mantissa+64);
        countStr+=String.valueOf(countChar);
        return countStr;
    }

    public void isSaveSuccess(int i,Boolean isSuccess){
        if(isSuccess==null){
            return ;
        }
        if(isSuccess){
            successSaveCount++;
        }else{
            errorRowMsg.add("<br/><span style='color:red'>第"+(i+1)+"行未导入成功请重新导入</span>");
        }
    }
    public void isUpdateSuccess(int i,Boolean isSuccess,String code){
        if(isSuccess==null){
            return ;
        }
        if(isSuccess){
            successUpdateCount++;
            updateCodeList.add(code);
        }else{
            errorRowMsg.add("<br/><span style='color:red'>第"+(i+1)+"行,案号："+code+",未更新成功请重新导入</span>");
        }
    }

    public void setIndex(Integer i,Integer j){
        if(i!=null){
            this.i=i;
        }
        if(j!=null){
            this.j=j;
        }

    }

    public int getJ() {
        return j;
    }

    public String getMsg(){
        String msg="";

        msg+="1、导入信息";
        msg+="<br/> 成功导入案件数量:"+successSaveCount;
        msg+="<br/> 成功更新案件数量:"+successUpdateCount;
        for (int i = 0; i < updateCodeList.size(); i++) {
            if(i==0){
                msg+="<br/>更新的案号:";
            }
            msg+="<br/>"+updateCodeList.get(i)+",";
        }

        for (int i = 0; i < errorRowMsg.size(); i++) {
            if(i==0){
                msg+="<br/>导入失败:";
            }
            msg+=errorRowMsg.get(i);
        }

        for (int i = 0; i < msgList.size(); i++) {
            if(i==0){
                msg+="<br/>2、错误信息:";
            }
            msg+=msgList.get(i);
        }

        return msg;
    }

    public boolean hasError(){
        if(isError){
            isError=false;
            return true;
        }else{
            return false;
        }
    }
}
