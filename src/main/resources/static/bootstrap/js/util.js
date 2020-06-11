/*!
  * Bootstrap util.js v4.3.1 (https://getbootstrap.com/)
  * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('jquery'), require('./ajax.js')) :
  typeof define === 'function' && define.amd ? define(['jquery', './ajax.js'], factory) :
  (global = global || self, global.Util = factory(global.jQuery, global.Ajax));
}(this, function ($, Ajax) { 'use strict';

  $ = $ && $.hasOwnProperty('default') ? $['default'] : $;
  Ajax = Ajax && Ajax.hasOwnProperty('default') ? Ajax['default'] : Ajax;

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v4.3.1): util.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Private TransitionEnd Helpers
   * ------------------------------------------------------------------------
   */

  var TRANSITION_END = 'transitionend';
  var MAX_UID = 1000000;
  var MILLISECONDS_MULTIPLIER = 1000; // Shoutout AngusCroll (https://goo.gl/pxwQGp)

  function toType(obj) {
    return {}.toString.call(obj).match(/\s([a-z]+)/i)[1].toLowerCase();
  }

  function getSpecialTransitionEndEvent() {
    return {
      bindType: TRANSITION_END,
      delegateType: TRANSITION_END,
      handle: function handle(event) {
        if ($(event.target).is(this)) {
          return event.handleObj.handler.apply(this, arguments); // eslint-disable-line prefer-rest-params
        }

        return undefined; // eslint-disable-line no-undefined
      }
    };
  }

  function transitionEndEmulator(duration) {
    var _this = this;

    var called = false;
    $(this).one(Util.TRANSITION_END, function () {
      called = true;
    });
    setTimeout(function () {
      if (!called) {
        Util.triggerTransitionEnd(_this);
      }
    }, duration);
    return this;
  }

  function setTransitionEndSupport() {
    $.fn.emulateTransitionEnd = transitionEndEmulator;
    $.event.special[Util.TRANSITION_END] = getSpecialTransitionEndEvent();
  }
  /**
   * --------------------------------------------------------------------------
   * Public Util Api
   * --------------------------------------------------------------------------
   */


  var Util = {
    TRANSITION_END: 'bsTransitionEnd',
    getUID: function getUID(prefix) {
      do {
        // eslint-disable-next-line no-bitwise
        prefix += ~~(Math.random() * MAX_UID); // "~~" acts like a faster Math.floor() here
      } while (document.getElementById(prefix));

      return prefix;
    },
    getSelectorFromElement: function getSelectorFromElement(element) {
      var selector = element.getAttribute('data-target');

      if (!selector || selector === '#') {
        var hrefAttr = element.getAttribute('href');
        selector = hrefAttr && hrefAttr !== '#' ? hrefAttr.trim() : '';
      }

      try {
        return document.querySelector(selector) ? selector : null;
      } catch (err) {
        return null;
      }
    },
    randomArray: function randomArray(array) {
      return array instanceof Array ? array[Math.floor(Math.random() * array.length)] : null;
    },
    eval: function _eval(value) {
      var fuc;

      try {
        // eslint-disable-next-line no-new-func
        fuc = new Function("return " + value)();
      } catch (error) {
        // eslint-disable-next-line no-console
        console.log("the value [" + value + "] is not function");
      }

      return fuc;
    },
    // First define default Modal on body
    // if Modal is not exist, create the default Modal
    // Modal's id is named 'modal'
    // ps:  Util.createDefaultModal(NAME,flag)
    // @param  : id the modal's name
    // @Param  : flag if flag is true ,the Modal will be create
    // @author : lyc
    // @Date   : 2020年05月13日17:00:21
    createDefaultModal: function createDefaultModal(id, flag) {
      if (!id) {
        return null;
      }

      var selector = "#" + id;
      var modalDIV;

      try {
        modalDIV = document.querySelector(selector);
        selector = modalDIV ? selector : null;
      } catch (err) {
        selector = null;
      } // if '#modal' is not exist ,create the new modal


      if (!selector || flag && flag === true) {
        var ModalStr = "\n      <div class=\"modal fade\" id=\"" + id + "\" role=\"dialog\" >\n        <div class=\"modal-dialog modal-dialog-centered modal-dialog-scrollable\">\n        </div>\n      </div>";
        var div = document.createElement('div');
        div.innerHTML = ModalStr;
        modalDIV = div.firstElementChild;
        document.body.appendChild(modalDIV);
        selector = "#" + id;
      } else {
        modalDIV.querySelector('.modal-dialog').innerHTML = '';
      }

      return selector;
    },
    getReomteData: function getReomteData(_config, target) {
      // http/https url
      var HttpUrlReg = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\\.,@?^=%&:/~\\+#]*[\w\-\\@?^=%&/~\\+#])?/i; // 内部调用地址

      var OwnUrlReg = /([\w\-\\.,@?^=%&:/~\\+#]*[\w\-\\@?^=%&/~\\+#])?/i; // 远程地址

      var url = _config.url; // 远程地址无效 直接返回

      if (!url || url && !HttpUrlReg.test(url) && !OwnUrlReg.test(url)) {
        return;
      } // 拉取远程数据


      var op = {
        url: url,
        contentType: Ajax.APPLICATION_X_WWW_FORM_URLENCODED,
        data: _config.data || {},
        type: Ajax.POST,
        dataType: Ajax.HTML,
        success: function success(res) {
          return $(target.querySelector('.modal-dialog')).html(res ? res : '');
        },
        error: function error(err) {
          return $(target.querySelector('.modal-dialog')).html(err);
        }
      };
      Ajax.send(op);
    },
    getTransitionDurationFromElement: function getTransitionDurationFromElement(element) {
      if (!element) {
        return 0;
      } // Get transition-duration of the element


      var transitionDuration = $(element).css('transition-duration');
      var transitionDelay = $(element).css('transition-delay');
      var floatTransitionDuration = parseFloat(transitionDuration);
      var floatTransitionDelay = parseFloat(transitionDelay); // Return 0 if element or transition duration is not found

      if (!floatTransitionDuration && !floatTransitionDelay) {
        return 0;
      } // If multiple durations are defined, take the first


      transitionDuration = transitionDuration.split(',')[0];
      transitionDelay = transitionDelay.split(',')[0];
      return (parseFloat(transitionDuration) + parseFloat(transitionDelay)) * MILLISECONDS_MULTIPLIER;
    },
    reflow: function reflow(element) {
      return element.offsetHeight;
    },
    triggerTransitionEnd: function triggerTransitionEnd(element) {
      $(element).trigger(TRANSITION_END);
    },
    // TODO: Remove in v5
    supportsTransitionEnd: function supportsTransitionEnd() {
      return Boolean(TRANSITION_END);
    },
    isHTML: function isHTML(str) {
      var pattern = /<(?:[^"'>]|"[^"]*"|'[^']*')*>/ig;
      return str && pattern.test(str);
    },
    isElement: function isElement(obj) {
      return (obj[0] || obj).nodeType;
    },
    typeCheckConfig: function typeCheckConfig(componentName, config, configTypes) {
      for (var property in configTypes) {
        if (Object.prototype.hasOwnProperty.call(configTypes, property)) {
          var expectedTypes = configTypes[property];
          var value = config[property];
          var valueType = value && Util.isElement(value) ? 'element' : toType(value);

          if (!new RegExp(expectedTypes).test(valueType)) {
            throw new Error(componentName.toUpperCase() + ": " + ("Option \"" + property + "\" provided type \"" + valueType + "\" ") + ("but expected type \"" + expectedTypes + "\"."));
          }
        }
      }
    },
    findShadowRoot: function findShadowRoot(element) {
      if (!document.documentElement.attachShadow) {
        return null;
      } // Can find the shadow root otherwise it'll return the document


      if (typeof element.getRootNode === 'function') {
        var root = element.getRootNode();
        return root instanceof ShadowRoot ? root : null;
      }

      if (element instanceof ShadowRoot) {
        return element;
      } // when we don't find a shadow root


      if (!element.parentNode) {
        return null;
      }

      return Util.findShadowRoot(element.parentNode);
    }
  };
  setTransitionEndSupport();

  return Util;

}));
//# sourceMappingURL=util.js.map
