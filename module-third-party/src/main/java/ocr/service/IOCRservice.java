package ocr.service;

import com.qcloud.image.ImageClient;
import ocr.entity.OcrBankCardVO;
import ocr.entity.OcrIdCardVO;

import java.io.File;

public interface IOCRservice {

    OcrIdCardVO getIdCardInfo(File file, int cardType);

    String getGeneralInfo(ImageClient imageClient, File file);

    OcrBankCardVO getBankCardInfo(File file);
}
