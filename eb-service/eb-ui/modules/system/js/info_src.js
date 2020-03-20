$(function () {

    vm.getEnum(dictGroupCodes);
    vm.getThisPlatformInfo();

    $("#jqGrid").jqGrid({
        url: baseURL + '/safeRest/ebrView/infoSrc/page',
        datatype: "json",
        colModel: [
            { label: '资源名称', name: 'ebrName', width: 100, align: "center" },
            { label: '单位名称', name: 'orgName', width: 80, align: "center" },
            { label: '联系人', name: 'contact', width: 60, align: "center" },
            { label: '联系电话', name: 'phoneNumber', width: 80, align: "center" },
            {
                label: '资源地址', name: 'srcHost', width: 150, align: "center"/*,
                formatter:function (cellvalue,options,row) {
                    if(cellvalue && cellvalue.length > 15) {
                        return cellvalue.substring(0,15)+ '...';
                    }
                    return cellvalue;
                }*/},
            {
                label: '目标地址', name: 'ebrUrl', width: 150, align: "center"/*,
                formatter:function (cellvalue,options,row) {
                    if(cellvalue && cellvalue.length > 15) {
                        return cellvalue.substring(0,15)+ '...';
                    }
                    return cellvalue;
                }*/},
            { label: '资源编号', name: 'ebrId', index: "ebrId", width: 150, key: true },
            { label: '备注', name: 'remark', width: 80, align: "center" },
            {
                label: '操作', name: '', width: 80, align: "center",
                formatter: function (cellvalue, options, row) {
                    var operateHtml = "";
                    if (vm.permissions["sys:infoSrc:edit"]) {
                        operateHtml += '&nbsp;&nbsp;&nbsp;<a style=\'text-decoration:underline;cursor:pointer;color: #00fbff\' onclick="toUpdateEbr(\'' + row.ebrId + '\',\'' + row.ebrType + '\')">修改</a>'
                    }
                    if (vm.permissions["sys:infoSrc:delete"]) {
                        operateHtml += '&nbsp;&nbsp;&nbsp;<a style=\'text-decoration:underline;cursor:pointer;color: #00fbff\' onclick="deleteEbr(\'' + row.ebrId + '\',\'' + row.ebrType + '\')">删除</a>'
                    }
                    return operateHtml;
                }
            },

        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        gridview: true,
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
        loadComplete: function () {

        },
        gridComplete: function () {
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x": "hidden" });
            $("#jqGrid").css({ "width": $("#gbox_jqGrid").width() - 1 });
            $("#gview_jqGrid").find(".ui-jqgrid-htable").css({ "width": $("#gbox_jqGrid").width() });
            $("#gview_jqGrid").find(".ui-jqgrid-hdiv").css({ "width": $("#gbox_jqGrid").width() });
            $("#gview_jqGrid").find(".ui-jqgrid-bdiv").css({ "width": $("#gbox_jqGrid").width() });
        },
        //当用户点击当前行在未选择此行时触发。rowid：此行id；e：事件对象。返回值为ture或者false。如果返回true则选择完成，如果返回false则不会选择此行也不会触发其他事件
        beforeSelectRow: function (rowid, e) {

        },
        //当点击单元格时触发。rowid：当前行id；iCol：当前单元格索引；cellContent：当前单元格内容；e：event对象
        onCellSelect: function (rowid, iCol, cellcontent, e) {

        },
        //当选择行时触发此事件。rowid：当前行id；status：选择状态，当multiselect 为true时此参数才可用
        onSelectRow: function (rowid, status) {
            var rowData = $("#jqGrid").jqGrid('getRowData', rowid);

        },
        //multiselect为ture，且点击头部的checkbox时才会触发此事件。aRowids：所有选中行的id集合，为一个数组。status：boolean变量说明checkbox的选择状态，true选中false不选中。无论checkbox是否选择，aRowids始终有 值
        onSelectAll: function (aRowids, status) {

        },
        //在行上右击鼠标时触发此事件。rowid：当前行id；iRow：当前行位置索引；iCol：当前单元格位置索引；e：event对象
        onRightClickRow: function (rowid, iRow, iCol, e) {

        },
        //双击行时触发。rowid：当前行id；iRow：当前行索引位置；iCol：当前单元格位置索引；e:event对象
        ondblClickRow: function (rowid, iRow, iCol, e) {

        }
    });

});


var dictGroupCodes = "EBR_TYPE,RES_STATUS_DICT,ORG_TYPE_DICT,RES_LEVEL_DICT,EBR_BROADCAST_SUB_TYPE,EBR_PLATFORM_SUB_TYPE,SUB_RES_TYPE_DICT,RES_CHANNEL_DICT";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req: {
            terminalEbrName: '',
            terminalState: ''
        },
        permissions: {
            "sys:infoSrc:add": false,
            "sys:infoSrc:detail": false,
            "sys:infoSrc:list": false,
            "sys:infoSrc:edit": false,
            "sys:infoSrc:delete": false
        },
        pageEnum: [],
        title: null,
        addLayer: true,
        //是否是当前平台级别
        currentLevel: true,
        isRelDisabled: true,
        thisEbrInfo: {},
        stationList: [],
        adapterList: [],
        resTypeList: [],
        ebrInfo: {
            orgType: "",
            ebrType: "",
            resType: "",
            subResType: "",
            ebrLevel: "",
            resChannel: '1',
            ebrStId: "",
            dataType: ""
        },
        filetype: "",
        upload: {
            myDropzone: null,
            layerIndex: null
        }
    },
    mounted: function () {
        getPermission(this);

    },
    created: function () {
        this.initData();
    },
    filters: {
        //获取枚举名称
        getEnumName: function (value, dictCode) {
            if (isEmpty(vm.pageEnum) || isEmpty(dictCode)) {
                return value;
            }
            return matchAndGetDictValue(vm.pageEnum, value, dictCode);
        },
        //字符串过长，截取掉。
        filterFun: function (value, len) {
            if (value && value.length > len) {
                value = value.substring(0, len) + '...';
            }
            return value;
        },
    },
    watch: {
        //监听ebrinfo的属性是否改变
        ebrInfo: {
            handler(val) {
                /**
                 * 监听资源子类型属性（subResType）是否改变
                 */
                const subResType = val.subResType;
                if (subResType == SYS_SUB_RES_TYPE_ADAPTER) {
                    vm.isRelDisabled = false;
                    vm.getThisEbrList('05');
                } else {
                    vm.ebrInfo.ebrStId = "";
                    vm.isRelDisabled = true;
                }
                //选择类型为平台或者台站联系人和电话需要必填
                if (subResType == SYS_SUB_RES_TYPE_PLATFORM || subResType == SYS_SUB_RES_TYPE_STATION) {
                    $(".tel-show").show();
                } else {
                    $(".tel-show").hide();
                }
            },
            deep: true,
        }
    },
    methods: {
        reload: function () {
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "terminalEbrName": vm.req.terminalEbrName
                    , "terminalState": vm.req.terminalState
                },
                page: page
            }).trigger("reloadGrid");
        },
        initData: function () {
            $("#my-awesome-dropzone").attr("action", baseURL + "safeRest/cert/importCert");
            Dropzone.options.myAwesomeDropzone = {
                url: baseURL + "safeRest/cert/importCert",
                autoProcessQueue: false,
                uploadMultiple: false,
                parallelUploads: 100,
                addRemoveLinks: true,
                dictRemoveFile: "删除",
                maxFiles: 1, // 一次最多上传文件
                maxFilesize: 50, // 单个文件最大内存
                // acceptedFiles: "audio/mpeg,audio/x-aac", // 只能上传以MP3和aac结尾的文件
                paramName: "multipartFile",
                headers: {
                    "token": token
                },
                accept: function (file, done) {
                    done();
                },

                init: function () {
                    vm.upload.myDropzone = this;
                    this.on("sendingmultiple", function (file) {
                    });
                    this.on("sendingmultiple", function () {
                    });
                    this.on("successmultiple", function (files, response) {
                    });
                    this.on("errormultiple", function (files, response) {
                    });
                    this.on("error", function (files, err, xhr) {
                        var dz = vm.upload.myDropzone;
                        var rfs = dz.getRejectedFiles();
                        if (rfs && rfs.length > 0) {
                            for (var i = 0; i < rfs.length; i++) {
                                dz.removeFile(rfs[i]);
                            }
                        }
                        layer.msg(err);
                    });
                    this.on("success", function (file, response) {
                        if (response.successful) {
                            layer.msg(response.msg);
                            console.log("===========" + response.data);
                            vm.reload();
                            /*$("#tabs").find("#tabs1").removeClass("active");
                            $("#tabs").find("#tabs2").addClass("active");
                            changeJqgrid(2);*/
                        } else if (response.code) {
                            layer.msg(response.code);
                        } else {
                            layer.msg(response);
                        }
                    });
                    this.on("queuecomplete", function () {
                        var dz = vm.upload.myDropzone;
                        if (dz.files != null && dz.files.length > 0) {
                            var sf = dz.getFilesWithStatus(Dropzone.SUCCESS);
                            if (sf != null && dz.files.length == sf.length) {
                                layer.close(vm.upload.layerIndex);
                                vm.reload();
                            }
                        }
                    });
                },
                previewTemplate: document.querySelector('#dropzoneTpl').innerHTML
            };
        },
        getEnum: function (dictGroupCode) {
            $.ajax({
                type: "get",
                async: false,
                url: baseURL + "safeRest/dict/getByGroupCode",
                contentType: "application/json",
                data: { dictGroupCode: dictGroupCode },
                success: function (r) {
                    if (r.successful) {
                        vm.pageEnum = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getThisPlatformInfo: function () {
            $.ajax({
                type: "get",
                async: false,
                url: baseURL + "safeRest/ebrPlatform/this",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful) {
                        vm.thisEbrInfo = r.data;
                    }
                }
            });
        },
        getThisEbrList: function (subResType) {
            $.ajax({
                type: "get",
                async: false,
                url: baseURL + "safeRest/ebrView/listEbrListforType",
                contentType: "application/json",
                data: { 'subResType': '05' },
                success: function (r) {
                    if (r.successful) {
                        vm.stationList = r.data;
                    }
                }
            });
        },
        getResTypeList: function (ebrType) {
            var dictGroupCode = '';
            if (ebrType == SYS_RES_TYPE_PLATFORM) {
                dictGroupCode = DICT_GROUP_RES_TYPE_PLATFORM;
            } else if (ebrType == SYS_RES_TYPE_TV) {
                dictGroupCode = DICT_GROUP_RES_TYPE_TV;
            } else if (ebrType == SYS_RES_TYPE_STATION) {
                dictGroupCode = DICT_GROUP_RES_TYPE_STATION;
            }
            if (!isEmpty(dictGroupCode)) {
                $.ajax({
                    type: "get",
                    async: false,
                    url: baseURL + "safeRest/dict/getByGroupCode",
                    contentType: "application/json",
                    data: { dictGroupCode: dictGroupCode },
                    success: function (r) {
                        if (r.successful) {
                            vm.resTypeList = r.data;
                        } else {
                            layer.msg(r.msg);
                        }
                    }
                });
            }
        },
        getEbrInfo: function (value, ebrType) {
            if (isEmpty(value)) {
                return;
            }
            var url = "";
            if (ebrType == SYS_SUB_RES_TYPE_PLATFORM) {
                url = baseURL + "safeRest/ebrPlatform/" + value;
            } else if (ebrType == SYS_SUB_RES_TYPE_TRANS_DEV) {
                url = baseURL + "safeRest/ebr/Broadcast/" + value;
            } else if (ebrType == SYS_SUB_RES_TYPE_STATION) {
                url = baseURL + "safeRest/ebr/ebrStation/" + value;
            } else if (ebrType == SYS_SUB_RES_TYPE_ADAPTER) {
                url = baseURL + "safeRest/ebr/ebrAdapter/" + value;
            }
            $.ajax({
                type: "get",
                async: false,
                url: url,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful) {
                        if (r.data.psEbrId) {
                            EbrPlateformToEbrInfo(r.data);
                        } else if (r.data.bsEbrId) {
                            EbrBroadcastToEbrInfo(r.data);
                        } else if (r.data.ebrAsId) {
                            EbrAdapterToEbrInfo(r.data);
                        } else if (r.data.ebrStId) {
                            EbrStationToEbrInfo(r.data);
                        }
                        if (!isEmpty(r.data) && !isEmpty(r.data.ebrType)) {
                            vm.getResTypeList(r.data.ebrType);
                        }
                    }
                }
            });
        },
        closeLayer: function () {
            layer.closeAll();
        },
        importCertFile: function (type) {
            vm.filetype = type;
            vm.upload.layerIndex = layer.open({
                type: 1,
                skin: 'layui-bg-red',
                title: '导入文件',
                area: ['500px', '300px'],
                shadeClose: false,
                content: jQuery("#uploadFileDiv2"),
                btn: ['导入证书'],
                offset: 'auto',
                yes: function () {
                    vm.upload.myDropzone.processQueue();
                },
                end: function () {
                    vm.title = null;
                    vm.upload.myDropzone.removeAllFiles(true);
                    //$("#rename_form").validate().resetForm();
                }
            });

        },
    }
});
function ebrTypeChange(obj) {
    var ebrType = $(obj).val();
    vm.getResTypeList(ebrType);
    if (ebrType == SYS_RES_TYPE_PLATFORM) {
        vm.ebrInfo.subResType = SYS_SUB_RES_TYPE_PLATFORM;
    } else {
        vm.ebrInfo.subResType = "";
    }
}
function ebrLevelChange(obj) {
    var level = $(obj).val();
    if (vm.thisEbrInfo.platLevel == level) {
        vm.currentLevel = true;
        vm.ebrInfo.ebrType = "";
        vm.ebrInfo.resType = "";
        vm.ebrInfo.subResType = "";
    } else {
        vm.currentLevel = false;
        vm.ebrInfo.ebrType = SYS_RES_TYPE_PLATFORM;
        vm.ebrInfo.resType = SYS_RES_TYPE_PLATFORM_COMMON;
        vm.ebrInfo.subResType = SYS_SUB_RES_TYPE_PLATFORM;
    }
}

function toUpdateEbr(ebrId, ebrType) {
    vm.getEbrInfo(ebrId, ebrType);
    showEbrUpdateLayer();
}

function showEbrUpdateLayer() {
    vm.addLayer = false;
    vm.title = "修改资源";
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#ebrLayer'),
        area: ['1100px', '600px'], //宽高
    });
}
function showEbrAddLayer() {
    vm.ebrInfo = { orgType: "", ebrType: "", ebrLevel: "", resType: "", subResType: "" };
    vm.addLayer = true;
    vm.title = "新增资源";
    vm.isRelDisabled = true;
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#ebrLayer'),
        area: ['1100px', '600px'], //宽高
    });
}
function deleteEbr(ebrId, reType) {
    if (isEmpty(ebrId) || isEmpty(reType)) {
        return;
    }
    var url = "";
    if (reType == SYS_SUB_RES_TYPE_TRANS_DEV) {
        url = baseURL + "/safeRest/ebr/Broadcast/delete/" + ebrId;
    } else if (reType == SYS_SUB_RES_TYPE_PLATFORM) {
        url = baseURL + "/safeRest/ebrPlatform/delete/" + ebrId;
    } else if (reType == SYS_SUB_RES_TYPE_STATION) {
        url = baseURL + "/safeRest/ebr/ebrStation/delete/" + ebrId;
    } else if (reType == SYS_SUB_RES_TYPE_ADAPTER) {
        url = baseURL + "/safeRest/ebr/ebrAdapter/delete/" + ebrId;
    }
    confirm("删除文件夹，该文件夹下的所有文件及文件夹将移动到系统默认文件夹下。确认要删除吗？", function () {
        $.ajax({
            type: "delete",
            async: false,
            url: url,
            contentType: "application/json",
            success: function (r) {
                if (r.successful) {
                    vm.reload();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}


function saveOrUpdateEbr() {
    if (isEmpty(vm.ebrInfo.ebrName)) {
        alert("请输入资源名称！");
        return;
    }
    if (isEmpty(vm.ebrInfo.ebrLevel)) {
        alert("请选择资源级别！");
        return;
    }
    if (isEmpty(vm.ebrInfo.ebrId) && vm.ebrInfo.ebrLevel < 4) {
        alert("请输入资源编码！");
        return;
    }
    var ebrType = vm.ebrInfo.ebrType;
    // var  resType = vm.ebrInfo.resType;
    var subResType = vm.ebrInfo.subResType;
    if (isEmpty(ebrType)) {
        alert("请选择资源类型！");
        return;
    }
    if (isEmpty(vm.ebrInfo.subResType)) {
        alert("请选择资源子类型！");
        return;
    }
    if (isEmpty(vm.ebrInfo.resChannel)) {
        alert("请选择消息发送类型！");
        return;
    }
    if (isEmpty(vm.ebrInfo.longitude)) {
        alert("请输入平台经度！");
        return;
    }
    if (isEmpty(vm.ebrInfo.latitude)) {
        alert("请输入平台经度！");
        return;
    }
    //如果为平台或者台站进行校验
    if (ebrType == SYS_SUB_RES_TYPE_PLATFORM || ebrType == SYS_SUB_RES_TYPE_STATION) {
        if (isEmpty(vm.ebrInfo.contact)) {
            alert("请输入联系人！");
            return;
        }
        if (isEmpty(vm.ebrInfo.phoneNumber)) {
            alert("请输入联系电话！");
            return;
        }
    }

    var url = "";
    var ajaxType = '';
    var data = null;
    if (vm.addLayer) {
        ajaxType = "post";
        if (subResType == SYS_SUB_RES_TYPE_PLATFORM) {
            //应急广播平台
            url = baseURL + "safeRest/ebrPlatform/save";
            data = toEbrPlatformInfo();
        } else if (subResType == SYS_SUB_RES_TYPE_TRANS_DEV) {
            //传输覆盖播出设备（大喇叭系统）
            url = baseURL + "safeRest/ebr/Broadcast/save";
            data = toEbrBroadcastInfo();
        } else if (subResType == SYS_SUB_RES_TYPE_ADAPTER) {
            //应急广播适配器
            url = baseURL + "safeRest/ebr/ebrAdapter/save";
            data = toEbrAdapterInfo();
        } else if (subResType == SYS_SUB_RES_TYPE_STATION) {
            //台站
            url = baseURL + "safeRest/ebr/ebrStation/save";
            data = toEbrStationInfo();
        }
    } else {
        ajaxType = "put";
        if (vm.ebrInfo.dataType == SYS_SUB_RES_TYPE_PLATFORM) {
            url = baseURL + "safeRest/ebrPlatform/update";
            data = toEbrPlatformInfo();
            data.subResType = vm.ebrInfo.dataType;
        } else if (vm.ebrInfo.dataType == SYS_SUB_RES_TYPE_TRANS_DEV) {
            url = baseURL + "safeRest/ebr/Broadcast/update";
            data = toEbrBroadcastInfo();
            data.subResType = vm.ebrInfo.dataType;
        } else if (vm.ebrInfo.dataType == SYS_SUB_RES_TYPE_ADAPTER) {
            //应急广播适配器
            url = baseURL + "safeRest/ebr/ebrAdapter/update";
            data = toEbrAdapterInfo();
            data.subResType = vm.ebrInfo.dataType;
        } else if (vm.ebrInfo.dataType == SYS_SUB_RES_TYPE_STATION) {
            //台站
            url = baseURL + "safeRest/ebr/ebrStation/update";
            data = toEbrStationInfo();
            data.subResType = vm.ebrInfo.dataType;
        }
    }

    $.ajax({
        type: ajaxType,
        async: false,
        url: url,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (r) {
            if (r.successful) {
                vm.closeLayer();
                vm.reload();
            }
        }
    });

}

function toEbrPlatformInfo() {
    var ebr = vm.ebrInfo;
    if (isEmpty(ebr)) {
        return null;
    }
    var data = {
        psEbrId: ebr.ebrId,
        psEbrName: ebr.ebrName,
        psUrl: ebr.ebrUrl,
        platLevel: ebr.ebrLevel,
        orgName: ebr.orgName,
        orgType: ebr.orgType,
        contact: ebr.contact,
        phoneNumber: ebr.phoneNumber,
        srcHost: ebr.srcHost,
        psType: ebr.ebrType,
        resType: ebr.resType,
        subResType: ebr.subResType,
        remark: ebr.remark,
        longitude: ebr.longitude,
        latitude: ebr.latitude,
        resChannel: ebr.resChannel
    };
    return data;
}

function toEbrBroadcastInfo() {
    var ebr = vm.ebrInfo;
    if (isEmpty(ebr)) {
        return null;
    }
    var data = {
        bsEbrId: ebr.ebrId,
        bsName: ebr.ebrName,
        bsUrl: ebr.ebrUrl,
        platLevel: ebr.ebrLevel,
        orgName: ebr.orgName,
        orgType: ebr.orgType,
        contact: ebr.contact,
        phoneNumber: ebr.phoneNumber,
        srcHost: ebr.srcHost,
        bsType: ebr.ebrType,
        resType: ebr.resType,
        subResType: ebr.subResType,
        remark: ebr.remark,
        longitude: ebr.longitude,
        latitude: ebr.latitude,
        resChannel: ebr.resChannel
    };
    return data;
}
function toEbrAdapterInfo() {
    var ebr = vm.ebrInfo;
    if (isEmpty(ebr)) {
        return null;
    }
    var data = {
        ebrAsId: ebr.ebrId,
        ebrAsName: ebr.ebrName,
        url: ebr.ebrUrl,
        platLevel: ebr.ebrLevel,
        orgName: ebr.orgName,
        orgType: ebr.orgType,
        contact: ebr.contact,
        phoneNumber: ebr.phoneNumber,
        srcHost: ebr.srcHost,
        asType: ebr.ebrType,
        resType: ebr.resType,
        subResType: ebr.subResType,
        remark: ebr.remark,
        longitude: ebr.longitude,
        latitude: ebr.latitude,
        resChannel: ebr.resChannel,
        ebrStId: ebr.ebrStId
    };
    return data;
}
function toEbrStationInfo() {
    var ebr = vm.ebrInfo;
    if (isEmpty(ebr)) {
        return null;
    }
    var data = {
        ebrStId: ebr.ebrId,
        ebrStName: ebr.ebrName,
        bsUrl: ebr.ebrUrl,
        platLevel: ebr.ebrLevel,
        orgName: ebr.orgName,
        orgType: ebr.orgType,
        contact: ebr.contact,
        phoneNumber: ebr.phoneNumber,
        srcHost: ebr.srcHost,
        stType: ebr.ebrType,
        resType: ebr.resType,
        subResType: ebr.subResType,
        remark: ebr.remark,
        longitude: ebr.longitude,
        latitude: ebr.latitude,
        resChannel: ebr.resChannel
    };
    return data;
}
function EbrBroadcastToEbrInfo(data) {
    vm.getResTypeList(data.bsType);
    vm.ebrInfo = {
        ebrId: data.bsEbrId,
        ebrName: data.bsName,
        ebrUrl: data.bsUrl,
        ebrLevel: data.platLevel,
        orgName: data.orgName,
        orgType: data.orgType,
        srcHost: data.srcHost,
        ebrType: data.bsType,
        resType: data.resType,
        subResType: data.subResType,
        remark: data.remark,
        longitude: data.longitude,
        latitude: data.latitude,
        dataType: data.subResType,
        resChannel: data.resChannel
    }
}
function EbrStationToEbrInfo(data) {
    vm.getResTypeList(data.stType);
    vm.ebrInfo = {
        ebrId: data.ebrStId,
        ebrName: data.ebrStName,
        ebrUrl: data.address,
        ebrLevel: data.platLevel,
        orgName: data.orgName,
        orgType: data.orgType,
        contact: data.contact,
        phoneNumber: data.phoneNumber,
        srcHost: data.srcHost,
        ebrType: data.stType,
        resType: data.resType,
        subResType: data.subResType,
        remark: data.remark,
        longitude: data.longitude,
        latitude: data.latitude,
        dataType: data.subResType,
        resChannel: data.resChannel
    }
}
function EbrAdapterToEbrInfo(data) {
    vm.getResTypeList(data.asType);
    vm.getThisEbrList('05');
    vm.isRelDisabled = false;
    vm.ebrInfo = {
        ebrId: data.ebrAsId,
        ebrName: data.ebrAsName,
        ebrUrl: data.url,
        ebrLevel: data.platLevel,
        orgName: data.orgName,
        orgType: data.orgType,
        contact: data.contact,
        phoneNumber: data.phoneNumber,
        srcHost: data.srcHost,
        ebrType: data.asType,
        resType: data.resType,
        subResType: data.subResType,
        remark: data.remark,
        longitude: data.longitude,
        latitude: data.latitude,
        dataType: data.subResType,
        resChannel: data.resChannel,
        ebrStId: data.ebrStId
    }
}
function EbrPlateformToEbrInfo(data) {
    vm.getResTypeList(data.psType);
    vm.ebrInfo = {
        ebrId: data.psEbrId,
        ebrName: data.psEbrName,
        ebrUrl: data.psUrl,
        ebrLevel: data.platLevel,
        orgName: data.orgName,
        orgType: data.orgType,
        contact: data.contact,
        phoneNumber: data.phoneNumber,
        srcHost: data.srcHost,
        ebrType: data.psType,
        resType: data.resType,
        subResType: data.subResType,
        remark: data.remark,
        longitude: data.longitude,
        latitude: data.latitude,
        dataType: data.subResType,
        resChannel: data.resChannel
    }
}