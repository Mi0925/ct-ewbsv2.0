$(function () {

    vm.getEnum(dictGroupCodes);
    vm.setFlowStyle();
    //vm.flowStepChange('1');
    initJqgrid();

    initTree("0");

});


var dictGroupCodes = "PROGRAM_STATE_DICT,PROGRAM_TYPE_DICT,PROGRAM_CONTENT_TYPE,YANLIAN_STRATEGY_TYPE,PROGRAM_LEVEL_DICT,AUDIT_STATUS_DICT,YANLIAN_TYPE_DICT,WEEK_MASK_DICT";
var  endTimeRender;
var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
            parentLibName:null,
            parentLibId:null,
            type:"",
            libName:null
        },
        pageEnum:[],
        monthArray:[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
       // programInfo:{},
        programInfo:{
            programName:null,
            programType:'',
            programLevel:'',
            languageCode:'zho',
            contentType:'',
            ebmEventType:'00000',
            ebmEventDesc:null,
            drillType:'',
            filesList:[],
            areaList:[],
            programContent:{
                content:null
            },
            programStrategy:{
                startTime:null,
                overTime:null,
                durationTime:null,
                playTime: null,
                weekMask:'',
                timeList:[]
            }
        },
        upload: {
            myDropzone: null,
            layerIndex: null
        },
        //节目类型，1-应急节目，2-日常
        programType:'',
        eventTypes:[],
        parentLib:{},
        eventDescBtnShow : false,
        commitBtnEnable : true,
        areaNames:"",
        weeks:[],
        flowStyles:[],
        addOperate:true,
        selectedFiles:[],
        uploadFileLibId:'',
        //上传文件默认审核通过
        uploadFileStatus:'1',
        selectedAreaCodeArr:[],
        timeStyle:'1',
        programId:''

    },
    mounted: function(){
        getPermission(this);
        this.setFlowWidth();
        this.programId = getQueryString("programId");
        //   this.getProgramInfo(this.programId);
        //this.getProgramInfo('411c078ac27547d0807f6f083f430b00');
      //  this.getProgramInfo('');
        this.getEventTypes("11000");
        this.getUploadLibId('program_make_upload_lib')

    },
    created:function(){
        this.initDropzone();
    },
    filters: {
        filterFun: function (value,len) {
            if(value&& value.length > len) {
                value= value.substring(0,len-1)+ '...';
            }
            return value;
        },
        filterDescFun:function (value,len) {
            if(value&& value.length > len) {
                value= value.substring(0,len-1)+ '...';
                this.eventDescBtnShow = true;
            }
            return value;
        },
        //获取枚举名称
        getEnumName:function (value,dictCode) {
            if(isEmpty(value)){
                return value
            }
            if(isEmpty(vm.pageEnum) || isEmpty(dictCode)){
                return value;
            }
            return matchAndGetDictValue(vm.pageEnum,value,dictCode);
        },
        getEventName:function (value) {
            if(isEmpty(value)){
                return value;
            }
            if(value == '00000'){
                return "所有事件";
            }
            var eventList = vm.eventTypes;
            var desc = value;
            if(!isEmpty(eventList)){
                for(var i = 0;i < eventList.length ; i++){
                    var event = eventList[i];
                    if(event.eventCode == value){
                        desc =  event.eventDesc;
                        break;
                    }
                }
            }
            return desc;
        }

    },

    methods:{
        weekChecked : function(value){
            var weekMsak = vm.programInfo.programStrategy.weekMask + '';
            var len = value.length;
            if(weekMsak.length < len){
                return false;
            }

            if(weekMsak.charAt(weekMsak.length - len) == 1){
                return true;
            }else{
                return false;
            }
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
        getUploadLibId:function(paramKey){
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/sys/param/getByParamKey",
                contentType: "application/json",
                data: {paramKey:paramKey},
                success: function (r) {
                    if (r.successful ) {
                        vm.uploadFileLibId = r.data.paramValue;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getEventTypes: function (parentCode) {
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/event/type/getNextByParentCode?parentCode="+parentCode,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.eventTypes=r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getProgramInfo:function (programId) {
            if(isEmpty(programId)){
                return ;
            }
            $.ajax({
                type: "get",
              //  async:false,
                url: baseURL+"safeRest/program/"+programId,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.programInfo = r.data;
                        vm.programType = r.data.programType;
                        if(isEmpty(vm.programInfo.areaList)){
                            vm.programInfo.areaList = [];
                        }
                        if(isEmpty(vm.programInfo.filesList)){
                            vm.programInfo.filesList = [];
                        }
                        if(isEmpty(vm.programInfo.programStrategy)){
                            vm.programInfo.programStrategy = {};
                        }
                        if(isEmpty(vm.programInfo.programContent)){
                            vm.programInfo.programContent = {};
                        }
                        if(r.data.areaList && r.data.areaList.length > 0){
                            vm.getAreaNames(r.data.areaList);
                        }
                        if(!isEmpty(vm.programInfo.programStrategy) && !isEmpty(vm.programInfo.programStrategy.weekMask)){
                            parseWeekMask(vm.programInfo.programStrategy.weekMask+'');
                        }
                        //更新区域树的选中状态
                        updateTreeNodeCheckState();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getAreaNames : function (areaList) {
            if(areaList==null || areaList.length == 0){
                return "";
            }
            var areaNames = "";
            for(var i=0;i < areaList.length;i++){
                var area = areaList[i];
                if(areaNames.length == 0){
                    areaNames += area.areaName;
                }else{
                    areaNames += ","+ area.areaName;
                }
            }
            vm.areaNames = areaNames;
        },
        //初始化流程导航的流程节点样式
        setFlowStyle : function () {
            vm.flowStyles = getFlowStyleArr();
        },
        //流程节点按钮点击事件
        flowStepChange:function (flowState) {
            vm.initLayDate();
            for(var i=0;i< vm.flowStyles.length;i++){
                var flowStyle = vm.flowStyles[i];
                if(flowStyle.flowState == flowState && flowStyle.flowClass == "flow-btn-unSelect"){
                    return;
                }
            }

            var flowStyles = vm.flowStyles;

            for(var i=0;i<flowStyles.length;i++){
                var flowStyle = flowStyles[i];
                if(flowStyle.flowState == flowState){
                    //当前点击的流程节点
                    if(flowStyle.flowClass != "flow-btn-unSelect"){
                        //点击的流程节点是流程类型中包含的节点
                        flowStyle.navArrClass = "";
                        flowStyle.flowClass = "flow-btn-select"
                    }
                }else{
                    flowStyle.flowClass = "flow-btn-current";
                    flowStyle.navArrClass = "nav-arr-hide";
                }
            }
            $(".flow-detail-content").hide();
            $("#flow-detail-"+flowState).show();
        },
        setFlowWidth:function(){
            var len=getFlowStyleArr().length;
            var unitOne=100/(3*len-1);
            this.percentThree=unitOne*3;
            this.percentTwo=unitOne*2;
        },

        reset:function () {
           // vm.getProgramInfo(vm.programId);
            vm.getProgramInfo("411c078ac27547d0807f6f083f430b00");
        },
        choseFiles:function(){
            initJqgrid();
            layer.open({
                type: 1,
                shade: false,
                title: false, //不显示标题
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: $('#fileChoseLayer'),
                area: ['1200px', '650px'], //宽高
            });
        },
        uploadFiles:function () {

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

        },
        initDropzone : function(){
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
                            onUploadSuccess(response.data);
                         //   vm.reload();
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
                              //  vm.reload();
                            }
                        }
                    });
                },
                previewTemplate: document.querySelector('#dropzoneTpl').innerHTML
            };
        },closeLayer:function(){
            vm.initProgramVueData();
            layer.closeAll();
        },
        initProgramVueData:function(){
            vm.selectedFiles = [];
        },
        reload : function(){
           // saveSelectedData();
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
        saveOrUpdate : function(programType){
            if(!vmDataCheck()){
                return;
            }
            vm.programInfo.programType = 1;
            vm.programInfo.programLevel = 10;
            vm.programInfo.ebmEventType = '00000';
            var url = '';
            var type = '';
            if(isEmpty(vm.programInfo.programId)){
                url = baseURL+"safeRest/program/save";
                type = 'post'
            }else{
                url = baseURL+"safeRest/program/update";
                type = 'put'
            }
            console.log(vm.programInfo);
            $.ajax({
                type: type,
                url: url,
                contentType: "application/json",
                data:JSON.stringify(vm.programInfo),
                success: function (r) {
                    if (r.successful ) {
                        window.location.href = 'drillList.html';
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        saveAndCommit:function(programType){
            if(vm.commitBtnEnable){
                vm.commitBtnEnable = false;

                if(!vmDataCheck()){
                    return;
                }
            	vm.programInfo.programType = 3;
                vm.programInfo.programLevel = 10;
                vm.programInfo.ebmEventType = '00000';
                var url = '';
                var type = '';
                if(isEmpty(vm.programInfo.programId)){
                    url = baseURL+"safeRest/program/save";
                    type = 'post'
                }else{
                    url = baseURL+"safeRest/program/update";
                    type = 'put'
                }
                vm.programInfo.state = '2';
                console.log(vm.programInfo);
                $.ajax({
                    type: type,
                    url: url,
                    contentType: "application/json",
                    data:JSON.stringify(vm.programInfo),
                    success: function (r) {
                        if (r.successful ) {
                            window.location.href = 'drillList.html';
                        } else {
                            layer.msg(r.msg);
                        }
                        vm.commitBtnEnable = true;
                    },
                    error:function(){
                        vm.commitBtnEnable = true;
                        layer.msg("节目创建失败，系统错误！");
                    }
                });
            }else{
                layer.msg("节目创建中，请勿重复点击。请稍候。。。");
            }
        },
        initLayDate: function () {
            var startTimeRender= laydate.render({
                elem: '#startTime'
                ,type: 'time'
                ,theme: 'molv'
                ,trigger: 'click'

                , change: function (value, date) {
                }
                , done: function (value, date) {
                   /* if (value == "" || value == null) {
                        Vue.set(vm.programInfo.programStrategy, 'startSendTime', '');
                    } else {
                        Vue.set(vm.req, 'startSendTime', value);
                    }*/

                }
            });
            var startTimeRender= laydate.render({
                elem: '#durationStartTime'
                ,type: 'time'
                ,theme: 'molv'
                ,trigger: 'click'

                , change: function (value, date) {
                }
                , done: function (value, date) {
                    /*  if (value == "" || value == null) {
                          Vue.set(vm.programInfo.programStrategy, 'startSendTime', '');
                      } else {
                          Vue.set(vm.req, 'startSendTime', value);
                      }*/

                }
            });

              endTimeRender= laydate.render({
                elem: '#overTime'
                , type: 'time'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    /*if (value == "" || value == null) {
                        Vue.set(vm.req, 'endTime', '');
                    } else {
                        Vue.set(vm.req, 'endTime', value);
                    }*/
                }
            });
            var  endTimeRender= laydate.render({
                elem: '#vStartTime'
                , type: 'date'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    if (value == "" || value == null) {
                        Vue.set(vm.programInfo.programStrategy, 'startTime', '');
                    } else {
                        Vue.set(vm.programInfo.programStrategy, 'startTime', value);
                    }
                }
            });
            var  endTimeRender= laydate.render({
                elem: '#vOverTime'
                , type: 'date'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    if (value == "" || value == null) {
                        Vue.set(vm.programInfo.programStrategy, 'overTime', '');
                    } else {
                        Vue.set(vm.programInfo.programStrategy, 'overTime', value);
                    }
                }
            });
            var  endTimeRender= laydate.render({
                elem: '#playTime'
                , type: 'date'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    if (value == "" || value == null) {
                        Vue.set(vm.programInfo.programStrategy, 'playTime', '');
                    } else {
                        Vue.set(vm.programInfo.programStrategy, 'playTime', value);
                    }
                }
            });
        }
    }
});

function showSchemeDetail(ebmId) {
    var page = $('#jqGrid').getGridParam('page');
    window.location.href = 'schemeDetail.html?ebmId='+ebmId+"&page="+page;
}

function toBrdRecord(ebmId) {
    window.location.href = '../brdRecord/brdRecord.html?ebmId='+ebmId;
}

function toNewsTopic(ebmId) {
    window.location.href = 'newsTopic.html?ebmId='+ebmId;
}

function eventDescBtnClick(obj) {
    $(obj).parent().find(".openDesc").show();
    $(obj).parent().find(".closeDesc").hide();
  //  $("#openDesc").show();
  //  $("#closeDesc").hide();
    if($(obj).html() =='展开全部'){
        $(obj).html("收起详情");
        $(obj).parent().find(".openDesc").show();
        $(obj).parent().find(".closeDesc").hide();
    }else{
        $(obj).html("展开全部");
        $(obj).parent().find(".openDesc").hide();
        $(obj).parent().find(".closeDesc").show();
    }
}

/**
 * 返回所有节目制作流程相关信息
 * @returns {Array}
 */
function getFlowStyleArr(){
    var flowStyleArr = [];
    var arr1 = {
        id:"1",
        flowState:"1",
        flowStateName:"基本信息",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : "flow-btn-select"
    };
    flowStyleArr.push(arr1);
    var arr2 = {
        id:"2",
        flowState:"2",
        flowStateName:"覆盖区域",
        navArrClass: "nav-arr-hide",
        flowArrowStyle: "",
        flowClass : "flow-btn-current"
    };
    flowStyleArr.push(arr2);
    var arr3 = {
        id:"3",
        flowState:"3",
        flowStateName:"演练内容",
        navArrClass: "nav-arr-hide",
        flowArrowStyle: "",
        flowClass : "flow-btn-current"
    };
    flowStyleArr.push(arr3);
    var arr4 = {
        id:"4",
        flowState:"4",
        flowStateName:"演练策略",
        navArrClass: "nav-arr-hide",
        flowArrowStyle: "",
        flowClass : "flow-btn-current"
    };
    flowStyleArr.push(arr4);

    return flowStyleArr;
}

function back(){
    window.location.href = document.referrer;
}


function next(flowState) {
    vm.flowStepChange(flowState);
}

function removeTime(obj) {
    var timeId = $(obj).parent().attr("timeId");
    var timeList = vm.programInfo.programStrategy.timeList;
    var times = [];
    if(!isEmpty(timeList)){
        for(var i=0;i<timeList.length;i++){
            if(timeId != timeList[i].timeId){
                times.push(timeList[i]);
            }
        }
    }
    vm.programInfo.programStrategy.timeList = times;
    if(vm.programInfo.programStrategy.strategyType == 1 && isEmpty(times)){
        $("#program-time-add-btn").attr("disabled",false);
        $("#program-time-add-btn").removeClass("program-add-disabled");
        $("#program-time-add-btn").addClass("program-time-add-btn");
    }
//    $(obj).parent().remove();
}
function removeFiles(obj) {
    var fileId = $(obj).parent().attr("fileId");
    var fileList = vm.programInfo.filesList;
    var files = [];
    if(!isEmpty(fileList)){
        for(var i=0;i<fileList.length;i++){
            if(fileId != fileList[i].fileId){
                files.push(fileList[i]);
            }
        }
    }
    vm.programInfo.filesList = files;
}
function addProgramTime(obj) {
    var startTime= null;
    var overTime = null;
    var durationTime = null;
    if(vm.timeStyle == 1){
         startTime = $("#startTime").val();
        overTime = $("#overTime").val();
    }else{
         startTime = $("#startTime").val();
        durationTime = $("#durationTime").val();
    }
    if(isEmpty(startTime) || (isEmpty(overTime) && isEmpty(durationTime))){
        return;
    }
    if(vm.programInfo && vm.programInfo.programStrategy){
        var programTimeList = vm.programInfo.programStrategy.timeList;
        if(isEmpty(programTimeList)){
            programTimeList = [];
        }
        var timestamp = (new Date()).getTime();
        var timeData = {
            timeId: timestamp,
            startTime:startTime,
            overTime:overTime,
            durationTime:durationTime,
            strategyId:vm.programInfo.programStrategy.strategyId,
            handleFlag:1,
            dayOfMonth:vm.programInfo.dayOfMonth
        }
        programTimeList.push(timeData);
        vm.programInfo.programStrategy.timeList = programTimeList;
    }
    //应急节目制作，默认策略类型为一次性任务
    if(isEmpty(vm.programInfo.programStrategy.strategyType)){
        vm.programInfo.programStrategy.strategyType = 1;
    }
    if(vm.programInfo.programStrategy.strategyType == 1 && vm.programInfo.programStrategy.timeList.length > 0 ){
        $(obj).attr("disabled",true);
        $(obj).removeClass("program-time-add-btn");
        $(obj).addClass("program-add-disabled");
    }

}
/*function addProgramTime(type) {
    var startTime= null;
    var overTime = null;
    var durationTime = null;
    if(type == 1){
        startTime = $("#startTime").val();
        overTime = $("#overTime").val();
    }else{
        startTime = $("#durationStartTime").val();
        durationTime = $("#durationTime").val();
    }
    if(isEmpty(startTime) || (isEmpty(overTime) && isEmpty(durationTime))){
        return;
    }
    if(vm.programInfo && vm.programInfo.programStrategy){
        var programTimeList = vm.programInfo.programStrategy.timeList;
        if(isEmpty(programTimeList)){
            programTimeList = [];
        }
        var timestamp = (new Date()).getTime();
        var timeData = {
            timeId: timestamp,
            startTime:startTime,
            overTime:overTime,
            durationTime:durationTime,
            strategyId:vm.programInfo.programStrategy.strategyId,
            handleFlag:1
        }
        programTimeList.push(timeData);
        vm.programInfo.programStrategy.timeList = programTimeList;
    }



}*/

function toFile(libName,id,type,url){
    vm.parentLib.libName = libName;
    vm.parentLib.libId = id;
    vm.parentLib.libType = type;
    vm.parentLib.url = url;
    vm.req.parentLibId = id;
    vm.reload();
    appendNav(libName,id,type);
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

function initJqgrid() {
    $.jgrid.defaults.height = 300;
    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/fileLib/page?auditState=1',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', index: "id", width: 45, key: true, hidden: true},
            {label: 'type', name: 'type',  width: 45,  hidden: true},
            { label: '名称', name: 'libName',  width: 150,align:"center",formatter:function(cellvalue,options,row){
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
            {label: 'lib_Name', name: 'libName',  width: 100,  hidden: true },
             { label: '所属文件夹', name: 'parentLibName', width: 150 ,align:"center"},
            { label: '大小', name: 'byteSize', width: 60 ,align:"center", formatter:function (cellvalue,options,row) {
                    if(row.type == 9){
                        return fileChange(cellvalue);
                    }else{
                        return '-';
                    }
                }},
            { label: '持续时长', name: 'secondLength', width: 60 ,align:"center"},
            { label: '审核状态', name: 'auditState', width: 60 ,align:"center",
                formatter:function (cellvalue,options,row) {
                   return matchAndGetDictValue(vm.pageEnum,cellvalue,'AUDIT_STATUS_DICT');
                }},


        ],
        viewrecords: true,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers:true,
        rownumWidth: 50,
        multiselect: true,
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
        onSelectRow:function (rowid, status) {
            var rowData = $("#jqGrid").jqGrid('getRowData',rowid);
            if(rowData.type != 9){
                return;
            }
            var fileData = {fileId:rowData.id,fileName:rowData.libName,secondLength:rowData.secondLength}
            if(!status){
                //取消选中
                if(!isEmpty(vm.selectedFiles)){
                    for(var i=0;i<vm.selectedFiles.length;i++){
                        var file =vm.selectedFiles[i];
                        if(file.fileId == rowData.id){
                            vm.selectedFiles.remove(i);
                        }
                    }
                }
            }else{
                if(isEmpty(vm.selectedFiles)){
                    vm.selectedFiles.push(fileData);
                }else{
                    var exists = false;
                    for(var j=0;j < vm.selectedFiles;j++){
                        if(fileData.fileId == vm.selectedFiles[j].fileId){
                            exists = true;
                            break;
                        }
                    }
                    if(!exists){
                        vm.selectedFiles.push(fileData);
                    }
                }
            }
        },
        gridComplete:function(){
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $("#jqGrid").css({"width": $("#gbox_jqGrid").width()-1});
            $("#gview_jqGrid").find(".ui-jqgrid-htable").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-hdiv").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-bdiv").css({"width": $("#gbox_jqGrid").width()});
        }
    });
}

function saveChoseFiles() {
    var programFileList = vm.programInfo.filesList;

    var files = vm.selectedFiles;
    if(!isEmpty(files)){
      //  var html = '';
        for(var i=0;i<files.length;i++){
            var file = files[i];
            var add = true;
            if(!isEmpty(programFileList)){
                for(var j=0;j<programFileList.length;j++){
                    if(file.fileId == programFileList[j].fileId){
                        add = false;
                        break;
                    }
                }
            }
            if(add){
       /*         html +='<tr class="layer-reso-name" fileId="'+file.fileId+'" fileName="'+file.fileName+'"><td>';
                html += file.fileName + '</td>';
                html += '<td style="color: red;cursor: pointer;" onclick="removeTime(this)">' +
                    '<span class="glyphicon glyphicon-remove"   aria-hidden="true"></span>移除</td></tr>';*/
                var fileData = {
                    fileId:file.fileId,
                    fileName:file.fileName,
                    secondLength:file.secondLength,
                    programId:vm.programId
                };
                vm.programInfo.filesList.push(fileData)
            }
        }
       // $("#program-file-tb").find("tbody").append(html)
    }
    vm.closeLayer();
}



var areaTreeUrl = baseURL+"safeRest/area/getByParentAreaCode";
function getAreaTreeNodesUrl(treeId, treeNode) {
    var param = "parentAreaCode=" + treeNode.areaCode;
    return areaTreeUrl+"?" + param;
}
function filter(treeId, parentNode, childNodes) {
    return initTreeCheckedStatus(childNodes.data);
}
function initTreeCheckedStatus(childNodes){
    var nodeDatas = childNodes;
        var areaList = vm.programInfo.areaList;
        for(var i=0;i<nodeDatas.length;i++ ){
            var node = nodeDatas[i];
            node.checked = false;
            for(var j=0 ;j<areaList.length;j++){
                var area = areaList[j];
                if(area.areaCode == node.areaCode){
                    node.checked = true;
                    break;
                }
            }
        }
    return nodeDatas;
}
var setting = {
    async : {
        enable : true,//设置 zTree 是否开启异步加载模式
        url:getAreaTreeNodesUrl,
        type:"get",
        dataFilter: filter
    },
    data : {
        simpleData : {
            enable : true,//是否采用简单数据模式
            idKey : "areaCode",//树节点ID名称
            pIdKey : "parentAreaCode",//父节点ID名称
            rootPId : null//根节点ID
        },
        key : {
            name: "areaName",
            isParent:'parent',
            title : "areaName",
            checked: "checked"
        }
    },
    check: {
        enable: true,
        nocheckInherit: true,
        chkDisabledInherit: true,
        chkboxType: { "Y": "", "N": "s" },
        chkStyle: "checkbox",  //多选框
        // radioType: "all"   //对所有节点设置单选
    },
    callback : {
        onCheck:treeNodeChecked,
    },
    view:{
        fontCss : {'color':'#00ffff','font-size':"16px"}
    }
};

var ztree;
function initTree(parentAreaCode) {
    $.ajax({
        url : areaTreeUrl,
        type : "get",
        contentType: "application/json",
        data:{parentAreaCode:parentAreaCode},
        success : function(r) {
            var nodeDatas = initTreeCheckedStatus(r.data);

            ztree= $.fn.zTree.init($("#areaTree"), setting, nodeDatas);//初始化树节点时，添加同步获取的数据
            var nodeList = ztree.getNodes();
            if(nodeList.length>0){
                ztree.expandNode(nodeList[0], true);
                // ztree.setting.callback.onClick(null, ztree.setting.treeId, nodeList[0]);
            }
        }
    });

}
function updateTreeNodeCheckState() {
    if(!isEmpty(vm.programInfo)){
        var nd = ztree.getNodes();
        var nodes = ztree.transformToArray(nd);
        if(!isEmpty(nodes)){
            for(var i=0;i<nodes.length;i++){
                var node = nodes[i];
                node.checked = false;
                for(var j=0 ;j<vm.programInfo.areaList.length;j++){
                    var area = vm.programInfo.areaList[j];
                    if(area.areaCode == node.areaCode){
                        node.checked = true;
                        ztree.updateNode(node,false)
                        break;
                    }
                }
            }
        }
    }
}
function treeNodeChecked(event, treeId, treeNode) {

    var nodes = ztree.getCheckedNodes();
    var areaDataList = [];
    var names = '';
    var areaList = [];
    for(var i=0;i<nodes.length;i++){
        var node = nodes[i];
        var needAdd = true;
        if(vm.programInfo && !isEmpty(vm.programInfo.areaList)){
            var pAreaList = vm.programInfo.areaList;
            for(var j=0;j < pAreaList.length;j++){
                var p_area = pAreaList[j];
                if(node.areaCode.indexOf(p_area.areaCode) == 0 && node.areaCode != p_area.areaCode){
                    needAdd = false;
                    break;
                }
            }
        }

        if(needAdd){
            var areaData = {
                areaName:node.areaName,
                areaCode:node.areaCode,
                programId:vm.programId,
            };
            areaList.push(areaData);
        }

    }
    //删除该节点子节点，以及更下一级节点

    for(var i =0 ;i<areaList.length;i++){
        var area = areaList[i];
        if(area.areaCode.indexOf(treeNode.areaCode) < 0 || area.areaCode == treeNode.areaCode ){
            areaDataList.push(area);
        }
    }

   for(var i=0;i< areaDataList.length;i++){
        if(isEmpty(names)){
            names += areaDataList[i].areaName;
        }else{
            names += "," + areaDataList[i].areaName;
        }
   }
    vm.programInfo.areaList = areaDataList;
    vm.areaNames = names;
}

function saveProgramFile() {
   var contentType = vm.programInfo.contentType;
   if(contentType == 2){
       //上传文件

   }else if(contentType == 3){
        //选择曲目
       var fileList = [];
       var obj = $("#program-file-tb").find("tr");
       if(!isEmpty(obj)){
           $("#program-file-tb").find("tr").each(function () {
                var fileId = $(this).attr("fileId");
                var fileName = $(this).attr("fileName");
                var fileData = {
                    fileId:fileId,
                    fileName:fileName,
                    programId:vm.programId
                };
               fileList.push(fileData);
           })
       }
       vm.programInfo.filesList = fileList;
        next(4);
   }
}

function onUploadSuccess(fileData) {
    if(isEmpty(vm.programInfo.filesList)){
        vm.programInfo.filesList = [];
    }
    var file = {
        fileId:fileData.id,
        fileName:fileData.originName,
        secondLength:fileData.secondLength,
        programId:vm.programId
    };
    vm.programInfo.filesList.push(file);


}

/**
 * 周循环策略，星期复选框点击事件
 */
function weekCheckBoxClicked(obj) {
    var weekVal =  vm.programInfo.programStrategy.weekMask;
    if(isEmpty(weekVal)){
        weekVal = 0;
    }
    if(obj.checked){
        weekVal = parseInt(weekVal) + parseInt($(obj).val());
    }else{
        weekVal = parseInt(weekVal) - parseInt($(obj).val());
    }
    vm.programInfo.programStrategy.weekMask = weekVal;
    parseWeekMask(weekVal+'');
  //  vm.weeks = p_weeks;
}

function parseWeekMask(weekVal) {
    var p_weeks = [];
    var len = weekVal.length;
    if(len < 7){
        for(var i=0;i< 7-len;i++){
            weekVal = "0"+weekVal;
        }
    }
    if(weekVal.charAt(6) == 1){
        p_weeks.push("星期一");
    }
    if(weekVal.charAt(5) == 1){
        p_weeks.push("星期二");
    }
    if(weekVal.charAt(4) == 1){
        p_weeks.push("星期三");
    }
    if(weekVal.charAt(3) == 1){
        p_weeks.push("星期四");
    }
    if(weekVal.charAt(2) == 1){
        p_weeks.push("星期五");
    }
    if(weekVal.charAt(1) == 1){
        p_weeks.push("星期六");
    }
    if(weekVal.charAt(0) == 1){
        p_weeks.push("星期日");
    }
    vm.weeks = p_weeks;
}

function contentTypeChange(obj) {
    var contentType = $(obj).val();
    if(contentType==4){
    	contentType =1;
    }
    $(".program-content").hide();
    $("#program-content-"+contentType).show();
}
function drillTypeChange(obj){
	var drillType = $(obj).val();
	vm.programInfo.drillType = drillType;
}
function programStrategyTypeChose(obj) {
    var type = $(obj).val();
    if(type == 4){
        $(".programStrategy").hide();
        $("#programStrategy-2").show();
        $("#programStrategy-"+type).show();
    }else if(type != 3){
        $(".programStrategy").hide();
        $("#programStrategy-"+type).show();
    }else {
        $(".programStrategy").hide();
        $("#programStrategy-2").show();
        $("#programStrategy-3").show();
    }

    vm.programInfo.programStrategy.strategyType = type;
}

function vmDataCheck() {
    var program = vm.programInfo;
    if(isEmpty(program)){
        return;
    }

    return true;
}

function durationTimeBlur() {
    $(".layui-laydate").hide();
}
function durationTimeFocus(){
	console.log();
}