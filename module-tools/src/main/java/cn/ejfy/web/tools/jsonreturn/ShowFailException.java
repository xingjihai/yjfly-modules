package cn.ejfy.web.tools.jsonreturn;

/**
 * 在service中抛出异常使事务回滚,并返回客户端错误信息。
 * @author wyj
 */
public class ShowFailException  extends RuntimeException{
    private static final long serialVersionUID = -5288912386269387973L;
    
    public ShowFailException(String message) {
        super(message);
        JsonResultUtil.showFailJson(message);
    }
    
    public ShowFailException(Exception e,String message) {
        
        super(message);
        e.printStackTrace();
    }
}
