//package cn.ejfy.web.tools.excel.poi.使用范例;
//
//import com.weitu.framework.admin.common.cache.ISysDictionaryCache;
//import com.weitu.framework.admin.common.entity.SysDepartment;
//import com.weitu.framework.admin.common.entity.SysDictionary;
//import com.weitu.framework.admin.common.entity.SysUser;
//import com.weitu.framework.admin.common.service.ISysDepartmentService;
//import com.weitu.framework.admin.common.service.ISysUserService;
//import com.weitu.framework.core.util.StringUtils;
//import com.weitu.songda.common.SongdaCommon;
//import com.weitu.songda.common.entity.*;
//import com.weitu.songda.common.service.ICaseLitigantAddressService;
//import com.weitu.songda.common.service.INationService;
//import com.weitu.songda.common.service.IRegionsService;
//import com.weitu.songda.common.service.ITbDocumentMatterreasonService;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * 类名：ImportCaseAndLitigantTool
// * 创建者：wuyijia
// * 版权：厦门维途信息技术有限公司 Copyright(c) 2017
// * 说明：导入案件工具类,案件和当事人为主，地址电话放入当事人中
// */
//public class ImportCaseAndLitigantTool {
//
//    private String caseAddressValue="";
//    private Integer caseAddressJ;
//    private String caseTelValue="";
//    private Integer caseTelJ;
//
//    public String getCaseAddressValue() {
//        return caseAddressValue;
//    }
//    public Integer getCaseAddressJ() {
//        return caseAddressJ;
//    }
//    public String getCaseTelValue() {
//        return caseTelValue;
//    }
//    public Integer getCaseTelJ() {
//        return caseTelJ;
//    }
//
//    private ITbDocumentMatterreasonService tbDocumentMatterreasonService;
//    private ISysDepartmentService sysDepartmentService;
//    private ISysUserService sysUserService;
//    private ISysDictionaryCache sysDictionaryCache;
//    private IRegionsService regionsService;
//    private INationService nationService;
//    private ICaseLitigantAddressService caseLitigantAddressService;
//
//    public static final String CASE_ADDRESS="CASE_ADDRESS";  //自定义地址字段（以;隔开）
//    public static final String CASE_TEL="CASE_TEL"; //自定义电话字段（以;隔开）
//
//    public static final String DISCUSSION_MEMBER="discussion_member";//其他合议庭成员
//    public static final String SERIES_CASE_CODE=CaseRow.T_SERIES_CASE_CODE;//系列案编号
//    public static final String REMARK=CaseRemark.T_REMARK;//案件备注
//
//    /**
//     * 特殊字段
//     */
//    public static final String CASE_CATEGORY_1="case_category_1";  //案件类别 （ 民事 ）
//    public static final String CASE_CATEGORY_2="case_category_2"; //审判程序 （ 一审 ）
//    public String caseCategoryStr="";
//
//    public static final String LEGAL_ID_TYPE="LEGAL_ID_TYPE";  //法定代表人证件号
//    public static final String LEGAL_ID_NUMBER="LEGAL_ID_NUMBER"; //法定代表人证件种类
//
//    public static final String CREDENTIALS_INFO="CREDENTIALS_INFO"; //证件信息（前海法院模板） --居民身份证：412328197903046054
//
//    public ImportCaseAndLitigantTool(ITbDocumentMatterreasonService tbDocumentMatterreasonService, ISysDepartmentService sysDepartmentService, ISysUserService sysUserService, ISysDictionaryCache sysDictionaryCache, IRegionsService regionsService, INationService nationService, ICaseLitigantAddressService caseLitigantAddressService){
//        this.tbDocumentMatterreasonService=tbDocumentMatterreasonService;
//        this.sysDepartmentService=sysDepartmentService;
//        this.sysUserService=sysUserService;
//        this.sysDictionaryCache=sysDictionaryCache;
//        this.regionsService=regionsService;
//        this.nationService=nationService;
//        this.caseLitigantAddressService=caseLitigantAddressService;
//    }
//
//    ExcelErrorMsg em=new ExcelErrorMsg();
//    public  ExcelErrorMsg getExcelErrorMsg(){
//        return em;
//    }
//    public void setExcelErrorMsg(ExcelErrorMsg excelErrorMsg){
//        em=excelErrorMsg;
//    }
//
//
//
//    public Case fillCase(Case caze, String[] caseArr, int index, String value){
//        if(StringUtils.isEmpty(value)){
//            return caze;
//        }else{
//            value=value.trim();
//        }
//        ExcelErrorMsg excelErrorMsg=getExcelErrorMsg();
//        switch (caseArr[index]){
//            case CASE_CATEGORY_1: //案件类别
//                caseCategoryStr=value;
//                break;
//            case CASE_CATEGORY_2: //审判程序
//                caseCategoryStr+=value;
//                SysDictionary caseCategoryDic=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_CASE_CATEGORY,value);
//                if(caseCategoryDic==null){
//                    excelErrorMsg.addMsg("系统中未找到该案件类别和审判程序:"+value);
//                    break;
//                }
//                caze.setCaseCategory( getDictionaryInteger(caseCategoryDic.getValue(),"新案件类别") );
//                break;
//            case Case.T_REGISTER_DATE: //立案日期
//                caze.setRegisterDate( getDate(value) );
//                break;
//            case Case.T_PRE_TRIAL_DATE: //预立案时间
//                caze.setPreTrialDate( getDate(value) );
//                break;
//            case Case.T_RECEIVE_DATE: //诉状日期
//                caze.setReceiveDate( getDate(value) );
//                break;
//            case Case.T_CASE_CLOSE_TIME: //结案时间
//                caze.setReceiveDate( getDate(value) );
//                break;
//            case Case.T_CASE_DETAIL: //简要案情
//                caze.setCaseDetail(value);
//                break;
//            case Case.T_SUE_AMOUNT: //起诉标金额
//                caze.setSueAmount( getCurrencyDouble(value));
//                break;
//
//            case Case.T_CASE_CAUSE_NAME:
//                TbDocumentMatterreason tbDocumentMatterreason=tbDocumentMatterreasonService.getByValue(value);
//                if(tbDocumentMatterreason==null){
//                    excelErrorMsg.addMsg("系统中未找到该立案案由:"+value);
//                    break;
//                }
//                caze.setCaseCauseId( tbDocumentMatterreason.getId() );
//                caze.setCaseCauseName(tbDocumentMatterreason.getValue());
//                break;
//            case Case.T_DEPT_NAME:
//                SysDepartment sysDepartment=sysDepartmentService.getByName(value);
//                if(sysDepartment==null){
//                    excelErrorMsg.addMsg("系统中未找到该承办部门:"+value);
//                    break;
//                }
//                caze.setDeptId(sysDepartment.getId() );
//                caze.setDeptName(sysDepartment.getName());
//                break;
//            case Case.T_REGISTRANT:
//                SysUser registrant= sysUserService.getUserByName(value);
//                if(registrant==null){
//                    excelErrorMsg.addMsg("系统中未找到收案人:"+value);
//                    break;
//                }
//                caze.setRegistrant(registrant.getId());
//                break;
//            case Case.T_JUDGE:
//                SysUser sysUser= sysUserService.getUserByName(value);
//                if(sysUser==null){
//                    excelErrorMsg.addMsg("系统中未找到该承办人:"+value);
//                    break;
//                }
//                caze.setJudgeId(sysUser.getId());
//                caze.setJudge(sysUser.getName() );
//                break;
//            case Case.T_CHIEF_JUDGE:
//                SysUser chiefJudge= sysUserService.getUserByName(value);
//                if(chiefJudge==null){
//                    excelErrorMsg.addMsg("系统中未找到该审判长:"+value);
//                    break;
//                }
//                caze.setChiefJudgeId(chiefJudge.getId());
//                caze.setChiefJudge(chiefJudge.getName() );
//                break;
//            case Case.T_COURT_CLERK:
//                SysUser courtClerk= sysUserService.getUserByName( value );
//                if(courtClerk==null){
//                    excelErrorMsg.addMsg("系统中未找到该书记员:"+value);
//                    break;
//                }
//                caze.setCourtClerkId( courtClerk.getId());
//                caze.setCourtClerk(courtClerk.getName());
//                break;
//            case Case.T_COURT_ROOM_NAME:
//                SysDepartment courtRoom=sysDepartmentService.getByName(value);
//                if(courtRoom==null){
//                    excelErrorMsg.addMsg("系统中未找到该承办法庭:"+value);
//                    break;
//                }
//                caze.setCourtRoomId( courtRoom.getId() );
//                caze.setCourtRoomName( courtRoom.getName());
//                break;
//            case Case.T_DEPT_ID:
//                SysDepartment dept=sysDepartmentService.getByName(value);
//                if(dept==null){
//                    excelErrorMsg.addMsg("系统中未找到该承办部门:"+value);
//                    break;
//                }
//                caze.setDeptId( dept.getId() );
//                caze.setDeptName( dept.getName());
//                break;
//            case Case.T_CASE_CATEGORY:
//                SysDictionary sysDictionary=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_CASE_CATEGORY,value);
//                if(sysDictionary==null){
//                    excelErrorMsg.addMsg("系统中未找到该新案件类别:"+value);
//                    break;
//                }
//                caze.setCaseCategory( getDictionaryInteger(sysDictionary.getValue(),"新案件类别") );
//                break;
//            case Case.T_APPLICABLE_PROCEDURE:
//                SysDictionary dictionaryAp=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_AP,value);
//                if(dictionaryAp==null){
//                    if(!value.contains("程序")){
//                        value+="程序";
//                    }
//                    dictionaryAp=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_AP,value);
//                    if(dictionaryAp==null) {
//                        excelErrorMsg.addMsg("系统中未找到该适用程序:" + value);
//                    }
//                    break;
//                }
//                caze.setCaseCategory( getDictionaryInteger(dictionaryAp.getValue(),"适用程序") );
//                break;
//            case Case.T_NEWCASE_SOURCE:
//                SysDictionary dictionaryNewcaseSoNurce=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_NEWCASE_SOURCE,value);
//                if(dictionaryNewcaseSoNurce==null){
//                    excelErrorMsg.addMsg("系统中未找到该新案件来源:"+value);
//                    break;
//                }
//                caze.setCaseCategory( getDictionaryInteger(dictionaryNewcaseSoNurce.getValue(),"新案件来源") );
//                break;
//            case Case.T_CASE_REFER:
//                SysDictionary dictionaryCaseRefer=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_CASE_REFER,value);
//                if(dictionaryCaseRefer==null){
//                    excelErrorMsg.addMsg("系统中未找到该案件涉及:"+value);
//                    break;
//                }
//                caze.setCaseCategory( getDictionaryInteger(dictionaryCaseRefer.getValue(),"案件涉及") );
//                break;
//            case Case.T_LITIGATION_FEE:
//                caze.setLitigationFee( getCurrencyDouble(value) );
//                break;
//            case Case.T_CASE_CLOSE_MODE:
//                SysDictionary dictionaryCaseCloseMode=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_CASE_CLOSE_MODE,value);
//                if(dictionaryCaseCloseMode==null){
//                    excelErrorMsg.addMsg("系统中未找到该结案方式:"+value);
//                    break;
//                }
//                caze.setCaseCategory( getDictionaryInteger(dictionaryCaseCloseMode.getValue(),"结案方式") );
//                break;
//            case Case.T_JURISDICTION_BASES: //管辖依据
//                SysDictionary jurisdictionBases=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_JURISDICTION_BASES,value);
//                if(jurisdictionBases==null){
//                    excelErrorMsg.addMsg("系统中未找到该管辖依据:"+value);
//                    break;
//                }
//                caze.setCaseCategory( getDictionaryInteger(jurisdictionBases.getValue(),"管辖依据") );
//                break;
//
//        }
//        return caze;
//    }
//
//
//    public CaseLitigant fillLitigant(CaseLitigant caseLitigant,String[] litigantArr,int index,String value){
//        if(StringUtils.isEmpty(value)){
//            return caseLitigant;
//        }else{
//            value=value.trim();
//        }
//        ExcelErrorMsg excelErrorMsg=getExcelErrorMsg();
//        switch (litigantArr[index]){
//            case CaseLitigant.T_LITIGANT_NAME: //当事人姓名
//                caseLitigant.setLitigantName( value );
//                break;
//            case CaseLitigant.T_BIRTHDAY: //出生日期
//                caseLitigant.setBirthday( getDate(value) );
//                break;
//            case CaseLitigant.T_LITIGATION_STATUS:
//                SysDictionary sysDictionary=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_LITIGATION_STATUS,value);
//                if(sysDictionary==null){
//                    excelErrorMsg.addMsg("系统中未找到该诉讼地位:"+value);
//                    break;
//                }
//                caseLitigant.setLitigationStatus( getDictionaryInteger(sysDictionary.getValue(),"诉讼地位") );
//                break;
//            case CaseLitigant.T_LITIGANT_TYPE:
//                SysDictionary litigantType=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_LITIGANT_TYPE,value);
//                if(litigantType==null){
//                    excelErrorMsg.addMsg("系统中未找到该当事人类型:"+value);
//                    break;
//                }
//                caseLitigant.setLitigantType( getDictionaryInteger(litigantType.getValue(),"该当事人类型") );
//                break;
//            case CaseLitigant.T_NATION:
//                Nation nation= nationService.getByName(value);
//                if(nation==null){
//                    excelErrorMsg.addMsg("系统中未找到该民族:"+value);
//                    break;
//                }
//                caseLitigant.setNation( nation.getId()==null?null:nation.getId().toString() );
//            case CaseLitigant.T_SEX: //性别
//                Integer sex= null;
//                if(value==null){
//                    break;
//                }
//                switch (value){
//                    case "男性":
//                        sex=SongdaCommon.SEX_MAN;
//                        break;
//                    case "女性":
//                        sex=SongdaCommon.SEX_WOMAN;
//                        break;
//                    case "不详":
//                        sex=SongdaCommon.SEX_NO_CLEAR;
//                        break;
//                }
//                caseLitigant.setSex(sex);
//                break;
//            case CaseLitigant.T_ID_TYPE:
//            case LEGAL_ID_TYPE:
//                SysDictionary idType=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_IT,value);
//                if(idType==null){
//                    excelErrorMsg.addMsg("系统中未找到该证件类型"+value);
//                    break;
//                }
//                caseLitigant.setIdType( getDictionaryInteger(idType.getValue(),"证件类型"));
//                break;
//            case CaseLitigant.T_ID_NUMBER:
//            case LEGAL_ID_NUMBER:
//                caseLitigant.setIdNumber(value);
//                break;
//            case CaseLitigant.T_LITIGANT_IDENTITY:
//                SysDictionary litigantIdentity=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_LITIGANT_IDENTITY,value);
//                if(litigantIdentity==null){
//                    excelErrorMsg.addMsg("系统中未找到该身份："+value);
//                    break;
//                }
//                caseLitigant.setLitigantIdentity( getDictionaryInteger(litigantIdentity.getValue(),"身份"));
//                break;
//
//            case CaseLitigant.T_ORGANAZITION_CODE:
//                caseLitigant.setOrganazitionCode(value);
//                break;
//            case CaseLitigant.T_CORPORATION_NAME: //法人代表姓名
//                caseLitigant.setCorporationName(value);
//                break;
//            case CASE_ADDRESS: //地址
//                caseAddressValue=value;
//                caseAddressJ=excelErrorMsg.getJ();
//                break;
//            case CASE_TEL: //电话
//                caseTelValue=value;
//                caseTelJ=excelErrorMsg.getJ();
//                break;
//            case CREDENTIALS_INFO: //证件信息（前海法院模板）
//                String[] credentials= value.split(":");
//                if(credentials!=null&&credentials.length==2){
//                    SysDictionary idType2=sysDictionaryCache.getByName(SongdaCommon.DICTIONARY_IT,credentials[0]);
//                    if(idType2==null){
//                        excelErrorMsg.addMsg("系统中未找到该证件类型"+value);
//                        break;
//                    }
//                    caseLitigant.setIdType( getDictionaryInteger(idType2.getValue(),"证件类型"));
//                    caseLitigant.setIdNumber( credentials[1] );
//                }
//                break;
//        }
//        return caseLitigant;
//    }
//
//    public CaseLitigantAddress fillAddress(CaseLitigantAddress caseLitigantAddress,String value,Map addressMap){
//        if(StringUtils.isEmpty(value)){
//            return null;
//        }else{
//            value=value.trim();
//        }
//        if(addressMap.get( value )!=null){  //判重
//            return null;
//        }
//        caseLitigantAddress.setDetailAddr( value );
//        caseLitigantAddress.setFullAddress( value);
//        return caseLitigantAddress;
//    }
//    public CaseLitigantAddress fillFormatAddress(CaseLitigantAddress caseLitigantAddress,String value,Map addressMap){
//        if(StringUtils.isEmpty(value)){
//            return null;
//        }else{
//            value=value.trim();
//        }
//        Map map=isInvalidAddress(value);
//        String fullAddress=(String)map.get("fullAddress");
//        String detailAddress=(String)map.get("detailAddress");
//        if(addressMap.get( fullAddress )!=null){  //判重
//            return null;
//        }
//        Integer [] ids=(Integer[] )map.get("ids");
//        caseLitigantAddress.setProvince(ids[0]);
//        caseLitigantAddress.setCity(ids[1]);
//        caseLitigantAddress.setRegion( ids[2] );
//        caseLitigantAddress.setStreet( ids[3] );
//        caseLitigantAddress.setDetailAddr( detailAddress );
//        caseLitigantAddress.setFullAddress( fullAddress);
//        return caseLitigantAddress;
//    }
//    public CaseLitigantTel fillTel(CaseLitigantTel caseLitigantTel,String value,Map telMap){
//        if(StringUtils.isEmpty(value)){
//            return null;
//        }else{
//            value=value.trim();
//        }
//        if(StringUtils.isEmpty(value)){
//            return null;
//        }
//        if(telMap.get(value.trim()) !=null  ){  //判重
//            return null;
//        }
//        caseLitigantTel.setTel(value);
//        return caseLitigantTel;
//    }
//
//    private Map isInvalidAddress(String address){
//        Map addressMap=new HashMap();
//        ExcelErrorMsg excelErrorMsg=getExcelErrorMsg();
//        int code = 0;
//        Integer [] ids = new Integer[4];
//        String fullAddress = "";
//        String detailAddress = "";
//        if(address != null && !"".equals(address.trim())){
//            String [] addrs = address.split("/");
//            if(addrs.length < 4){
//                //code = code|1;//数据项过少
//                excelErrorMsg.addMsg("地区数据格式不正确："+address);
//            }else{//省、市、区
//                List<Regions> regionses =regionsService.getRegionsByName(addrs[0].trim());
//                if(regionses.isEmpty()){
//                    code = code|2;//省份不存在
//                    excelErrorMsg.addMsg("["+addrs[0].trim()+"]省不存在");
//                }else{
//                    ids[0] = regionses.get(0).getId();
//                    fullAddress+=addrs[0].trim();
//                }
//                regionses =regionsService.getRegionsByName(addrs[1].trim());
//                if(regionses.isEmpty()){
//                    code = code|3;//市不存在
//                    excelErrorMsg.addMsg("["+addrs[1].trim()+"]市不存在");
//                }else{
//                    ids[1] = regionses.get(0).getId();
//                    fullAddress+=addrs[1].trim();
//                }
//                regionses =regionsService.getRegionsByName(addrs[2].trim());
//                if(regionses.isEmpty()){
//                    code = code|4;//区不存在
//                    excelErrorMsg.addMsg("["+addrs[2].trim()+"]区不存在");
//                }else{
//                    ids[2] = regionses.get(0).getId();
//                    fullAddress+=addrs[2].trim();
//                }
//
//                if(addrs.length == 4){
//                    fullAddress+= addrs[3].trim();
//                    detailAddress+= addrs[3].trim();
//                }else{
//                    regionses =regionsService.getRegionsByName(addrs[3].trim());
//                    if(regionses.isEmpty()){
//                        code = code|8;//
//                        excelErrorMsg.addMsg("["+addrs[3].trim()+"]街道不存在");
//                    }else{
//                        ids[3] = regionses.get(0).getId();
//                    }
//                    for(int i = 4; i < addrs.length; i++){
//                        fullAddress += addrs[i].trim();
//                        detailAddress += addrs[i].trim();
//                    }
//                }
//
//            }
//        }
//        addressMap.put("ids",ids);
//        addressMap.put("fullAddress",fullAddress);
//        addressMap.put("detailAddress",detailAddress);
//        return addressMap;
//    }
//
//
//    //============================== 格式  ==============================
//
//    public static String[] sdfArr={"EEE MMM dd HH:mm:ss Z yyyy","yyyy-MM-dd"};
//    private int dateIndex=0;
//    /**
//     * 日期格式
//     * @param dateStr
//     * @return
//     */
//    private Date getDate(String dateStr){
//        ExcelErrorMsg excelErrorMsg=getExcelErrorMsg();
//        if(dateIndex>=sdfArr.length){
//            excelErrorMsg.addMsg("日期格式错误："+dateStr);
//            return null;
//        }
//        try {
//            SimpleDateFormat sdfDate = new SimpleDateFormat(sdfArr[dateIndex], Locale.UK);
//            Date date=sdfDate.parse(dateStr);
//            return date;
//        } catch (ParseException e) {
//            dateIndex++;
//            getDate(dateStr);
//        }
//        return null;
//    }
//    /**
//     * Double格式
//     * @return
//     */
//    private Double getCurrencyDouble(String value){
//        ExcelErrorMsg excelErrorMsg=getExcelErrorMsg();
//        try {
//            Double doub=Double.parseDouble(value);
//            return doub;
//        } catch (Exception e) {
//            excelErrorMsg.addMsg("金额格式错误："+value);
//        }
//        return null;
//    }
//    private Integer getDictionaryInteger(String value,String dictionaryStr){
//        ExcelErrorMsg excelErrorMsg=getExcelErrorMsg();
//        try{
//            return Integer.parseInt(value);
//        }catch (Exception e){
//            excelErrorMsg.addMsg(dictionaryStr+"字典值设置错误,应为整数");
//            return null;
//        }
//    }
//    private Integer getInteger(String value,String errorMsg){
//        ExcelErrorMsg excelErrorMsg=getExcelErrorMsg();
//        try{
//            return Integer.parseInt(value);
//        }catch (Exception e){
//            excelErrorMsg.addMsg( errorMsg );
//            return null;
//        }
//    }
//}
