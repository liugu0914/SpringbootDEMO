$(function () {
    initUI.init();
});
var initUI = {
    initData: {
        nextInit: 0,
        nextHeight: 0,
        winW: 767.98,
        winM: 1024,
        winH: 1200
    },
    resizeWin: function () {
        var winWidth = window.innerWidth;
        var $body = $('body');
        var enlarged = 'enlarged';
        // var sidebar = 'sidebar-enable';
        if (winWidth > initUI.initData.winM)
            $body.attr('class', '');
        if (winWidth <= initUI.initData.winM)
            $body.attr('class', enlarged);
        if (winWidth <= initUI.initData.winW)
            $body.attr('class', '');
    },
    imgError: function () {
        var $this = $(this);
        var attr = $this.attr('src-error');
        this.onerror = null;
        if (attr == 'user')
            return $this.attr('src', '/img/user.png');
        if (attr == 'img' || !attr)
            return $this.attr('src', '/img/img.png');
        if (attr)
            return $this.attr('src', $this.attr('src-error'));
    },
    init: function () {
        //窗口调整
        window.onresize = function () {
            initUI.resizeWin();
        };
        initUI.resizeWin();
        //菜单收缩
        $('ul.metismenu li.side-nav-item>a').on('click', function (e) {
            if (initUI.initData.nextInit)
                return;
            var $in = 'in';
            var $active = 'active';
            var dom = $(this);
            var next = dom.next();
            var $li = dom.parents('li:first');
            if ($li.hasClass($active))
                $li.removeClass($active);
            else
                $li.addClass($active);
            if (!initUI.initData.nextInit) {
                initUI.initData.nextInit = 1;
                initUI.initData.nextHeight = next.height();
            }
            if (next.hasClass($in)) {
                next.animate({height: 0}, 200, function () {
                    next.removeClass($in);
                    next.removeAttr("style");
                    initUI.initData.nextInit = 0;
                });
            } else {
                next.addClass($in);
                next.height(0);
                next.animate({height: initUI.initData.nextHeight}, 200, function () {
                    next.removeAttr("style");
                    initUI.initData.nextInit = 0;
                });
            }
            $li.siblings().removeClass($active);
            $li.siblings().find('.collapse').removeClass($in);
            e.stopPropagation(); //阻止冒泡事件
            e.preventDefault();
        });
        //收缩按钮
        $('.button-menu-mobile').on('click', function () {
            //浏览器宽度
            var winWidth = window.innerWidth;
            var $body = $('body');
            var enlarged = 'enlarged';
            var sidebar = 'sidebar-enable';
            if (winWidth > initUI.initData.winW) {
                $body.hasClass(enlarged) ? $body.removeClass(enlarged) : $body.addClass(enlarged);
            } else {
                $body.hasClass(sidebar) ? $body.removeClass(sidebar) : $body.attr('class', sidebar);
            }
            // $('ul.metismenu li.side-nav-item').find('.collapse').removeClass('in');
        });
        //初始化a标签链接到tab
        $("a.menu-item").tab();
        //菜单收缩遮罩层
        $('.phone-body-shade').on('click', function () {
            //浏览器宽度
            var $body = $('body');
            var sidebar = 'sidebar-enable';
            if ($body.hasClass(sidebar))
                $body.removeClass(sidebar);
        });
        $('img[src-error]').each(function () {
            var $this=$(this);
            var src=$(this).attr('src');
            $this.error(initUI.imgError);
            (src && src != window.location.href)?$this.attr('src',src):$this.trigger('error');
        });
    }
}