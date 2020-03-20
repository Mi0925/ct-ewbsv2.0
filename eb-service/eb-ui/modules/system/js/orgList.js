$(function () {
    initTree("0");
    $("#jqGrid").jqGrid({
        url: baseURL + '/safeRest/sys/org/page',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 100, key: true, hidden: true },
            { label: '单位名称', name: 'companyName', width: 100, align: "center" },
            { label: '联系人名称', name: 'contactName', width: 120, align: "center" },
            { label: '联系人电话', name: 'contactTel', width: 80, align: "center", },
            { label: '所属区域名称', name: 'areaName', width: 100, align: "center" },
            { label: '用户名称', name: 'userName', width: 50, align: "center" },
            { label: '创建时间', name: 'createTime', width: 100, align: "center" },
            {
                label: "操作",
                field: "empty",
                width: 100,
                align: "center",
                name: "id",
                formatter: function (cellvalue, options, row) {
                    var operateHtml = "<a  href='javascript:toOrgUpdate(\"" + cellvalue + "\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
                    operateHtml += "&nbsp;&nbsp;&nbsp;<a  href='javascript:toOrgDelete(\"" + cellvalue + "\")' style='text-decoration:underline;color: #00fbff'>删除</a>";
                    return operateHtml;
                }
            }
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        autowidth: true,
        pager: "#jqGridPager",
        jsonReader: {    //jsonReader来跟服务器端返回的数据做对应
            root: "data",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x": "hidden" });
            $("#jqGrid").css({ "width": $("#gbox_jqGrid").width() - 1 });
            $("#gview_jqGrid").find(".ui-jqgrid-htable").css({ "width": $("#gbox_jqGrid").width() });
            $("#gview_jqGrid").find(".ui-jqgrid-hdiv").css({ "width": $("#gbox_jqGrid").width() });
            $("#gview_jqGrid").find(".ui-jqgrid-bdiv").css({ "width": $("#gbox_jqGrid").width() });
        }
    });

    $("#jquserGrid").jqGrid({
        url: baseURL + 'safeRest/user/page?deleteFlag=false',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'userId', index: "userId", width: 10, hidden: true },
            { label: '账户', name: 'account', width: 20, align: "center" },
            { label: '姓名', name: 'userName', width: 20, align: "center" },
            {
                label: '状态', name: 'status', width: 20, align: "center",
                formatter: function (cellvalue, options, row) {
                    if (cellvalue == 1) {
                        return "启用";
                    } else {
                        return "停用";
                    }
                }
            },
            { label: '创建时间', name: 'createTime', width: 30, align: "center" }
        ],
        viewrecords: true,
        rowNum: 10,
        // width: "100%",
        // height: "100%",
        rowList: [10, 30, 50],
        rownumbers: true,
        autowidth: true,
        pager: "#jquserGridPager",
        jsonReader: {    //jsonReader来跟服务器端返回的数据做对应
            root: "data",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jquserGrid").closest(".ui-jquserGrid-bdiv").css({ "overflow-x": "hidden" });
            $("#jquserGrid").css({ "width": $("#gbox_jquserGrid").width() - 1 });
            $("#gview_jquserGrid").find(".ui-jqgrid-htable").css({ "width": $("#gbox_jquserGrid").width() });
            $("#gview_jquserGrid").find(".ui-jqgrid-hdiv").css({ "width": $("#gbox_jquserGrid").width() });
            $("#gview_jquserGrid").find(".ui-jqgrid-bdiv").css({ "width": $("#gbox_jquserGrid").width() });
            $("#jquserGrid").jqGrid("setGridWidth", 828);
        }
    });
    // $(window).resize(function () {
    //     $("#jquserGrid").setGridWidth($(window).width());
    // });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        permissions: {
            "sys:org:add": true,
            "sys:org:edit": true,
            "sys:org:view": true,
            "sys:org:ref": true,
        },
        req: {

        },
        orgInfo: {},
        orgUpdateShow: false,
        orgAddShow: false,
        title: "",
        pageEnum: [],
        areaList: []
    },
    filters: {
        //字符串过长，截取掉。
        filterFun: function (value) {
            if (value && value.length > 6) {
                value = value.substring(0, 5) + "...";
            }
            return value;
        }
    },
    methods: {
        add: function () {
            vm.orgUpdateShow = false;
            vm.orgAddShow = true;
            vm.title = "新增机构";
            vm.orgInfo = {};
            layer.open({
                type: 1,
                shade: false,
                title: false, //不显示标题
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: $('#orgLayer'),
                area: ['500px', '520px'], //宽高
            });
        },

        save: function () {
            if (isEmpty(vm.orgInfo.companyName)) {
                alert("请输入机构名称！");
                return;
            }
            if (isEmpty(vm.orgInfo.contactName)) {
                alert("请输入联系人名称！");
                return;
            }
            if (isEmpty(vm.orgInfo.contactTel)) {
                alert("请输入联系人电话！");
                return;
            }
            if (isEmpty(vm.orgInfo.areaName)) {
                alert("请选择所属区域！");
                return;
            }
            if (isEmpty(vm.orgInfo.userName)) {
                alert("请选择关联用户！");
                return;
            }
            var url = '';
            var type = "post";
            if (vm.orgUpdateShow) {
                url = baseURL + "/safeRest/sys/org/update";
                type = "put";
            } else {
                url = baseURL + "/safeRest/sys/org/save";
            }
            $.ajax({
                type: type,
                url: url,
                contentType: "application/json",
                data: JSON.stringify(vm.orgInfo),
                success: function (r) {
                    if (r.successful) {
                        vm.closeLayer();
                        reload();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        closeLayer: function () {
            layer.closeAll();
        },
        openAreaTreeDlg: function () {
            layer.open({
                type: 1,
                shade: false,
                //  title: "区域选择", 
                title: false, //不显示标题
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: $('#areaTreeLayer'),
                area: ['350px', '500px'], //宽高
            });
        },
        closeLayerTree: function () {
            layer.close(layer.index);
        },
        addTreeArea: function () {
            // 按钮【按钮二】的回调
            var nodes = ztree.getCheckedNodes();
            if (nodes.length > 0) {
                vm.orgInfo.areaId = nodes[0].id;
                vm.orgInfo.areaName = nodes[0].areaName;
                $("#OrgareaName").val(vm.orgInfo.areaName);
            }
            layer.close(layer.index);
        },
        openUserSelectDlg: function () {
            layer.open({
                type: 1,
                shade: false,
                title: false, //不显示标题
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: $('#selectUserlayer'),
                area: ['850px', '500px'], //宽高
            });
        },
        addUser: function () {
            //按钮【按钮二】的回调
            var rowid = $("#jquserGrid").jqGrid("getGridParam", "selrow");
            if (rowid != null) {
                var rowData = jQuery("#jquserGrid").jqGrid("getRowData", rowid);
                vm.orgInfo.userId = rowData.userId;
                vm.orgInfo.userName = rowData.userName;
                $("#OrguserName").val(vm.orgInfo.userName);
            }
            layer.close(layer.index);
        }
    }
});


function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        postData: { companyName: vm.req.companyName, contactName: vm.req.contactName, areaName: vm.req.areaName, userName: vm.req.userName },
        page: page
    }).trigger("reloadGrid");
}


function toOrgDelete(id) {
    confirm("确认删除？", function () {
        $.ajax({
            type: "delete",
            url: baseURL + "/safeRest/sys/org/delete/" + id,
            contentType: "application/json",
            //data:{'id':id},
            success: function (r) {
                if (r.successful) {
                    reload();
                } else {
                    // layer.msg(r.msg);
                    alert(r.msg);
                }
            }
        });
    });
}

function toOrgUpdate(id) {
    vm.orgUpdateShow = true;
    vm.orgAddShow = true;
    vm.title = "修改机构";
    $.ajax({
        type: "get",
        url: baseURL + "/safeRest/sys/org/get/" + id,
        contentType: "application/json",
        data: { 'id': id },
        success: function (r) {
            if (r.successful) {
                vm.orgInfo = r.data;
                layer.open({
                    type: 1,
                    shade: false,
                    title: false, //不显示标题
                    closeBtn: 0, //不显示关闭按钮
                    anim: 2,
                    shadeClose: true, //开启遮罩关闭
                    content: $('#orgLayer'),
                    area: ['500px', '500px'], //宽高
                });
            } else {
                // layer.msg(r.msg);
                alert(r.msg);
            }
        }
    });
}
var setting = {
    async: {
        enable: true,//设置 zTree 是否开启异步加载模式
        url: getAreaTreeNodesUrl,
        type: "get",
        dataFilter: filter
    },
    data: {
        simpleData: {
            enable: true,//是否采用简单数据模式
            idKey: "areaCode",//树节点ID名称
            pIdKey: "parentAreaCode",//父节点ID名称
            rootPId: null//根节点ID
        },
        key: {
            name: "areaName",
            isParent: 'parent',
            title: "areaName",
            checked: "checked"
        }
    },
    check: {
        enable: true,
        nocheckInherit: true,
        chkDisabledInherit: true,
        chkboxType: { "Y": "", "N": "s" },
        chkStyle: "radio",  //多选框
        radioType: "all"   //对所有节点设置单选
    },
    callback: {
        onCheck: treeNodeChecked,
    },
    view: {
        fontCss: { 'color': '#00ffff', 'font-size': "16px" }
    }
};
var ztree;
function initTree(parentAreaCode) {
    $.ajax({
        url: baseURL + "safeRest/area/getByParentAreaCode",
        type: "get",
        contentType: "application/json",
        data: { parentAreaCode: parentAreaCode },
        success: function (r) {
            var nodeDatas = initTreeCheckedStatus(r.data);
            console.log(r.data);
            ztree = $.fn.zTree.init($("#areaTree"), setting, r.data);//初始化树节点时，添加同步获取的数据
            var nodeList = ztree.getNodes();
            if (nodeList.length > 0) {
                ztree.expandNode(nodeList[0], true);
                // ztree.setting.callback.onClick(null, ztree.setting.treeId, nodeList[0]);
            }
        }
    });
}
function getAreaTreeNodesUrl(treeId, treeNode) {
    var param = "parentAreaCode=" + treeNode.areaCode;
    return baseURL + "safeRest/area/getByParentAreaCode" + "?" + param;
}
function filter(treeId, parentNode, childNodes) {
    return initTreeCheckedStatus(childNodes.data);
}
function initTreeCheckedStatus(childNodes) {
    var nodeDatas = childNodes;
    var areaList = vm.areaList;
    for (var i = 0; i < nodeDatas.length; i++) {
        var node = nodeDatas[i];
        node.checked = false;
        for (var j = 0; j < areaList.length; j++) {
            var area = areaList[j];
            if (area.areaCode == node.areaCode) {
                node.checked = true;
                break;
            }
        }
    }
    return nodeDatas;
}
function treeNodeChecked(event, treeId, treeNode) {
    var nodes = ztree.getCheckedNodes();
    var areaDataList = [];
    var names = '';
    var areaList = [];
    for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];
        var needAdd = true;
        if (!isEmpty(vm.areaList)) {
            var pAreaList = vm.areaList;
            for (var j = 0; j < pAreaList.length; j++) {
                var p_area = pAreaList[j];
                if (node.areaCode.indexOf(p_area.areaCode) == 0 && node.areaCode != p_area.areaCode) {
                    needAdd = false;
                    break;
                }
            }
        }

        if (needAdd) {
            var areaData = {
                areaName: node.areaName,
                areaCode: node.areaCode
            };
            areaList.push(areaData);
        }

    }
    //删除该节点子节点，以及更下一级节点

    for (var i = 0; i < areaList.length; i++) {
        var area = areaList[i];
        if (area.areaCode.indexOf(treeNode.areaCode) < 0 || area.areaCode == treeNode.areaCode) {
            areaDataList.push(area);
        }
    }

    for (var i = 0; i < areaDataList.length; i++) {
        if (isEmpty(names)) {
            names += areaDataList[i].areaName;
        } else {
            names += "," + areaDataList[i].areaName;
        }
    }
    vm.areaList = areaDataList;
    //vm.areaNames = names;
}