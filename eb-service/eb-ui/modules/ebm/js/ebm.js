$(function () {
    vm.getSeverityEnum("SEVERITY_DICT");
    vm.getEnum(dictGroupCodes);
    vm.getPlateform();
    vm.initLayDate();
 /*   $(".float-div").click(function () {
        $(".float-div").hide()
    });*/

    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/ebm/page?sendFlag=1',
        datatype: "json",
        colModel: [
            { label: 'EBM编号', name: 'ebmId', index: "ebmId", width: 150,align:"center" },
            { label: '事件标题', name: 'msgTitle', width: 200 ,align:"center"},
            { label: '发布机构名称', name: 'sendName', width: 100 ,align:"center"},
            { label: '事件级别', name: 'severity', width: 50,align:"center",formatter:function (cellvalue,options,row) {
                    return matchAndGetDictValue(vm.ebmEnum,cellvalue,'SEVERITY_DICT');
                }
            },
            { label: '发布时间', name: 'sendTime', width: 90 ,align:"center"},
            { label: '接入状态', name: 'accessState', width: 50,align:"center",formatter:function (cellvalue,options,row) {
            	//var endTime = DateFormat.formatToDate(cellvalue);
            	//var res = DateFormat.compareDate(new Date(),endTime);
            	if(cellvalue==3){
            		return "预警过期"
            	}else{
            		return "预警正常";
            	}
            	}
            },
            { label: '播发状态', name: 'broadcastState', width: 50,align:"center",formatter:function (cellvalue,options,row) {
            	var state ='未处理';
            	switch (cellvalue) {
	                case 0:
	                    state = "未处理";
	                    break;
	                case 1:
	                	state = "等待播发";
	                     break;
	                case 2:
	                	state = "播发中";
	                     break;
	                case 3:
	                	state = "播发成功";
	                     break;
	                case 4:
	                	state = "播发失败";
	                     break;
	                case 5:
	                	state = "播发取消";
	                     break;
            	}
            	return state;
            	}
            },
            { label: '操作', name: '', width: 120 ,align:"center",formatter:function (cellvalue,options,row) {
            	var operateHtml = '';
            	if(row.auditResult==1){
            		operateHtml = "<button class='disable-button'  style='margin-right:10px' >已审核</button>";
            	}else if(vm.permissions["core:ebm:audit"] && row.auditResult!=2){
            		operateHtml = "<button class='pause-button' id='disable-button' style='margin-right:10px' onclick='ebmAudit(\""+row.ebmId+"\")'>消息审核</button>";
            	}else if(row.auditResult==2){
            		operateHtml = "<button class='disable-button'  style='margin-right:10px' >审核不过</button>";
            	}else{
            		operateHtml = "<button class='disable-button'  style='margin-right:10px' >未审核</button>";
            	}
            	if(vm.permissions["core:ebm:cancelPlay"])
	            	if(row.broadcastState==2 && new Date()<DateFormat.formatToDate(row.endTime)){
	            		operateHtml += "<button class='pause-button' id=\'"+row.ebmId+"\'  onclick='cancelPlayer(\""+row.ebmId+"\")'>取消播发</button>";
	            	}else{
	            		operateHtml += "<button class='disable-button' id='cancel_button'>取消播发</button>";
	            	}
                 return   operateHtml;
            }
            }
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
        },
        onSelectRow:function(rowid, status){
        	//audiojs.createAll();
        },
        onCellSelect:function(rowid,iCol,cellcontent,e){
        	if(iCol!=8){
            	var rowData = $("#jqGrid").jqGrid('getRowData',rowid);
                $("#msgDesc-btn").html("展开全部");
                $("#openDesc").hide();
                $("#closeDesc").show();
                getEbmDetail(rowData.ebmId)
        	}
        }
    });

});
function cancelPlayer(ebmId){
    $.ajax({
        type: "POST",
        async:false,
        url: baseURL+"safeRest/ebm/cancelEbmPlay/"+ebmId,
        contentType: "application/json",
        success: function (r) {
        	var content = "";
            if (r.successful) {
            	content = "停止播放成功！";
            	$("#"+ebmId).removeClass();
            	$("#"+ebmId).addClass("disable-button");
            } else {
            	content = "停止播放失败！";
            }
        	layer.msg(content, {
        	    time: 2500, //20s后自动关闭
        	});
        }
    });
}
function auditEbm(state) {
	var data = {
		ebmId:vm.auditEbmId,
		auditResult:state,
		auditOpinion:vm.auditComment,
		auditUser:'admin'
	}
    $.ajax({
        type: "POST",
        async:false,
        url: baseURL+"safeRest/ebm/audit",
        contentType: "application/json",
        data:JSON.stringify(data),
        success: function (r) {
            if (r.successful ) {
            	 layer.closeAll();
                vm.reload();
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function ebmAudit(ebmId){
	    vm.auditEbmId = ebmId;
	    layer.open({
	        type: 1,
	        shade: false,
	        title: false, //不显示标题
	      //  closeBtn: 0, //不显示关闭按钮
	        anim: 2,
	        shadeClose: true, //开启遮罩关闭
	        content: $('#ebmAuditLayer'),
	        area: ['600px', '350px'], //宽高
	 });
}
var dictGroupCodes = "EBR_TYPE,SEVERITY_DICT,MSG_LANG_DICT,EVENT_TYPE_DICT,MSG_TYPE_DICT,AUXILIARY_TYPE_DICT";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
            ebmId:null,
            severity:'',
            startTime:null,
            endTime:null,
            sendName:null,
            senderCode:'',
            msgTitle:null,
        },
        permissions: {
            "core:ebm:query": false,
            "core:ebm:audit": false,
            "core:ebm:cancelPlay":false
        },
        severityEnum:[],
        ebmEnum:[],
        ebrViewList:[],
        ebmInfo:null,
        msgDescBtnShow:true,
        auditComment:'',
        auditEbmId:'',
    },
    mounted: function(){
        getPermission(this);
    },
    created:function(){
    	
    },
    filters: {
        //字符串过长，截取掉。
        filterFun: function (value) {
            if(value&& value.length > 12) {
                value= value.substring(0,10)+ '...';
            }
            return value;
        },
        filterDescFun:function (value) {
            if(value&& value.length > 30) {
                value= value.substring(0,30)+ '...';
                vm.msgDescBtnShow = true;
            }
            return value;
        },
        //获取区域名称
        getAreaNameFilter:function (value) {
           // var name = value;
            if(isEmpty(value)){
                return '';
            }
            var name;
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/area/getByAreaCode",
                contentType: "application/json",
                data:{areaCode:value},
                success: function (r) {
                    if (r.successful &&!isEmpty(r.data) && !isEmpty(r.data.areaName)) {
                        name = r.data.areaName;
                    } else {
                        name = value;
                    }
                }
            });

            return name;
        },
        //获取枚举名称
        getEnumName:function (value,dictCode) {
            if(isEmpty(vm.ebmEnum) || isEmpty(dictCode)){
                return value;
            }
            return matchAndGetDictValue(vm.ebmEnum,value,dictCode);
        },
        getEbrNamesFilter:function (value) {
            var ebrInfos = vm.ebmInfo.ebrInfoList;
            if(ebrInfos == null || ebrInfos.length == 0){
                return '';
            }
            var names = '';
            for(var i=0;i<ebrInfos.length;i++){
                var ebrInfo = ebrInfos[i];
                if(value == ebrInfo.ebrType){
                    if(isEmpty(names)){
                        names = ebrInfo.ebrName;
                    }else{
                        names += ","+ebrInfo.ebrName;
                    }
                }
            }
            return names;
        }

    },
    methods:{
        reload : function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            var startTime="",endTime="";
            if(vm.req.startTime!=null &&　vm.req.startTime!=''){
            	startTime = vm.req.startTime+" 00:00:00";
            }
            if(vm.req.endTime!=null &&　vm.req.endTime!=''){
            	endTime = vm.req.endTime+" 23:59:59";
            }
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{"ebmId":vm.req.ebmId
                    ,"severity":vm.req.severity
                    ,"startTime":startTime
                    ,"endTime":endTime
                    ,"sendName":vm.req.sendName
                    ,"senderCode":vm.req.senderCode
                    ,"msgTitle":vm.req.msgTitle
                    ,"sendFlag":"1"
                },
                page:page
            }).trigger("reloadGrid");
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
                        vm.ebmEnum = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getSeverityEnum: function (dictGroupCode) {
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/dict/getByGroupCode",
                contentType: "application/json",
                data:{dictGroupCode:dictGroupCode},
                success: function (r) {
                    if (r.successful ) {
                        vm.severityEnum = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getPlateform:function(){
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/ebrView/fromEbrlist",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.ebrViewList=r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        downAuxiliaryFile : function(fileId){
            // 模拟表单提交同步方式下载文件,能够弹出保存文件对话框
            var url = baseURL+"safeRest/file/download";
            jQuery('<form action="'+url+'" method="post">' +
                '<input type="text" name="fileId" value="'+fileId+'"/>' +
                '<input type="text" name="fileType" value="2"/>' +
                '<input type="text" name="token" value="'+token+'"/>' +
                '</form>')
                .appendTo('body').submit().remove();
        },
        downOriginFile : function(fileId){
            // 模拟表单提交同步方式下载文件,能够弹出保存文件对话框
            var url = baseURL+"safeRest/file/download";
            jQuery('<form action="'+url+'" method="post">' +
                '<input type="text" name="fileId" value="'+fileId+'"/>' +
                '<input type="text" name="fileType" value="1"/>' +
                '<input type="text" name="token" value="'+token+'"/>' +
                '</form>')
                .appendTo('body').submit().remove();
        },
        initLayDate: function () {
            var startTimeRender= laydate.render({
                elem: '#startTime'
                ,type: 'date'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    if (value == "" || value == null) {
                        Vue.set(vm.req, 'startTime', '');
                    } else {
                        Vue.set(vm.req, 'startTime', value);
                    }

                }
            });

            var  endTimeRender= laydate.render({
                elem: '#endTime'
                , type: 'date'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    if (value == "" || value == null) {
                        Vue.set(vm.req, 'endTime', '');
                    } else {
                        Vue.set(vm.req, 'endTime', value);
                    }
                }
            });
        }
    }
});

function getEbmDetail(ebmId) {
    if(isEmpty(ebmId)){
        return;
    }
    $.ajax({
        type: "get",
        url: baseURL+"safeRest/ebm/getEbmInfoAll",
        contentType: "application/json",
        data:{ebmId:ebmId},
        success: function (r) {
            if (r.successful ) {
                vm.ebmInfo = r.data;
            //    vm.msgDescBtnShow = false;
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"514px"},300);
                setTimeout(function () {
    	    	var mp3 = vm.ebmInfo.audioPath;
    	    	//$("#audioEle").empty();
    	    	//$("#audio-source").attr('src',mp3);
    	    	//$('audio').audioPlayer();
    	        //var audio = $('<audio/>', {id: 'ebmInfo-audio'}).appendTo('#audioEle').attr('src', mp3).attr('preload','auto').attr('loop','loop')[0];
    	        //var testAS = audiojs.create(audio);
    	        //audio.load();
    	    	$("#audioEle").html('<audio preload="auto" controls><source id="audio-source" src="'+mp3+'" /></audio>');
    	    	//$('audio').audioPlayer();
                }, 500);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function leftSidebarClick() {
    $(".right-float-div").animate({width:"0px"},300,function () {
        $(".float-div").hide();
    });
}

function msgDescBtnClick(obj) {
    $("#openDesc").show();
    $("#closeDesc").hide();
    if($(obj).html() =='展开全部'){
        $(obj).html("收起详情");
        $("#openDesc").show();
        $("#closeDesc").hide();
    }else{
        $(obj).html("展开全部");
        $("#openDesc").hide();
        $("#closeDesc").show();
    }
  //  vm.msgDescBtnShow = true;
 /*   if(vm.ebmInfo != null){
        var desc = vm.ebmInfo.msgDesc;
        $(obj).parent().find("span").html(desc);
        $("#msgDesc-btn").hide();
    }*/
}

function downFile(fileId){
    // 模拟表单提交同步方式下载文件,能够弹出保存文件对话框
    var url = baseURL+"safeRest/file/download";
    jQuery('<form action="'+url+'" method="post">' +
        '<input type="text" name="fileId" value="'+fileId+'"/>' +
        '<input type="text" name="fileType" value="1"/>' +
        '<input type="text" name="token" value="'+token+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
