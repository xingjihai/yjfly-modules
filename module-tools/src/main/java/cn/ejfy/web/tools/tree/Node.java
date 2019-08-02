package cn.ejfy.web.tools.tree;

public class Node {
    private Integer id;
    private Integer pid;
    private String text;
    
    public Node(Integer id,Integer pid,String text) {
        this.id=id;
        this.pid=pid;
        this.text=text;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPid() {
        return pid;
    }
    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
