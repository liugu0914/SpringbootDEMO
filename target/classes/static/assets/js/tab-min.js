(function () {
    var scrollSetp = 500,
        operationWidth = 90,
        leftOperationWidth = 30,
        animatSpeed = 150,
        linkframe = function (url, value) {
            $("#menu-list a.active").removeClass("active");
            $("#menu-list a[data-url='" + url + "'][data-value='" + value + "']").addClass("active");
            $("#page-content iframe.active").removeClass("active");
            $("#page-content .iframe-content[data-url='" + url + "'][data-value='" + value + "']").addClass("active");
            $("#menu-all-ul li.active").removeClass("active");
            $("#menu-all-ul li[data-url='" + url + "'][data-value='" + value + "']").addClass("active");
            event.stopPropagation();
        },
        move = function (selDom) {
            var nav = $("#menu-list");
            var releft = selDom.offset().left;
            var wwidth = parseInt($("#page-tab").width());
            var left = parseInt(nav.css("margin-left"));
            if (releft < 0 && releft <= wwidth) {
                nav.animate({"margin-left": (left - releft + leftOperationWidth) + "px"}, animatSpeed)
            } else {
                if (releft + selDom.width() > wwidth - operationWidth) {
                    nav.animate({"margin-left": (left - releft + wwidth - selDom.width() - operationWidth) + "px"}, animatSpeed)
                }
            }
        },
        createmove = function () {
            var nav = $("#menu-list");
            var wwidth = parseInt($("#page-tab").width());
            var navwidth = parseInt(nav.width());
            if (wwidth - operationWidth < navwidth) {
                nav.animate({"margin-left": "-" + (navwidth - wwidth + operationWidth) + "px"}, animatSpeed)
            }
        },
        closemenu = function ($items) {
            if ($items.length == 0)
                return;
            $items.animate({"width": "0", "padding": "0"}, 0, function () {
                var jthis = $(this);
                if (jthis.hasClass("active")) {
                    var linext = jthis.next();
                    if (linext.length > 0) {
                        linext.trigger('click');
                        move(linext);
                    } else {
                        var liprev = jthis.prev();
                        if (liprev.length > 0) {
                            liprev.trigger('click');
                            move(liprev);
                        }
                    }
                }
                this.remove();
                $("#page-content .iframe-content[data-url='" + jthis.data("url") + "'][data-value='" + jthis.data("value") + "']").remove()
            });
            // event.stopPropagation();
        },
        closeitem = function () {
            closemenu($(this.parentElement));
        }
    init = function () {
        $('#menu-list a.home-page').on('click', function () {
            var jthis = $(this);
            linkframe(jthis.data("url"), jthis.data("value"));
        });
        $("#page-prev").on("click", function () {
            var nav = $("#menu-list");
            var left = parseInt(nav.css("margin-left"));
            if (left !== 0) {
                nav.animate({
                    "margin-left": (left + scrollSetp > 0 ? 0 : (left + scrollSetp)) + "px"
                }, animatSpeed)
            }
        });
        $("#page-next").on("click", function () {
            var nav = $("#menu-list");
            var left = parseInt(nav.css("margin-left"));
            var wwidth = parseInt($("#page-tab").width());
            var navwidth = parseInt(nav.width());
            var allshowleft = -(navwidth - wwidth + operationWidth);
            if (allshowleft !== left && navwidth > wwidth - operationWidth) {
                var temp = (left - scrollSetp);
                nav.animate({
                    "margin-left": (temp < allshowleft ? allshowleft : temp) + "px"
                }, animatSpeed)
            }
        });
        $("#page-tab-reflesh").on("click", function () {
            var jthis = $("#menu-list a.active");
           var $frame= $("#page-content .iframe-content[data-url='" + jthis.data("url") + "'][data-value='" + jthis.data("value") + "']");
            $frame.attr('src',$frame.data('url'));
        });
        $("#page-tab-now").on("click", function () {
            var $a = $("#menu-list a.pagetab.active");
            if ($a)
                closemenu($a);
        });
        $("#page-tab-other").on("click", function () {
            var $a = $("#menu-list a.active");
            closemenu($a.siblings('.pagetab'));
        });
        $("#page-tab-all").on("click", function () {
            var $as = $("#menu-list a.pagetab");
            if ($as)
                closemenu($as);
        });
        $("body").on("mousedown", function (event) {
            if (!(event.target.id === "menu-all" || event.target.id === "menu-all-ul" ||
                event.target.id === "page-operation" || event.target.id === "page-operation" ||
                event.target.parentElement.id === "menu-all-ul")) {
                $("#menu-all").hide()
            }
        })
    };
    $.fn.tab = function () {
        init();
        this.bind("click", function () {
            var linkUrl = this.href;
            var linkHtml = this.text.trim();
            var selDom = $("#menu-list a[data-url='" + linkUrl + "'][data-value='" + linkHtml + "']");
            if (selDom.length === 0) {
                var iel = $("<i>", {
                    "class": "menu-close"
                }).on("click", closeitem);
                $("<a>", {
                    "html": linkHtml,
                    "class": "pagetab",
                    "href": "javascript:void(0);",
                    "data-url": linkUrl,
                    "data-value": linkHtml
                }).on("click", function () {
                    var jthis = $(this);
                    linkframe(jthis.data("url"), jthis.data("value"));
                }).append(iel).appendTo("#menu-list");
                $("<iframe>", {
                    "class": "iframe-content",
                    "data-url": linkUrl,
                    "data-value": linkHtml,
                    src: linkUrl
                }).appendTo("#page-content");

                $("<li>", {
                    "html": linkHtml,
                    "data-url": linkUrl,
                    "data-value": linkHtml
                }).on("click", function () {
                    var jthis = $(this);
                    linkframe(jthis.data("url"), jthis.data("value"));
                    move($("#menu-list a[data-url='" + linkUrl + "'][data-value='" + linkHtml + "']"));
                    $("#menu-all").hide();
                    event.stopPropagation()
                }).appendTo("#menu-all-ul");
                createmove();
            } else {
                move(selDom);
            }
            linkframe(linkUrl, linkHtml);
            var $active = 'active';
            var $item = $(this).parents('li.side-nav-item:last');
            if (!$item.hasClass($active))
                $item.addClass($active);
            $item.siblings().removeClass($active);
            var $body = $('body');
            var sidebar = 'sidebar-enable';
            if ($body.hasClass(sidebar))
                $body.attr('class', '');
            return false;
        });
        return this;
    }
})();