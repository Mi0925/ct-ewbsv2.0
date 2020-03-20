$(function () {

    vm.getEnum(dictGroupCodes);


    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/fileLib/page',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', index: "id", width: 45, key: true, hidden: true},
            {label: 'type', name: 'type',  width: 45,  hidden: true},
            { label: '名称', name: 'libName', index: "libName", width: 150,align:"center",formatter:function(cellvalue,options,row){
                    if(row.type == 9){
                        var str="<div style='cursor: pointer;'><span >";
                        if(row.fileExt=='mp3'){
                            str+= "<i class='fa fa-file-audio-o'></i>";
                        }else  if(row.fileExt=='txt'){
                            str+= "<i class='fa fa-file-text-o'></i>";
                        }else{
                            str+= "<i class='fa fa-file-o'></i>";
                        }
                        str+="</span> &nbsp;<span>"+cellvalue+"</span></div>";
                        return str;
                    }else{
                        return "<div style='cursor: pointer;' onclick='toFile(\""+row.libName+"\",\""+row.id+"\",\""+row.type+"\",\""+row.url+"\")'><span style='color: lightyellow;'><i class='fa fa-folder'></i></span> &nbsp;<span style='text-decoration:underline;'>"+cellvalue+"</span></div>";
                    }
                }
            },
            // { label: '上级文件夹名称', name: 'parentLibName', width: 150 ,align:"center"},
            { label: '大小', name: 'byteSize', width: 60 ,align:"center", formatter:function (cellvalue,options,row) {
                    if(row.type == 9){
                        return fileChange(cellvalue);
                    }else{
                        return '-';
                    }
                }},
            { label: '文件（夹）路径', name: 'url', width: 150 ,align:"center"},
            // { label: '文件夹类型', name: 'typeName', width: 80,align:"center"},
            { label: '创建时间', name: 'createDate', width: 100 ,align:"center"},
            { label: '创建人', name: 'createUser', width: 80 ,align:"center"},
            { label: '操作', name: '', width: 120 ,align:"center" ,
                formatter:function (cellvalue,options,row) {
                    var operateHtml="";
                    operateHtml +=  '&nbsp;&nbsp;&nbsp;<a style=\'text-decoration:underline;cursor:pointer;color: #00fbff\' onclick="showFileOrLibDetail(\'' + row.id + '\',\''+row.type+'\')">详情</a>';
                    if((row.type== 9 && vm.permissions["res:file:edit"])
                                || (row.type != 9 && vm.permissions["res:filedir:edit"])){
                        operateHtml +=  '&nbsp;&nbsp;&nbsp;<a style=\'text-decoration:underline;cursor:pointer;color: #00fbff\' onclick="updateFileOrLib(\'' + row.id + '\',\''+row.type+'\')">修改</a>'
                    }
                    if((row.type== 9 && vm.permissions["res:file:delete"])
                                    || (row.type != 9 && vm.permissions["res:filedir:delete"])){
                        if(row.type !=1 ){
                            operateHtml +=  '&nbsp;&nbsp;&nbsp;<a style=\'text-decoration:underline;cursor:pointer;color: #00fbff\' onclick="deleteFileOrLib(\'' + row.id + '\',\''+row.type+'\')">删除</a>'
                        }
                    }
                    return   operateHtml;
                }},

        ],
        viewrecords: true,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers:true,
        rownumWidth: 50,
        gridview: true,
        autowidth:true,
        pager: "#jqGridPager",
        jsonReader : {    //jsonReader来跟服务器端返回的数据做对应
            root: "data",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        loadComplete:function(){

        },
        gridComplete:function(){
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $("#jqGrid").css({"width": $("#gbox_jqGrid").width()-1});
            $("#gview_jqGrid").find(".ui-jqgrid-htable").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-hdiv").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-bdiv").css({"width": $("#gbox_jqGrid").width()});
        }
    });

});


var dictGroupCodes = "FILE_LIB_TYPE";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
            parentLibName:null,
            parentLibId:null,
            type:"",
            libName:null
        },
        permissions: {
            "res:file:download":false,
            "res:file:upload":false,
            "res:file:delete":false,
            "res:file:edit":false,
            "res:file:audit":false,
            "res:file:list":false,
            "res:file:detail":false,
            "res:filedir:create":false,
            "res:filedir:edit":false,
            "res:filedir:delete":false
        },
        pageEnum:[],
        parentLib:{},
        fileInfo:{},
        libInfo:{},
        title:"",
        libAddOrEditShow:false,
        onlySee:false,
        selectedRowData:[],
        upload: {
            myDropzone: null,
            layerIndex: null
        },
        ebrInfo:null
    },
    mounted: function(){
        getPermission(this);
        this.getParentLibInfo(0);
    },
    created:function(){
        this.initData();
    },
    filters: {
        //获取枚举名称
        getEnumName:function (value,dictCode) {
            if(isEmpty(vm.pageEnum) || isEmpty(dictCode)){
                return value;
            }
            return matchAndGetDictValue(vm.pageEnum,value,dictCode);
        },
        //字符串过长，截取掉。
        filterFun: function (value,len) {
            if(value&& value.length > len) {
                value= value.substring(0,len)+ '...';
            }
            return value;
        },
        filterFileSize :function (value) {
            return fileChange(value);
        },
        getPlatformName:function (value) {
            if(isEmpty(value)){
                return '无';
            }
            var name;
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/ebrPlatform/"+value,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        name = r.data.psEbrName;
                    } else {
                        name = value;
                    }
                }
            });
            return name;
        },

    },
    methods:{
        reload : function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "type":vm.req.type
                    ,"parentLibName":vm.req.parentLibName
                    ,"parentLibId":vm.req.parentLibId
                    ,"libName":vm.req.libName
                },
                page:page
            }).trigger("reloadGrid");
        },
        initData : function(){
            $("#my-awesome-dropzone").attr("action", baseURL + "safeRest/file/upload");
            Dropzone.options.myAwesomeDropzone = {
                url: baseURL + "safeRest/file/upload",
                autoProcessQueue: false,
                uploadMultiple: false,
                parallelUploads: 100,
                addRemoveLinks: true,
                dictRemoveFile: "删除",
                maxFiles: 10, // 一次最多上传文件
                maxFilesize: 50, // 单个文件最大内存
                // acceptedFiles: "audio/mpeg,audio/x-aac", // 只能上传以MP3和aac结尾的文件
                paramName: "multipartFile",
                headers: {
                    "token": token
                },
                accept: function (file, done) {
                    var reg = /(.aac)$|(.mp3)|(.txt)|(.flac)$/;
                    reg.test(file.name) ? done() : done("请上传以.acc或以.mp3或以.txt或以.flac结尾的文件！");
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
                            console.log("==========="+ response.data);
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
        reset:function(){
            vm.fileInfo={};
            vm.libInfo ={};
            vm.title="";
            vm.libAddOrEditShow=false;
            vm.onlySee=false;
            vm.selectedRowData=[];
        },
        getEnum: function (dictGroupCode) {
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/dict/getByGroupCode",
                contentType: "application/json",
                data:{dictGroupCode:dictGroupCode},
                success: function (r) {
                    if (r.successful ) {
                        vm.pageEnum = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getParentLibInfo : function(libId){
            if(libId != 0 && isEmpty(libId)){
                return;
            }
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/fileLibrary/"+libId,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.parentLib = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        updateFile :function(){
            if(isEmpty(vm.fileInfo.originName)){
                alert("请输入文件名称");
                return;
            }
            var data={originName : vm.fileInfo.originName,id:vm.fileInfo.id}
            $.ajax({
                type: "put",
                async:false,
                url: baseURL+"safeRest/file/update",
                contentType: "application/json",
                data:JSON.stringify(data),
                success: function (r) {
                    if (r.successful ) {
                        vm.closeLayer();
                        vm.reload();
                        $(".left-sidebar").click();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        downFile:function(fileInfo){
            // 模拟表单提交同步方式下载文件,能够弹出保存文件对话框
            var url = baseURL+"safeRest/file/downloadByUrl";
            var originName = fileInfo.originName;
            var filePath = fileInfo.filePath;
            jQuery('<form action="'+url+'" method="post">' +
                '<input type="text" name="filePath" value="'+filePath+'"/>' +
                '<input type="text" name="originName" value="'+originName+'"/>' +
                '<input type="text" name="token" value="'+token+'"/>' +
                '</form>')
                .appendTo('body').submit().remove();
        },
        saveOrUpdateLib:function () {
            if(isEmpty(vm.libInfo.libName)){
                alert("请输入文件夹名称！");
                return;
            }

            var url = "";
            var type = "";
            if(isEmpty(vm.libInfo.libId)){
                vm.libInfo.parentLibId = vm.parentLib.libId;
                if(vm.parentLib.url==undefined){
                    vm.libInfo.libURI = vm.parentLib.libURI  + vm.libInfo.libName + "/";
                }else{
                    vm.libInfo.libURI = vm.parentLib.url  + vm.libInfo.libName + "/";
                }
                url = baseURL+"safeRest/fileLibrary/save";
                type = "post";
            }else{
                url = baseURL+"safeRest/fileLibrary/update";
                type = "put";
            }
            
            $.ajax({
                type: type,
                async:false,
                url: url,
                contentType: "application/json",
                data:JSON.stringify(vm.libInfo),
                success: function (r) {
                    if (r.successful ) {
                        vm.closeLayer();
                        vm.reload();                        
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        closeLayer:function(){
            vm.reset();
            layer.closeAll();
        },
        uploadFiles:function () {

            if (isEmpty(vm.parentLib.libId)) {
                window.alert("请进入文件夹后再上传文件！")
                return false;
            }

            vm.upload.layerIndex = layer.open({
                type: 1,
                skin: 'layui-bg-blue',
                title: vm.title,
                area: ['600px', '400px'],
                shadeClose: false,
                content: jQuery("#uploadFileDiv"),
                btn: ['上传'],
                yes: function () {
                    vm.upload.myDropzone.processQueue();
                },
                end: function () {
                    vm.title = null;
                    vm.upload.myDropzone.removeAllFiles(true);
                    //$("#rename_form").validate().resetForm();
                }
            });

        }

    }
});

function getFileDetail(fileId) {
    if(isEmpty(fileId)){
        return;
    }
    $.ajax({
        type: "get",
        async:false,
        url: baseURL+"safeRest/file/"+fileId,
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                vm.fileInfo = r.data;
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"440px"},300);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}
function getLibDetail(libId) {
    if(isEmpty(libId)){
        return;
    }
    $.ajax({
        type: "get",
        async:false,
        url: baseURL+"safeRest/fileLibrary/"+libId,
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                vm.libInfo = r.data;
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function appendNav(libName,libId,type) {
    var html = '<span class="parent_lib_name" onclick="parentLibNavClick(\''+libId+'\',this)">&nbsp;>>&nbsp;'+libName+'</span>';
    $(".file_Lib_Nav").append(html);
}
function parentLibNavClick(libId,obj) {
    vm.getParentLibInfo(libId);
    vm.req.parentLibId = libId;
    vm.reload();
    $(obj).nextAll().remove();
}

function showLibAddLayer() {
    vm.libAddOrEditShow = true;
    vm.title = "创建文件夹";
    if(isEmpty(vm.parentLib.libId)){
        vm.parentLib.libId = 0;
        vm.parentLib.libName = "根目录";
    }
    vm.libInfo={parentLibName:vm.parentLib.libName,parentLibId : vm.parentLib.libId};
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#libLayer'),
        area: ['500px', '350px'], //宽高
    });
}
function showLibEditLayer() {
    vm.libAddOrEditShow = true;
    vm.title = "修改文件夹";
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#libLayer'),
        area: ['500px', '350px'], //宽高
    });
}
function showFileEditLayer() {
    vm.title = "修改文件名称";
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#fileLayer'),
        area: ['500px', '350px'], //宽高
    });
}
function showLibSeeLayer() {
    vm.libAddOrEditShow = false;
    vm.onlySee = true;
    vm.title = "查看文件夹详情";
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#libLayer'),
        area: ['550px', '600px'], //宽高
    });
}

function showFileOrLibDetail(id,type) {
    if(type == 9){
        getFileDetail(id);
    }else{
        getLibDetail(id);
        showLibSeeLayer();
    }

}

function updateFileOrLib(id,type) {
    if(type == 9){
        getFileDetail(id);
        showFileEditLayer();
    }else{
        getLibDetail(id);
        showLibEditLayer();
    }
}

function deleteFileOrLib(id ,type) {
    if(isEmpty(id) || isEmpty(type)){
        return;
    }

    if(type == 9 ){
        confirm("确认要删除该文件吗？",function () {
            $.ajax({
                type: "delete",
                async:false,
                url: baseURL+"safeRest/file/delete/"+id,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.reload();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        })
    }else{
        confirm("删除文件夹，该文件夹下的所有文件及文件夹将移动到系统默认文件夹下。确认要删除吗？",function () {
            $.ajax({
                type: "delete",
                async:false,
                url: baseURL+"safeRest/fileLibrary/deleteThis/"+id,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                       vm.reload();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        })

    }
}

function toFile(libName,id,type,url){
    vm.parentLib.libName = libName;
    vm.parentLib.libId = id;
    vm.parentLib.libType = type;
    vm.parentLib.url = url;
    vm.req.parentLibId = id;
    vm.reload();
    appendNav(libName,id,type);
}
