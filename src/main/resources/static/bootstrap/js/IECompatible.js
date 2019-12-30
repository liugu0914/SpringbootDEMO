//IE的console兼容处理
(function() {
    var method;
    var noop = function () {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeline', 'timelineEnd', 'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());
// 注：经验证在IE7/8/9下有效。

// 判断IE浏览器版本
function IEVersion() {
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
	var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
	var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
	if(isIE) {
		var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
		reIE.test(userAgent);
		var fIEVersion = parseFloat(RegExp["$1"]);
		if(fIEVersion == 7) {
			return 7;
		} else if(fIEVersion == 8) {
			return 8;
		} else if(fIEVersion == 9) {
			return 9;
		} else if(fIEVersion == 10) {
			return 10;
		} else {
			return 6; //IE版本<=7
		}
	} else if(isEdge) {
		return 'edge'; //edge
	} else if(isIE11) {
		return 11; //IE11  
	} else {
		return -1; //不是ie浏览器
	}
}

//兼容IE9+的placeholder写法
$(function() {
	var ie=IEVersion();
	if(ie!=-1){
		console.log("IE ",ie);
		//ie浏览器隐藏切换明文密码的图标
		$(".switchPwdMode").css("display","none");
		//ie9以下占位符兼容处理
		if(ie<=9){
			console.log("ie9及以下占位符兼容处理");
			//占位符数组
			var hasplaceArr = $("input[placeholder]");
			hasplaceArr.each(function() {
				var current = $(this),
					currentPlace = current.attr("placeholder"),
					currentType = (current.attr("type") || '').toLowerCase();
				if(currentPlace != "") {
					current.val(currentPlace);
					placeholderFill(current, currentPlace, currentType);
					if(currentType == 'password') {
						current.attr("type", "text");
					}
				}
			});
			$("body").append("<input id='IE-CompatibleFlag' type='hidden' value='"+ie+"' />");
		}
	}
});
function placeholderFill(inputObj, inputValue, inputType) {
	inputObj.focus(function() {
		if($.trim(inputObj.val()) == inputValue) {
			inputObj.val('');
			if(inputType == 'password') {
				inputObj.attr("type", "password");
			}
		}
	});
	inputObj.blur(function() {
		if($.trim(inputObj.val()) == '') {
			inputObj.val(inputValue);
			if(inputType == 'password') {
				inputObj.attr("type", "text");
			}
		}
	});

}