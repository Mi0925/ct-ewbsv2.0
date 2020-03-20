
$(function () {
    vm.getEnum(dictGroupCodes);
});

var dictGroupCodes = "EBR_TYPE,SEVERITY_DICT,MSG_LANG_DICT,EVENT_TYPE_DICT,MSG_TYPE_DICT,AUXILIARY_TYPE_DICT,plan_math_flow,AUDIT_STATUS_DICT";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
        },
        permissions: {
            "sys:user:add": false
        },
        pageEnum:[],
        schemeId : null,
        areaNames: "",
        schemeInfo:{},
        ebmInfo:{},
        areaNamesBtnShow :false,
        msgDescBtnShow:false,
        flowStyles:[],
        resoList :[],
        planMatched: false,
        percentThree:0,
        percentTwo:0,
        ebmDispatchFile:[],
        infoNoAudit:false,
        infoAuditState:"",
    },
    mounted: function(){
        getPermission(this);
        this.setFlowWidth();
    },
    created:function(){
        this.ebmId = getQueryString("ebmId");
        this.getEbmInfoAll(this.ebmId);
        this.getEbmDispatchFile(this.ebmId); //初始化消息生成数据。
    },
    filters: {
        //字符串过长，截取掉。
        filterFun: function (value) {
            if(value&& value.length > 20) {
                value= value.substring(0,17)+ '...';
            }
            return value;
        },
        //获取枚举名称
        getEnumName:function (value,dictCode) {
            if(isEmpty(vm.pageEnum) || isEmpty(dictCode)){
                return value;
            }
            return matchAndGetDictValue(vm.pageEnum,value,dictCode);
        },
        filterAreaNamesFun: function (value) {
            if(value&& value.length > 20) {
                value= value.substring(0,17)+ '...';
                vm.areaNamesBtnShow = true;
            }
            return value;
        },
        filterMsgDescFun: function (value) {
            if(value&& value.length > 20) {
                value= value.substring(0,17)+ '...';
                vm.msgDescBtnShow = true;
            }
            return value;
        },
        filterFileSize:function (value) {
            return fileChange(value);
        }
    },
    methods:{

        getEnum: function (dictGroupCode) {
            if(isEmpty(dictGroupCode)){
                return
            }
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
        getEbmInfoAll:function (ebmId) {
            if(isEmpty(ebmId)){
                return;
            }
            $.ajax({
                type: "get",
                // async:false,
                url: baseURL+"safeRest/ebm/getEbmInfoAll",
                contentType: "application/json",
                data:{ebmId:ebmId},
                success: function (r) {
                    if (r.successful ) {
                        initVueData(r.data);
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getEbmDispatchFile:function(ebmId){
            $.ajax({
                type: "get",
                // async:false,
                url: baseURL+"safeRest/ebm/dispatch/getEbmDispatchAndEbdFileByEbmId",
                contentType: "application/json",
                data:{ebmId:ebmId},
                success: function (r) {
                    if (r.successful ) {
                        vm.ebmDispatchFile = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getArea :function (areaList) {
            for(var i=0;i<areaList.length;i++){
                if(this.areaNames==''){
                    this.areaNames = areaList[i].areaName;
                }else{
                    this.areaNames += ","+ areaList[i].areaName;
                }
            }
        },
        setFlowStyle : function (ebmInfo) {
            if(isEmpty(ebmInfo)){
                return;
            }
            var dispatchFlowInfo = ebmInfo.dispatchFlowInfo;    //方案当前流程节点
            var currFlowState = dispatchFlowInfo.flowState;
            var flowStage = dispatchFlowInfo.flowStage;
            vm.flowStyles = getMatchFlowStyleArr("",currFlowState);
            if(flowStage != 3 ){
                //如果当前流程节点不在分发传输阶段，默认选择点击最后一个流程
                var flowState = "";
                if(vm.flowStyles.length > 0 ){
                    for(var i=0;i< vm.flowStyles.length;i++){
                        var flowStyle = vm.flowStyles[i];
                        if(flowStyle.flowClass != 'flow-btn-unSelect'){
                            flowState = flowStyle.flowState;
                        }
                    }
                }
                if(!isEmpty(flowState)){
                    vm.flowStepChange(flowState);
                }
                return;
            }
            vm.flowStepChange(currFlowState);
        },
        flowStepChange:function (flowState) {
            for(var i=0;i< vm.flowStyles.length;i++){
                var flowStyle = vm.flowStyles[i];
                if(flowStyle.flowState == flowState && flowStyle.flowClass == "flow-btn-unSelect"){
                    return;
                }
            }
            var dispatchFlowInfo = vm.ebmInfo.dispatchFlowInfo;    //方案当前流程节点
            var currFlowState = dispatchFlowInfo.flowState;
            vm.flowStyles = getMatchFlowStyleArr("",currFlowState);

            var flowStyles = vm.flowStyles;

            for(var i=0;i<flowStyles.length;i++){
                var flowStyle = flowStyles[i];
                if(flowStyle.flowState == flowState){
                    //当前点击的流程节点
                    if(flowStyle.flowClass != "flow-btn-unSelect"){
                        //点击的流程节点是流程类型中包含的节点
                        flowStyle.navArrClass = "";
                        if(currFlowState == flowStyle.flowState){
                            flowStyle.flowClass = "flow-btn-doing-sel";
                        }else{
                            flowStyle.flowClass = "flow-btn-select"
                        }
                    }
                }else{
                    flowStyle.navArrClass = "nav-arr-hide";
                }
            }
            $(".flow-detail-content").hide();
            $("#flow-detail-"+flowState).show();
        },
        getResoList:function () {
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/ebrView/listForType",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.resoList=r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        doAudit : function () {
            var ebmId = vm.ebmInfo.ebmId;
            var auditResult = $("#auditResult").val();
            var auditOpinion = $("#auditOpinion").val();
            if(isEmpty(ebmId)){
                return
            }
            if(isEmpty(auditResult)){
                return;
            }
            if(auditResult =='0'){
                alert("请选择审核结果！");
                return;
            }
            var data = {
                ebmId:ebmId,
                auditResult:auditResult,
                auditOpinion:auditOpinion
            };
            $.ajax({
                type: "put",
                url: baseURL+"safeRest/ebm/dispatchInfo/audit",
                contentType: "application/json",
                data:JSON.stringify(data),
                success: function (r) {
                    if (r.successful ) {
                        vm.infoNoAudit=false;
                        if(auditResult=="1"){
                            vm.infoAuditState="审核通过";
                        }else{
                            vm.infoAuditState="审核不通过";
                        }
                        //更新当前选中流程未消息分发
                        vm.ebmInfo.dispatchFlowInfo.flowState=33;
                        vm.setFlowStyle(vm.ebmInfo);
                        layer.msg("审核成功！");
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });

        },
        setFlowWidth:function(){
            var len=getFlowStyleArr().length;
            var unitOne=100/(3*len-1);
            this.percentThree=unitOne*3;
            this.percentTwo=unitOne*2;
        },
        reset : function () {
            $("#auditResult").val("0");
            $("#auditOpinion").val("");

        },
         downFile:function(fileId){
            // 模拟表单提交同步方式下载文件,能够弹出保存文件对话框
            var url = baseURL+"safeRest/file/download";
            jQuery('<form action="'+url+'" method="post">' +
                '<input type="text" name="fileId" value="'+fileId+'"/>' +
                '<input type="text" name="fileType" value="1"/>' +
                '<input type="text" name="token" value="'+token+'"/>' +
                '</form>')
                .appendTo('body').submit().remove();
         }
    }
});


function initVueData(data) {
    vm.ebmInfo = data;
    vm.schemeInfo = data.schemeInfoAll;

    //设置覆盖区域
    if(!isEmpty(vm.ebmInfo) && !isEmpty(vm.ebmInfo.schemeInfoAll)
        && !isEmpty(vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo)
        && !isEmpty(vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo.areaList)){
        var areaList = vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo.areaList;
        vm.getArea(areaList);
    }

    //设置流程导航样式
    vm.setFlowStyle(data);

    //审核状态处理
    if(!isEmpty(vm.ebmInfo.ebmDispatchInfoInfo)){
        if(vm.ebmInfo.ebmDispatchInfoInfo.auditResult==null||vm.ebmInfo.ebmDispatchInfoInfo.auditResult=="0"){
            vm.infoNoAudit=true;
            vm.infoAuditState="待审核";
        }else if(vm.ebmInfo.ebmDispatchInfoInfo.auditResult=="1"){
            vm.infoAuditState="审核通过";
        }else if(vm.ebmInfo.ebmDispatchInfoInfo.auditResult=="2"){
            vm.infoAuditState="审核不通过";
        }
    }
}


function back(){
    window.location.href = document.referrer;
}

function areaNamesBtnClick(obj) {
    $(obj).parent().find("span").html(vm.areaNames);
    $("#areaNames-btn").hide();
}

function msgDescBtnClick(obj) {
    if(vm.ebmInfo != null){
        var desc = vm.ebmInfo.msgDesc;
        $(obj).parent().find("span").html(desc);
        $("#msgDesc-btn").hide();
    }
}

/**
 * 返回所有流程相关信息
 * @returns {Array}
 */
function getFlowStyleArr(){
    var flowStyleArr = [];
    var arr1 = {
        id:"1",
        flowStage:"3",
        flowState:"31",
        flowStageName:"消息生成",
        flowStateName:"消息生成",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr1);
    var arr2 = {
        id:"2",
        flowStage:"3",
        flowState:"32",
        flowStageName:"消息审核",
        flowStateName:"消息审核",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr2);
    var arr3 = {
        id:"3",
        flowStage:"3",
        flowState:"33",
        flowStageName:"消息分发",
        flowStateName:"消息分发",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr3);
    return flowStyleArr;
}

function getMatchFlowStyleArr(flowType, currFlowState) {
    var flowStyleArr = getFlowStyleArr();  //所有流程节点
    var reserveFlow =['31','32','33'];
    for(var i=0;i<flowStyleArr.length;i++){
        var flowStyle = flowStyleArr[i];
        flowStyle.navArrClass = "nav-arr-hide";    //隐藏所有向上的箭头
        if(reserveFlow.indexOf(flowStyle.flowState) > -1){
            if(currFlowState == flowStyle.flowState){
                //此为方案当前的流程节点
                flowStyle.flowClass = "flow-btn-current-sel";
                flowStyle.navArrClass = "";    //当前流程的箭头显示处理
            }else{
                //该流程类型包含的流程节点，但未选中，也不是当前的流程节点
                flowStyle.flowClass = "flow-btn-current" ;
            }
        }else{
            //该流程类型不包含的流程节点，置灰色
            flowStyle.flowClass = "flow-btn-unSelect";
        }
    }
    return flowStyleArr;

}
