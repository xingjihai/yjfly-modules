package cn.ejfy.web.tools.excel.xmlpoi;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ExcelUtilTest {


    @Test
    public void test(){
        //读取xlsx
        try {
            Map<Integer, List<String[]>> map = ExcelUtil.readAllSheets("C:\\Users\\Administrator\\Desktop\\新建文件夹 (2)\\import_sstf_model (1).xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
