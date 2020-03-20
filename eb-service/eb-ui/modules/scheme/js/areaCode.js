$(function() {

	initTree("0");

});

var dictGroupCodes = "PROGRAM_STATE_DICT,PROGRAM_TYPE_DICT,PROGRAM_CONTENT_TYPE,PROGRAM_STRATEGY_TYPE,PROGRAM_LEVEL_DICT,AUDIT_STATUS_DICT,MSG_LANG_DICT,WEEK_MASK_DICT";
var endTimeRender;
var vm = new Vue(
		{
			el : '#rrapp',
			data : {
				req : {
					parentLibName : null,
					parentLibId : null,
					type : "",
					libName : null
				},
				pageEnum : [],
				// programInfo:{},
				programInfo : {
					programName : null,
					programType : '',
					programLevel : '',
					languageCode : '',
					contentType : '',
					ebmEventType : '00000',
					ebmEventDesc : null,
					filesList : [],
					areaList : [],
					programContent : {
						content : null
					},
					programStrategy : {
						startTime : null,
						overTime : null,
						durationTime : null,
						playTime : null,
						weekMask : null,
						timeList : []
					}
				},
				upload : {
					myDropzone : null,
					layerIndex : null
				},
				// 节目类型，1-应急节目，2-日常
				programType : '',
				eventTypes : [],
				parentLib : {},
				eventDescBtnShow : false,
				commitBtnEnable : true,
				areaNames : "",
				weeks : [],
				flowStyles : [],
				addOperate : true,
				selectedFiles : [],
				uploadFileLibId : '',
				// 上传文件默认审核通过
				uploadFileStatus : '1',
				selectedAreaCodeArr : [],
				timeStyle : '1',
				programId : ''

			},
			mounted : function() {
				getPermission(this);
				this.setFlowWidth();
				this.programId = getQueryString("programId");
				// this.getProgramInfo(this.programId);
				// this.getProgramInfo('411c078ac27547d0807f6f083f430b00');
				// this.getProgramInfo('');
				this.getEventTypes("11000");
				this.getUploadLibId('program_make_upload_lib')

			},
			created : function() {
				this.initDropzone();
			},
			filters : {
				filterFun : function(value, len) {
					if (value && value.length > len) {
						value = value.substring(0, len - 1) + '...';
					}
					return value;
				},
				filterDescFun : function(value, len) {
					if (value && value.length > len) {
						value = value.substring(0, len - 1) + '...';
						this.eventDescBtnShow = true;
					}
					return value;
				},
				// 获取枚举名称
				getEnumName : function(value, dictCode) {
					if (isEmpty(value)) {
						return value
					}
					if (isEmpty(vm.pageEnum) || isEmpty(dictCode)) {
						return value;
					}
					return matchAndGetDictValue(vm.pageEnum, value, dictCode);
				},
				getEventName : function(value) {
					if (isEmpty(value)) {
						return value;
					}
					if (value == '00000') {
						return "所有事件";
					}
					var eventList = vm.eventTypes;
					var desc = value;
					if (!isEmpty(eventList)) {
						for (var i = 0; i < eventList.length; i++) {
							var event = eventList[i];
							if (event.eventCode == value) {
								desc = event.eventDesc;
								break;
							}
						}
					}
					return desc;
				}

			},

			methods : {
				weekChecked : function(value) {
					var weekMsak = vm.programInfo.programStrategy.weekMask + '';
					var len = value.length;
					if (weekMsak.length < len) {
						return false;
					}

					if (weekMsak.charAt(weekMsak.length - len) == 1) {
						return true;
					} else {
						return false;
					}
				},
				getEnum : function(dictGroupCode) {
					$.ajax({
						type : "get",
						async : false,
						url : baseURL + "safeRest/dict/getByGroupCode",
						contentType : "application/json",
						data : {
							dictGroupCode : dictGroupCode
						},
						success : function(r) {
							if (r.successful) {
								vm.pageEnum = r.data;
							} else {
								layer.msg(r.msg);
							}
						}
					});
				},
				getUploadLibId : function(paramKey) {
					$.ajax({
						type : "get",
						url : baseURL + "safeRest/sys/param/getByParamKey",
						contentType : "application/json",
						data : {
							paramKey : paramKey
						},
						success : function(r) {
							if (r.successful) {
								vm.uploadFileLibId = r.data.paramValue;
							} else {
								layer.msg(r.msg);
							}
						}
					});
				},
				getEventTypes : function(parentCode) {
					$
							.ajax({
								type : "get",
								url : baseURL
										+ "safeRest/event/type/getNextByParentCode?parentCode="
										+ parentCode,
								contentType : "application/json",
								success : function(r) {
									if (r.successful) {
										vm.eventTypes = r.data;
									} else {
										layer.msg(r.msg);
									}
								}
							});
				},
				getProgramInfo : function(programId) {
					if (isEmpty(programId)) {
						return;
					}
					$
							.ajax({
								type : "get",
								// async:false,
								url : baseURL + "safeRest/program/" + programId,
								contentType : "application/json",
								success : function(r) {
									if (r.successful) {
										vm.programInfo = r.data;
										vm.programType = r.data.programType;
										if (isEmpty(vm.programInfo.areaList)) {
											vm.programInfo.areaList = [];
										}
										if (isEmpty(vm.programInfo.filesList)) {
											vm.programInfo.filesList = [];
										}
										if (isEmpty(vm.programInfo.programStrategy)) {
											vm.programInfo.programStrategy = {};
										}
										if (isEmpty(vm.programInfo.programContent)) {
											vm.programInfo.programContent = {};
										}
										if (r.data.areaList
												&& r.data.areaList.length > 0) {
											vm.getAreaNames(r.data.areaList);
										}
										if (!isEmpty(vm.programInfo.programStrategy)
												&& !isEmpty(vm.programInfo.programStrategy.weekMask)) {
											parseWeekMask(vm.programInfo.programStrategy.weekMask
													+ '');
										}
										// 更新区域树的选中状态
										updateTreeNodeCheckState();
									} else {
										layer.msg(r.msg);
									}
								}
							});
				},
				getAreaNames : function(areaList) {
					if (areaList == null || areaList.length == 0) {
						return "";
					}
					var areaNames = "";
					for (var i = 0; i < areaList.length; i++) {
						var area = areaList[i];
						if (areaNames.length == 0) {
							areaNames += area.areaName;
						} else {
							areaNames += "," + area.areaName;
						}
					}
					vm.areaNames = areaNames;
				},
				// 初始化流程导航的流程节点样式
				setFlowStyle : function() {
					vm.flowStyles = getFlowStyleArr();
				},
				// 流程节点按钮点击事件
				flowStepChange : function(flowState) {
					vm.initLayDate();
					for (var i = 0; i < vm.flowStyles.length; i++) {
						var flowStyle = vm.flowStyles[i];
						if (flowStyle.flowState == flowState
								&& flowStyle.flowClass == "flow-btn-unSelect") {
							return;
						}
					}

					var flowStyles = vm.flowStyles;

					for (var i = 0; i < flowStyles.length; i++) {
						var flowStyle = flowStyles[i];
						if (flowStyle.flowState == flowState) {
							// 当前点击的流程节点
							if (flowStyle.flowClass != "flow-btn-unSelect") {
								// 点击的流程节点是流程类型中包含的节点
								flowStyle.navArrClass = "";
								flowStyle.flowClass = "flow-btn-select"
							}
						} else {
							flowStyle.flowClass = "flow-btn-current";
							flowStyle.navArrClass = "nav-arr-hide";
						}
					}
					$(".flow-detail-content").hide();
					$("#flow-detail-" + flowState).show();
				},
				setFlowWidth : function() {
					var len = getFlowStyleArr().length;
					var unitOne = 100 / (3 * len - 1);
					this.percentThree = unitOne * 3;
					this.percentTwo = unitOne * 2;
				},

				reset : function() {
					// vm.getProgramInfo(vm.programId);
					vm.getProgramInfo("411c078ac27547d0807f6f083f430b00");
				},
				choseFiles : function() {
					initJqgrid();
					layer.open({
						type : 1,
						shade : false,
						title : false, // 不显示标题
						closeBtn : 0, // 不显示关闭按钮
						anim : 2,
						shadeClose : true, // 开启遮罩关闭
						content : $('#fileChoseLayer'),
						area : [ '1200px', '650px' ], // 宽高
					});
				},
				uploadFiles : function() {
				},
				initDropzone : function() {
				},
				closeLayer : function() {
					vm.initProgramVueData();
					layer.closeAll();
				},
				initProgramVueData : function() {
					vm.selectedFiles = [];
				},
				reload : function() {
					// saveSelectedData();
					var page = $("#jqGrid").jqGrid('getGridParam', 'page');
					$("#jqGrid").jqGrid('setGridParam', {
						postData : {
							"type" : vm.req.type,
							"parentLibName" : vm.req.parentLibName,
							"parentLibId" : vm.req.parentLibId,
							"libName" : vm.req.libName
						},
						page : page
					}).trigger("reloadGrid");
				},
				getParentLibInfo : function(libId) {
					if (libId != 0 && isEmpty(libId)) {
						return;
					}
					$.ajax({
						type : "get",
						url : baseURL + "safeRest/fileLibrary/" + libId,
						contentType : "application/json",
						success : function(r) {
							if (r.successful) {
								vm.parentLib = r.data;
							} else {
								layer.msg(r.msg);
							}
						}
					});
				},
				saveOrUpdate : function(programType) {
					if (!vmDataCheck()) {
						return;
					}
					if (programType == 1) {
						vm.programInfo.programType = 1;
					} else if (programType == 2) {
						vm.programInfo.programType = 2;
						vm.programInfo.programLevel = 10;
					}
					var url = '';
					var type = '';
					if (isEmpty(vm.programInfo.programId)) {
						url = baseURL + "safeRest/program/save";
						type = 'post'
					} else {
						url = baseURL + "safeRest/program/update";
						type = 'put'
					}
					$.ajax({
						type : type,
						url : url,
						contentType : "application/json",
						data : JSON.stringify(vm.programInfo),
						success : function(r) {
							if (r.successful) {
								window.location.href = 'program.html';
							} else {
								layer.msg(r.msg);
							}
						}
					});
				},
				saveAndCommit : function(programType) {
					if (vm.commitBtnEnable) {
						vm.commitBtnEnable = false;

						if (!vmDataCheck()) {
							return;
						}
						if (programType == 1) {
							vm.programInfo.programType = 1;
						} else if (programType == 2) {
							vm.programInfo.programType = 2;
							vm.programInfo.programLevel = 10;
							vm.programInfo.ebmEventType = '00000';
						}
						var url = '';
						var type = '';
						if (isEmpty(vm.programInfo.programId)) {
							url = baseURL + "safeRest/program/save";
							type = 'post'
						} else {
							url = baseURL + "safeRest/program/update";
							type = 'put'
						}
						vm.programInfo.state = '2';
						$.ajax({
							type : type,
							url : url,
							contentType : "application/json",
							data : JSON.stringify(vm.programInfo),
							success : function(r) {
								if (r.successful) {
									window.location.href = 'program.html';
								} else {
									layer.msg(r.msg);
								}
								vm.commitBtnEnable = true;
							},
							error : function() {
								vm.commitBtnEnable = true;
								layer.msg("节目创建失败，系统错误！");
							}
						});
					} else {
						layer.msg("节目创建中，请勿重复点击。请稍候。。。");
					}
				},
				initLayDate : function() {
					var startTimeRender = laydate.render({
						elem : '#startTime',
						type : 'time',
						theme : 'molv',
						trigger : 'click'

						,
						change : function(value, date) {
						},
						done : function(value, date) {
							/*
							 * if (value == "" || value == null) {
							 * Vue.set(vm.programInfo.programStrategy,
							 * 'startSendTime', ''); } else { Vue.set(vm.req,
							 * 'startSendTime', value); }
							 */

						}
					});
					var startTimeRender = laydate.render({
						elem : '#durationStartTime',
						type : 'time',
						theme : 'molv',
						trigger : 'click'

						,
						change : function(value, date) {
						},
						done : function(value, date) {
							/*
							 * if (value == "" || value == null) {
							 * Vue.set(vm.programInfo.programStrategy,
							 * 'startSendTime', ''); } else { Vue.set(vm.req,
							 * 'startSendTime', value); }
							 */

						}
					});

					endTimeRender = laydate.render({
						elem : '#overTime',
						type : 'time',
						theme : 'molv',
						change : function(value, date) {
						},
						done : function(value, date) {
							/*
							 * if (value == "" || value == null) {
							 * Vue.set(vm.req, 'endTime', ''); } else {
							 * Vue.set(vm.req, 'endTime', value); }
							 */
						}
					});
					var endTimeRender = laydate.render({
						elem : '#vStartTime',
						type : 'date',
						theme : 'molv',
						change : function(value, date) {
						},
						done : function(value, date) {
							if (value == "" || value == null) {
								Vue.set(vm.programInfo.programStrategy,
										'startTime', '');
							} else {
								Vue.set(vm.programInfo.programStrategy,
										'startTime', value);
							}
						}
					});
					var endTimeRender = laydate.render({
						elem : '#vOverTime',
						type : 'date',
						theme : 'molv',
						change : function(value, date) {
						},
						done : function(value, date) {
							if (value == "" || value == null) {
								Vue.set(vm.programInfo.programStrategy,
										'overTime', '');
							} else {
								Vue.set(vm.programInfo.programStrategy,
										'overTime', value);
							}
						}
					});
					var endTimeRender = laydate.render({
						elem : '#playTime',
						type : 'date',
						theme : 'molv',
						change : function(value, date) {
						},
						done : function(value, date) {
							if (value == "" || value == null) {
								Vue.set(vm.programInfo.programStrategy,
										'playTime', '');
							} else {
								Vue.set(vm.programInfo.programStrategy,
										'playTime', value);
							}
						}
					});
				}
			}
		});

function showSchemeDetail(ebmId) {
	var page = $('#jqGrid').getGridParam('page');
	window.location.href = 'schemeDetail.html?ebmId=' + ebmId + "&page=" + page;
}

function toBrdRecord(ebmId) {
	window.location.href = '../brdRecord/brdRecord.html?ebmId=' + ebmId;
}

function toNewsTopic(ebmId) {
	window.location.href = 'newsTopic.html?ebmId=' + ebmId;
}

function eventDescBtnClick(obj) {
	$(obj).parent().find(".openDesc").show();
	$(obj).parent().find(".closeDesc").hide();
	// $("#openDesc").show();
	// $("#closeDesc").hide();
	if ($(obj).html() == '展开全部') {
		$(obj).html("收起详情");
		$(obj).parent().find(".openDesc").show();
		$(obj).parent().find(".closeDesc").hide();
	} else {
		$(obj).html("展开全部");
		$(obj).parent().find(".openDesc").hide();
		$(obj).parent().find(".closeDesc").show();
	}
}

/**
 * 返回所有节目制作流程相关信息
 * 
 * @returns {Array}
 */
function getFlowStyleArr() {
	var flowStyleArr = [];
	var arr1 = {
		id : "1",
		flowState : "1",
		flowStateName : "基本信息",
		navArrClass : "",
		flowArrowStyle : "",
		flowClass : "flow-btn-select"
	};
	flowStyleArr.push(arr1);
	var arr2 = {
		id : "2",
		flowState : "2",
		flowStateName : "覆盖区域",
		navArrClass : "nav-arr-hide",
		flowArrowStyle : "",
		flowClass : "flow-btn-current"
	};
	flowStyleArr.push(arr2);
	var arr3 = {
		id : "3",
		flowState : "3",
		flowStateName : "节目内容",
		navArrClass : "nav-arr-hide",
		flowArrowStyle : "",
		flowClass : "flow-btn-current"
	};
	flowStyleArr.push(arr3);
	var arr4 = {
		id : "4",
		flowState : "4",
		flowStateName : "节目策略",
		navArrClass : "nav-arr-hide",
		flowArrowStyle : "",
		flowClass : "flow-btn-current"
	};
	flowStyleArr.push(arr4);

	return flowStyleArr;
}

function back() {
	window.location.href = document.referrer;
}

function next(flowState) {
	vm.flowStepChange(flowState);
}

function appendNav(libName, libId, type) {
	var html = '<span class="parent_lib_name" onclick="parentLibNavClick(\''
			+ libId + '\',this)">&nbsp;>>&nbsp;' + libName + '</span>';
	$(".file_Lib_Nav").append(html);
}

function parentLibNavClick(libId, obj) {
	vm.getParentLibInfo(libId);
	vm.req.parentLibId = libId;
	vm.reload();
	$(obj).nextAll().remove();
}

var areaTreeUrl = baseURL + "safeRest/area/getByParentAreaCode";
function getAreaTreeNodesUrl(treeId, treeNode) {
	var param = "parentAreaCode=" + treeNode.areaCode;
	return areaTreeUrl + "?" + param;
}
function filter(treeId, parentNode, childNodes) {
	return initTreeCheckedStatus(childNodes.data);
}
function initTreeCheckedStatus(childNodes) {
	var nodeDatas = childNodes;
	var areaList = vm.programInfo.areaList;
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
var setting = {
	async : {
		enable : true,// 设置 zTree 是否开启异步加载模式
		url : getAreaTreeNodesUrl,
		type : "get",
		dataFilter : filter
	},
	view : {
		fontCss : {
			color : "#5fabec",
			'background-color' : 'black'
		},
		addHoverDom : addHoverDom,
		removeHoverDom : removeHoverDom,
		selectedMulti : false,
		autoCancelSelected : true
	},
	edit : {
		enable : true,

		// 给节点额外添加属性来控制“重命名”、“删除”图标的显示或隐藏
		showRenameBtn : showRenameBtn,
		showRemoveBtn : showRemoveBtn,
		removeTitle : "删除区域",
		renameTitle : "编辑区域",
		addTitle : "添加区域"
	},
	data : {
		simpleData : {
			enable : true,// 是否采用简单数据模式
			idKey : "areaCode",// 树节点ID名称
			pIdKey : "parentAreaCode",// 父节点ID名称
			rootPId : null
		// 根节点ID
		},
		key : {
			name : "areaName",
			isParent : 'parent',
			title : "areaName",
			checked : "checked"
		}
	},
	callback : {
		beforeDrag : beforeDrag,
		beforeEditName : beforeEditName,
		beforeRemove : beforeRemove,
		beforeRename : beforeRename,
		onRemove : onRemove,
		onRename : onRename,
		onClick : onTreeClick
	},
	check : {
		enable : false,
		nocheckInherit : true,
		chkDisabledInherit : true
	// chkboxType: { "Y": "", "N": "s" },
	// radioType: "all" //对所有节点设置单选
	}
};

function onTreeClick(event, treeId, treeNode) {
	$("#areanodeId").val(treeNode.id);
	$("#parentAreaCode").val(treeNode.parentAreaCode);
	$("#areaCode").val(treeNode.areaCode);
	$("#areaName").val(treeNode.areaName);
	$("#areaLevel").val(treeNode.areaLevel);
}
function btnCancelZtree() {

}
function btnSaveZtree2() {
	var areanodeId = $("#areanodeId").val();
	var areaCode = $("#areaCode").val();
	var areaName = $("#areaName").val();
	var parentAreaCode = $("#parentAreaCode").val();
	var areaLevel = $("#areaLevel").val();
	var zTreeOjb = $.fn.zTree.getZTreeObj("areaTree");
	var selectedNodes = zTreeOjb.getSelectedNodes();
	if (selectedNodes.length != 0) {
		var data = {
			id : areanodeId,
			areaCode : areaCode,
			areaName : areaName,
			parentAreaCode : parentAreaCode,
			areaLevel : areaLevel
		};
		$.ajax({
			type : "PUT",
			async : false,
			url : baseURL + "safeRest/area/updateOrSaveArea",
			contentType : "application/json",
			data : JSON.stringify(data),
			success : function(r) {
				if (r.successful) {
					selectedNodes[0].areaName = areaName;
					zTreeOjb.updateNode(selectedNodes[0]);
					alert("修改成功！");
				} else {
					layer.msg(r.msg);
				}
			}
		});
	} else {
		alert("请先选择区域！");
	}
}
var log, className = "dark";
function beforeDrag(treeId, treeNodes) {
	return false;
}
function beforeEditName(treeId, treeNode) {
	className = (className === "dark" ? "" : "dark");
	showLog("[ " + getTime() + " beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; "
			+ treeNode.name);
	var zTree = $.fn.zTree.getZTreeObj("areaTree");
	zTree.selectNode(treeNode);
	return true;
}
function beforeRemove(treeId, treeNode) {
	className = (className === "dark" ? "" : "dark");
	showLog("[ " + getTime() + " beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; "
			+ treeNode.name);
	var zTree = $.fn.zTree.getZTreeObj("areaTree");
	zTree.selectNode(treeNode);

	return true;
}
function onRemove(e, treeId, treeNode) {
	$.ajax({
		type : "DELETE",
		async : false,
		url : baseURL + "safeRest/area/deleteAreaById/" + treeNode.id,
		contentType : "application/json",
		success : function(r) {
			if (r.successful) {
				var xx = confirm("删除区域 -- " + treeNode.areaName + "成功！");
			} else {
				layer.msg(r.msg);
			}
		}
	});
	showLog("[ " + getTime() + " onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; "
			+ treeNode.name);
}
function beforeRename(treeId, treeNode, newName) {
	className = (className === "dark" ? "" : "dark");
	showLog("[ " + getTime() + " beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; "
			+ treeNode.name);
	if (newName.length == 0) {
		alert("节点名称不能为空.");
		var zTree = $.fn.zTree.getZTreeObj("areaTree");
		setTimeout(function() {
			zTree.editName(treeNode)
		}, 10);
		return false;
	}
	return true;
}
function onRename(e, treeId, treeNode) {
	showLog("[ " + getTime() + " onRename ]&nbsp;&nbsp;&nbsp;&nbsp; "
			+ treeNode.name);
}
function showLog(str) {
	if (!log)
		log = $("#log");
	log.append("<li class='" + className + "'>" + str + "</li>");
	if (log.children("li").length > 8) {
		log.get(0).removeChild(log.children("li")[0]);
	}
}
function getTime() {
	var now = new Date(), h = now.getHours(), m = now.getMinutes(), s = now
			.getSeconds(), ms = now.getMilliseconds();
	return (h + ":" + m + ":" + s + " " + ms);
}

// 是否显示编辑button
function showRenameBtn(treeId, treeNode) {
	// 获取节点所配置的noEditBtn属性值
	if (treeNode.noEditBtn != undefined && treeNode.noEditBtn) {
		return false;
	} else {
		return true;
	}
}
// 是否显示删除button
function showRemoveBtn(treeId, treeNode) {
	// 获取节点所配置的noRemoveBtn属性值
	if (treeNode.noRemoveBtn != undefined && treeNode.noRemoveBtn) {
		return false;
	} else {
		return true;
	}
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {

	// 在addHoverDom中推断第0级的节点不要显示“新增”button

	if (treeNode.level === 0) {
		return false;
	} else {

		// 给节点加入"新增"button

		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.editNameFlag || $("#addBtn_" + treeNode.id).length > 0)
			return;
		var addStr = "<span class='button add' id='addBtn_" + treeNode.id
				+ "' title='添加区域' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $("#addBtn_" + treeNode.id);
		if (btn)
			btn.bind("click", function() {
				var zTree = $.fn.zTree.getZTreeObj("areaTree");
				var areaLevel = parseInt(treeNode.areaLevel) + 1;
				var node = {
					areaCode : (100 + newCount),
					parentAreaCode : treeNode.areaCode,
					areaLevel : areaLevel,
					areaName : "新区域节点" + (newCount++)
				};
				console.log(node);
				zTree.addNodes(treeNode, node);
				return false;
			});
	}

};
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_" + treeNode.id).unbind().remove();
};
function selectAll() {
	var zTree = $.fn.zTree.getZTreeObj("areaTree");
	zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
}

var ztree;
function initTree(parentAreaCode) {
	$.ajax({
		url : areaTreeUrl,
		type : "get",
		contentType : "application/json",
		data : {
			parentAreaCode : parentAreaCode
		},
		success : function(r) {
			var nodeDatas = initTreeCheckedStatus(r.data);
			ztree = $.fn.zTree.init($("#areaTree"), setting, nodeDatas);// 初始化树节点时，添加同步获取的数据
			var nodeList = ztree.getNodes();
			if (nodeList.length > 0) {
				ztree.expandNode(nodeList[0], true);
				// ztree.setting.callback.onClick(null, ztree.setting.treeId,
				// nodeList[0]);
			}
		}
	});

}
function updateTreeNodeCheckState() {
	if (!isEmpty(vm.programInfo)) {
		var nd = ztree.getNodes();
		var nodes = ztree.transformToArray(nd);
		if (!isEmpty(nodes)) {
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i];
				node.checked = false;
				for (var j = 0; j < vm.programInfo.areaList.length; j++) {
					var area = vm.programInfo.areaList[j];
					if (area.areaCode == node.areaCode) {
						node.checked = true;
						ztree.updateNode(node, false)
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
	for (var i = 0; i < nodes.length; i++) {
		var node = nodes[i];
		var needAdd = true;
		if (vm.programInfo && !isEmpty(vm.programInfo.areaList)) {
			var pAreaList = vm.programInfo.areaList;
			for (var j = 0; j < pAreaList.length; j++) {
				var p_area = pAreaList[j];
				if (node.areaCode.indexOf(p_area.areaCode) == 0
						&& node.areaCode != p_area.areaCode) {
					needAdd = false;
					break;
				}
			}
		}

		if (needAdd) {
			var areaData = {
				areaName : node.areaName,
				areaCode : node.areaCode,
				programId : vm.programId,
			};
			areaList.push(areaData);
		}

	}
	// 删除该节点子节点，以及更下一级节点

	for (var i = 0; i < areaList.length; i++) {
		var area = areaList[i];
		if (area.areaCode.indexOf(treeNode.areaCode) < 0
				|| area.areaCode == treeNode.areaCode) {
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
	vm.programInfo.areaList = areaDataList;
	vm.areaNames = names;
}
