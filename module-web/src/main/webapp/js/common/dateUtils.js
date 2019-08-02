/**
 *  日期工具
 */

;(function () {

    $.wt = $.wt || {};


    $.extend($.wt, {

        getDateStr:function (time,fmt) {
            try {
                if( typeof time=="number" ){
                    return $.wt.getDateStrByTimestamp(time,fmt);
                }else if(  time instanceof Date  ){
                    return $.wt.getDateStrByDate(time,fmt);
                }else if(typeof time =="string"){
                    return $.wt.formatDateStr(time,fmt);
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
        /** 格式化时间字符串 */
        ,formatDateStr:function (dateStr,fmt) {
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


    });

})(jQuery);