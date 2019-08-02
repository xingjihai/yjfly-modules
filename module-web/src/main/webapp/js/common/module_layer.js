/**
 *  // ==============  基于layui  ========================
 *  layer 进一步封装
 */

;(function () {

    $.wt = $.wt || {};


    $.extend($.wt, {

        /**
         * 基于laytpl 的模板引擎 https://www.layui.com/doc/modules/laytpl.html
         * @param templeId   模板id （script）
         * @param fillElement  填充元素id (html元素)
         * @param data 数据
         */
        laytpl:function (templeId,fillElement,data) {
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

    });

})(jQuery);