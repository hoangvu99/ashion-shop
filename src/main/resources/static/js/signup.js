(function($) {

    $(".toggle-password").click(function() {

        $(this).toggleClass("zmdi-eye zmdi-eye-off");
        var input = $($(this).attr("toggle"));
        if (input.attr("type") == "password") {
          input.attr("type", "text");
        } else {
          input.attr("type", "password");
        }
      });

})(jQuery);

let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

let email = document.getElementById('email')
let pass = document.getElementById('password');
let rePass = document.getElementById('re_password');
let submitButton = document.getElementById('submit-btn');
let signup_form = document.getElementById('signup-form');
let emailErrorMsg = document.getElementById('email-error-message');
let rpErrorMsg = document.getElementById('rePass-error-message');
let submitMsg = document.getElementById('submit-message');
let emailExistMsg = document.getElementById('emailExistMsg');
let checkmail = false;
let check_re_pass = false;

email.addEventListener('input', (e) => {
	if(emailExistMsg != null){
		emailExistMsg.style.display = 'none';
	}
  if(e.target.value.match(mailformat)){
	
    
    checkmail = true;
    emailErrorMsg.innerText = "Email hợp lệ";
    emailErrorMsg.style.color = '#28a745';
  }else{
   
    checkmail = false;
     emailErrorMsg.innerText = "Email không hợp lệ";
     emailErrorMsg.style.color = '#bd2130';
  }
});


rePass.addEventListener('input', (e) => {
	console.log("repass: " +e.target.value);
	console.log("pass: "+pass.value)
  if(e.target.value == pass.value){
    
    check_re_pass = true
     rpErrorMsg.innerText = "Mật khẩu chính xác";
    rpErrorMsg.style.color = '#28a745';
  }else {
   
    check_re_pass = false
    rpErrorMsg.innerText = "Mật khẩu chưa chính xác";
    rpErrorMsg.style.color = '#bd2130';
  }

})

submitButton.addEventListener('click', (e) => {



  if(checkmail == true && check_re_pass == true){
    signup_form.submit();
  }else{
    submitMsg.innerText="Vui lòng kiểm tra lại!";
    submitMsg.style.color ='#bd2130';
  }
})



