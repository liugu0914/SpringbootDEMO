/*!
  * Bootstrap initui.js v4.3.1 (https://getbootstrap.com/)
  * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('jquery'), require('./ajax.js'), require('./confirm.js'), require('./toast.js'), require('./tool.js'), require('./tree.js')) :
  typeof define === 'function' && define.amd ? define(['jquery', './ajax.js', './confirm.js', './toast.js', './tool.js', './tree.js'], factory) :
  (global = global || self, global.InitUI = factory(global.jQuery, global.Ajax, global.Confirm, global.Toast, global.Tool, global.Tree));
}(this, function ($, Ajax, Confirm, Toast, Tool, Tree) { 'use strict';

  $ = $ && $.hasOwnProperty('default') ? $['default'] : $;
  Ajax = Ajax && Ajax.hasOwnProperty('default') ? Ajax['default'] : Ajax;
  Confirm = Confirm && Confirm.hasOwnProperty('default') ? Confirm['default'] : Confirm;
  Toast = Toast && Toast.hasOwnProperty('default') ? Toast['default'] : Toast;
  Tool = Tool && Tool.hasOwnProperty('default') ? Tool['default'] : Tool;
  Tree = Tree && Tree.hasOwnProperty('default') ? Tree['default'] : Tree;

  function _defineProperties(target, props) {
    for (var i = 0; i < props.length; i++) {
      var descriptor = props[i];
      descriptor.enumerable = descriptor.enumerable || false;
      descriptor.configurable = true;
      if ("value" in descriptor) descriptor.writable = true;
      Object.defineProperty(target, descriptor.key, descriptor);
    }
  }

  function _createClass(Constructor, protoProps, staticProps) {
    if (protoProps) _defineProperties(Constructor.prototype, protoProps);
    if (staticProps) _defineProperties(Constructor, staticProps);
    return Constructor;
  }

  function _defineProperty(obj, key, value) {
    if (key in obj) {
      Object.defineProperty(obj, key, {
        value: value,
        enumerable: true,
        configurable: true,
        writable: true
      });
    } else {
      obj[key] = value;
    }

    return obj;
  }

  function _objectSpread(target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i] != null ? arguments[i] : {};
      var ownKeys = Object.keys(source);

      if (typeof Object.getOwnPropertySymbols === 'function') {
        ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function (sym) {
          return Object.getOwnPropertyDescriptor(source, sym).enumerable;
        }));
      }

      ownKeys.forEach(function (key) {
        _defineProperty(target, key, source[key]);
      });
    }

    return target;
  }

  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  var NAME = 'initUI';
  var VERSION = '1.0.0';
  var DATA_KEY = 'lyc.init';
  var EVENT_KEY = "." + DATA_KEY;
  var DATA_INFO = DATA_KEY + ".";
  var DATA_API_KEY = '.data-api';
  var JQUERY_NO_CONFLICT = $.fn[NAME];
  var Selector = {
    MENU: '[target="menu"]',
    TAB: '[target="tab"]',
    SEARCH: '[target="search"]',
    SELECT: '[target="select"]',
    QUERY: '[target="query"]',
    FORM: '[target="form"]',
    AJAX: '[target="ajax"]',
    HTML: '[target="html"]',
    CLEAR: '[target="clear"]',
    PAGE: '[target="page"]',
    TREE: '[target="tree"]',
    TOOLTIP: '[show="tooltip"]',
    ERROR_IMG: 'img[src-error]',
    NAV_LIST_GROUP: '.nav, .list-group',
    ACTIVE: '.active',
    ACTIVE_UL: '> li > .active'
  };
  var Event = {
    CLICK_QUERY: "click.query" + EVENT_KEY + DATA_API_KEY,
    CLICK_FORM: "click.form" + EVENT_KEY + DATA_API_KEY,
    CLICK_AJAX: "click.ajax" + EVENT_KEY + DATA_API_KEY,
    CLICK_DATA_API: "click" + EVENT_KEY + DATA_API_KEY,
    CHANGE_DATA_API: "change" + EVENT_KEY + DATA_API_KEY,
    ERROR_IMG: "error.img" + EVENT_KEY + DATA_API_KEY
  };
  var ClassName = {
    QUERY_MAIN: '.query-main',
    QUERY_DATA: '.query-data',
    MODAL_CONTENT: '.modal-content',
    PAGE_LINK: '.page-link',
    CHECK_ALL: '.chk-all',
    CHECK: '.chk',
    ZTREE: 'ztree',
    WARN: 'warn',
    ACTIVE: 'active'
  };
  var Customer = {
    CHECK: 'chk',
    CUSTOM: 'cus',
    BEFORE: 'bef',
    SUCCESS: 'suc'
  };
  var WinData = {
    nextInit: 0,
    nextHeight: 0,
    winW: 767.98,
    winM: 1024,
    winH: 1200
  };
  var DataKey = {
    QUERY: DATA_INFO + "query",
    TREE: 'tree'
    /**
     * ------------------------------------------------------------------------
     *  初始化方法 init()
     *  @author lyc
     *  @date 2020年06月04日17:48:50
     * ------------------------------------------------------------------------
     */

  };

  var InitUI =
  /*#__PURE__*/
  function () {
    function InitUI(element) {
      this._element = element;
      this.verifyJQuery(); // this.resizeWin()

      this.imgError();
      this.tooltip();

      if (this.verifySelect2()) {
        this.search();
        this.select();
      }

      this.clear();
      this.checkbox();
      this.query();
      this.form();
      this.ajax();
      this.html();
      this.page();
      this.tree();
    }

    var _proto = InitUI.prototype;

    _proto.verifyJQuery = function verifyJQuery() {
      if (!$ || !$.fn) {
        throw new TypeError('JQuery is not load, plz check out');
      }
    };

    _proto.resizeWin = function resizeWin() {
      if (this._element !== document) {
        return;
      }

      $(window).on('resize', function () {
        var winWidth = window.innerWidth;
        var $body = $('body');
        var enlarged = 'enlarged'; // var sidebar = 'sidebar-enable';

        if (winWidth > WinData.winM) {
          $body.attr('class', '');
        }

        if (winWidth <= WinData.winM) {
          $body.attr('class', enlarged);
        }

        if (winWidth <= WinData.winW) {
          $body.attr('class', '');
        }
      });
      $(window).trigger('resize');
    } // ----------------------------------------------------------------------
    //  图片错误加载
    // ----------------------------------------------------------------------
    ;

    _proto.imgError = function imgError() {
      $(Selector.ERROR_IMG, this._element).each(function (index, element) {
        var $this = $(element);
        var src = $this.attr('src');
        $this.on(Event.ERROR_IMG, function () {
          var attr = $this.attr('src-error');

          if (!attr) {
            src = '/img/user.png';
          }

          if (attr === 'user') {
            src = '/img/user.png';
          } else if (attr === 'img') {
            src = '/img/img.png';
          }

          return $this.attr('src', src);
        });
        var error = $.Event(Event.ERROR_IMG);
        return src && src !== window.location.href ? $this.attr('src', src) : $this.trigger(error);
      });
    } // ----------------------------------------------------------------------
    //  提示插件 需要tooltip.js
    // ----------------------------------------------------------------------
    ;

    _proto.tooltip = function tooltip() {
      if ($.fn.tooltip) {
        $(Selector.TOOLTIP, this._element).tooltip({
          bgcolor: 'dark'
        });
      }
    } // ----------------------------------------------------------------------
    //  验证是否存在select2插件
    //  给select2插件中文国际化
    // ----------------------------------------------------------------------
    ;

    _proto.verifySelect2 = function verifySelect2() {
      if (!$.fn.select2) {
        // eslint-disable-next-line no-console
        console.error('the select2 plugin do not loaded');
        return false;
      }

      if ($.fn.select2.amd) {
        var e = $.fn.select2.amd;
        e.define('select2/i18n/zh-CN', [], function () {
          return {
            errorLoading: function errorLoading() {
              return '无法载入结果。';
            },
            inputTooLong: function inputTooLong(e) {
              var t = e.input.length - e.maximum;
              var n = "\u8BF7\u5220\u9664" + t + "\u4E2A\u5B57\u7B26";
              return n;
            },
            inputTooShort: function inputTooShort(e) {
              var t = e.minimum - e.input.length;
              var n = "\u8BF7\u518D\u8F93\u5165\u81F3\u5C11" + t + "\u4E2A\u5B57\u7B26";
              return n;
            },
            loadingMore: function loadingMore() {
              return '载入更多结果…';
            },
            maximumSelected: function maximumSelected(e) {
              var t = "\u6700\u591A\u53EA\u80FD\u9009\u62E9" + e.maximum + "\u4E2A\u9879\u76EE";
              return t;
            },
            noResults: function noResults() {
              return '未找到结果';
            },
            searching: function searching() {
              return '搜索中…';
            }
          };
        });
      }

      return true;
    } // ----------------------------------------------------------------------
    //  select2插件ajax获取数据
    // ----------------------------------------------------------------------
    ;

    _proto.search = function search() {
      $(Selector.SEARCH, this._element).each(function (index, element) {
        var $this = $(element);
        var id = $this.data('id');
        var text = $this.data('text');
        var op = {
          placeholder: '请选择',
          // allowClear : true,
          ajax: {
            url: $this.data('url'),
            delay: 250,
            data: function data(params) {
              var query = {
                search: params.term
              };
              query = $.extend(query, $this.data());
              return query;
            },
            dataType: Ajax.JSON,
            type: Ajax.POST,
            processResults: function processResults(result) {
              var data = [];

              if (result.data) {
                data = $.map(result.data, function (obj) {
                  obj.text = obj.text || obj.name;
                  return obj;
                });
              }

              return {
                results: data
              };
            }
          },
          templateSelection: formatState,
          templateResult: formatState,
          minimumInputLength: 0,
          maximumSelectionLength: 100
        };

        function formatState(state) {
          var pattern = /<(?:[^"'>]|"[^"]*"|'[^']*')*>/g;

          if (!state.id) {
            return state.text;
          }

          return state.text && pattern.test(state.text) ? $(state.text) : state.text;
        }

        $this.select2(op);

        if ($this.hasClass('select2-hidden-accessible') && id) {
          var option = new Option(text, id, true, true);
          $this.append(option).trigger('change');
        }
      });
    } // ----------------------------------------------------------------------
    //  select2前台数据(相当于原生select)
    //  目的在于统一样式
    // ----------------------------------------------------------------------
    ;

    _proto.select = function select() {
      $(Selector.SELECT, this._element).each(function (index, element) {
        $(element).select2({
          minimumResultsForSearch: Infinity
        });
      });
    } // ----------------------------------------------------------------------
    //  重置form表单元素
    // ----------------------------------------------------------------------
    ;

    _proto.clear = function clear() {
      $(Selector.CLEAR, this._element).on(Event.CLICK_DATA_API, function (event) {
        if (event) {
          event.preventDefault();
        }

        var $this = $(event.currentTarget);
        var $form;

        if ($this.closest('form').length !== 0) {
          $form = $this.closest('form');
        } else if ($this.closest(ClassName.QUERY_MAIN).find('form').length !== 0) {
          $form = $this.closest(ClassName.QUERY_MAIN).find('form');
        } else if ($this.closest(ClassName.MODAL_CONTENT).find('form').length !== 0) {
          $form = $this.closest(ClassName.MODAL_CONTENT).find('form');
        } else {
          return;
        }

        var attrs = $form.find('input,select,textarea');
        attrs.each(function (index, element) {
          var $this = $(element);

          if ($this.hasClass('select2-hidden-accessible')) {
            var target = $this.attr('target');

            if (target === 'search') {
              return $this.empty();
            }

            if (target === 'select') {
              return $this.select2('val', ' '); // 预留空格
            }
          }

          return $this.val('');
        });
      });
    };

    _proto.checkbox = function checkbox() {
      $(ClassName.CHECK_ALL, this._element).each(function (index, element) {
        var $this = $(element);
        var $querydata = $this.closest(ClassName.QUERY_DATA);
        var chks = $querydata.find('.chk');
        $this.on(Event.CLICK_DATA_API, function (event) {
          chks.prop('checked', $(event.target).prop('checked'));
        });
        chks.on(Event.CHANGE_DATA_API, function () {
          var ischked = $querydata.find('.chk:checked');

          if (ischked.length === 0) {
            $this.prop('indeterminate', false);
            $this.prop('checked', false);
            return;
          }

          if (ischked.length !== 0 && chks.length === ischked.length) {
            $this.prop('indeterminate', false);
            $this.prop('checked', true);
            return;
          }

          if (ischked.length !== 0 && chks.length !== ischked.length) {
            $this.prop('indeterminate', true);
          }
        });
      });
    } // ----------------------------------------------------------------------
    //  主页面查询表单控件
    // ----------------------------------------------------------------------
    ;

    _proto.query = function query() {
      var _this = this;

      $(Selector.QUERY, this._element).on(Event.CLICK_QUERY, function (event) {
        if (event) {
          event.preventDefault();
        }

        var $this = $(event.currentTarget);
        $this.blur();
        var $form;

        if ($this.closest('form').length !== 0) {
          $form = $this.closest('form');
        } else if ($this.closest(ClassName.QUERY_MAIN).find('form').length !== 0) {
          $form = $this.closest(ClassName.QUERY_MAIN).find('form');
        } else {
          return;
        }

        var config = _objectSpread({}, $form.data(), $this.data(), {
          url: $form.attr('action'),
          data: Tool.formData($form),
          type: Ajax.POST,
          dataType: Ajax.HTML
        });

        $this.closest(ClassName.QUERY_MAIN).data(DataKey.QUERY);

        _this._ajaxUseful($this, config);
      });
    } // ----------------------------------------------------------------------
    //  用于form提交
    // ----------------------------------------------------------------------
    ;

    _proto.form = function form() {
      var _this2 = this;

      $(Selector.FORM, this._element).on(Event.CLICK_FORM, function (event) {
        if (event) {
          event.preventDefault();
        }

        var $this = $(event.currentTarget);
        $this.blur();
        var $form;

        if ($this.closest('form').length !== 0) {
          $form = $this.closest('form');
        } else if ($this.closest(ClassName.MODAL_CONTENT).find('form').length !== 0) {
          $form = $this.closest(ClassName.MODAL_CONTENT).find('form');
        } else {
          return;
        }

        var config = _objectSpread({}, $form.data(), $this.data(), {
          url: $form.attr('action'),
          data: Tool.formData($form),
          type: Ajax.POST,
          dataType: Ajax.JSON
        });

        _this2._ajaxUseful($this, config);
      });
    } // ----------------------------------------------------------------------
    //  普通ajax交互
    // ----------------------------------------------------------------------
    ;

    _proto.ajax = function ajax() {
      var _this3 = this;

      $(Selector.AJAX, this._element).on(Event.CLICK_AJAX, function (event) {
        if (event) {
          event.preventDefault();
        }

        var $this = $(event.currentTarget);
        $this.blur();
        var data = $this.data();

        var config = _objectSpread({}, data, {
          url: $this.attr('href') || $this.data('url'),
          data: data,
          type: Ajax.POST,
          dataType: Ajax.JSON
        });

        _this3._ajaxUseful($this, config);
      });
    } // ----------------------------------------------------------------------
    //  ajax 返回html
    // ----------------------------------------------------------------------
    ;

    _proto.html = function html() {
      var _this4 = this;

      $(Selector.HTML, this._element).on(Event.CLICK_AJAX, function (event) {
        if (event) {
          event.preventDefault();
        }

        var $this = $(event.currentTarget);
        $this.blur();
        var listElement = $this.closest(Selector.NAV_LIST_GROUP)[0];
        var activeElements = listElement && (listElement.nodeName === 'UL' || listElement.nodeName === 'OL') ? $(listElement).find(Selector.ACTIVE_UL) : $(listElement).children(Selector.ACTIVE);

        if (activeElements) {
          $(activeElements).removeClass(ClassName.ACTIVE);
        }

        $this.addClass(ClassName.ACTIVE);
        var data = $this.data();

        var config = _objectSpread({}, data, {
          url: $this.attr('href') || $this.data('url'),
          data: data,
          type: Ajax.POST,
          dataType: Ajax.HTML
        });

        _this4._ajaxUseful($this, config);
      });
    } // ----------------------------------------------------------------------
    //  分页器js处理
    // ----------------------------------------------------------------------
    ;

    _proto.page = function page() {
      $(Selector.PAGE, this._element).each(function (index, element) {
        var $main = $(element).closest(ClassName.QUERY_MAIN);
        var op = $main.data(DataKey.QUERY) || {
          data: {}
        };
        $(element).find(ClassName.PAGE_LINK).on(Event.CHANGE_DATA_API, function (event) {
          if (event) {
            event.preventDefault();
          }

          var $this = $(event.target);
          var pageNum = $this.data('pagenum');
          op.data.pageNum = pageNum;
          Ajax.send(op);
        });
      });
    } // ----------------------------------------------------------------------
    //  tree js处理
    // ----------------------------------------------------------------------
    ;

    _proto.tree = function tree() {
      $(Selector.TREE, this._element).each(function (index, element) {
        var tree = new Tree(element);
        $(element).data(DataKey.TREE, tree);
      });
    } // ----------------------------------------------------------------------
    //  ajax默认success方法
    // ----------------------------------------------------------------------
    ;

    _proto._suc = function _suc($this, config, callback) {
      if (config.dataType === Ajax.HTML) {
        return function (result) {
          if (!config.target) {
            return;
          }

          var $target = ($this.closest(ClassName.QUERY_MAIN) || document).find(config.target + ":first");
          $target.html(result);
          $target.data(DATA_KEY, new InitUI($target.html(result)[0]));

          if (callback && typeof callback === 'function') {
            callback($this, config);
          }
        };
      }

      return function (result) {
        if (!result || typeof result !== 'object') {
          return Toast.err('未知错误');
        } // 后台返回参数 result


        var flag = result.result;

        if (callback && typeof callback === 'function') {
          callback($this, config);
        } else if (flag) {
          if ($('#modal').length > 0) {
            $('#modal').modal('hide');
          }

          if ($('#query').length > 0) {
            $('#query').trigger('click');
          }
        }

        return Toast[flag ? 'suc' : 'err'](result.message);
      };
    } // ----------------------------------------------------------------------
    //  通用处理Customer中的事件
    //  事件顺序:[chk , cus, bef ,suc]
    // ----------------------------------------------------------------------
    ;

    _proto._ajaxUseful = function _ajaxUseful($this, config) {
      var _this5 = this;

      var chk = Tool.eval(config[Customer.CHECK]);

      if (typeof chk === 'function') {
        var flag = chk($this, config.data);

        if (typeof flag === 'boolean' && !flag) {
          return;
        }
      } // 自定义函数式  用于封装数据


      var cus = Tool.eval(config[Customer.CUSTOM]);

      if (cus && typeof cus === 'object' && typeof cus.fuc === 'function') {
        var _cus$fuc;

        var rescus = (_cus$fuc = cus.fuc).call.apply(_cus$fuc, [$this[0]].concat(cus.args));

        if (rescus && typeof rescus === 'object') {
          config.data = _objectSpread({}, config.data, rescus);
        } else if (typeof rescus === 'boolean' && !rescus) {
          return;
        } else {
          return;
        }
      }

      var warn = config[ClassName.WARN];

      if (warn) {
        var confirm = new Confirm(warn);
        confirm.ok(function () {
          return _this5._ajaxUsefulMain($this, config);
        }).show();
      } else {
        this._ajaxUsefulMain($this, config);
      }
    } // ----------------------------------------------------------------------
    //  通用处理Customer中的事件
    //  事件顺序:[chk , cus, bef ,suc]
    // ----------------------------------------------------------------------
    ;

    _proto._ajaxUsefulMain = function _ajaxUsefulMain($this, config) {
      var bef = Tool.eval(config[Customer.BEFORE]);

      if (typeof bef === 'function') {
        var obj = bef($this, config.data);

        if (!bef) {
          return;
        }

        config.data = _objectSpread({}, config.data, typeof obj === 'object' && obj ? obj : {});
      }

      var suc = Tool.eval(config[Customer.SUCCESS]);
      config.success = this._suc($this, config, suc);
      Ajax.send(config);
    } // ----------------------------------------------------------------------
    //  默认启动方法init()
    // ----------------------------------------------------------------------
    ;

    InitUI._init = function _init() {
      return this.each(function () {
        $(this).data(DATA_KEY, new InitUI(this));
      });
    };

    _createClass(InitUI, null, [{
      key: "VERSION",
      get: function get() {
        return VERSION;
      }
    }]);

    return InitUI;
  }();
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   */


  $.fn[NAME] = InitUI._init;
  $.fn[NAME].Constructor = InitUI;

  $.fn[NAME].noConflict = function () {
    $.fn[NAME] = JQUERY_NO_CONFLICT;
    return InitUI._init;
  };

  return InitUI;

}));
//# sourceMappingURL=initui.js.map
