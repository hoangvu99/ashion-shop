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
let checkmail = false;
let check_re_pass = false;

email.addEventListener('input', (e) => {
  if(e.target.value.match(mailformat)){
    
    checkmail = true;
  }else{
   
    checkmail = false;
  }
});


rePass.addEventListener('input', (e) => {
  if(e.target.value.trim == pass.textContent.trim){
    
    check_re_pass = true
  }else {
   
    check_re_pass = false
  }

})

submitButton.addEventListener('click', (e) => {



  if(checkmail == true && check_re_pass == true){
    signup_form.submit();
  }else{
    console.log("false")
  }
})



