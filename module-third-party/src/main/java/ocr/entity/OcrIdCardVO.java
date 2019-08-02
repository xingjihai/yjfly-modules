package ocr.entity;

import java.io.Serializable;

// OCR
public class OcrIdCardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idNumber; // 身份证号

    private String name; // 姓名

    private String code;// 错误码

    private String message;// 错误描述

    private String sex; // 性别

    private String nation; // 民族

    private Integer nationId; // 民族id

    private String birth; // 出生日期

    private String address;// 地址

    private String authority;// 发证机关

    private String validDate; // 证件有效期

    private String idImgFont; // 身份证正面url

    private String idImgBack; // 身份证反面url

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public OcrIdCardVO() {

    }

    public OcrIdCardVO(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getIdImgFont() {
        return idImgFont;
    }

    public void setIdImgFont(String idImgFont) {
        this.idImgFont = idImgFont;
    }

    public String getIdImgBack() {
        return idImgBack;
    }

    public void setIdImgBack(String idImgBack) {
        this.idImgBack = idImgBack;
    }

    public Integer getNationId() {
        return nationId;
    }

    public void setNationId(Integer nationId) {
        this.nationId = nationId;
    }
}
