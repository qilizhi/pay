var Time = 60, TimerID;
var profile = {
	init: function(){
//		this.login();
//		this.register();
//		this.password();
//		this.comment();
//		this.changePassword();
//		this.deleteConfirm();
		this.casher();
	},
	login: function(){
		if( $(".login").size() > 0 ){
			var mobile = $(".login").find("input[name = loginName]");
			var password = $(".login").find("input[name = loginPwd]");
			if(window.localStorage){
				mobile.val( localStorage.getItem("mobile") );
			}
			$(".button").on("click", function(){
				
				if( !(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile.val())) ){
					profile.msg(mobile, "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801");
					mobile.focus();
					return false;
				}
				profile.msg(mobile, "");
				if( password.val().trim().length == 0 ){
					profile.msg(password, "\u8bf7\u8f93\u5165\u5bc6\u7801");
					password.focus();
					return false;
				}
				profile.msg(password, "");
				$("form").submit();
			});
		}
	},
	register: function(){
		if( $(".register").size() > 0 ){
			checkTimer();
			var mobile = $(".register").find("input[name = loginName]");
			var code = $(".register").find("input[name = code]");
			var password = $(".register").find("input[name = pwd]");
			var pwd = $(".register").find("input[name = loginPwd]");
			//fn
			function check_mobile(){
				var SIGN = false;
				if( !(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile.val())) ){
					profile.msg(mobile, "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801");
					mobile.focus();
					SIGN = false;
				}else{
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'mobile=' + mobile.val(),
						async: false,
						url: 'ismobile',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) != 0 ){
								profile.msg(mobile, "\u6b64\u53f7\u7801\u5df2\u7ecf\u6ce8\u518c");
								mobile.focus();
								SIGN = false;
							}else{
								profile.msg(mobile, "");
								SIGN = true;
							}
						}
					});
				}
				return SIGN;
			};
			function check_code(){
				var SIGN = false;
				if( code.val().trim().length < 4 ){
					profile.msg(code, "\u8bf7\u8f93\u51654\u4f4d\u9a8c\u8bc1\u7801");
					code.focus();
					return false;
				}else{
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'randomKey=' + code.val() + '&mobile=' + mobile.val(),
						async: false,
						url: 'iscaptcha',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) != 1 ){
								profile.msg(code, "\u9a8c\u8bc1\u7801\u9519\u8bef");
								code.focus();
								SIGN = false;
							}else{
								profile.msg(code, "");
								SIGN = true;
							}
						}
					});
				}
				return SIGN;
			};
			function check_password(){
				if( password.val().trim().length < 6 ){
					profile.msg(password, "\u5bc6\u7801\u4e0d\u80fd\u5c11\u4e8e\u516d\u4f4d");
					password.focus();
					return false;
				}else{
					profile.msg(password, "");
					if( password.val().trim() != pwd.val().trim() ){
						profile.msg(pwd, "\u4e24\u6b21\u5bc6\u7801\u8f93\u5165\u4e0d\u540c");
						pwd.focus();
						return false;
					}else{
						profile.msg(pwd, "");
						return true;
					}
				}
			};
			// check
			$(".button").on("click", function(){
				if(check_mobile()){
					if(check_code()){
						if(check_password()){
							$("form").submit();
						}
					}
				}
			});
			$(".get").on("click", function(){
				if(check_mobile()){
					$(".get").attr('disabled', 'disabled');
					$("input[name = loginName]").attr('readonly', 'readonly');
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'mobile=' + $("input[name = loginName]").val(),
						url: 'captcha',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) === 1 ){
								var d = new Date(), s = d.getTime();
								$.cookie('code_mobile', $("input[name = loginName]").val());
								$.cookie('code_time', Math.round(s/1000));
								$("input[name = code]").focus();
								TimerID = setInterval('Timer()', 1000);
							}
						}
					});
				}
			});
		}
	},
	password: function(){
		if( $(".password").size() > 0 ){
			checkTimer();
			var mobile = $(".phone").find("input[name = phone]");
			var code = $(".password").find("input[name = code]");
			var password = $(".password").find("input[name = loginPwd]");
			var pwd = $(".password").find("input[name = pwd]");
			//fn
			function check_mobile(){
				var SIGN = false;
				if( !(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile.val())) ){
					profile.msg(mobile, "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801");
					mobile.focus();
					SIGN = false;
				}else{
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'mobile=' + mobile.val(),
						async: false,
						url: 'ismobile',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) == 0 ){
								profile.msg(mobile, "\u6b64\u624b\u673a\u53f7\u5c1a\u672a\u6ce8\u518c");
								mobile.focus();
								SIGN = false;
							}else{
								profile.msg(mobile, "");
								SIGN = true;
							}
						}
					});
				}
				return SIGN;
			};
			function check_code(){
				var SIGN = false;
				if( code.val().trim().length < 4 ){
					profile.msg(code, "\u8bf7\u8f93\u51654\u4f4d\u9a8c\u8bc1\u7801");
					code.focus();
					return false;
				}else{
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'randomKey=' + code.val() + '&mobile=' + mobile.val(),
						async: false,
						url: 'iscaptcha',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) != 1 ){
								profile.msg(code, "\u9a8c\u8bc1\u7801\u9519\u8bef");
								code.focus();
								SIGN = false;
							}else{
								profile.msg(code, "");
								SIGN = true;
							}
						}
					});
				}
				return SIGN;
			};
			function check_password(){
				if( password.val().trim().length < 6 ){
					profile.msg(password, "\u5bc6\u7801\u4e0d\u80fd\u5c11\u4e8e\u516d\u4f4d");
					password.focus();
					return false;
				}else{
					profile.msg(password, "");
					if( password.val().trim() != pwd.val().trim() ){
						profile.msg(pwd, "\u4e24\u6b21\u5bc6\u7801\u8f93\u5165\u4e0d\u540c");
						pwd.focus();
						return false;
					}else{
						profile.msg(pwd, "");
						return true;
					}
				}
			};
			// check
			$(".button").on("click", function(){
				if(check_mobile()){
					if(check_code()){
						if(check_password()){
							$("form").submit();
						}
					}
				}
			});
			$(".get").on("click", function(){
				if(check_mobile()){
					$(".get").attr('disabled', 'disabled');
					$("input[name = loginName]").attr('readonly', 'readonly');
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'mobile=' + $("input[name = loginName]").val(),
						url: 'captcha',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) === 1 ){
								var d = new Date(), s = d.getTime();
								$.cookie('code_mobile', $("input[name = loginName]").val());
								$.cookie('code_time', Math.round(s/1000));
								$("input[name = code]").focus();
								TimerID = setInterval('Timer()', 1000);
							}
						}
					});
				}
			});
		}
	},
	comment: function(){
		if( $(".comment").size() > 0 ){
			var comment = $(".comment").find("textarea");
			$(".button").on("click", function(){
				if( comment.val().length < 5 ){
					comment.prev("h3").addClass("red");
					comment.focus();
					return false;
				}
				comment.prev("h3").removeClass("red");
				$("form").submit();
			});
		}
	},
	changePassword: function(){
		if( $(".info").size() > 0 ){
			var opwd = $(".info").find("input[name = opwd]");
			var npwd = $(".info").find("input[name = npwd]");
			var cpwd = $(".info").find("input[name = cpwd]");
			$(".button").on("click", function(){
				if( opwd.val().trim().length < 6 ){
					profile.msg(opwd, "\u539f\u5bc6\u7801\u4e0d\u5c11\u4e8e\u516d\u4f4d");
					opwd.focus();
					return false;
				}
				profile.msg(opwd, "");
				if( npwd.val().trim().length < 6 ){
					profile.msg(npwd, "\u65b0\u5bc6\u7801\u4e0d\u80fd\u5c11\u4e8e\u516d\u4f4d");
					npwd.focus();
					return false;
				}
				profile.msg(npwd, "");
				if( npwd.val().trim() != cpwd.val().trim() ){
					profile.msg(cpwd, "\u4e24\u6b21\u65b0\u5bc6\u7801\u8f93\u5165\u4e0d\u540c");
					cpwd.focus();
					return false;
				}
				profile.msg(cpwd, "");
				$("form").submit();
			});
		}
	},
	casher: function(){
		if( $(".cashier").size() > 0 ){
			checkTimer();
			var mobile = $(".cashier").find("input[name = phone]");
			var code = $(".cashier").find("input[name = code]");
			var password = $(".cashier").find("input[name = pwd]");

			//fn
			function check_mobile(){
				var SIGN = true;
				if( !(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile.val())) ){
					profile.msg(mobile, "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801");
					mobile.focus();
					SIGN = false;
				}
				return SIGN;
			};
			function check_code(){
				var SIGN = false;
				if( code.val().trim().length < 4 ){
					profile.msg(code, "\u8bf7\u8f93\u51654\u4f4d\u9a8c\u8bc1\u7801");
					code.focus();
					return false;
				}else{
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'randomKey=' + code.val() + '&mobile=' + mobile.val(),
						async: false,
						url: 'iscaptcha',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) != 1 ){
								profile.msg(code, "\u9a8c\u8bc1\u7801\u9519\u8bef");
								code.focus();
								SIGN = false;
							}else{
								profile.msg(code, "");
								SIGN = true;
							}
						}
					});
				}
				return SIGN;
			};

			// check
			$(".button").on("click", function(){
				if(check_mobile()){
					if(check_code()){
							$("form").submit();
					}
				}
			});
			$(".get").on("click", function(){
				if(check_mobile()){
					$(".get").attr('disabled', 'disabled');
					$("input[name = phone]").attr('readonly', 'readonly');
					//TimerID = setInterval('Timer()', 1000);
					InAjaxLoader();
					$.ajax({
						type: 'POST',
						data: 'mobile=' + $("input[name = phone]").val(),
						url: 'captcha',
						success: function(data){
							UnAjaxLoader();
							if( parseInt(data, 10) === 1 ){
								var d = new Date(), s = d.getTime();
								$.cookie('code_mobile', $("input[name = phone]").val());
								$.cookie('code_time', Math.round(s/1000));
								$("input[name = code]").focus();
								TimerID = setInterval('Timer()', 1000);
							}
						}
					});
				}
			});
		}
	},
	msg: function(elem, txt){
		if(txt.trim().length == 0){
			$(elem).parents(".t").next(".err").empty().hide();
		}else{
			$(elem).parents(".t").next(".err").html(txt).show();
			$(elem).parents(".t").siblings(".t").next(".err").empty().hide();
		}
	},
	deleteConfirm: function(){
		$("a.delete, a.del").on("click", function(e){
			if( !confirm("\u771f\u7684\u8981\u53d6\u6d88\u5417\uff1f") ){
				e.preventDefault();
			}	
		});	
	}
};

function checkTimer(){
	var d = new Date(), c = d.getTime(), s = parseInt($.cookie('code_time'));
	var t = Math.round(c/1000) - s;
	var _1 = $("input[name = phone]");
	if( t < 60 ){
		Time = parseInt( Time - t );
		_1.val( $.cookie('code_mobile') ).attr('readonly', 'readonly');
		$('.get').attr('disabled', 'disabled');
		TimerID = setInterval('Timer()', 1000);
	}
}

function Timer(){
	Time -= 1;
	$('.get').val(Time + "\u79d2\u540e\u518d\u83b7\u53d6");
	if( Time == 0 ){
		Time = 60;
		$('.get').val("\u83b7\u53d6\u9a8c\u8bc1\u7801").removeAttr('disabled');
		$("input[name = phone]").removeAttr("readonly");
		clearInterval(TimerID);
	}
}

$(function(){
	profile.init();
});