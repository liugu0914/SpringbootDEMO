var Ajax = {
    POST: 'POST',
    GET: 'GET',
    JSON: 'json',
    HTML: 'html',
    post: function (url, data) {
        var settings = {
            url: url,
            data: data || {},
            type: Ajax.POST,
            dataType: Ajax.JSON
        };
        Ajax.send(settings);
    },
    get: function (url,data) {
        var settings = {
            url: url,
            data: data || {},
            type: Ajax.GET,
            dataType: Ajax.JSON
        };
        Ajax.send(settings);
    },
    /*
     * 交互唯一处理方式
     */
    send: function (op) {
        //从本地获取token
        var token = localStorage.getItem('access_token') || '';
        var settings = {
            url: op.url,
            headers: {
                "access_token": token
            },
            type: op.type || 'POST',
            dataType: op.dataType || 'json',
            data: op.data || {},
            success: Ajax.success,
            error: Ajax.error
        };
        settings=$.extend({}, settings, op);
        $.ajax(settings);
    },
    success: function (res) {
        console.info(res);
    },
    error: function (XMLHttpRequest) {
        console.info(XMLHttpRequest);
    },
    setCookie:function(key,value,exdays){
        var expires="";
        if(exdays){
            var d = new Date();
            d.setTime(d.getTime() + (exdays*24*60*60*1000));//有效期一天
            expires = "expires="+ d.toUTCString();
        }
        document.cookie = key + "=" + value + ";" + expires + ";path=/";
    },
    getCookie: function(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(';');
        for(var i = 0; i <ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
             }
             if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
             }
         }
        return "";
    } 
}