/*!
  * Bootstrap toast.js v4.3.1 (https://getbootstrap.com/)
  * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('jquery')) :
  typeof define === 'function' && define.amd ? define(['jquery'], factory) :
  (global = global || self, global.Toast = factory(global.jQuery));
}(this, function ($) { 'use strict';

  $ = $ && $.hasOwnProperty('default') ? $['default'] : $;

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

  var _$$extend;
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  var NAME = 'toast';
  var VERSION = '1.0.0';
  var DATA_KEY = 'lyc.toast';
  var EVENT_KEY = "." + DATA_KEY;
  var JQUERY_NO_CONFLICT = $[NAME];
  var Event = {
    CLICK_DISMISS: "click.dismiss" + EVENT_KEY
  };
  var PositionClasses = ['bottom-left', 'bottom-right', 'top-right', 'top-left', 'bottom-center', 'top-center', 'mid-center'];
  var DefaultIcons = ['success', 'error', 'info', 'warning'];
  var DefaultOptions = {
    text: '',
    heading: '',
    showHideTransition: 'fade',
    allowToastClose: true,
    hideAfter: 3000,
    loader: true,
    loaderBg: '#9EC600',
    stack: 5,
    position: 'top-center',
    bgColor: false,
    textColor: false,
    textAlign: 'left',
    icon: false,
    beforeShow: function beforeShow() {
      return null;
    },
    afterShown: function afterShown() {
      return null;
    },
    beforeHide: function beforeHide() {
      return null;
    },
    afterHidden: function afterHidden() {
      return null;
    }
  };
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  var Toast =
  /*#__PURE__*/
  function () {
    function Toast(_options) {
      this._toastEl = null;
      this.options = this.prepareOptions(_options);
      this.process();
    } // Getters


    var _proto = Toast.prototype;

    _proto.prepareOptions = function prepareOptions(options) {
      var _options = {};

      if (typeof options === 'string' || options instanceof Array) {
        _options.text = options;
      } else {
        _options = typeof options === 'object' ? options : {};
      }

      return _objectSpread({}, DefaultOptions, _options);
    };

    _proto.process = function process() {
      this.setup();
      this.addToDom();
      this.position();
      this.bindToast();
      this.animate();
    };

    _proto.setup = function setup() {
      var _toastContent = '';
      this._toastEl = this._toastEl || $('<div></div>', {
        class: 'jq-toast-single text-break'
      }); // For the loader on top

      _toastContent += '<span class="jq-toast-loader"></span>'; // if (this.options.allowToastClose) {
      //   _toastContent += '<span class="close-jq-toast-single">&times;</span>'
      // }

      if (this.options.text instanceof Array) {
        if (this.options.heading) {
          _toastContent += "<h2 class=\"jq-toast-heading\">" + this.options.heading + "</h2>";
        }

        _toastContent += '<ul class="jq-toast-ul">';

        for (var i = 0; i < this.options.text.length; i++) {
          _toastContent += "<li class=\"jq-toast-li\" id=\"jq-toast-item-" + i + "\">" + this.options.text[i] + "</li>";
        }

        _toastContent += '</ul>';
      } else {
        if (this.options.heading) {
          _toastContent += "<h2 class=\"jq-toast-heading\">" + this.options.heading + "</h2>";
        }

        _toastContent += this.options.text;
      }

      this._toastEl.html(_toastContent);

      if (this.options.bgColor !== false) {
        this._toastEl.css('background-color', this.options.bgColor);
      }

      if (this.options.textColor !== false) {
        this._toastEl.css('color', this.options.textColor);
      }

      if (this.options.textAlign) {
        this._toastEl.css('text-align', this.options.textAlign);
      }

      if (this.options.icon !== false) {
        this._toastEl.addClass('jq-has-icon');

        if ($.inArray(this.options.icon, DefaultIcons) !== -1) {
          this._toastEl.addClass("jq-icon-" + this.options.icon);
        }
      }

      if (this.options.class !== false) {
        this._toastEl.addClass(this.options.class);
      }
    };

    _proto.position = function position() {
      var two = 2;

      if (typeof this.options.position === 'string' && $.inArray(this.options.position, PositionClasses) !== -1) {
        if (this.options.position === 'bottom-center') {
          this._container.css({
            left: $(window).outerWidth() / two - this._container.outerWidth() / two,
            bottom: 20
          });
        } else if (this.options.position === 'top-center') {
          this._container.css({
            left: $(window).outerWidth() / two - this._container.outerWidth() / two,
            top: 20
          });
        } else if (this.options.position === 'mid-center') {
          this._container.css({
            left: $(window).outerWidth() / two - this._container.outerWidth() / two,
            top: $(window).outerHeight() / two - this._container.outerHeight() / two
          });
        } else {
          this._container.addClass(this.options.position);
        }
      } else if (typeof this.options.position === 'object') {
        this._container.css({
          top: this.options.position.top ? this.options.position.top : 'auto',
          bottom: this.options.position.bottom ? this.options.position.bottom : 'auto',
          left: this.options.position.left ? this.options.position.left : 'auto',
          right: this.options.position.right ? this.options.position.right : 'auto'
        });
      } else {
        this._container.addClass('bottom-left');
      }
    };

    _proto.bindToast = function bindToast() {
      var that = this;

      this._toastEl.on('afterShown', function () {
        that.processLoader();
      });

      if (this.options.allowToastClose) {
        this._toastEl.on(Event.CLICK_DISMISS, function (e) {
          e.preventDefault();

          if (that.options.showHideTransition === 'fade') {
            that._toastEl.trigger('beforeHide');

            that._toastEl.fadeOut(function () {
              that._toastEl.trigger('afterHidden');
            });
          } else if (that.options.showHideTransition === 'slide') {
            that._toastEl.trigger('beforeHide');

            that._toastEl.slideUp(function () {
              that._toastEl.trigger('afterHidden');
            });
          } else {
            that._toastEl.trigger('beforeHide');

            that._toastEl.hide(function () {
              that._toastEl.trigger('afterHidden');
            });
          }
        });
      }

      if (typeof this.options.beforeShow === 'function') {
        this._toastEl.on('beforeShow', function () {
          that.options.beforeShow();
        });
      }

      if (typeof this.options.afterShown === 'function') {
        this._toastEl.on('afterShown', function () {
          that.options.afterShown();
        });
      }

      if (typeof this.options.beforeHide === 'function') {
        this._toastEl.on('beforeHide', function () {
          that.options.beforeHide();
        });
      }

      if (typeof this.options.afterHidden === 'function') {
        this._toastEl.on('afterHidden', function () {
          that.options.afterHidden();
        });
      }
    };

    _proto.addToDom = function addToDom() {
      var _container = $('.jq-toast-wrap');

      if (_container.length === 0) {
        _container = $('<div></div>', {
          class: 'jq-toast-wrap',
          role: 'alert',
          'aria-live': 'polite'
        });
        $('body').append(_container);
      } else if (!this.options.stack || isNaN(parseInt(this.options.stack, 10))) {
        _container.empty();
      }

      _container.find('.jq-toast-single:hidden').remove();

      _container.append(this._toastEl);

      var ten = 10;

      if (this.options.stack && !isNaN(Number(this.options.stack), ten)) {
        var _prevToastCount = _container.find('.jq-toast-single').length;

        var _extToastCount = _prevToastCount - this.options.stack;

        if (_extToastCount > 0) {
          $('.jq-toast-wrap').find('.jq-toast-single').slice(0, _extToastCount).remove();
        }
      }

      this._container = _container;
    };

    _proto.canAutoHide = function canAutoHide() {
      return this.options.hideAfter !== false && !isNaN(parseInt(this.options.hideAfter, 10));
    };

    _proto.processLoader = function processLoader() {
      // Show the loader only, if auto-hide is on and loader is demanded
      if (!this.canAutoHide() || this.options.loader === false) {
        return false;
      }

      var loader = this._toastEl.find('.jq-toast-loader');

      var newLocal = 400;
      var milliseconds = 1000; // 400 is the default time that jquery uses for fade/slide
      // Divide by 1000 for milliseconds to seconds conversion

      var transitionTime = (this.options.hideAfter - newLocal) / milliseconds + "s";
      var loaderBg = this.options.loaderBg;
      var style = loader.attr('style') || '';
      style = style.substring(0, style.indexOf('-webkit-transition')); // Remove the last transition definition

      style += "-webkit-transition: width " + transitionTime + " ease-in;               -o-transition: width " + transitionTime + " ease-in;               transition: width " + transitionTime + " ease-in;               background-color: " + loaderBg + ";";
      return loader.attr('style', style).addClass('jq-toast-loaded');
    };

    _proto.animate = function animate() {
      var _this = this;

      this._toastEl.hide();

      this._toastEl.trigger('beforeShow');

      if (this.options.showHideTransition.toLowerCase() === 'fade') {
        this._toastEl.fadeIn(function () {
          _this._toastEl.trigger('afterShown');
        });
      } else if (this.options.showHideTransition.toLowerCase() === 'slide') {
        this._toastEl.slideDown(function () {
          _this._toastEl.trigger('afterShown');
        });
      } else {
        this._toastEl.show(function () {
          _this._toastEl.trigger('afterShown');
        });
      }

      if (this.canAutoHide()) {
        window.setTimeout(function () {
          if (_this.options.showHideTransition.toLowerCase() === 'fade') {
            _this._toastEl.trigger('beforeHide');

            _this._toastEl.fadeOut(function () {
              _this._toastEl.trigger('afterHidden');
            });
          } else if (_this.options.showHideTransition.toLowerCase() === 'slide') {
            _this._toastEl.trigger('beforeHide');

            _this._toastEl.slideUp(function () {
              _this._toastEl.trigger('afterHidden');
            });
          } else {
            _this._toastEl.trigger('beforeHide');

            _this._toastEl.hide(function () {
              _this._toastEl.trigger('afterHidden');
            });
          }
        }, this.options.hideAfter);
      }
    } // Staticx
    ;

    Toast.suc = function suc(text) {
      var option = {
        heading: '成功',
        text: text,
        position: 'top-center',
        hideAfter: 1500,
        stack: false,
        icon: 'success'
      };
      return new Toast(option);
    };

    Toast.err = function err(text) {
      var option = {
        heading: '错误',
        text: text,
        position: 'top-center',
        hideAfter: 1500,
        stack: false,
        icon: 'error'
      };
      return new Toast(option);
    };

    Toast.info = function info(text) {
      var option = {
        heading: '提示',
        text: text,
        position: 'top-center',
        hideAfter: 1500,
        stack: false,
        icon: 'info'
      };
      return new Toast(option);
    };

    Toast.warn = function warn(text) {
      var option = {
        heading: '警告',
        text: text,
        position: 'top-center',
        hideAfter: 1500,
        stack: false,
        icon: 'warning'
      };
      return new Toast(option);
    };

    Toast.reset = function reset(resetWhat) {
      if (resetWhat === 'all') {
        $('.jq-toast-wrap').remove();
      } else {
        this._toastEl.remove();
      }
    };

    Toast.update = function update(options) {
      this.prepareOptions(options, this.options);
      this.setup();
      this.bindToast();
    };

    Toast._jQueryInterface = function _jQueryInterface(options) {
      return new Toast(options);
    };

    _createClass(Toast, null, [{
      key: "VERSION",
      get: function get() {
        return VERSION;
      }
    }]);

    return Toast;
  }();
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   */


  $.extend((_$$extend = {}, _$$extend[NAME] = Toast._jQueryInterface, _$$extend));

  $[NAME].noConflict = function () {
    $[NAME] = JQUERY_NO_CONFLICT;
    return Toast._jQueryInterface;
  };

  return Toast;

}));
//# sourceMappingURL=toast.js.map
