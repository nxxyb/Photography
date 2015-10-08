$(document).ready(function(){
	$('#loginForm').bootstrapValidator({
	 	message: 'This value is not valid',
        feedbackIcons: {
            valid: '',
            invalid: '',
            validating: 'glyphicon glyphicon-refresh'
    	},
    	 fields: {
    		 mobile: {
                validators: {
                    notEmpty: {
                        message: '手机号未填写！'
                    },
                    regexp: {
                        regexp: /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
                        message: '请输入正确手机号！'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '密码未填写！'
                    }
                }
            }
	 	}
	 });
	
	$('#signupForm').bootstrapValidator({
	 	message: 'This value is not valid',
        feedbackIcons: {
            valid: '',
            invalid: '',
            validating: 'glyphicon glyphicon-refresh'
    	},
    	 fields: {
    		 mobile: {
                validators: {
                    notEmpty: {
                        message: '手机号未填写！'
                    },
                    regexp: {
                        regexp: /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
                        message: '请输入正确手机号！'
                    }
                }
            },
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
                        message: '密码未填写！'
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
	
	$('#forgetForm').bootstrapValidator({
	 	message: 'This value is not valid',
        feedbackIcons: {
            valid: '',
            invalid: '',
            validating: 'glyphicon glyphicon-refresh'
    	},
    	 fields: {
    		 mobile: {
                validators: {
                    notEmpty: {
                        message: '手机号未填写！'
                    },
                    regexp: {
                        regexp: /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
                        message: '请输入正确手机号！'
                    }
                }
            },
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