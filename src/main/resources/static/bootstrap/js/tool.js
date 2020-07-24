/*!
  * Bootstrap tool.js v4.3.1 (https://getbootstrap.com/)
  * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('jquery'), require('./toast.js')) :
  typeof define === 'function' && define.amd ? define(['jquery', './toast.js'], factory) :
  (global = global || self, global.Tool = factory(global.jQuery, global.Toast));
}(this, function ($, Toast) { 'use strict';

  $ = $ && $.hasOwnProperty('default') ? $['default'] : $;
  Toast = Toast && Toast.hasOwnProperty('default') ? Toast['default'] : Toast;

  /**
   * ------------------------------------------------------------------------
   *  工具类
   *  @author lyc
   *  @date 2020年06月04日17:48:50
   * ------------------------------------------------------------------------
   */

  var Tool =
  /*#__PURE__*/
  function () {
    function Tool() {}

    // ----------------------------------------------------------------------
    // 获取form数据转为Object
    // $form 为form的JQuery对象
    // ----------------------------------------------------------------------
    Tool.formData = function formData($form) {
      if (!$form || $form.length === 0 || !$ || !$.fn) {
        return {};
      }

      var array = $form.serializeArray();
      var data = {};

      for (var index in array) {
        if (!Object.prototype.hasOwnProperty.call(array, index)) {
          continue;
        }

        var name = array[index].name;
        var value = array[index].value;
        data[name] = data[name] ? [].concat($.isArray(data[name]) ? data[name] : [data[name]], [value]) : value;
      }

      return data;
    } // ----------------------------------------------------------------------
    // Object序列化
    // ----------------------------------------------------------------------
    ;

    Tool.toSerialize = function toSerialize(obj) {
      var serialize = '';

      if (!obj || !(obj instanceof Object)) {
        return serialize;
      }

      for (var key in obj) {
        if (!Object.prototype.hasOwnProperty.call(obj, key)) {
          continue;
        }

        var data = obj[key];

        if (data instanceof Array) {
          for (var index in data) {
            if (!Object.prototype.hasOwnProperty.call(data, index)) {
              continue;
            }

            var value = data[index];
            serialize = serialize ? serialize.concat("&" + key + "=" + value) : key + "=" + value;
          }
        } else if (data instanceof Object) {
          continue;
        } else {
          serialize = serialize ? serialize.concat("&" + key + "=" + data) : key + "=" + data;
        }
      }

      return serialize;
    } // ----------------------------------------------------------------------
    // 数组数据转为Object
    // ----------------------------------------------------------------------
    ;

    Tool.toObject = function toObject(target) {
      if (!target) {
        return {};
      }

      if (!(target instanceof Array)) {
        return target;
      }

      var data = {};
      var flag = false;

      for (var _len = arguments.length, needs = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
        needs[_key - 1] = arguments[_key];
      }

      for (var index in target) {
        if (!Object.prototype.hasOwnProperty.call(target, index)) {
          continue;
        }

        var targetData = target[index];

        if (typeof targetData !== 'object') {
          flag = true;
          break;
        }

        for (var key in targetData) {
          if (!Object.prototype.hasOwnProperty.call(targetData, key)) {
            continue;
          }

          if (needs && needs.length > 0 && !needs.includes(key)) {
            continue;
          }

          var value = targetData[key];
          data[key] = data[key] ? [].concat($.isArray(data[key]) ? data[key] : [data[key]], [value]) : value;
        }
      }

      return flag ? target : data;
    } // ----------------------------------------------------------------------
    // 字符串转function
    // ----------------------------------------------------------------------
    ;

    Tool.eval = function _eval(value) {
      var args;
      var fuc;

      if (!value) {
        return fuc;
      }

      var regval = value.match(/\(.*\)/gi);

      if (regval && regval instanceof Array && regval.length > 0) {
        var _JSON = window.JSON;
        var argstr = regval.pop();
        value = value.replace(argstr, '');
        args = argstr.replace(/\(|\)/g, '').split(';');
        args = args.filter(function (arg) {
          return arg || arg === 0;
        }).map(function (arg) {
          return _JSON.parse(arg.replace(/'/g, '"'));
        });
      }

      try {
        // eslint-disable-next-line no-new-func
        fuc = new Function("return " + value)();
      } catch (error) {
        // eslint-disable-next-line no-console
        console.log("the value [" + value + "] is not function");
      }

      return args ? {
        args: args,
        fuc: fuc
      } : fuc;
    } // ----------------------------------------------------------------------
    // 判断是否为JSON
    // ----------------------------------------------------------------------
    ;

    Tool.isJSON = function isJSON(value) {
      if (!window.JSON || Object.prototype.toString.call(window.JSON) !== '[object JSON]') {
        throw new ReferenceError('JSON is not exist in window');
      }

      var JSON = window.JSON;
      value = typeof value === 'string' ? value : JSON.stringify(value);

      try {
        value = Object.prototype.toString.call(JSON.parse(value));

        if (value === '[object Object]' || value === '[object Array]') {
          return true;
        }

        return false;
      } catch (e) {
        return false;
      }
    } // ----------------------------------------------------------------------
    //  获取选中的项转为Object
    //  dataName可为 'id' 或 ['id','name']
    // ----------------------------------------------------------------------
    ;

    Tool.checkedData = function checkedData(dataName, one) {
      var data = {};
      var $chks = $(this).closest('.query-data').find('.chk:checked');

      if (!one && (!$chks || $chks.length === 0)) {
        Toast.warn('请选择之后进行操作');
        return false;
      }

      if (one && (!$chks || $chks.length !== 1)) {
        Toast.warn('请选择一项之后进行操作');
        return false;
      }

      if (!dataName) {
        dataName = 'id';
      }

      if ($.isArray(dataName)) {
        var _loop = function _loop(index) {
          if (!Object.prototype.hasOwnProperty.call(dataName, index)) {
            return "continue";
          }

          var name = dataName[index];

          if (typeof name !== 'string') {
            return "continue";
          }

          var attrs = $.map($chks, function (element) {
            return $(element).data(name);
          });
          data[name] = attrs.join(',');
        };

        for (var index in dataName) {
          var _ret = _loop(index);

          if (_ret === "continue") continue;
        }
      } else if (typeof dataName === 'string') {
        var attrs = $.map($chks, function (element) {
          return $(element).data(dataName);
        });
        data[dataName] = attrs.join(',');
      }

      return data;
    };

    return Tool;
  }();

  return Tool;

}));
//# sourceMappingURL=tool.js.map
