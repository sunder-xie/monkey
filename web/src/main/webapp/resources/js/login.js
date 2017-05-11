$(document).ready(function(){
	var mainIframe = parent.document.getElementById('mainIframe');
	if(mainIframe != null){
		top.location.href = '/loginController/login';
		return;
	}

    backgroundReady();
	handleLogin();
    if($('#message').val() != ''){
		$('.alert-danger', $('.login-form')).show();
	}
});

function handleLogin(){
	$('.login-form').validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			username: {
				required: true
			},
			password: {
				required: true
			},
			remember: {
				required: false
			}
		},

		messages: {
			username: {
				required: "用户名不能为空."
			},
			password: {
				required: "密码不能为空."
			}
		},

		invalidHandler: function (event, validator) { //display error alert on form submit
			$('.alert-danger', $('.login-form')).show();
		},

		highlight: function (element) { // hightlight error inputs
			$(element)
				.closest('.form-group').addClass('has-error'); // set error class to the control group
		},

		success: function (label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},

		errorPlacement: function (error, element) {
			error.insertAfter(element.closest('.input-icon'));
		},

		submitHandler: function (form) {
			form.submit();
		}
	});

	$('.login-form input').keypress(function (e) {
		if (e.which == 13) {
			if ($('.login-form').validate().form()) {
				$('.login-form').submit();
			}
			return false;
		}
	});

}

//背景展示
function backgroundReady(){
	$.backstretch([
		        "../resources/assets/img/bg/1.jpg",
		        "../resources/assets/img/bg/2.jpg",
		        "../resources/assets/img/bg/3.jpg",
		        "../resources/assets/img/bg/4.jpg"
		        ], {
		          fade: 1000,
		          duration: 8000
	});

}

