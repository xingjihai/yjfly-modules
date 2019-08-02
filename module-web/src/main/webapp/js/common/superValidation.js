/**  =========   js验证        =============
 *  提示基于 layer.tips
 *  @author  wuyijia
 使用：
 控件中添加  rule 属性，如 rule="required;positiveInteger"
 rule说明:
 1、各个规则用";"号隔开
 2、参数用  "[]" 包含,  用"," 号分隔

 rule的message属性可以用参数， 如{0} 表示参数一

 js中添加判断：
 if(!$.wt.superValidation( $("#copyCaseForm") )){
    return false;
}
 */

;(function(){
    var superRule={
        //=======    日期    ============= //
        /**
         * 调用方式  rule="afterDate[#id]"
         */
        afterDate:{
            validator: function(value,param){
                if(value!=null&&value!=""){
                    var current=getDateByStr(value);
                    var before=getDateByStr( $(param[0]).val() );
                    if(current<before){
                        return false;
                    }
                }
                return true;
            },
            message: "不能早于起始时间"
        },
        /** 不晚于当前时间返回true */
        timeNotAfterNow:{
            validator: function(value,param){
                if(value!=null&&value!=""){
                    var date=new Date(  value.replace(/-/g,"/") );
                    var now=new Date();
                    if(date>now){
                        return false;
                    }
                }
                return true;
            },
            message: "不晚于当前时间"
        },
        /** 晚于当前时间返回true */
        timeNotBeforeNow:{
            validator: function(value,param){
                if(value!=null&&value!=""){
                    var date=new Date(  value.replace(/-/g,"/") );
                    var now=new Date();
                    if(date<=now){
                        return false;
                    }
                }
                return true;
            },
            message: "需晚于当前时间"
        },

        // ========== 范围 ============= //
        charMax:{
            validator: function(value,param){
                if(value!=null&&value.length>param[0]){
                    return false;
                }
                return true;
            },
            message: "不能超过{0}个字符"
        }


        // ==========  格式  ============= //
        ,positiveInteger:{
            validator:function(value){
                return /^[0-9]\d*$/.test(value);
            },
            message:"请输入正整数"
        }
        ,doubleNumber:{
            validator: function (value, param) {
                return /^([1-9]\d{0,15}|0)(\.\d{1,4})?$/.test(value);
            },
            message: '请输入大于0的数字'
        }
        ,numberBetween:{
            validator: function (value, param) {
                var min=param[0];
                var max=param[1];

                if( isNaN(value)){
                    return false;
                }

                if(min!=null&&parseFloat(value)<parseFloat(min)){
                    return false;
                }
                if(max!=null&&parseFloat(value)>parseFloat(max)){
                    return false;
                }
                return true;
            },
            message: '请输入介于{0}到{1}的数字'
        }

        ,mobile: {
            validator: function (value, param) {
                if(value==""){
                    return false;
                }
                //return /^0?(13[0-9]|15[012356789]|18[02356789]|14[57]|170)[0-9]{8}$/.test(value);
                //return /^0?(13[0-9]|15[012356789]|18[023456789]|14[57]|17[02356789])[0-9]{8}$/.test(value);
                return /^1[34578]\d{9}$/.test(value);

            },
            message: '手机号码不正确'
        }, tels: {                 //多个电话 , 逗号分隔
            validator: function (value, param) {
                if (value == "") {
                    return false;
                }
                var arr = value.split(",");
                for (var i = 0; i < arr.length; i++) {
                    if(!(/^[0-9]\d*[-]?\d*$/.test(arr[i]))){
                        return false;
                    }
                }
                return true;
                //return /^0?(13[0-9]|15[012356789]|18[02356789]|14[57]|170)[0-9]{8}$/.test(value);
                //return /^0?(13[0-9]|15[012356789]|18[023456789]|14[57]|17[02356789])[0-9]{8}$/.test(value);
            },
            message: '电话号码输入不正确'
        }

        /** 文件支持格式   accept[doc|docx] */
        ,accept:{
            validator: function (value, param) {
                var key=param[0];
                if(key==null||key==""){
                    throw new Error("rule:accept 参数不能为空");
                }
                var mime = value.substring(value.lastIndexOf(".")+1).toLowerCase();
                var arr = key.split("|");
                for (var i = 0; i < arr.length; i++) {
                    if( arr[i]==mime ){
                        return true;
                    }
                }
                return false;
            },
            message: '请上传{0}类型文件'
        }

        // ==========  其他验证  ============= //

        /**
         * 参数1: 同组关键字
         */
        ,equalArr:{}  //判重数组
        ,notEqual:{
            validator: function (value, param) {
                var key=param[0];
                if(key==null||key==""){
                    throw new Error("rule:notEqual 参数不能为空");
                }
                if(superRule.equalArr[key]==null){
                    superRule.equalArr[key]={};
                }
                if(value!=null&&value!=""&&superRule.equalArr[value]!=null ){
                    return false;
                }else{
                    superRule.equalArr[value]="notEqual";
                }
                return true;
            },
            message: '重复输入'
        }
        ,idNumber:{
            validator:function (value,param) {
                return /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(value);
            },
            message:'身份证格式不正确'
        }
        ,email:{
            validator:function (value,param) {
                return /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test(value);
            },
            message:'邮箱格式不正确'
        }
        ,onlyNumberOrEng:{
            validator:function (value,param) {
                return /^[A-Za-z0-9]+$/.test(value);
            },
            message:'只能输入数字或者英文'
        }
        ,account:{
            validator:function (value,param) {
                return /^(([1-9][0-9]*)|(([0]\.\d{0,2}|[1-9][0-9]*\.\d{0,2})))$/.test(value);
            },
            message:'输入金额不正确，小数点后面只能有2位'
        }
    };

    /**
     * 支持ie8
     */
    var getDateByStr=function(dateStr) {
        if(dateStr==null||dateStr==""){
            return null;
        }
        var value=dateStr.replace(/-/g,"/");
        if(value.length<=4){ //如 "2018"
            value+="/01/01";
        }else if( value.length<=7 ){ //如 "2018/01"
            value+="/01";
        }
        return new Date(  value );
    };

    $.wt = $.wt || {};
    $.extend($.wt, {
        superValidation:function (area) {
            try {
                superRule.equalArr={};  //初始化判重数组
                var validateArr = area.find("input:visible,select:visible,textarea:visible,.forceValidation");  //TODO 完善 其他特殊的输入框
                for (var i = 0; i < validateArr.length; i++) {
                    var name = validateArr[i].getAttribute("name");
                    var rule = validateArr[i].getAttribute("rule");
                    if (name == null || name == '') {
                        continue;
                    }
                    if (rule != null && rule != '') {
                        var ruleArr = rule.split(";");
                        for (var j = 0; j < ruleArr.length; j++) {

                            var start = ruleArr[j].indexOf("[");
                            var end = ruleArr[j].indexOf("]");
                            var params = [];
                            var key = ruleArr[j];
                            if (start > -1 && end > -1 && start < end) {
                                params = (ruleArr[j].substring(start + 1, end)).split(",");
                                key = ruleArr[j].substring(0, start);
                            }
                            switch (key) {
                                case 'required':
                                    if (validateArr[i].value == null || validateArr[i].value == '') {
                                        var requiredMsg="不能为空";
                                        if(params.length>0){
                                            requiredMsg=params[0];
                                        }
                                        if( $(validateArr[i]).is(":hidden")){
                                            layer.msg(requiredMsg);
                                        }else{
                                            layer.tips( requiredMsg , validateArr[i]);
                                        }
                                        $(validateArr[i]).focus();  //会导致layDate日期出不来
                                        $(validateArr[i]).blur(); //会导致layDate日期出不来--取消焦点来解决
                                        return false;
                                    }
                                    break;
                                default:
                                    var rule = superRule[key];
                                    if (validateArr[i].value != null && validateArr[i].value != '') { //非空
                                        if (rule != null && !rule.validator(validateArr[i].value, params)) {

                                            for (var index in params) {
                                                rule.message=rule.message.replace("{"+index+"}",params[index]);
                                            }

                                            if( $(validateArr[i]).is(":hidden")){
                                                layer.msg(rule.message);
                                            }else{
                                                layer.tips( rule.message , validateArr[i]);
                                            }
                                            $(validateArr[i]).focus();  //会导致layDate日期出不来
                                            $(validateArr[i]).blur(); //会导致layDate日期出不来--取消焦点来解决
                                            return false;
                                        }
                                    }
                            }

                        }
                    }
                }
                return true;
            } catch (e) {
                console.error(e);
                return true;
            }
        }

    });


    /**
     * 拓展 $.wt.rule 对象
     */
    $.wt.rule=$.wt.rule||{};
    for (var rule in superRule) {
        $.wt.rule[rule]=superRule[rule].validator;
    }

})(jQuery);