/**
 * 表单验证 for jQuery
 * 周帆 20080910
 *
 * $('input[@name=txtLoginName]').validation({rule:'required'} ,  '登录名必填');
 */

var validation_ajax_count	= 0;
jQuery.fn.validation	= function (options , error , description) {
	
	var rules	= {
		required	: "^.+$",  //必填
		password	: "^.+$",  //密码
		loginname	: "^[a-z][a-z0-9-_.]+$",  //登录名
		email		: "^[a-z0-9][a-z0-9.+]+[@][a-z0-9-]+[.][a-z]",  //电子邮件
		phone		: "^([0-9]{0}|[0-9]{3,4})[-]{0,1}[0-9]{7,8}$",  //固话
		cellphone	: "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$",  //手机
		zip			: "^[0-9]{6}$", //邮编
		numeric		: "^[0-9]+$",  //数字
		idcard		: "^([0-9]{15}|[0-9]{18})$",  //身份证
		maxlenght   : "^.{1,50}$"  //1到50
	}
	
	var el	= $(this);
	
	if (description) {
		el.after($('<span class="validationDescription">' + description + '</span>'));
	}
	
	// 获得焦点时
	$(this).focus(function() {
		// 清除错误提示
		el.next('span.validationError').remove();
		el.next('span.validationDescription').show();
		el.removeClass('validationError');
	})
	
	$(this).blur(function() {				// 失去焦点时
		return validation_check(el , rules , options , error);
	}).parents("form").submit(function() {	// 表单提交时		
		return validation_check(el , rules , options , error);
	})
	
	return $(this);
	
}

function validation_check(el , rules , options , error) {
	// 如果当前表单项已有错误消息，则不再检查
	if (el.attr('class') == 'validationError') {
		return false;
	}
	
	var result	= null
	var validation_value	= $.trim(el.val());
	el.val(validation_value);
	
	// 检查是否有自定函数
	eval('function_exist = typeof validation_rule_' + options.rule);
	
	// 有自定义函数则通过自定义函数匹配
	if (function_exist == 'function') {
		result	= validation_check_rule(el , options , error);
		if (typeof result != 'boolean') {
			return false;
		}
	} else if (!validation_value && options.rule != 'required' && options.required != true) {
		return true;
	} else if (typeof options.check != 'undefined') {
		// 若定义了检查指定对象
		result	= $.trim($(options.check).val())?true:false;
	} else {
		eval('validation_patrn = rules.' + options.rule  + ';');
		var reg	= new RegExp(validation_patrn , "gi");
		result	= reg.test(validation_value)?true:false;
	}
	
	return validation_after_check(el, result , error);
	
}

function validation_after_check(el , result , error) {
	
	if (!result) {
		el.next('span.validationError').remove();
		el.next('span.validationDescription').hide();
		el.after($('<span class="validationError">' + error + ' ' + el.next('span.validationDescription').text() + '</span>'));
		el.addClass('validationError');
	} else {
		el.next('span.validationError').remove();
		el.next('span.validationDescription').show();
		el.removeClass('validationError');
		
		// 还有ajax验证未返回
		//if (validation_ajax_count) {
		//	return false;
		//}
		
		return true;
	}
	
	return false;
}

function validation_check_rule(el , options , error) {
	eval('validation_patrn = validation_rule_' + options.rule  + '(el , options , error);');
	return validation_patrn;
}

/**
 * 验证两次输入是否一致
 * @param {Object} el
 * @param {Object} options
 */
function validation_rule_match(el , options) {
	return (el.val() == $(options.el).val())?true:false;
}

/**
 * 验证两次输入是否不一致
 * @param {Object} el
 * @param {Object} options
 */
function validation_rule_unmatch(el , options) {
	return (el.val() == $(options.el).val())?false:true;
}

function validation_rule_exist(el , options , error) {
	validation_ajax_count++;
	validation_after_check(el , false , '请稍候……');
	
	$.getJSON(options.url , {data:el.val()} , function(json) {
		validation_ajax_count--;
		if (json) {
			var result	= (json.result == 1)?false:true;
			validation_after_check(el , result , error);
			
			/*
			if (result && validation_ajax_count == 0) {
				if ($('span.validationError').length == 0) {
					el.parents('form').unbind('submit').submit();
				}
			}
			*/
		}
		
	})
	
	return true;
	//return true;
}