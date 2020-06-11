/*!
  * Bootstrap confirm.js v4.3.1 (https://getbootstrap.com/)
  * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('jquery'), require('./util.js')) :
  typeof define === 'function' && define.amd ? define(['jquery', './util.js'], factory) :
  (global = global || self, global.Confirm = factory(global.jQuery, global.Util));
}(this, function ($, Util) { 'use strict';

  $ = $ && $.hasOwnProperty('default') ? $['default'] : $;
  Util = Util && Util.hasOwnProperty('default') ? Util['default'] : Util;

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

  var NAME = 'confirm';
  var VERSION = '4.3.1';
  var DATA_KEY = 'bs.confirm';
  var EVENT_KEY = "." + DATA_KEY;
  var DEFAULT_ICON = '<i class=\'cs cs-xinxi\'></i>';
  var DEFAULT_CONFIRM = "\n                          <div class=\"toast fade\" target=\"confirm\">\n                          <div class=\"modal-dialog modal-dialog-centered modal-dialog-scrollable\">\n                              <div class=\"toast-content {{toastClass}}\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\">\n                                  <div class=\"toast-header\">\n                                      {{icon}}\n                                      <strong class=\"ml-1 mr-auto\">{{header}}</strong>\n                                      <i class=\"cs cs-close pointer\" target=\"confirm-no\"></i>\n                                  </div>\n                                  <div class=\"toast-body\">{{text}}</div>\n                                  <div class=\"toast-footer\">\n                                      <button type=\"button\" target=\"confirm-no\" class=\"btn btn-light btn-xm mr-2\">\n                                          <span aria-hidden=\"true\">\u53D6\u6D88</span>\n                                      </button>\n                                      <button type=\"button\" target=\"confirm-ok\" class=\"btn {{btnClass}} btn-xm\">\n                                          <span aria-hidden=\"true\">\u786E\u8BA4</span>\n                                      </button>\n                                  </div>\n                              </div>\n                          </div>\n                          </div>";
  var Event = {
    CLICK_DISMISS: "click.dismiss" + EVENT_KEY,
    HIDE: "hide" + EVENT_KEY,
    HIDDEN: "hidden" + EVENT_KEY,
    SHOW: "show" + EVENT_KEY,
    SHOWN: "shown" + EVENT_KEY
  };
  var ClassName = {
    FADE: 'fade',
    HIDE: 'hide',
    SHOW: 'show',
    SHOWING: 'showing',
    OPEN: 'modal-open',
    BACKDROP: 'toast-backdrop',
    TOAST_FILLED: 'toast-filled',
    BG_BTN: 'btn-outline-light',
    BTN_RIMARY: 'btn-primary',
    BG_PRIMARY: 'bg-primary',
    BG_INFO: 'bg-info',
    BG_DANGER: 'bg-danger',
    BG_WARNING: 'bg-warning',
    BG_SUCCESS: 'bg-success',
    BG_DARK: 'bg-dark'
  };
  var State = ['primary', 'info', 'danger', 'warning', 'success', 'dark'];
  var position = ['bottom-left', 'bottom-right', 'top-right', 'top-left', 'bottom-center', 'top-center', 'mid-center'];
  var DefaultType = {
    animation: 'boolean',
    backdrop: 'boolean',
    header: 'string',
    text: 'string',
    icon: 'string',
    position: '(string|object)',
    ok: 'function',
    no: 'function'
  };
  var Default = {
    animation: true,
    backdrop: false,
    header: 'JIOPEEL',
    text: '',
    icon: '',
    position: 'mid-center',
    ok: function ok() {
      return true;
    },
    no: function no() {
      return false;
    }
  };
  var Selector = {
    DATA_CONFIRM: '[target="confirm"]',
    DATA_NO: '[target="confirm-no"]',
    DATA_OK: '[target="confirm-ok"]'
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

  };

  var Confirm =
  /*#__PURE__*/
  function () {
    function Confirm(config) {
      this._config = this._getConfig(config);
      this._element = this._createDIV();
      this._backdrop = null;
      this._isShown = false;
      this._flag = false; // this.show()

      this._setListeners();

      return this;
    } // Getters


    var _proto = Confirm.prototype;

    // Public
    _proto.show = function show() {
      var _this = this;

      $(this._element).trigger(Event.SHOW);
      this._isShown = true;
      $(document.body).addClass(ClassName.OPEN); // 打开阴影层

      this._showBackdrop(function () {
        return _this._showElement();
      });
    };

    _proto.hide = function hide(flag) {
      var _this2 = this;

      if (!this._element.classList.contains(ClassName.SHOW)) {
        return;
      }

      $(this._element).trigger(Event.HIDE);
      this._isShown = false;

      this._close(); // 打开阴影层


      this._showBackdrop(function () {
        return _this2._judge(flag);
      });
    };

    _proto.dispose = function dispose() {
      clearTimeout(this._timeout);
      this._timeout = null;

      if (this._element.classList.contains(ClassName.SHOW)) {
        this._element.classList.remove(ClassName.SHOW);
      }

      $(this._element).off(Event.CLICK_DISMISS);
      $.removeData(this._element, DATA_KEY);
      this._element = null;
      this._config = null;
    };

    _proto.ok = function ok(callback) {
      if (typeof callback === 'function') {
        this._config.ok = callback;
      }

      return this;
    };

    _proto.no = function no(callback) {
      if (typeof callback === 'function') {
        this._config.no = callback;
      }

      return this;
    };

    _proto.over = function over() {
      return this._flag;
    } // Private
    ;

    _proto._showElement = function _showElement() {
      var _this3 = this;

      if (this._config.animation) {
        this._element.classList.add(ClassName.FADE);
      } // this._position()


      var complete = function complete() {
        _this3._element.classList.add(ClassName.SHOW);

        $(_this3._element).trigger(Event.SHOWN);
      };

      this._element.classList.remove(ClassName.HIDE);

      this._element.style.display = 'block';

      if (this._config.animation) {
        var transitionDuration = Util.getTransitionDurationFromElement(this._element);
        $(this._element).one(Util.TRANSITION_END, complete).emulateTransitionEnd(transitionDuration);
      } else {
        complete();
      }
    };

    _proto._judge = function _judge(flag) {
      $(document.body).removeClass(ClassName.OPEN);
      this._flag = flag;
      return flag ? this._config.ok() : this._config.no();
    };

    _proto._getConfig = function _getConfig(_config) {
      var config = {};

      if (typeof _config === 'string') {
        config.text = _config;
      } else if (typeof _config === 'object' && _config) {
        config = _config;
      }

      var random = Util.randomArray(State);
      random = random ? random : '';
      config = _objectSpread({}, Default, {
        icon: random
      }, config);
      Util.typeCheckConfig(NAME, config, this.constructor.DefaultType);
      return config;
    };

    _proto._position = function _position() {
      var local = 2;
      var _position = this._config.position;
      var $this = $(this._element);

      if (typeof _position === 'string' && $.inArray(_position, position) !== -1) {
        if (_position === 'bottom-center') {
          $this.css({
            left: $(window).outerWidth() / local - $this.outerWidth() / local,
            bottom: 20
          });
        } else if (_position === 'top-center') {
          $this.css({
            left: $(window).outerWidth() / local - $this.outerWidth() / local,
            top: 20
          });
        } else if (_position === 'mid-center') {
          $this.css({
            left: $(window).outerWidth() / local - $this.outerWidth() / local,
            top: $(window).outerHeight() / local - $this.outerHeight() / local
          });
        } else {
          $this.addClass(_position);
        }
      } else if (typeof _position === 'object') {
        $this.css({
          top: _position.top ? _position.top : 'auto',
          bottom: _position.bottom ? _position.bottom : 'auto',
          left: _position.left ? _position.left : 'auto',
          right: _position.right ? _position.right : 'auto'
        });
      } else {
        $this.addClass('mid-center');
      }
    };

    _proto._showBackdrop = function _showBackdrop(callback) {
      var _this4 = this;

      var animate = $(this._element).hasClass(ClassName.FADE) ? ClassName.FADE : '';

      if (this._isShown) {
        this._backdrop = document.createElement('div');
        this._backdrop.className = ClassName.BACKDROP;

        if (animate) {
          this._backdrop.classList.add(animate);
        }

        $(this._backdrop).appendTo(document.body);
        $(this._element).on(Event.CLICK_DISMISS, function (event) {
          if (!_this4._config.backdrop) {
            return;
          }

          if (event.target !== event.currentTarget) {
            return;
          }

          _this4.hide();
        });

        if (animate) {
          Util.reflow(this._backdrop);
        }

        $(this._backdrop).addClass(ClassName.SHOW);

        if (!callback) {
          return;
        }

        if (!animate) {
          callback();
          return;
        }

        var backdropTransitionDuration = Util.getTransitionDurationFromElement(this._backdrop);
        $(this._backdrop).one(Util.TRANSITION_END, callback).emulateTransitionEnd(backdropTransitionDuration);
      } else {
        // 关闭阴影层
        $(this._backdrop).removeClass(ClassName.SHOW);

        var callbackRemove = function callbackRemove() {
          _this4._removeBackdrop();

          if (callback) {
            callback();
          }
        };

        if ($(this._element).hasClass(ClassName.FADE)) {
          var _backdropTransitionDuration = Util.getTransitionDurationFromElement(this._backdrop);

          $(this._backdrop).one(Util.TRANSITION_END, callbackRemove).emulateTransitionEnd(_backdropTransitionDuration);
        } else {
          callbackRemove();
        }
      }
    };

    _proto._removeBackdrop = function _removeBackdrop() {
      if (this._backdrop) {
        $(this._backdrop).remove();
        this._backdrop = null;
      }
    };

    _proto._setListeners = function _setListeners() {
      var _this5 = this;

      $(this._element).on(Event.CLICK_DISMISS, Selector.DATA_NO, function () {
        return _this5.hide(false);
      });
      $(this._element).on(Event.CLICK_DISMISS, Selector.DATA_OK, function () {
        return _this5.hide(true);
      });
    };

    _proto._close = function _close() {
      var _this6 = this;

      var complete = function complete() {
        _this6._element.classList.add(ClassName.HIDE);

        $(_this6._element).trigger(Event.HIDDEN);
        $(_this6._element).remove();
        _this6._element = null;
      };

      this._element.classList.remove(ClassName.SHOW);

      if (this._config.animation) {
        var transitionDuration = Util.getTransitionDurationFromElement(this._element);
        $(this._element).one(Util.TRANSITION_END, complete).emulateTransitionEnd(transitionDuration);
      } else {
        complete();
      }
    };

    _proto._createDIV = function _createDIV() {
      $(Selector.DATA_CONFIRM, document).remove();
      $("." + ClassName.BACKDROP, document).remove();
      var div = DEFAULT_CONFIRM;
      div = div.replace('{{header}}', this._config.header);
      div = div.replace('{{text}}', this._config.text);

      var icon = this._config.icon.trim();

      var iconName = icon.toUpperCase();

      for (var state in State) {
        if (icon === State[state]) {
          div = div.replace('{{toastClass}}', ClassName["BG_" + iconName] + " " + ClassName.TOAST_FILLED);
          div = div.replace('{{btnClass}}', ClassName.BG_BTN);
          icon = DEFAULT_ICON;
          break;
        }
      }

      div = div.replace('{{toastClass}}', '');
      div = div.replace('{{btnClass}}', ClassName.BTN_RIMARY);
      icon = Util.isHTML(icon) ? icon : '';
      div = div.replace('{{icon}}', icon);
      var creatediv = document.createElement('div');
      creatediv.innerHTML = div;
      return document.body.appendChild(creatediv.firstElementChild);
    } // Static
    ;

    Confirm._jQueryInterface = function _jQueryInterface(config) {
      return new Confirm(config);
    };

    _createClass(Confirm, null, [{
      key: "VERSION",
      get: function get() {
        return VERSION;
      }
    }, {
      key: "DefaultType",
      get: function get() {
        return DefaultType;
      }
    }, {
      key: "Default",
      get: function get() {
        return Default;
      }
    }]);

    return Confirm;
  }();
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   */


  $.extend((_$$extend = {}, _$$extend[NAME] = Confirm._jQueryInterface, _$$extend));

  return Confirm;

}));
//# sourceMappingURL=confirm.js.map
