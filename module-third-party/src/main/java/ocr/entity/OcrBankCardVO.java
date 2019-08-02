package ocr.entity;

import java.io.Serializable;

public class OcrBankCardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code; // 错误码
    private String message;// 错误信息
    private String cardId; // 卡号
    private String cardType; // 卡类型
    private String cardName; // 卡名字
    private String bankInfo; // 银行信息
    private String valid_date; // 有效期

    public OcrBankCardVO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }
}
