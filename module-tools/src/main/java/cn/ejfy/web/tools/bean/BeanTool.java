package cn.ejfy.web.tools.bean;

import java.util.Date;


import cn.ejfy.web.tools.Member;

public class BeanTool {
    public static void main(String[] args) {
        Member member=new Member();
        Member member2=new Member();
        member.setMember_id(1);
        member.setDate(new Date());
        org.springframework.beans.BeanUtils.copyProperties(member, member2,Member.class);
        System.out.println(member2.getDate());
        System.out.println(member2.getMember_id());
    }
}
