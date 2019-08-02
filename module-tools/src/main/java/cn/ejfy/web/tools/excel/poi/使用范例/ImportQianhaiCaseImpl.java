//package cn.ejfy.web.tools.excel.poi.使用范例;
//
//import com.weitu.framework.admin.common.cache.ISysDictionaryCache;
//import com.weitu.framework.admin.common.service.ISysDepartmentService;
//import com.weitu.framework.admin.common.service.ISysUserService;
//import com.weitu.framework.component.orm.support.Commit;
//import com.weitu.framework.core.support.Constants;
//import com.weitu.framework.core.util.StringUtils;
//import com.weitu.framework.web.util.SpringContextUtils;
//import com.weitu.songda.common.SongdaCommon;
//import com.weitu.songda.common.entity.Case;
//import com.weitu.songda.common.entity.CaseLitigant;
//import com.weitu.songda.common.entity.CaseLitigantAddress;
//import com.weitu.songda.common.entity.CaseLitigantTel;
//import com.weitu.songda.common.service.*;
//import com.weitu.songda.common.utils.PoiExcelImportTool;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * 类名：ImportQianhaiCaseImpl
// * 创建者：wuyijia
// * 版权：厦门维途信息技术有限公司 Copyright(c) 2017
// * 说明：导入案件
// */
//@Service("importQianHaiCase")
//public class ImportQianhaiCaseImpl implements IImportQianHaiCase {
//    @Autowired
//    private ICaseService caseService;
//    @Autowired
//    private ICaseLitigantService caseLitigantService;
//    @Autowired
//    private ICaseLitigantAddressService caseLitigantAddressService;
//    @Autowired
//    private ICaseLitigantTelService caseLitigantTelService;
//    @Autowired
//    private ITbDocumentMatterreasonService tbDocumentMatterreasonService;
//    @Autowired
//    private ISysDepartmentService sysDepartmentService;
//    @Autowired
//    private ISysUserService sysUserService;
//    @Autowired
//    private ISysDictionaryCache sysDictionaryCache;
//    @Autowired
//    private IRegionsService regionsService;
//    @Autowired
//    private INationService nationService;
//
//    /**
//     * 导入异议期案件
//     */
//    private static String[] serviceCaseArr = {"序号",Case.T_CASE_CODE,Case.T_APPLICABLE_PROCEDURE,Case.T_CASE_CAUSE_NAME,Case.T_CHIEF_JUDGE,Case.T_JUDGE,Case.T_COURT_CLERK,ImportCaseAndLitigantTool.DISCUSSION_MEMBER,Case.T_SUE_AMOUNT,Case.T_REGISTER_DATE,"结案日期"};
//    //序号	案号	适用程序	立案案由	审判长	承办人	书记员	其他成员	诉讼标的金额	立案日期	结案日期
//    // 诉讼地位	当事人姓名	手机号	证件信息
//    private static String[] serviceLitigantArr = { CaseLitigant.T_LITIGATION_STATUS,CaseLitigant.T_LITIGANT_NAME,ImportCaseAndLitigantTool.CASE_TEL,ImportCaseAndLitigantTool.CREDENTIALS_INFO};
//
//    //    ThreadLocal<ExcelErrorMsg> excelErrorMsg=new ThreadLocal<>();
////    public  ExcelErrorMsg getExcelErrorMsg(){
////        ExcelErrorMsg em=excelErrorMsg.get();
////        if(em==null){
////            em=new  ExcelErrorMsg();
////            excelErrorMsg.set(em);
////        }
////        return em;
////    }
////    public void initExcelErrorMsg(){
////        excelErrorMsg.set(new  ExcelErrorMsg());
////    }
//    public String caseId = "";
//    public String caseCodeVar = "";
//    public boolean isSave=true; //保存或修改
//
//    public String importCase(File file) {
//        Map<Integer, List<String[]>> map = null;
//        try {
//            map = PoiExcelImportTool.readXlsxByPath( file.getAbsolutePath() );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(map==null){
//            return "当前导入的excel表格版本无法解析,请尝试更换excel版本";
//        }
//        final ImportCaseAndLitigantTool importCaseTool = new ImportCaseAndLitigantTool( tbDocumentMatterreasonService, sysDepartmentService, sysUserService, sysDictionaryCache, regionsService,  nationService,  caseLitigantAddressService);
//        final List<String[]> list = map.get(0);
//        for (int ii = 1; ii < list.size(); ii++) {
//            PlatformTransactionManager manager =
//                    SpringContextUtils.getBean(SpringContextUtils.getProperty(Constants.DEFAULT_TRANSACTION_MANAGER));
//            Commit commit = new Commit(manager);
//
//            final int i = ii;
//            try {
//                commit.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            String[] data = list.get(i);
////                            String caseId = "";
////                            String caseCodeVar = "";
//                            String caseLitigantId = "";
//
//                            boolean isOneCase=true; //是否同一个案件
//
//                            {//========  案件开始标志 --案号
//                                importCaseTool.getExcelErrorMsg().setIndex(i, 0);
//                                String caseCode = data[1].trim();
//                                if (!StringUtils.isEmpty(caseCode)&&!caseCode.equals(caseCodeVar)) {
//                                    isOneCase=false;
//                                    Case caze = caseService.getCaseByCaseCode(caseCode);
//                                    if (caze == null) {
//                                        caze = new Case();
//                                        String year = caseCode.substring(1, 5);
//                                        Pattern p = Pattern.compile("[0-9]+号", 2 | Pattern.DOTALL);
//                                        Matcher m = p.matcher(caseCode);
//                                        String case_words = "";
//                                        String case_words_sn = "";
//                                        while (m.find()) {
//                                            case_words_sn = m.group(0).substring(0, m.group(0).length() - 1);
//                                            case_words = caseCode.substring(6, m.start(0));
//                                        }
//                                        caze.setCaseWordsSn(Integer.parseInt(case_words_sn));
//                                        caze.setCaseYear(year);
//                                        caze.setCaseWords(case_words);
//                                        caze.setCaseCode(caseCode);
//                                        caze.setCaseState(SongdaCommon.CASE_STATUS9);
//                                    }
//
//                                    for (int j = 1; j < serviceCaseArr.length; j++) {
//                                        importCaseTool.getExcelErrorMsg().setIndex(i, j);
//                                        caze = importCaseTool.fillCase(caze, serviceCaseArr, j, data[j].trim());
//                                    }
//
//
//                                    if (StringUtils.isEmpty(caze.getId())) {
//                                        isSave=true;
//                                        caseService.save(caze);
//                                    } else {
//                                        caseService.update(caze);
//                                        isSave=false;
//                                    }
//                                    caseId = caze.getId();
//                                    caseCodeVar=caseCode;
//                                }
//
//                            } //=======  案件结束
//
//                            int plus = serviceCaseArr.length ;
//                            int litigantIndex = plus;
//
//                            CaseLitigant caseLitigant =new CaseLitigant();
//
//                            for (; litigantIndex < plus + serviceLitigantArr.length; litigantIndex++) {
//                                if(litigantIndex>=data.length){
//                                    break;
//                                }
//                                importCaseTool.getExcelErrorMsg().setIndex(i, litigantIndex);
//                                caseLitigant = importCaseTool.fillLitigant(caseLitigant, serviceLitigantArr, litigantIndex - plus, data[litigantIndex].trim());
//                            }
//                            if(!StringUtils.isEmpty( caseLitigant.getLitigantName() ) && !StringUtils.isEmpty(caseId)  ){
//                                CaseLitigant cl = caseLitigantService.getLitigantByName(caseId, caseLitigant.getLitigantName());
//                                if(cl!=null){
//                                    caseLitigant.setId(cl.getId());
//                                }
//                                caseLitigant.setCaseId(caseId);
//                            }else{
//                                importCaseTool.getExcelErrorMsg().addMsg("案号为空或当事人为空");
//                            }
//
//                            if (StringUtils.isEmpty(caseLitigant.getId())) {
//                                caseLitigantService.save(caseLitigant);
//                            } else {
//                                caseLitigantService.update(caseLitigant);
//                            }
//                            caseLitigantId = caseLitigant.getId();
//
//
//                            {//地址
//                                if( !StringUtils.isEmpty( importCaseTool.getCaseAddressValue())){
//                                    importCaseTool.getExcelErrorMsg().setIndex(i, importCaseTool.getCaseAddressJ());
//                                    Integer litigantType=caseLitigant.getLitigantType();
//                                    Integer addrType=SongdaCommon.ADDRESS_TYPE_2;
//                                    if(litigantType!=null){
//                                        switch(litigantType){
//                                            case SongdaCommon.LITIGANT_TYPE_NATURE:
//                                                addrType=SongdaCommon.ADDRESS_TYPE_2;
//                                                break;
//                                            case SongdaCommon.LITIGANT_TYPE_LEGAL:
//                                            case SongdaCommon.LITIGANT_TYPE_NO_LEGAL:
//                                                addrType=SongdaCommon.ADDRESS_TYPE_5;
//                                                break;
//                                        }
//                                    }
//                                    List<CaseLitigantAddress> addressList = caseLitigantAddressService.getAddress(caseId, caseLitigantId);
//                                    Map addressMap = new HashMap();
//                                    for (CaseLitigantAddress caseLitigantAddress : addressList) {
//                                        addressMap.put(caseLitigantAddress.getFullAddress(), caseLitigantAddress.getFullAddress());
//                                    }
//
//                                    String[] addressArr = importCaseTool.getCaseAddressValue().trim().split(";");
//                                    for (String address : addressArr) {
//                                        CaseLitigantAddress caseLitigantAddress = new CaseLitigantAddress();
//                                        caseLitigantAddress.setCaseId(caseId);
//                                        caseLitigantAddress.setSdLitigantId(caseLitigantId);
//                                        caseLitigantAddress.setAddrType(addrType);
//                                        caseLitigantAddress = importCaseTool.fillAddress(caseLitigantAddress, address, addressMap);
//                                        if(caseLitigantAddress!=null){
//                                            caseLitigantAddressService.save(caseLitigantAddress);
//                                        }
//                                    }
//                                }
//                            }
//                            {//电话
//                                if( !StringUtils.isEmpty( importCaseTool.getCaseTelValue())){
//                                    importCaseTool.getExcelErrorMsg().setIndex(i, importCaseTool.getCaseTelJ());
//                                    List<CaseLitigantTel> telList = caseLitigantTelService.getTel(caseId, caseLitigantId);
//                                    Map telMap = new HashMap();
//                                    for (CaseLitigantTel caseLitigantTel : telList) {
//                                        telMap.put(caseLitigantTel.getTel(), caseLitigantTel.getTel());
//                                    }
//
//                                    String[] telArr = importCaseTool.getCaseTelValue().trim().split(";");
//
//                                    for (String tel : telArr) {
//                                        CaseLitigantTel caseLitigantTel = new CaseLitigantTel();
//                                        caseLitigantTel.setCaseId(caseId);
//                                        caseLitigantTel.setSdLitigantId(caseLitigantId);
//                                        caseLitigantTel = importCaseTool.fillTel(caseLitigantTel,  tel, telMap);
//                                        if(caseLitigantTel!=null){
//                                            caseLitigantTelService.save(caseLitigantTel);
//                                        }
//                                    }
//                                }
//
//                            }
//                            Boolean isSuccess=null;
//
//                            if (importCaseTool.getExcelErrorMsg().hasError()) {
//                                if(!isOneCase){
//                                    isSuccess = false;
//                                }
//                                if(isSave){
//                                    importCaseTool.getExcelErrorMsg().isSaveSuccess(i, isSuccess);
//                                }else{
//                                    importCaseTool.getExcelErrorMsg().isUpdateSuccess(i, isSuccess,caseCodeVar);
//                                }
//                                throw new RuntimeException("异常");
//                            } else {
//                                if(!isOneCase){
//                                    isSuccess = true;
//                                }
//                                if(isSave){
//                                    importCaseTool.getExcelErrorMsg().isSaveSuccess(i, isSuccess);
//                                }else{
//                                    importCaseTool.getExcelErrorMsg().isUpdateSuccess(i, isSuccess,caseCodeVar);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            throw e;
//                        }
//                    }
//                });
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return importCaseTool.getExcelErrorMsg().getMsg();
//    }
//
//
//}
