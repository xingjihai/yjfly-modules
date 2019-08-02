;(function () {

    $.wt = $.wt || {};

    var CurrCourtId="";  //只对当前页面有效

    $.extend($.wt, {
        isEmpty:function (obj) {
            if(typeof obj =="undefined" || obj==null || obj ==""||obj=="undefined"||obj=="null" ){
                return true;
            }else {
                return false;
            }
        },
        consoleError:function (e) {
            if (window.console && console.error) {
                console.error(e);
            }else if (window.console && console.log) {
                console.log(e);
            }
        },
        setCurrUser:function (user) {
            try {
                sessionStorage["currUser"]=JSON.stringify(user);
            } catch (e) {
                $.wt.consoleError(e);
            }
        }
        /**
         id: "${user.id}",
         name: "${user.name}"
         */
        ,getCurrUser:function () {
            try {
                var key="currUser";
                if( !$.wt.isEmpty( sessionStorage[key] ) ){
                    return JSON.parse(sessionStorage[key])||{};
                }else {
                    return "";
                }
            } catch (e) {
                $.wt.consoleError(e);
            }
        }
        ,setCurrCourtId:function (courtId) {
            // sessionStorage["currCourtId"]=JSON.stringify(courtId);
            CurrCourtId=courtId;
        }
        ,getCurrCourtId:function () {
            // var key="currCourtId";
            // if(sessionStorage[key]!=null&&typeof sessionStorage[key]!="undefined"&&sessionStorage[key]!=""){
            //     return JSON.parse(sessionStorage[key])||"";
            // }else{
            //     return "";
            // }
            return CurrCourtId;
        }
        ,getCourtIdByCase:function (caseId) {
            var courtId;
            $.wt.ajax({
                async: false,
                url: "wt://sd/allcaselist/getcourtid",
                data: {caseId: caseId},
                success: function (result) {
                    if(result.code==0){
                        courtId=result.data;
                    }
                }
            });
            return courtId;
        }
        ,getCacheKey:function (key) {
            try {
                var currCourtId=$.wt.getCurrCourtId();
                if(currCourtId!=null&&currCourtId!=""){
                    key="c-"+currCourtId+":"+key;
                    return key;
                }else{
                    var currUser=$.wt.getCurrUser();
                    key="u-"+currUser.id+":"+key;
                    return key;
                }
            } catch (e) {
                $.wt.consoleError(e);
                return key;
            }
        }
        /**
         * 存储缓存
         * @param key 缓存关键字
         * @param data 要缓存的内容
         */
        ,setCache:function (key,data,isDiffUser) {
            try {
                if(key==null||key==""||data==null){
                    throw new Error( "$.wt.setCache,参数错误");
                    return;
                }
                var cacheKey = key;
                if (isDiffUser == null || isDiffUser) {
                    cacheKey = $.wt.getCacheKey(key);
                }
                if(data!=null){
                    sessionStorage[cacheKey]=JSON.stringify(data);
                }
            } catch (e) {
                $.wt.consoleError(e);
            }
        }
        /**获取缓存
         * @param key  要缓存的关键字
         * @param getValueFun  没有缓存时的值获取方式（function） --注意同步处理
         */
        ,getCache:function(key,getValueFun,isDiffUser){
            try {
                if(key==null||key==""||getValueFun==null){
                    throw new Error( "$.wt.getCache,参数错误");
                    return;
                }
                var cacheKey=key;
                if(isDiffUser==null||isDiffUser){
                    cacheKey=$.wt.getCacheKey(key);
                }
                if(  !$.wt.isEmpty(sessionStorage[cacheKey])  ){
                    return JSON.parse(sessionStorage[cacheKey])||{};
                }
                var data=getValueFun.call(this);
                if(data==null){
                    throw new Error( "$.wt.getCache,获取值方法错误,请检查");
                    return ;
                }
                $.wt.setCache(  key,data,isDiffUser  ) ;
                return data;
            } catch (e) {
                $.wt.consoleError(e);
                return "error!!!";
            }
        }

        /**
         * 字典缓存获取
         * @param dicType  字典类型关键字
         * TB_NATION 民族
         * TB_NATIONALITY 国籍
         * @param value  根据value获取对应的字典值
         */
        ,getDicValue:function(dicType,value){
            try {
                if (dicType == null || dicType == "") {
                    throw new Error( "$.wt.getDicValue,参数错误" );
                }
                if (value == null) {
                    console.log("$.wt.getDicValue,传入value为null");
                    return "";
                }

                var keyWords="unkown";
                var url="";
                if(dicType=="TB_NATION"){
                    keyWords="TB_NATION";
                    url="wt://sd/nation/list";
                }else if(dicType=="TB_NATIONALITY"){
                    keyWords="TB_NATIONALITY";
                    url="wt://sd/nationality/list";
                }else if(dicType=="TB_CASECAUSE"){
                    keyWords="TB_CASECAUSE";
                    url="wt://sd/metterreason/select";
                }else{
                    keyWords="DIC_" + dicType ;
                    url="wt://sys/dictionary/cache/list";
                }
                var dictionaryArr = $.wt.getCache(keyWords, function () {
                    var dicArr = {};
                    $.wt.ajax({
                        async: false,
                        url: url ,
                        data: {code: dicType},
                        success: function (result) {
                            var dictionarys = result.data;
                            for (key in dictionarys) {
                                dicArr[dictionarys[key].id] = dictionarys[key].text;
                            }
                        }
                    });
                    return dicArr;
                });


                if (dictionaryArr[value] != null) {
                    return dictionaryArr[value];
                }
                throw new Error("$.wt.getDicValue,无法获取字典:" + dicType + "=" + value);
            } catch (e) {
                $.wt.consoleError(e);
                return value;
            }
        }

        ,getDateStr:function (time,fmt) {
            try {
                if( typeof time=="number" ){
                    return $.wt.getDateStrByTimestamp(time,fmt);
                }else if(  time instanceof Date  ){
                    return $.wt.getDateStrByDate(time,fmt);
                }else{
                    throw new Error("$.wt.getDateStr,参数time错误");
                }
            } catch (e) {
                $.wt.consoleError(e);
                return "";
            }
        }
        // 对Date的扩展，将 Date 转化为指定格式的String
        // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
        // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
        // 例子：
        // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
        // (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
        ,getDateStrByDate:function (date,fmt) {
            try {
                if( $.wt.isEmpty(date) ){
                    return "";
                }
                if (date.getFullYear() == null) {
                    throw new Error("$.wt.getDateStrByFormat,参数date错误");
                }
                var o = {
                    "M+": date.getMonth() + 1, //月份
                    "d+": date.getDate(), //日
                    "H+": date.getHours(), //小时
                    "m+": date.getMinutes(), //分
                    "s+": date.getSeconds(), //秒
                    "q+": Math.floor((date.getMonth() + 3) / 3), //季度
                    "S": date.getMilliseconds() //毫秒
                };
                if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
                for (var k in o)
                    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                return fmt;
            } catch (e) {
                $.wt.consoleError(e);
                return "";
            }
        }
        /**
         * @param timestamp 时间戳
         * @param fmt
         * @returns {*}
         */
        ,getDateStrByTimestamp:function (timestamp,fmt) {
            try {
                if( $.wt.isEmpty(timestamp) ){
                    return "";
                }
                if((timestamp + "").length <= 10){
                    timestamp=timestamp+"000";
                }
                timestamp=Number(timestamp);
                var date = new Date(timestamp);
                return $.wt.getDateStrByDate(date,fmt);
            } catch (e) {
                $.wt.consoleError(e);
                return "";
            }
        }
        ,formatDate:function (dateStr,fmt) {
            try {
                if( $.wt.isEmpty(dateStr) ){
                    return "";
                }
                var date = new Date( dateStr.replace(/-/g,"/") );
                return $.wt.getDateStrByDate(date,fmt);
            } catch (e) {
                $.wt.consoleError(e);
                return "";
            }
        }
        /**
         * var defaultFields =[ 默认显示字段 ];
         var optionalFields = [ 设置字段 ];
         setGridFields(gridData, defaultFields.concat(optionalFields));
         */
        ,setGridFields:function (gridConfig, fields) {
            for (var field in gridConfig) {
                if (gridConfig[field])
                    gridConfig[field].key = true;
            }
            for (var i = 1; i < fields.length; i++) {
                var field = fields[i];
                if (gridConfig[field])
                    gridConfig[field].key = false;
            }
        }
        /**
         * 简单模板替换
         * @param templateContent  模板html {参数}
         * @param objData
         */
        ,replaceTemplate:function(templateContent,objData){
            if(templateContent==null){
                return "";
            }
            for (var key in objData) {
                var restr=new RegExp("{"+key+"}","g");
                templateContent=templateContent.replace(restr,objData[key]);
            }

            templateContent=templateContent.replace(/null/g,"");
            return templateContent;
        }
        /**
         * 获取最上层元素
         * @param elem
         */
        ,getTopElem:function (elem) {
            var w=window;
            var result;
            while (true) {
                try {
                    if(w[elem]!=null){
                        result=w[elem];
                        break;
                    }
                    w=w.parent;
                } catch (e) {
                    break;
                }
            }
            return result;
        }
        /**
         * select2 下拉缓存
         * @param id
         * @param options
         */
        ,selectCache:function (id, options) {
            if(options && options.url){
                var selectData=$.wt.getCache("SELECT_"+options.url,function () {   //获取缓存,关键字为selectObj加上**
                    var selectData;
                    $.wt.ajax({
                        async:false,
                        url:options.url,
                        success:function (result) {
                            selectData=result.data;
                        }
                    });
                    return selectData;
                });
                options.url=null;
                options.data=selectData;
                return $.wt.select2( id, options );
            }else{
                return $.wt.select2( id, options );
            }
        }

        /**与getValues 功能类似
         * 不同点： 可以上传多个同name的数组
         * @param formId
         * @returns {{}}
         */
        ,getFormData:function (formId) {
            var fields=$("#"+formId).serializeArray();
            var o={};
            jQuery.each(fields, function(i, fields){   //数组转param对象.
                if(o[this.name]){
                    /*
                    表单中可能有多个相同标签，比如有多个label，
                    那么你在json对象o中插入第一个label后，还要继续插入，
                    那么这时候o[label]在o中就已经存在，所以你要把o[label]做嵌套数组处理
                    */
                    //如果o[label]不是嵌套在数组中
                    if(!o[this.name].push){
                        o[this.name]=[o[this.name]];     // 将o[label]初始为嵌套数组，如o={a,[a,b,c]}
                    }
                    o[this.name].push(this.value || ''); // 将值插入o[label]
                }else{
                    o[this.name]=this.value || '';       // 第一次在o中插入o[label]
                }
            });
            return o;
        }



        // ==============  基于layui  ========================
        /**
         * 基于laytpl 的模板引擎 https://www.layui.com/doc/modules/laytpl.html
         * @param templeId   模板id （script）
         * @param fillElement  填充元素id (html元素)
         * @param data 数据
         */
        ,laytpl:function (templeId,fillElement,data) {
            try {
                var laytpl;
                layui.use('laytpl', function () {
                    laytpl = layui.laytpl;
                });
                var getTpl = $("#" + templeId)[0].innerHTML;
                laytpl(getTpl).render(data, function (html) {
                    html = html.replace(/null/g, "");
                    $("#" + fillElement)[0].innerHTML = html;
                });
            } catch (e) {
                $.wt.consoleError(e);
            }
        }


        // ============== 环境判断   ====================
        /**
         * 环境判断
         * @param
         * option.h5   普通环境回调
         * option.miniprogram   小程序环境回调
         * option.wx   微信浏览器环境回调
         * option.common   默认回调
         */
        ,switchEnv:function (option) {
            var callFun=function (fun) {
                if(fun){
                    fun.call(this);
                }else if(option.common){
                    option.common.call(this);
                }
            };
            var ready=function(){
                if (window.__wxjs_environment == 'miniprogram') {
                    //微信小程序环境
                    callFun(option.miniprogram);
                }else{
                    //微信浏览器环境
                    callFun(option.wx);
                }
            };
            var ua = navigator.userAgent.toLowerCase();//获取判断用的对象
            if (ua.match(/MicroMessenger/i) == "micromessenger") {  //在微信中打开
                if (!window.WeixinJSBridge || !WeixinJSBridge.invoke) {
                    document.addEventListener('WeixinJSBridgeReady', ready, false)
                } else {
                    ready();
                }
            }else{
                //h5环境
                callFun(option.h5);
            }
        }

    });

})(jQuery);