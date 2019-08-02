package debugger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
/**统计执行时间工具
 * 2018/1/12
 */
public class TimeGap {
    private final static Logger log = LoggerFactory.getLogger(TimeGap.class);
 
 
    public static void main(String[] args) throws Exception{
        TimeGap timeGap = new TimeGap();
        timeGap.start("uri1");
        timeGap.start("uri2");
        Thread.sleep(1000);
        timeGap.end();
        timeGap.start("uri3");
        Thread.sleep(1500);
        timeGap.end();
        timeGap.end();
        timeGap.end();
        timeGap.print();
    }
 
    LinkedList<TimeItem> queue = new LinkedList<>();
    Map<String,Long> result = new HashMap<>();
    /**开始统计
     * @param title
     */
    public void start(String title){
        queue.add(new TimeItem(title));
    }
 
    /**
     * 调用结束，并统计该段代码执行时间
     */
    public void end(){
        if(queue.size()>0){
            TimeItem timeItem = queue.getLast();
            result.put(getTile(),(System.currentTimeMillis()-timeItem.getTime()));
            queue.removeLast();
        }
    }
 
    /**获取父uri
     * @return
     */
    public String getTile(){
        StringBuffer sb = new StringBuffer(50);
        for(TimeItem item:queue){
            sb.append(item.getTitle()).append('+');
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
 
    /**打印统计结果
     * @return
     */
    public void print(){
        Set<String> keys = result.keySet();
        for(String key:keys){
//            System.out.println(key+" = "+result.get(key));
            log.debug(key+" = "+result.get(key)+"ms");
        }
    }
 
    class TimeItem{
        String title;
        long time;
        public TimeItem(){
            this.time = System.currentTimeMillis();
        }
        public TimeItem(String title){
            this();
            this.title = title;
        }
 
        public String getTitle() {
            return title;
        }
 
        public long getTime() {
            return time;
        }
    }
 
 
}
