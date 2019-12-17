var Ajax = {
    POST: 'POST',
    GET: 'GET',
    JSON: 'json',
    HTML: 'html',
    post: function (url, data,suc,err) {
        var settings = {
            url: url,
            data: data || {},
            type: Ajax.POST,
            suc:suc,
            err:err,
            dataType: Ajax.JSON
        };
        Ajax.send(settings);
    },
    get: function (url,data,suc,err) {
        var settings = {
            url: url,
            data: data || {},
            type: Ajax.GET,
            suc:suc,
            err:err,
            dataType: Ajax.JSON
        };
        Ajax.send(settings);
    },
    /*
     * 交互唯一处理方式
     */
    send: function (op) {
        //从本地获取token
        var settings = {
            url: op.url,
            type: op.type || Ajax.POST,
            dataType: op.dataType || Ajax.JSON,
            data: op.data || {},
            success:op.suc ||  Ajax.success,
            error: op.err ||  Ajax.error
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