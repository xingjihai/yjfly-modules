package ocr.service;

import com.qcloud.image.ImageClient;
import com.qcloud.image.request.GeneralOcrRequest;
import com.qcloud.image.request.IdcardDetectRequest;
import com.qcloud.image.request.OcrBankCardRequest;
import net.sf.json.JSONObject;
import ocr.entity.OcrBankCardVO;
import ocr.entity.OcrIdCardVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class OCRServiceImpl implements IOCRservice {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCRServiceImpl.class);

    private Properties config;

    /**
     * 调用腾讯OCR接口获取身份证信息
     *
     * @param file
     * @param cardType 0 为身份证有照片的一面（正面）；1 为身份证有国徽的一面（反面）。如果未指定，默认为0，但反面必须填1。
     * @return
     */
    @Override
    public OcrIdCardVO getIdCardInfo(File file, int cardType) {
        String appId = config.getProperty(OCRConstants.APPID);
        String secretId = config.getProperty(OCRConstants.SECRETID);
        String secretKey = config.getProperty(OCRConstants.SECRETKEY);
        System.out.println("appid:" + appId + ",secId:" + secretId + "sekey:" + secretKey);
        ImageClient imageClient = new ImageClient(appId, secretId, secretKey, OCRConstants.DOMAIN);

        OcrIdCardVO ocrIdCardVO = new OcrIdCardVO("0");
        String realCode = "";
        String realMessage = "";
        try {
            File[] idcardImageList = new File[]{file};
            System.out.println(idcardImageList[0].getPath());
            IdcardDetectRequest idReq = new IdcardDetectRequest("", idcardImageList, cardType);
            String ret = imageClient.idcardDetect(idReq);
            Map map = JSONObject.fromObject(ret);
            List list = (List) map.get(OCRConstants.RESULT_LIST_KEY);
            Map message = (Map) list.get(0);
            ocrIdCardVO.setCode(String.valueOf(message.get("code")));
            ocrIdCardVO.setMessage(String.valueOf(message.get("message")));
            realCode = String.valueOf(message.get("code"));
            realMessage = String.valueOf(message.get("message"));
            Map data = (Map) message.get("data");
            if (cardType == 0) { // 正面
                ocrIdCardVO.setIdNumber(String.valueOf(data.get("id")));
                ocrIdCardVO.setName(String.valueOf(data.get("name")));
                ocrIdCardVO.setSex(String.valueOf(data.get("sex")));
                ocrIdCardVO.setBirth(String.valueOf(data.get("birth")));
                ocrIdCardVO.setAddress(String.valueOf(data.get("address")));
                String nation = String.valueOf(data.get("nation"));
                ocrIdCardVO.setNation(nation.contains("族") ? nation : nation + "族");
            } else if (cardType == 1) { // 反面
                ocrIdCardVO.setValidDate(String.valueOf(data.get("valid_date")));
                ocrIdCardVO.setAuthority(String.valueOf(data.get("authority")));
            }
        } catch (Exception e) {
            ocrIdCardVO.setCode("-1");
            ocrIdCardVO.setMessage("获取身份证信息有误，请重新上传！");
            LOGGER.error("OCR身份证识别失败。。。code：" + realCode + " ,message: " + realMessage);
            e.printStackTrace();
        }

        return ocrIdCardVO;
    }

    /**
     * 获取银行卡信息
     * @param file
     * @return
     */
    @Override
    public OcrBankCardVO getBankCardInfo(File file) {
        String appId = config.getProperty(OCRConstants.APPID);
        String secretId = config.getProperty(OCRConstants.SECRETID);
        String secretKey = config.getProperty(OCRConstants.SECRETKEY);
        ImageClient imageClient = new ImageClient(appId, secretId, secretKey, OCRConstants.DOMAIN);
        OcrBankCardVO ocrBankCard = new OcrBankCardVO("0");
        String realCode = "";
        String realMessage = "";
        try{
            OcrBankCardRequest request = new OcrBankCardRequest("", file);
            String ret = imageClient.ocrBankCard(request);
            Map map = JSONObject.fromObject(ret);
            ocrBankCard.setCode(String.valueOf(map.get("code")));
            ocrBankCard.setMessage(String.valueOf(map.get("message")));
            realCode = String.valueOf(map.get("code"));
            realMessage = String.valueOf(map.get("message"));
            Map data = (Map) map.get("data");
            List<Map> items = (List) data.get("items");
            for (Map item : items) {
                String itemstring = String.valueOf(item.get("itemstring"));
                if ("卡号".equals(String.valueOf(item.get("item")))){
                    ocrBankCard.setCardId(itemstring);
                }
                if ("卡类型".equals(String.valueOf(item.get("item")))){
                    ocrBankCard.setCardType(itemstring);
                }
                if ("卡名字".equals(String.valueOf(item.get("item")))){
                    ocrBankCard.setCardName(itemstring);
                }
                if ("银行信息".equals(String.valueOf(item.get("item")))){
                    ocrBankCard.setBankInfo(itemstring);
                }
                if ("有效期".equals(String.valueOf(item.get("item")))){
                    ocrBankCard.setValid_date(itemstring);
                }

            }
        }catch (Exception e){
            ocrBankCard.setCode("-1");
            ocrBankCard.setMessage("获取银行卡信息代码出错！");
            LOGGER.error("OCR银行卡识别失败。。。code：" + realCode + " ,message: " + realMessage);
            e.printStackTrace();
        }
        return ocrBankCard;
    }

    /**
     * 通用OCR获取文字信息
     *
     * @param imageClient
     * @param file
     * @return
     */
    @Override
    public String getGeneralInfo(ImageClient imageClient, File file) {
        String ret = null;
        GeneralOcrRequest request = new GeneralOcrRequest("", file);
        Map map = null;
        String info = null;
        try {
            ret = imageClient.generalOcr(request);
            map = JSONObject.fromObject(ret);
            Map<String, Object> map1 = (Map<String, Object>) ((List) ((Map) map.get("data")).get("items")).get(0);
            info = (String) map1.get("itemstring");
            System.out.println("ocrGeneral:" + ret);
            return info;
        } catch (Exception e) {
            LOGGER.error("OCR识别失败。。。code：" + map.get("code") + " : " + map.get("message"));
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

}
