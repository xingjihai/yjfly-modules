package cn.ejfy.web.tools.jsonreturn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author wyj
 */
@JsonPropertyOrder({ "code", "message","data" })
@JsonInclude(Include.NON_EMPTY)
public class JsonResult {
	/** 成功状态码 --成功有数据 */
	public static final int CODE_SUCCESS = 0;
	/** 失败状态码  */
	public static final int CODE_Fail = 1;       
	/** 未登录 */
	public static final int CODE_NOT_LOGGED_IN = 2;   

	/** 返回给用户的信息 */
	private String message;
	/** 状态码 */
	private Integer code;
	/** 请求的返回数据对象，也将被转为json格式 */
	private Object data;

	public JsonResult() {
	}

	public JsonResult(int code,String message) {
		this.code = code;
		this.message = message;
	}


	public JsonResult(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	
}
