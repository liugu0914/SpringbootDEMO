/*!
  * Bootstrap tree.js v4.3.1 (https://getbootstrap.com/)
  * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('jquery'), require('./ajax.js'), require('./toast.js'), require('./tool.js')) :
  typeof define === 'function' && define.amd ? define(['jquery', './ajax.js', './toast.js', './tool.js'], factory) :
  (global = global || self, global.Tree = factory(global.jQuery, global.Ajax, global.Toast, global.Tool));
}(this, function ($, Ajax, Toast, Tool) { 'use strict';

  $ = $ && $.hasOwnProperty('default') ? $['default'] : $;
  Ajax = Ajax && Ajax.hasOwnProperty('default') ? Ajax['default'] : Ajax;
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

  var NAME = 'tree';
  var JQUERY_NO_CONFLICT = $.fn[NAME];
  var Customer = {
    ZTREE: 'ztree',
    CHECK: 'check',
    EDIT: 'edit',
    AUTO: 'auto',
    BAN_URL: 'ban-url',
    NOTURL: 'NotUrl',
    URL: 'url',
    TARGET: 'target',
    TARGET_URL: 'targeturl',
    PARENT: 'parent',
    SUC: 'suc',
    ID: 'id',
    PID: 'pid'
    /**
     * ------------------------------------------------------------------------
     *  使用ztree作为树结构 http://www.treejs.cn/v3
     *  @author lyc
     *  @date 2020年06月04日17:48:50
     * ------------------------------------------------------------------------
     */

  };

  var Tree =
  /*#__PURE__*/
  function () {
    function Tree(element, setting, nodes) {
      this.checkZtree();
      this._element = element;
      this._config = this.prepareOptions(setting);
      this._nodes = nodes;
      this.ztree = this.init();
      return this;
    }

    var _proto = Tree.prototype;

    // ----------------------------------------------------------------------
    // 检查ztree 是否存在
    // ----------------------------------------------------------------------
    _proto.checkZtree = function checkZtree() {
      if (!$.fn.zTree) {
        throw new TypeError('ztree is not load, plz check out');
      }
    } // ----------------------------------------------------------------------
    // 初始化树结构
    // ----------------------------------------------------------------------
    ;

    _proto.init = function init() {
      return $.fn.zTree.init($(this._element), this._config, this._nodes);
    } // ----------------------------------------------------------------------
    // 数据配置准备
    // ----------------------------------------------------------------------
    ;

    _proto.prepareOptions = function prepareOptions(setting) {
      if (!this._element.classList.contains(Customer.ZTREE)) {
        this._element.classList.add(Customer.ZTREE);
      }

      var $this = $(this._element); // 是否开启选中

      var check = this._element.hasAttribute(Customer.CHECK); // 是否开启编辑


      var edit = this._element.hasAttribute(Customer.EDIT); // 禁用url


      var banUrl = this._element.hasAttribute(Customer.BAN_URL); // 自动参数


      var autoParam = this._element.getAttribute(Customer.AUTO);

      autoParam = autoParam && typeof autoParam !== 'string' ? JSON.parse(autoParam) : [Customer.ID, Customer.PID]; // 加载树的url

      var url = $this.data(Customer.URL); // 跳转目标容器

      var target = $this.data(Customer.TARGET); // 跳转目标需要的url

      var targeturl = $this.data(Customer.TARGET_URL); // 父节点是否可以点击

      var parent = !this._element.hasAttribute(Customer.PARENT); // 树加载成功之后的回调

      var suc = $this.data(Customer.SUC);
      var params = $this.data();

      if (params) {
        delete params.url;
        delete params.target;
        delete params.targeturl;
        delete params.suc;
      }

      var _option = {
        // ztree config
        view: _objectSpread({}, edit ? {
          addHoverDom: this.addHoverDom,
          removeHoverDom: this.removeHoverDom
        } : {}, {
          selectedMulti: false
        }),
        check: {
          enable: check
        },
        edit: {
          enable: edit
        },
        data: {
          simpleData: {
            enable: true,
            idKey: Customer.ID,
            pIdKey: Customer.PID,
            rootPId: 0
          },
          key: {
            url: banUrl ? Customer.URL : Customer.NOTURL
          }
        },
        async: {
          enable: true,
          otherParam: params,
          autoParam: autoParam,
          type: Ajax.POST,
          url: url,
          dataType: Ajax.JSON,
          dataFilter: this.filter
        },
        callback: {
          onClick: this.onClick(target, targeturl, parent, params),
          onAsyncError: this.onAsyncError,
          onAsyncSuccess: this.onAsyncSuccess(suc)
        }
      };

      if (!setting) {
        setting = {};
      }

      _option = _objectSpread({}, _option, setting);
      return _option;
    } // ----------------------------------------------------------------------
    // 点击跳转
    // ----------------------------------------------------------------------
    ;

    _proto.onClick = function onClick(target, targeturl, parent, params) {
      return function (event, treeId, treeNode) {
        var isParent = treeNode.isParent;

        if (isParent && parent) {
          return;
        }

        if (!targeturl || !target) {
          return;
        }

        var datas = _objectSpread({}, treeNode, params);

        Ajax.postHTML(targeturl, datas, function (result) {
          if (!result) {
            return Toast.err('请求失败');
          }

          var $target = ($("#" + treeId).closest('.query-main') || document).find(target + ":first");
          return $target.html(result).initUI();
        });
      };
    } // ----------------------------------------------------------------------
    // 交互成功
    // ----------------------------------------------------------------------
    ;

    _proto.onAsyncSuccess = function onAsyncSuccess(callback) {
      var suc = Tool.eval(callback);

      if (typeof suc === 'function') {
        return suc;
      }

      return null;
    } // ----------------------------------------------------------------------
    // 交互失败
    // ----------------------------------------------------------------------
    ;

    _proto.onAsyncError = function onAsyncError(event, treeId, treeNode, XMLHttpRequest) {
      var err = Ajax.error();
      return err(XMLHttpRequest);
    } // ----------------------------------------------------------------------
    // 数据过滤
    // ----------------------------------------------------------------------
    ;

    _proto.filter = function filter(treeId, parentNode, datas) {
      if (!datas || !datas.data) {
        return null;
      }

      return datas.data;
    } // ----------------------------------------------------------------------
    // 添加节点
    // ----------------------------------------------------------------------
    ;

    _proto.addHoverDom = function addHoverDom(treeId, treeNode) {
      var sObj = $("#" + treeNode.tId + "_span");

      if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) {
        return;
      }

      var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='add node' onfocus='this.blur();'></span>";
      sObj.after(addStr);
      var btn = $("#addBtn_" + treeNode.tId);

      if (btn) {
        btn.on('click', function () {
          var zTree = $.fn.zTree.getZTreeObj('treeDemo');
          var id = new Date().getTime();
          zTree.addNodes(treeNode, {
            id: id,
            pId: treeNode.id,
            name: "new node" + id
          });
          return false;
        });
      }
    } // ----------------------------------------------------------------------
    // 删除节点
    // ----------------------------------------------------------------------
    ;

    _proto.removeHoverDom = function removeHoverDom(treeId, treeNode) {
      $("#addBtn_" + treeNode.tId).off().remove();
    } // ----------------------------------------------------------------------
    // 搜索树结构
    // @param zTreeId ztree对象的id,不需要#
    // @param searchField 输入框选择器
    // @param isHighLight 是否高亮,默认高亮,传入false禁用
    // @param isExpand 是否展开,默认合拢,传入true展开
    // ----------------------------------------------------------------------
    ;

    Tree.fuzzySearch = function fuzzySearch(zTreeId, searchField, isHighLight, isExpand) {
      var zTreeObj = $.fn.zTree.getZTreeObj(zTreeId); // get the ztree object by ztree id

      if (!zTreeObj) {
        return;
      }

      var nameKey = zTreeObj.setting.data.key.name; // get the key of the node name

      isHighLight = isHighLight !== false; // default true, only use false to disable highlight

      isExpand = Boolean(isExpand); // not to expand in default

      zTreeObj.setting.view.nameIsHTML = isHighLight; // allow use html in node name for highlight use
      // eslint-disable-next-line no-useless-escape

      var metaChar = '[\\[\\]\\\\\^\\$\\.\\|\\?\\*\\+\\(\\)]'; // js meta characters

      var rexMeta = new RegExp(metaChar, 'gi'); // regular expression to match meta characters
      // keywords filter function

      function ztreeFilter(zTreeObj, _keywords) {
        if (!_keywords) {
          _keywords = ''; // default blank for _keywords
        } // function to find the matching node


        function filterFunc(node) {
          if (node && node.oldname && node.oldname.length > 0) {
            node[nameKey] = node.oldname; // recover oldname of the node if exist
          }

          zTreeObj.updateNode(node); // update node to for modifications take effect

          if (_keywords.length === 0) {
            // return true to show all nodes if the keyword is blank
            zTreeObj.showNode(node);
            zTreeObj.expandNode(node, isExpand);
            return true;
          } // transform node name and keywords to lowercase


          if (node[nameKey] && node[nameKey].toLowerCase().indexOf(_keywords.toLowerCase()) !== -1) {
            if (isHighLight) {
              // highlight process
              // a new variable 'newKeywords' created to store the keywords information
              // keep the parameter '_keywords' as initial and it will be used in next node
              // process the meta characters in _keywords thus the RegExp can be correctly used in str.replace
              var newKeywords = _keywords.replace(rexMeta, function (matchStr) {
                return (// add escape character before meta characters
                  "\\" + matchStr
                );
              });

              node.oldname = node[nameKey]; // store the old name

              var rexGlobal = new RegExp(newKeywords, 'gi'); // 'g' for global,'i' for ignore case
              // use replace(RegExp,replacement) since replace(/substr/g,replacement) cannot be used here

              node[nameKey] = node.oldname.replace(rexGlobal, function (originalText) {
                // highlight the matching words in node name
                var highLightText = "<span style=\"color: whitesmoke;background-color: darkred;border-radius: 0.25rem;\">" + originalText + "</span>";
                return highLightText;
              });
              zTreeObj.updateNode(node); // update node for modifications take effect
            }

            zTreeObj.showNode(node); // show node with matching keywords

            return true; // return true and show this node
          }

          zTreeObj.hideNode(node); // hide node that not matched

          return false; // return false for node not matched
        }

        var nodesShow = zTreeObj.getNodesByFilter(filterFunc); // get all nodes that would be shown

        processShowNodes(nodesShow, _keywords); // nodes should be reprocessed to show correctly
      }

      function processShowNodes(nodesShow, _keywords) {
        if (nodesShow && nodesShow.length > 0) {
          // process the ancient nodes if _keywords is not blank
          if (_keywords.length > 0) {
            $.each(nodesShow, function (n, obj) {
              var pathOfOne = obj.getPath(); // get all the ancient nodes including current node

              if (pathOfOne && pathOfOne.length > 0) {
                // i < pathOfOne.length-1 process every node in path except self
                for (var i = 0; i < pathOfOne.length - 1; i++) {
                  zTreeObj.showNode(pathOfOne[i]); // show node

                  zTreeObj.expandNode(pathOfOne[i], true); // expand node
                }
              }
            });
          } else {
            // show all nodes when _keywords is blank and expand the root nodes
            var rootNodes = zTreeObj.getNodesByParam('level', '0'); // get all root nodes

            $.each(rootNodes, function (n, obj) {
              zTreeObj.expandNode(obj, true); // expand all root nodes
            });
          }
        }
      } // listen to change in input element


      $(searchField).on('input propertychange', function () {
        var _keywords = $(this).val();

        if (_keywords) {
          _keywords = _keywords.trim();
        }

        searchNodeLazy(_keywords); // call lazy load
      });
      var timeoutId = null;
      var lastKeyword = '';
      var time = 500; // excute lazy load once after input change, the last pending task will be cancled

      function searchNodeLazy(_keywords) {
        if (timeoutId) {
          // clear pending task
          clearTimeout(timeoutId);
        }

        timeoutId = setTimeout(function () {
          if (lastKeyword === _keywords) {
            return;
          }

          ztreeFilter(zTreeObj, _keywords); // lazy load ztreeFilter function
          // $(searchField).focus();//focus input field again after filtering

          lastKeyword = _keywords;
        }, time);
      }
    };

    Tree._jQueryInterface = function _jQueryInterface(setting, nodes) {
      var tree = new Tree(this, setting, nodes);
      $(this).data(NAME, tree);
      return tree;
    };

    _createClass(Tree, null, [{
      key: "NAME",
      get: function get() {
        return NAME;
      }
    }]);

    return Tree;
  }();
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   */


  $.fn[NAME] = Tree._jQueryInterface;
  $.fn[NAME].Constructor = Tree;

  $.fn[NAME].noConflict = function () {
    $.fn[NAME] = JQUERY_NO_CONFLICT;
    return Tree._jQueryInterface;
  };

  return Tree;

}));
//# sourceMappingURL=tree.js.map
