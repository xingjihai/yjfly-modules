/**
 *  表单工具
 */

;(function () {

    $.wt = $.wt || {};


    $.extend($.wt, {

        /** 获取form表单数据
         * 可以上传多个同name的数组
         * @param formId
         * @returns {{}}
         */
        getFormData:function (formId) {
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

    });

})(jQuery);