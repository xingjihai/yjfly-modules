package cn.ejfy.web.tools.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ejfy.web.tools.datatype.MapTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TreeTools {
    
    /**
     * @param allList   以map为单元的List
     * @param allMap  所有的结点<id,Map>形式
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<Map<String, Object>>  getTree(List<T> list,String idChar,String pidChar) throws Exception{
        Map<Object,Object> allMap=new HashMap<>();
        List<Map<String,Object>> allList=new ArrayList<>();
        for (T node : list) {
            Map<String,Object> map= MapTool.objectToMap(node);
            allMap.put( map.get( idChar ), map);
            allList.add(map);
        }
        List<Map<String,Object>> treeList=new ArrayList<>();
        
        for (Map<String,Object> node : allList) {
            boolean isTop=true;

            Map<String,Object> pNode=(Map<String,Object>)allMap.get( node.get( pidChar ) );
            if( pNode!=null){
                List<Map<String,Object>> childList=(List<Map<String, Object>>)pNode.get( "childList");
                if(childList!=null){
                    childList.add(node );
                }else{
                    childList=new ArrayList<>();
                    childList.add(  node );
                    pNode.put( "childList",childList );
                }
                isTop=false;
            }

            if(isTop){
                treeList.add( node );
            }
        }
        return treeList;
    }
    
    public static void main(String[] args) throws Exception {
        List<Node> list=new ArrayList<>();
        list.add(new Node(1,0,"第一级"));
        list.add(new Node(2,0,"第一级"));
        list.add(new Node(3,0,"第一级"));
        list.add(new Node(4,0,"第一级"));
        
        list.add(new Node(5,1,"第二级"));
        list.add(new Node(6,1,"第二级"));
        list.add(new Node(7,2,"第二级"));
        list.add(new Node(8,3,"第二级"));
        
        list.add(new Node(9,5,"第三级"));
        list.add(new Node(10,6,"第三级"));
        
        
        List<Map<String,Object>> treeList=getTree(list, "id", "pid");
        System.out.println( JSONArray.fromObject(treeList).toString() );
    }
    
    
}
