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

let pass = document.getElementById('password');
let rePass = document.getElementById('re_password');
let submitButton = document.getElementById('submit-btn');
let rePass_form = document.getElementById('rePass-form');
let rpErrorMsg = document.getElementById('rePass-error-message');
let submitMsg = document.getElementById('submit-message');

let check_re_pass = false;




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



  if(check_re_pass == true){
    rePass_form.submit();
  }else{
    submitMsg.innerText="Vui lòng kiểm tra lại!";
    submitMsg.style.color ='#bd2130';
  }
})



