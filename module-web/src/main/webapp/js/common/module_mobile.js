/**
 *   移动端工具类
 */

;(function () {

    $.wt = $.wt || {};


    $.extend($.wt, {

        // ============== 环境判断   ====================
        /**
         * 环境判断
         * @param
         * option.h5   普通环境回调
         * option.miniprogram   小程序环境回调
         * option.wx   微信浏览器环境回调
         * option.common   默认回调
         */
        switchEnv:function (option) {
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