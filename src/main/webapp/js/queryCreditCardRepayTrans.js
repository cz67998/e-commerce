WB.WFTS.HandleRefund = {
	status : "1",
	url : {
		queryTransUrl : "smyCreditCardRepay/querySmyCreditCardRepayTrans.do",
		querySubTransUrl : "smyCreditCardRepay/querySmyCreditCardRepaySubTrans.do"
	},
	INITPAGE : 1,
	INITROWS : 10,
	curpage : "",
	currows : "",
	localParaData : {
		'page' : '1',
		'rows' : '10'
	}
}

WB.WFTS.HandleRefund.init = function() {
	this.initPage();
	this.queryEvt();
	var commonObj = WB.WFTS.common;
	refundObj = WB.WFTS.HandleRefund;
}

// 重置按钮
WB.WFTS.HandleRefund.searchReset = function() {
	$('#busSeq').val('');
	$('#webankCardNo').val('');
	$('#start_date').datebox({
		required : false
	});
	$('#end_date').datebox({
		required : false
	});
}

// 显示数据表格中数据
WB.WFTS.HandleRefund.showGridData = function(dataParams) {
	var handleObj = WB.WFTS.HandleRefund, commonObj = WB.WFTS.common;
	function fillGridData(gribData) {
		handleObj.total = gribData.total;
		handleObj.paginationId = "refund_show_data";

		if (gribData.rows == null || gribData.rows.length == 0) {
			$('#refund_show_data').datagrid("loadData", []);
		} else {
			$('#refund_show_data').datagrid("loadData", gribData);
		}

		var callback = WB.WFTS.HandleRefund.showGridData;
		commonObj.setPagination(dataParams, callback, handleObj);
		$(".datagrid-pager").attr("style", "position:absolute; bottom:3px;width: 100%;");
	}
	dataParams.page = WB.WFTS.HandleRefund.curpage;
	dataParams.rows = WB.WFTS.HandleRefund.currows;
	var queryUrl = handleObj.url.queryTransUrl;
	commonObj.ajaxLoadData(queryUrl, dataParams, function(data) {
		if (data.status == "SUCCESS") {
			fillGridData(data.result);
		} else {
			$.messager.alert('提示', "加载数据错误，请稍后再试。", 'error');
		}
	}, function() {
		$.messager.alert('提示', "系统调用失败，请稍后再试。", 'error');
	});
}

WB.WFTS.HandleRefund.initPage = function() {
	var refundObj = WB.WFTS.HandleRefund, commonObj = WB.WFTS.common;
	refundObj.curpage = refundObj.INITPAGE;
	refundObj.currows = refundObj.INITROWS;
	
	function getParamData() {
		for ( var key in localParaData) {
			$('#' + key).html("");
			$('#' + key).append('<option value="">请选择</option>');
			// 增加新增和编辑的下列列表
			for (var i = 0; i < localParaData[key].length; i++) {
				var tempObj = localParaData[key][i];
				var optionHtml = '<option value="' + tempObj.prm_value + '">'
						+ tempObj.prm_showmsg + '</option>';
				$('#' + key).append(optionHtml);
				// 添加查询常量
				if ($('#search_' + key).size() > 0) {
					$('#search_' + key).append(optionHtml);
				}
			}
		}
	}
	localParaData = JSON.parse(window.localStorage.getItem('PARAMDATA'));
	getParamData();

	function createButton(val,rec) {
		var button = "<input id='button_" + rec.busSeq + "'onClick='openInfo(" + '"' + rec.busSeq + '"' + "," + '"' + rec.webankCardNo + '"' + ")' " +
				"type='button' class='send_out' value='详情'/>";
		return button;
	};

	$('#refund_show_data').datagrid({
		singleSelect : true,
		rownumbers : true,
		fitColumns : false,
		height : '95%',
		toolbar : '#refund_toolbar',
		pagination : true,
		ScrollBars : 1,
		columns : [ [ {
			field : 'busSeq',
			title : '业务流水号',
			'align' : 'center',
			'width' : '180px'
		}, {
			field : 'payerName',
			title : '姓名',
			'align' : 'center',
			'width' : '70px'
		}, {
			field : 'orgNo',
			title : '合作机构号',
			'align' : 'center',
			'width' : '100px'
		}, {
			field : 'webankCardNo',
			title : 'Webank卡号',
			'align' : 'center',
			'width' : '100px'
		}, {
			field : 'totalRepay',
			title : '还款金额',
			'align' : 'center',
			'width' : '80px'
		}, {
			field : 'customerRepay',
			title : '用户实际还款金额',
			'align' : 'center',
			'width' : '80px'
		}, {
			field : 'marketingRepay',
			title : '优惠金额',
			'align' : 'center',
			'width' : '80px'
		}, {
			field : 'dealStatus',
			title : '交易状态',
			'align' : 'center',
			'width' : '50px'
		}, {
			field : 'retStatus',
			title : '处理状态',
			'align' : 'center',
			'width' : '50px'
		}, {
			field : 'retCode',
			title : '错误码',
			'align' : 'center',
			'width' : '50px'
		}, {
			field : 'retMsg',
			title : '错误原因',
			'align' : 'center',
			'width' : '100px'
		}, {
			field : 'payerCardNo',
			title : '代扣储蓄卡号',
			'align' : 'center',
			'width' : '180px'
		}, {
			field : 'payerBank',
			title : '代扣储蓄卡银行',
			'align' : 'center',
			'width' : '100px'
		}, {
			field : 'payeeCardNo',
			title : '代付信用卡号',
			'align' : 'center',
			'width' : '180px'
		}, {
			field : 'payeeBank',
			title : '代付信用卡银行',
			'align' : 'center',
			'width' : '100px'
		}, {
            field : 'create_time',
            title : '交易日期',
            'align' : 'center',
            'width' : '100px'
        }, {
			field : "xxx",
			title : '操作',
			'align' : 'center',
			'width' : '50px',
			formatter : createButton
		} ] ],
		onLoadSuccess : function(data) {
			if (data) {
				$.each(data.rows, function(index, item) {
					if (item.checked) {
						$('#refund_show_data').datagrid('checkRow', index);
					}
				});
			}
		}
	});
}

function openInfo(bq,wo) {
	// setp1 新建详情表格
	// step2 根据详情按钮对应的业务流水号去后台查询数据并返回
	// step3 填充数据进详情表格中
	var dataParams = {};
	var handleObj = WB.WFTS.HandleRefund, commonObj = WB.WFTS.common;
	dataParams.busSeq = bq;
    dataParams.webankCardNo = wo;
	var queryUrl = handleObj.url.querySubTransUrl;
	commonObj.ajaxLoadData(queryUrl, dataParams,
		function(data) {
			if (data.status == "SUCCESS") {
				// alert(JSON.stringify(data));
				// 初始化页面数据
                $("#out_bus_seq").html("");
                $("#out_sys_seq").html("");
                $("#out_webank_acct").html("");
                $("#out_webank_acct_name").html("");
                $("#out_trans_amt").html("");
                $("#out_deal_status").html("");
                $("#back_bus_seq").html("");
                $("#back_sys_seq").html("");
                $("#back_webank_acct").html("");
                $("#back_webank_acct_name").html("");
                $("#back_trans_amt").html("");
                $("#back_deal_status").html("");
                $("#market_bus_seq").html("");
                $("#market_sys_seq").html("");
                $("#market_webankCardNo").html("");
                $("#market_payerName").html("");
                $("#market_total_amt").html("");
                $("#market_deal_status").html("");
                $("#market_reversal_status").html("");
                $("#two_bus_seq").html("");
                $("#two_sys_seq").html("");
                $("#two_webankCardNo").html("");
                $("#two_payerName").html("");
                $("#two_total_amt").html("");
                $("#two_deal_status").html("");
                $("#two_reversal_status").html("");
                $("#in_bus_seq").html("");
                $("#in_sys_seq").html("");
                $("#in_webank_acct").html("");
                $("#in_webank_acct_name").html("");
                $("#in_trans_amt").html("");
                $("#in_deal_status").html("");

				$("#addDialog").dialog({
					"title" : "交易详情"
				});
				$("#addDialog").dialog("open")

				// 显示隐藏
                $(".out").show();
                $(".back").show();
                $(".market").show();
                $(".two").show();
                $(".in").show();

				// 萨摩耶客户一类户转二类户
				$("#out_bus_seq").html(data.result.rows.out_bus_seq);
				$("#out_sys_seq").html(data.result.rows.out_sys_seq);
				$("#out_webank_acct").html(data.result.rows.out_webank_acct);
				$("#out_webank_acct_name").html(data.result.rows.out_webank_acct_name);
				$("#out_trans_amt").html(data.result.rows.out_trans_amt);
				$("#out_deal_status").html(data.result.rows.out_deal_status);
				$(".detail").css("width",100);
                if (!data.result.rows.out_bus_seq||!data.result.rows.out_sys_seq) {
                    $(".out").css("display","none");
                }
				
				// 萨摩耶客户二类户转一类户
				$("#back_bus_seq").html(data.result.rows.back_bus_seq);
				$("#back_sys_seq").html(data.result.rows.back_sys_seq);
				$("#back_webank_acct").html(data.result.rows.back_webank_acct);
				$("#back_webank_acct_name").html(data.result.rows.back_webank_acct_name);
				$("#back_trans_amt").html(data.result.rows.back_trans_amt);
				$("#back_deal_status").html(data.result.rows.back_deal_status);
				if (!data.result.rows.back_bus_seq||!data.result.rows.back_sys_seq) {
					$(".back").css("display","none");
				}
				
				// 萨摩耶营销专户转机构待清算户
				$("#market_bus_seq").html(data.result.rows.market_bus_seq);
				$("#market_sys_seq").html(data.result.rows.market_sys_seq);
				$("#market_webankCardNo").html(data.result.rows.market_webankCardNo);
				$("#market_payerName").html(data.result.rows.market_payerName);
				$("#market_total_amt").html(data.result.rows.market_total_amt);
				$("#market_deal_status").html(data.result.rows.market_deal_status);
				$("#market_reversal_status").html(data.result.rows.market_reversal_status);
				if (!data.result.rows.market_bus_seq||!data.result.rows.market_sys_seq) {
					$(".market").css("display","none");
				}
				
				// 萨摩耶二类户转机构待清算户
				$("#two_bus_seq").html(data.result.rows.two_bus_seq);
				$("#two_sys_seq").html(data.result.rows.two_sys_seq);
				$("#two_webankCardNo").html(data.result.rows.two_webankCardNo);
				$("#two_payerName").html(data.result.rows.two_payerName);
				$("#two_total_amt").html(data.result.rows.two_total_amt);
				$("#two_deal_status").html(data.result.rows.two_deal_status);
				$("#two_reversal_status").html(data.result.rows.two_reversal_status);
				if (!data.result.rows.two_bus_seq||!data.result.rows.two_sys_seq) {
					$(".two").css("display","none");
				}
				
				// 萨摩机构待清算户转客户信用卡
				$("#in_bus_seq").html(data.result.rows.in_bus_seq);
				$("#in_sys_seq").html(data.result.rows.in_sys_seq);
				$("#in_webank_acct").html(data.result.rows.in_webank_acct);
				$("#in_webank_acct_name").html(data.result.rows.in_webank_acct_name);
				$("#in_trans_amt").html(data.result.rows.in_trans_amt);
				$("#in_deal_status").html(data.result.rows.in_deal_status);
				if (!data.result.rows.in_bus_seq||!data.result.rows.in_sys_seq) {
					$(".in").css("display","none");
				}
			} else {
				$.messager.alert('提示', "交易详情失败", 'error');
			}
		}, function() {
			$.messager.alert('提示', "系统调用失败，请稍后再试", 'error');
		});
}

WB.WFTS.HandleRefund.queryEvt = function(argument) {
	var refundObj = WB.WFTS.HandleRefund, commonObj = WB.WFTS.common;

	// 基本查询
	$('#search').click(function() {
		var busSeq = $.trim($('#busSeq').val());
		var webankCardNo = $.trim($('#webankCardNo').val());
		
		var searchData = {};
		searchData.busSeq = busSeq;
		searchData.webankCardNo = webankCardNo;
	
		var start_date = $('#start_date').datebox('getValue');
		var end_date = $('#end_date').datebox('getValue');
		
		if (webankCardNo == "") {
			$.messager.alert('提示', "请输入微众卡号!", 'info');
			return;
		}
		
		if (!(start_date == "" && end_date == "")) {
			if (start_date == "") {
				$.messager.alert('提示', "开始日期不能为空", 'info');
				return;
			} else if (end_date == "") {
				$.messager.alert('提示', "结束日期不能为空", 'info');
				return;
			}
			var tmpDate = commonObj.formatStandardTime(new Date(), 'yyyy-mm-dd');
			var newDate = tmpDate + " 23:59:59";
			var oldDate = tmpDate + " 00:00:00";
			$('#start_date').val(oldDate);
			$('#end_date').val(newDate);
			var beginArr = start_date.split("-");
			var endArr = end_date.split("-");
			var beginDate = new Date(beginArr);
			var endDate = new Date(endArr);
			var endLong = endDate.getTime();
			var beginLong = beginDate.getTime();
			var days = (endLong - beginLong) / (24 * 60 * 60 * 1000);
			if (days > 7) {
				$.messager.alert('提示', "交易日期相差不能超过七天", 'info');
				return;
			}
			var reg = new RegExp("-|:|\\s", "g"); // 创建正则RegExp对象
			var start_date = start_date.replace(reg, "") + "000000"; // 以yyymmdd格式传到后台
			var end_date = end_date.replace(reg, "") + "235959"; // 以yyymmdd格式传到后台
			$('#start_date').val(start_date);
			$('#end_date').val(end_date);
			if (start_date != "" && end_date != "") {
				if (start_date > end_date) {
					$.messager.alert("提示", "条件开始日期不能大于结束日期", "info");
					return;
				}
			}
		}
	
		// 提交数据
		var msgDomArray = $('input[id]', $('#search_keyword')[0]);
		for (var i = 0; i < msgDomArray.length; i++) {
			var tempDom = msgDomArray[i];
			if (tempDom.id != undefined && tempDom.id != "") {
				var tempStr = tempDom.id.replace("search_", "");
				searchData[tempStr] = $.trim($('#' + tempDom.id).val());
			}
		}
		refundObj.curpage = 1;
		refundObj.currows = refundObj.INITROWS;
		var pageOptions = $('#refund_show_data').datagrid('getPager').data("pagination").options;
		pageOptions.pageNumber = refundObj.curpage;
		refundObj.showGridData(searchData);
	});
}

WB.WFTS.HandleRefund.init();