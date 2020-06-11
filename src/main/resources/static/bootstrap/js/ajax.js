/*!
  * Bootstrap ajax.js v4.3.1 (https://getbootstrap.com/)
  * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('jquery'), require('./jq.toast.js'), require('./tool.js')) :
  typeof define === 'function' && define.amd ? define(['jquery', './jq.toast.js', './tool.js'], factory) :
  (global = global || self, global.Ajax = factory(global.jQuery, global.jq.toast, global.Tool));
}(this, function ($, Toast, Tool) { 'use strict';

  $ = $ && $.hasOwnProperty('default') ? $['default'] : $;
  Toast = Toast && Toast.hasOwnProperty('default') ? Toast['default'] : Toast;
  Tool = Tool && Tool.hasOwnProperty('default') ? Tool['default'] : Tool;

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

  var NAME = 'ajax';
  var VERSION = '1.0.0';
  var POST = 'post';
  var GET = 'get';
  var JSON = 'json';
  var HTML = 'html';
  var ONE_DAY = 86400000;
  var APPLICATION_JSON = 'application/json;charset=utf-8';
  var APPLICATION_X_WWW_FORM_URLENCODED = 'application/x-www-form-urlencoded;charset=utf-8';
  var MULTIPART_FORM_DATA = 'multipart/form-data;charset=utf-8';

  var Ajax =
  /*#__PURE__*/
  function () {
    function Ajax() {}

    Ajax.post = function post(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: POST,
        success: suc,
        error: err,
        dataType: JSON
      };
      this.send(settings);
    };

    Ajax.get = function get(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: GET,
        success: suc,
        error: err,
        dataType: JSON
      };
      this.send(settings);
    };

    Ajax.postHTML = function postHTML(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: POST,
        success: suc,
        error: err,
        dataType: HTML
      };
      this.send(settings);
    };

    Ajax.getHTML = function getHTML(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: GET,
        success: suc,
        error: err,
        dataType: HTML
      };
      this.send(settings);
    };

    Ajax.postJSON = function postJSON(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: POST,
        contentType: APPLICATION_JSON,
        success: suc,
        error: err,
        dataType: JSON
      };
      this.send(settings);
    };

    Ajax.getJSON = function getJSON(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: GET,
        contentType: APPLICATION_JSON,
        success: suc,
        error: err,
        dataType: JSON
      };
      this.send(settings);
    };

    Ajax.postJSONHTML = function postJSONHTML(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: POST,
        contentType: APPLICATION_JSON,
        success: suc,
        error: err,
        dataType: HTML
      };
      this.send(settings);
    };

    Ajax.getJSONHTML = function getJSONHTML(url, data, suc, err) {
      var settings = {
        url: url,
        data: data || {},
        type: GET,
        contentType: APPLICATION_JSON,
        success: suc,
        error: err,
        dataType: HTML
      };
      this.send(settings);
    };

    Ajax.send = function send(op) {
      // 默认同步请求
      var settings = {
        type: POST,
        async: false,
        contentType: APPLICATION_X_WWW_FORM_URLENCODED,
        dataType: JSON,
        data: {},
        success: this.success,
        error: this.error
      };
      op = typeof op === 'object' && op ? op : {};
      var opData = typeof op.data === 'object' && op.data ? op.data : {};

      switch (op.contentType) {
        default:
        case APPLICATION_X_WWW_FORM_URLENCODED:
          break;

        case MULTIPART_FORM_DATA:
          break;

        case APPLICATION_JSON:
          op.data = window.JSON.stringify(opData);
          break;
      }

      op = _objectSpread({}, settings, op);
      $.ajax(op);
    };

    Ajax.success = function success(result) {
      if (!result || typeof result !== 'object') {
        return Toast.err('未知错误');
      }

      return Toast.suc(result.message);
    };

    Ajax.error = function error(XMLHttpRequest) {
      if (XMLHttpRequest && XMLHttpRequest.responseText) {
        var responseText = XMLHttpRequest.responseText;

        if (Tool.isJSON(responseText)) {
          return Toast.err(window.JSON.parse(responseText).message);
        }
      }

      return Toast.err('未知错误');
    };

    Ajax.setCookie = function setCookie(key, value, exdays) {
      var expires = '';

      if (exdays) {
        var d = new Date();
        d.setTime(d.getTime() + exdays * ONE_DAY); // 有效期一天

        expires = "expires=" + d.toUTCString();
      }

      document.cookie = key + "=" + value + ";" + expires + ";path=/";
    };

    Ajax.getCookie = function getCookie(cname) {
      var name = cname + "=";
      var decodedCookie = decodeURIComponent(document.cookie);
      var ca = decodedCookie.split(';');

      for (var i = 0; i < ca.length; i++) {
        var c = ca[i];

        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }

        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }

      return '';
    };

    _createClass(Ajax, null, [{
      key: "VERSION",
      get: function get() {
        return VERSION;
      }
    }, {
      key: "NAME",
      get: function get() {
        return NAME;
      }
    }, {
      key: "POST",
      get: function get() {
        return POST;
      }
    }, {
      key: "GET",
      get: function get() {
        return GET;
      }
    }, {
      key: "JSON",
      get: function get() {
        return JSON;
      }
    }, {
      key: "HTML",
      get: function get() {
        return HTML;
      }
    }, {
      key: "APPLICATION_JSON",
      get: function get() {
        return APPLICATION_JSON;
      }
    }, {
      key: "APPLICATION_X_WWW_FORM_URLENCODED",
      get: function get() {
        return APPLICATION_X_WWW_FORM_URLENCODED;
      }
    }, {
      key: "MULTIPART_FORM_DATA",
      get: function get() {
        return MULTIPART_FORM_DATA;
      }
    }]);

    return Ajax;
  }();

  return Ajax;

}));
//# sourceMappingURL=ajax.js.map
