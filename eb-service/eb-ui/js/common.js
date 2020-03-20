//请求前缀
var baseURL = "/api/";
//var baseURL = "http://127.0.0.1:9001/";
var baseURI = "/modules/";
//var baseURI = "../";
var serverPath = "/";
var gatewayPath = "/cbs";
var languageKey = "cbsLang";

/*if (parseURL(window.location.href).path.indexOf(gatewayPath) == 0) {
    baseURL = gatewayPath + serverPath;
} else {
    baseURL = serverPath;
}*/
var S_OK = "CBS-000";
var reg_phone = /^\d[13,15,16,17,18]\d{9}$/;
var reg_password = /^[a-z][A-z][0-9][_]{6,30}$/;
var wsBasePath = "ws://" + window.location.host + baseURL;

var resLocale = (navigator.language || navigator.browserLanguage);
//登录token
var token = localStorage.getItem("token");
var userId = localStorage.getItem("userId");
var sysId = localStorage.getItem("sysId");
var orgId = localStorage.getItem("orgId");
// if (isEmpty(token)) {
//     // if(parent.location.href.indexOf("login.html")==-1){
//     //     parent.location.href = baseURL + 'login.html';
//     // }
// /*    if (parent.location.href.indexOf("login.html") == -1 && parent.location.href.indexOf("register.html") == -1
//     && parent.location.href.indexOf("login_develop.html") == -1) {
//         parent.location.href = baseURL + 'login.html';
//     }*/
//     if(parent.location.href.indexOf("login.html")==-1){
//         parent.location.href = 'modules/common/login.html';
//     }
// }

function isEmpty(obj) {
    if(obj == null || obj == undefined || $.trim(obj) == '' || obj == 'null' || obj.length == 0){
        return true
    }
    return false;
}

//权限判断
function getPermission(pvm) {
    var permissionCodes = [];
    for (var permissionCode in pvm.permissions) {
        permissionCodes.push(permissionCode);
    }
    var param = $.param({permissionCodes: permissionCodes.toString()});
    $.ajax({
        url: baseURL + "openRest/oauth2/checkPermission?" + param,
        contentType: "application/json",
        async: false,
        success: function (r) {
            if (Boolean(r.successful)) {
                pvm.permissions = r.data;
            } else {
                layer.msg("获取权限失败 ：" + r.msg);
            }
        }
    });
}

function getErrorResponseMsg(errorResponse) {
    var msg = errorResponse.msg;
    if (errorResponse.code == 'CBS-602') {
        var errors = errorResponse.data;
        if (errors != null) {
            for (var i = 0; i < errors.length; i++) {
                var error = errors[i];
                msg = msg + "<br>[" + error.fieldId + "]:" + error.errorMessage;
            }
        }
    } else if (errorResponse.code == "CBS-521" && errorResponse.data != null) {
        msg = msg + "<br>" + errorResponse.data;
    }
    return msg;
}

function sysAdmin() {
    return window.parent.sysAdmin;
}


//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
};

function parseURL(url) {
    var a = document.createElement('a');
    a.href = url;
    return {
        source: url,
        protocol: a.protocol.replace(':', ''),
        host: a.hostname,
        port: a.port,
        query: a.search,
        params: (function () {
            var ret = {},
                seg = a.search.replace(/^\?/, '').split('&'),
                len = seg.length, i = 0, s;
            for (; i < len; i++) {
                if (!seg[i]) {
                    continue;
                }
                s = seg[i].split('=');
                ret[s[0]] = s[1];
            }
            return ret;
        })(),
        file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
        hash: a.hash.replace('#', ''),
        path: a.pathname.replace(/^([^\/])/, '/$1'),
        relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
        segments: a.pathname.replace(/^\//, '').split('/')
    };
}

T.p = url;




//重写alert
window.alert = function (msg, callback) {
    $("#alertLayer").remove();
    var alert_html = '<div class="alert_layer eb-layer page-content" id="alertLayer"><div class="eb-layer-title">提示</div>'
                        + '<div class="alert_main">' + msg + '</div>'
                        + '<div class="alert_btns"><button class="eb-btn-search" type="button" id="alert_close_btn">'
                        + '<i class="fa fa-close"></i> &nbsp; 关闭</button>'
                        + '</div></div>';
    $("body").append(alert_html);
    $(".alert_layer").css({
        "padding": "0 0 0 0"
    });
    $(".alert_main").css({
        "text-align": 'center',
        "font-size" : "26px",
        "color": "#00ffff",
        "width": "100%",
        "margin-bottom": "24px",
        "word-break": "break-all"
    });
    $(".alert_btns").css({
        "position": "relative",
        "text-align": "right",
        "width": "100%",
        "padding-right": "20px",
        "margin-top": "10px",
        "margin-bottom": "10px"
    });    
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#alertLayer'),
        area: ['400px', ''], //宽高
    });
    $("#alert_close_btn").on("click", function () {
        if (typeof(callback) == "function") {
            callback();
        }
        layer.close(layer.index);
    });
    // parent.layer.alert(msg, function (index) {
    //     parent.layer.close(index);
    //     if (typeof(callback) === "function") {
    //         callback("ok");
    //     }
    // });
}

//重写confirm式样框
window.confirm = function (msg, callback) {
    $("#confirmLayer").remove();
    var confirm_html = '<div class="confirm_layer eb-layer page-content" id="confirmLayer"><div class="eb-layer-title">提示</div>'
                        + '<div class="confirm_main">' + msg + '</div>'
                        + '<div class="confirm_btns"><button class="eb-btn-search" type="button" id="confirm_cancel_btn">'
                        + '<i class="fa fa-close"></i> &nbsp; 取消</button>'
                        + '<button class="eb-btn-search" type="button" id="confirm_ok_btn">'
                        + '<i class="fa fa-check"></i> &nbsp; 确认</button>'
                        + '</div></div>';
    $("body").append(confirm_html);
    $(".confirm_layer").css({
        "padding": "0 0 0 0"
    });
    $(".confirm_main").css({
        "text-align": 'center',
        "font-size" : "26px",
        "color": "#00ffff",
        "width": "100%",
        "margin-bottom": "24px",
        "word-break": "break-all"
    });
    $(".confirm_btns").css({
        "position": "relative",
        "text-align": "center",
        "width": "100%",
        "margin-top": "10px",
        "margin-bottom": "10px"
    });    
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#confirmLayer'),
        area: ['400px', ''], //宽高
    });
    $("#confirm_ok_btn").on("click", function () {
        if (typeof(callback) == "function") {
            callback();
        }
        layer.close(layer.index);
    });
    $("#confirm_cancel_btn").on("click", function () {
        layer.close(layer.index);
    });
    // parent.layer.confirm(msg, {skin: 'layui-layer-lan',icon: 3, title: '提示', btn: ['确定', '取消']},
    //     function (index) {//确定事件
    //         parent.layer.close(index);
    //         if (typeof(callback) === "function") {
    //             callback("ok");
    //         }
    //     });
}


//jquery全局配置
$.ajaxSetup({
    dataType: "json",
    cache: false,
    headers: {
        "token": token,
        "Access-Control-Allow-Origin":"*"
    },
    complete: function (xhr) {
        //token过期，则跳转到登录页面
        if (xhr.responseJSON != null && xhr.responseJSON.code != null) {
            if (xhr.responseJSON.code == 'EB-102') {
                // parent.location.href = baseURL + 'login.html';
                parent.location.href = baseURI+'common/login.html';
            }
        }
    }
});

if ($.jgrid && typeof($.jgrid) != "undefined") {
    //jqgrid全局配置
    $.extend($.jgrid.defaults, {
        ajaxGridOptions: {
            headers: {
                "token": token
            }
        }
    });
}

//jqGrid的配置信息
if ($.jgrid && typeof($.jgrid) != "undefined") {
    $.jgrid.defaults.width = 1000;
    var searchDivHeight=$("#searchDiv").height();
    console.log(searchDivHeight);
    if(searchDivHeight!=null && searchDivHeight > 0){
        $.jgrid.defaults.height = $(this).height() - 148-searchDivHeight;
    }else{
         $.jgrid.defaults.height = $(this).height() - 210;
     }
    $.jgrid.defaults.responsive = true;
    $.jgrid.defaults.styleUI = 'Bootstrap';
}

//国际化
function i18n(language,callBack,module){
    var module = module==undefined?"":"_"+module;
    jQuery.i18n.properties({
        name:'messages'+module,
        path:baseURL+"i18n/",
        mode:'both',
        language:language,
        checkAvailableLanguages: true,
        async: true,
        callback: function() {
            callBack();
        }
    });
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }

    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    return grid.getGridParam("selarrrow");
}

function params(selector) {
    var formParams = {};
    $.each($(selector).serializeArray(), function (index, obj) {
        formParams[obj.name] = obj.value;
    });
    return formParams;
}

function toggleBar(jqGridId) {
    $("#sidebar").toggleClass("collapsed");
    $("#content").toggleClass("col-sm-12 col-sm-10");
    window.setTimeout(function () {
        var _jqGrid = $("#jqGrid");
        var _gviewJqGrid = $("#gview_jqGrid");
        if (jqGridId) {
            _jqGrid = $("#" + jqGridId);
            _gviewJqGrid = $("#gview_" + jqGridId);
        }
        _jqGrid.setGridWidth($("#content").width() - 50);
        _gviewJqGrid.find(".ui-jqgrid-htable").css({"width": $("#gbox_jqGrid").width()});
        _gviewJqGrid.find(".ui-jqgrid-hdiv").css({"width": $("#gbox_jqGrid").width()});
        _gviewJqGrid.find(".ui-jqgrid-bdiv").css({"width": $("#gbox_jqGrid").width()});
    }, 30);
}

Array.prototype.remove = function (index) {
    if (isNaN(index) || index > this.length) {
        return false;
    }
    for (var i = 0, n = 0; i < this.length; i++) {
        if (this[i] != this[index]) {
            this[n++] = this[i];
        }
    }
    this.length -= 1
}

Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};
Array.prototype.delete = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.remove(index);
    }
};

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function resizeLayer(layerIndex, layerInitWidth, layerInitHeight) {
    var docWidth = $(document).width() * 0.75;
    var docHeight = $(document).height() * 0.55;
    var minWidth = layerInitWidth > docWidth ? docWidth : layerInitWidth;
    var minHeight = layerInitHeight > docHeight ? docHeight : layerInitHeight;
    console.log("doc:", docWidth, docHeight);
    console.log("lay:", layerInitWidth, layerInitHeight);
    console.log("min:", minWidth, minHeight);
    layer.style(layerIndex, {
        top: 0,
        width: minWidth,
        height: minHeight
    });
}

/*
*数组元素去重
*/
if (typeof Array.prototype.distinct !== "function") {
    Array.prototype.distinct = function () {
        this.sort();
        for (var i = 0; i < this.length - 1; i++) {
            if ($.isPlainObject(this[i]) && $.isPlainObject(this[i + 1])) {
                if (o2o(this[i], this[i + 1])) {
                    this.splice(i, 1);
                }
            } else if ($.isArray(this[i]) && $.isArray(this[i + 1])) {
                if (a2a(this[i], this[i + 1])) {
                    this.splice(i, 1);
                }
            } else if (this[i] === this[i + 1]) {
                this.splice(i, 1);
            }
        }
    }
}

/*
*比较对象是否相同
*/
function o2o(o1, o2) {
    if (!($.isPlainObject(o1) && $.isPlainObject(o2))) {
        return false;
    }

    var k1k2 = [], k1 = [], k2 = [];
    $.each(o1, function (k, v) {
        k1.push(k);
    });

    $.each(o2, function (k, v) {
        k2.push(k);
    });
    if (k1.length !== k2.length) {
        return false;
    }
    k1k2 = k1;
    k1k2 = k1k2.concat(k2);
    k1k2.distinct();
    if (k1.length !== k1k2.length || k2.length !== k1k2.length) {
        return false;
    }

    var flag = true;
    $.each(k1k2, function (i, v) {
        var v1 = o1[v];
        var v2 = o2[v];
        if (typeof v1 !== typeof v2) {
            flag = false;
        } else {
            if ($.isPlainObject(v1) && $.isPlainObject(v2)) {//recursion
                flag = o2o(v1, v2);
                if (!flag) {
                    return false;
                }
            } else if ($.isArray(v1) && $.isArray(v2)) {
                flag = a2a(v1, v2);
                if (!flag) {
                    return false;
                }
            } else {
                if (v1 !== v2) {
                    flag = false;
                }
            }
        }
    });
    return flag;
}

/*
*比较数组是否完全相同
*/
function a2a(a1, a2) {
    if (!($.isArray(a1) && $.isArray(a2))) {
        return false;
    }
    if (a1.length !== a2.length) {
        return false;
    }

    a1.sort();
    a2.sort();
    for (var i = 0; i < a1.length; i++) {
        if (typeof a1[i] !== typeof a2[i]) {
            return false;
        }
        if ($.isPlainObject(a1[i]) && $.isPlainObject(a2[i])) {
            var retVal = o2o(a1[i], a2[i]);
            if (!retVal) {
                return false;
            }
        } else if ($.isArray(a1[i]) && $.isArray(a2[i])) {//recursion
            if (!a2a(a1[i], a2[i])) {
                return false;
            }
        } else if (a1[i] !== a2[i]) {
            return false;
        }
    }
    return true;
}

function getErrorMsg(errorResponse) {
    if(errorResponse.errorInfo==null || errorResponse.errorInfo==undefined){
        return errorResponse.msg;
    }else{
        return errorResponse.errorInfo;
    }

}


function matchAndGetDictValue(dictArray,b,dictGroupCode) {
    if(isEmpty(dictArray)){
        return b
    }
    var r = '';
    for(var i=0;i<dictArray.length;i++){
        var dict = dictArray[i];
        if(isEmpty(r) && b == dict.dictKey && (isEmpty(dictGroupCode) || dict.dictGroupCode == dictGroupCode)){
            r = dict.dictValue;
            break
        }
    }
    return r==''? b : r;
}

//关闭右边浮出框
function leftSidebarClick() {
    $(".right-float-div").animate({width:"0px"},300,function () {
        $(".float-div").hide();
    });
}


function getQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

//文件大小转换
function fileChange(limit){
    var size = "";
    if(limit < 0.1 * 1024){                            //小于0.1KB，则转化成B
        size = limit.toFixed(2) + "B"
    }else if(limit < 0.1 * 1024 * 1024){            //小于0.1MB，则转化成KB
        size = (limit/1024).toFixed(2) + "KB"
    }else if(limit < 0.1 * 1024 * 1024 * 1024){        //小于0.1GB，则转化成MB
        size = (limit/(1024 * 1024)).toFixed(2) + "MB"
    }else{                                            //其他转化成GB
        size = (limit/(1024 * 1024 * 1024)).toFixed(2) + "GB"
    }

    var sizeStr = size + "";                        //转成字符串
    var index = sizeStr.indexOf(".");                    //获取小数点处的索引
    var dou = sizeStr.substr(index + 1 ,2)            //获取小数点后两位的值
    if(dou == "00"){                                //判断后两位是否为00，如果是则删除00
        return sizeStr.substring(0, index) + sizeStr.substr(index + 3, 2)
    }
    return size;
}


function DateAdd(interval, number, date) {
    switch (interval) {
        case "y": {
            date.setFullYear(date.getFullYear() + number);
            return date;
            break;
        }
        case "q": {
            date.setMonth(date.getMonth() + number * 3);
            return date;
            break;
        }
        case "m ": {
            date.setMonth(date.getMonth() + number);
            return date;
            break;
        }
        case "w": {
            date.setDate(date.getDate() + number * 7);
            return date;
            break;
        }
        case "d": {
            date.setDate(date.getDate() + number);
            return date;
            break;
        }
        case "h": {
            date.setHours(date.getHours() + number);
            return date;
            break;
        }
        case "m": {
            date.setMinutes(date.getMinutes() + number);
            return date;
            break;
        }
        case "s": {
            date.setSeconds(date.getSeconds() + number);
            return date;
            break;
        }
        default: {
          /*  date.setDate(d.getDate() + number);
            return date;*/
            break;
        }
    }
}

var SYS_RES_TYPE_PLATFORM="01";
var SYS_RES_TYPE_TV="02";
var SYS_RES_TYPE_STATION="03";
var SYS_RES_TYPE_PLATFORM_COMMON="0101";
var SYS_RES_TYPE_PLATFORM_CREATE="0102";
var SYS_RES_TYPE_STATION_EBS="0314";

var SYS_SUB_RES_TYPE_PLATFORM="01";
var SYS_SUB_RES_TYPE_ADAPTER="02";
var SYS_SUB_RES_TYPE_TRANS_DEV="03";
var SYS_SUB_RES_TYPE_TERMINAL="04";
var SYS_SUB_RES_TYPE_STATION="05";

var DICT_GROUP_RES_TYPE_PLATFORM="RES_TYPE_PLATFORM_DICT";
var DICT_GROUP_RES_TYPE_TV="RES_TYPE_TV_DICT";
var DICT_GROUP_RES_TYPE_STATION="RES_TYPE_STATION_DICT";
var DICT_GROUP_SUB_RES_TYPE="SUB_RES_TYPE_DICT";

/**
 * 验证6-30位的字母数字下划线
 * @param {*} param 
 */
function checkAccount(param) {
    var path = /^[a-zA-Z]\w{5,31}$/;
    if (param.match(path)) {
        return true;
    }
    return false;
}