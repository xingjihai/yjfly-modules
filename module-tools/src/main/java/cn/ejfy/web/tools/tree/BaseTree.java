package cn.ejfy.web.tools.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public  abstract class BaseTree  {
    private String tree_node;  //树节点
    private String parent_node;  //父节点
    /**
     *  children不声明为具体的泛型，否则getChildren() 将无法转换为具体子类。
     */
    private List children=new ArrayList<>(); 
    
    public String getTree_node() {
        tree_node=tree_node==null?init_TN():tree_node;
        return tree_node;
    }

    public void setTree_node(String tree_node) {
        this.tree_node = tree_node;
    }

    public String getParent_node() {
        parent_node=parent_node==null?init_PN():parent_node;
        return parent_node;
    }

    public void setParent_node(String parent_node) {
        this.parent_node = parent_node;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }
    
    /**初始化树节点
     */
    public abstract String init_TN();
    /**父节点
     */
    public abstract String init_PN();

    /**
     * 将结果集中的结果整理成一颗树。
     * @return 
     */
    @SuppressWarnings("unchecked")
    public static  <T extends BaseTree> List<T> sortForTree(List<T> list){      
        List<T> viewList=new ArrayList<T>();
        //1、搜集集合中的id
        List<String> pidList=new ArrayList<String>();
        for (BaseTree item : list) {
            pidList.add(item.getTree_node());
        }
        //2、遍历集合，查询是否有父类在集合中,有则将它添加到父类中
        Iterator<T> iter=list.iterator();
        while(iter.hasNext()){
            T item=iter.next();
            boolean isTop=true;
            for (String pid : pidList) {
                if(item.getParent_node()==null){   
                    break;
                }
                if(item.getParent_node().compareTo(pid)==0){
                    T p=getbyId(list,pid);  //根据id查找父类
                    p.getChildren().add(item);
                    isTop=false;
                }
            }
            if(isTop){
                viewList.add(item);
            }
        }
        
        return viewList;
    }
     //根据id查找父类
    private static <T extends BaseTree> T getbyId(List<T> list,String id){
        for (T item : list) {
            if(item.getTree_node().compareTo(id)==0){
                return item;
            }
        }
        return null;
    }
}
