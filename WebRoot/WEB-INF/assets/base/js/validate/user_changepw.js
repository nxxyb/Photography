$(document).ready(function(){
	$('#changepwForm').bootstrapValidator({
	 	message: 'This value is not valid',
        feedbackIcons: {
            valid: '',
            invalid: '',
            validating: 'glyphicon glyphicon-refresh'
    	},
    	 fields: {
            verifyCode: {
                validators: {
                    notEmpty: {
                        message: '验证码未填写！'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '修改密码未填写！'
                    }
                }
            },
            confirmPassword: {
                validators: {
                    notEmpty: {
                        message: '确认密码未填写！'
                    },
                    identical: {
                        field: 'password',
                        message: '二次输入的密码不一致！'
                    }
                }
            }
	 	}
	 });
});