var m = {
    init: function(){
        this.nav();
        this.slide();
        this.radio();
        this.checkbox();
    },
    nav: function(){
        $("#top").find('.menu').on("click", function(){
            if( $(this).is(".open") ){
                $(this).removeClass("open");
                $(".nav").animate({"left": "-320px"}, 300);
            }else{
                $(this).addClass("open");
                $(".nav").animate({"left": "0px"}, 300);
            }
        });
        $(".nav").on("click", function(e){
            if( !$(e.target).is(".nav-shadow") ){
                $("#top").find('.menu').removeClass("open");
                $(".nav").animate({"left": "-320px"}, 300);
            } 
        });
    },
    slide: function(){
        if( $(".swiper-container").size() > 0 ){
            $('<script/>').attr({'type':'text/javascript','src':'http://127.0.0.1:9015/tire/common/js/html5/idangerous.swiper.js'}).appendTo('body');
            //$('<script/>').attr({'type':'text/javascript','src':'http://115.28.243.211:8080/tire/common/js/html5/idangerous.swiper.js'}).appendTo('body');
            var mySwiper = $('.swiper-container').swiper({
                mode: 'horizontal',
                speed: 500,
                autoplay: 5000,
                loop: true,
                pagination: ".swiper-pagination"
            });
        }
    },
    radio: function(){
        if( $(".radio").size() > 0 ){
            $(":radio").on("click", function(){
                if( !$(this).parents(".radio").is(".checked") ){
                    $(this).parents(".radio").addClass("checked");
                    $(this).prop("checked", true);
                    $(this).parents(".radio").siblings(".radio").removeClass("checked");
                    $(this).parents(".radio").siblings(".radio").find(":radio").prop("checked", false);
                }
            });
        }
    },
    checkbox: function(){
        if( $(".checkbox").size() > 0 ){
            $(":checkbox").on("click", function(){
                if( $(this).parents(".checkbox").is(".checked") ){
                    $(this).parents(".checkbox").removeClass("checked");
                    $(this).prop("checked", false);
                }else{
                    $(this).parents(".checkbox").addClass("checked");
                    $(this).prop("checked", true);
                }
            });
        }
    },
    localStorage: function(){
        if(window.localStorage){
            if( $(".uid").size() > 0 ){
                var m = $(".uid").text().substr(-11);
                if( m != localStorage.getItem("mobile") ){
                    localStorage.setItem("mobile", m);
                } 
            }
            //localStorage.clear();
        }  
    },
    geoLocation: function(){
        if(navigator.geolocation){
            if(window.localStorage){
                navigator.geolocation.getCurrentPosition(savePosition,showError);
            }else{
                alert("\u6d4f\u89c8\u5668\u4e0d\u652f\u6301\u672c\u5730\u5b58\u50a8");
                return;
            }
        }else{
            alert("\u6d4f\u89c8\u5668\u4e0d\u652f\u6301\u5730\u7406\u4f4d\u7f6e");
            return;
        }

        function savePosition(position){
            if(window.localStorage){
                localStorage.setItem("latitude", position.coords.latitude);
                localStorage.setItem("longitude", position.coords.longitude);
            };
        }
        function showError(error){
            switch(error.code) {
                case error.PERMISSION_DENIED:
                    alert("\u7528\u6237\u62d2\u7edd\u5171\u4eab\u5730\u7406\u4f4d\u7f6e");
                    break;
                case error.POSITION_UNAVAILABLE:
                    alert("\u5730\u7406\u4f4d\u7f6e\u4fe1\u606f\u4e0d\u53ef\u7528");
                    break;
                case error.TIMEOUT:
                    alert("\u83b7\u53d6\u5730\u7406\u4f4d\u7f6e\u8d85\u65f6");
                    break;
                case error.UNKNOWN_ERROR:
                    alert("\u672a\u77e5\u9519\u8bef");
                    break;
            }
        }
    }
};

function InAjaxLoader(){
    var h = $(document).height();
    $("<div class=\"ajax-overlay\" />").height(h).appendTo("body");
    $("<div class=\"ajax-loader\" />").appendTo("body");
}

function UnAjaxLoader(){
    $(".ajax-loader").remove();
    $(".ajax-overlay").remove();
}

// jquery cookie plugin
jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options = $.extend({}, options);
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

$(function(){
    m.init()
})

