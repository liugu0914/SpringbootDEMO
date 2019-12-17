var Login = {
    vars: {
        lowin: document.querySelector('.lowin'),
        lowin_brand: document.querySelector('.lowin-brand'),
        lowin_wrapper: document.querySelector('.lowin-wrapper'),
        lowin_login: document.querySelector('.lowin-login'),
        lowin_wrapper_height: 0,
        login_back_link: document.querySelector('.login-back-link'),
        forgot_link: document.querySelector('.forgot-link'),
        login_link: document.querySelector('.login-link'),
        login_btn: document.querySelector('.login-btn'),
        register_link: document.querySelector('.register-link'),
        password_group: document.querySelector('.password-group'),
        password_group_height: 0,
        lowin_register: document.querySelector('.lowin-register'),
        lowin_footer: document.querySelector('.lowin-footer'),
        box: document.getElementsByClassName('lowin-box'),
        signup: document.querySelector('.lowin-sign-up'),
        option: {}
    },
    register: function (e) {
        Login.vars.lowin_login.className += ' lowin-animated';
        setTimeout(function () {
            Login.vars.lowin_login.style.display = 'none';
        }, 500);
        Login.vars.lowin_register.style.display = 'block';
        Login.vars.lowin_register.className += ' lowin-animated-flip';
        Login.setHeight(Login.vars.lowin_register.offsetHeight + Login.vars.lowin_footer.offsetHeight);
        if (e)
            e.preventDefault();
    },
    login: function (e) {
        Login.vars.lowin_register.classList.remove('lowin-animated-flip');
        Login.vars.lowin_register.className += ' lowin-animated-flipback';
        Login.vars.lowin_login.style.display = 'block';
        Login.vars.lowin_login.classList.remove('lowin-animated');
        Login.vars.lowin_login.className += ' lowin-animatedback';
        setTimeout(function () {
            Login.vars.lowin_register.style.display = 'none';
        }, 500);
        setTimeout(function () {
            Login.vars.lowin_register.classList.remove('lowin-animated-flipback');
            Login.vars.lowin_login.classList.remove('lowin-animatedback');
        }, 1000);
        Login.setHeight(Login.vars.lowin_login.offsetHeight + Login.vars.lowin_footer.offsetHeight);
        if (e)
            e.preventDefault();
    },
    forgot: function (e) {
        Login.vars.password_group.classList += ' lowin-animated';
        Login.vars.login_back_link.style.display = 'block';
        setTimeout(function () {
            Login.vars.login_back_link.style.opacity = 1;
            Login.vars.password_group.style.height = 0;
            Login.vars.password_group.style.margin = 0;
        }, 100);
        Login.vars.login_btn.innerText = 'Forgot Password';
        Login.setHeight(Login.vars.lowin_wrapper_height - Login.vars.password_group_height);
        Login.vars.lowin_login.querySelector('form').setAttribute('action', Login.vars.option.forgot_url);
        if (e)
            e.preventDefault();
    },
    loginback: function (e) {
        Login.vars.password_group.classList.remove('lowin-animated');
        Login.vars.password_group.classList += ' lowin-animated-back';
        Login.vars.password_group.style.display = 'block';
        setTimeout(function () {
            Login.vars.login_back_link.style.opacity = 0;
            Login.vars.password_group.style.height = Login.vars.password_group_height + 'px';
            Login.vars.password_group.style.marginBottom = 30 + 'px';
        }, 100);
        setTimeout(function () {
            Login.vars.login_back_link.style.display = 'none';
            Login.vars.password_group.classList.remove('lowin-animated-back');
        }, 1000);
        Login.vars.login_btn.innerText = 'Sign In';
        Login.vars.lowin_login.querySelector('form').setAttribute('action', Login.vars.option.login_url);
        Login.setHeight(Login.vars.lowin_wrapper_height);
        if (e)
            e.preventDefault();
    },
    setHeight: function (height) {
        Login.vars.lowin_wrapper.style.minHeight = height + 'px';
    },
    brand: function () {
        Login.vars.lowin_brand.classList += ' lowin-animated';
        setTimeout(function () {
            Login.vars.lowin_brand.classList.remove('lowin-animated');
        }, 1000);
    },
    init: function (option) {
        Login.setHeight(Login.vars.box[0].offsetHeight + Login.vars.lowin_footer.offsetHeight);
        Login.vars.password_group.style.height = Login.vars.password_group.offsetHeight + 'px';
        Login.vars.password_group_height = Login.vars.password_group.offsetHeight;
        Login.vars.lowin_wrapper_height = Login.vars.lowin_wrapper.offsetHeight;
        Login.vars.option = option;

        var loginform = $(Login.vars.lowin_login).find('form');
        loginform.find('input').on('change', function () {
            if ($(this).val())
                $(this).parents('div.lowin-group:first').find('span').remove();
        });

        var registerform = $(Login.vars.lowin_register).find('form');
        registerform.find('input').on('change', function () {
            if ($(this).val())
                $(this).parents('div.lowin-group:first').find('span').remove();
        });

        var len = Login.vars.box.length - 1;
        for (var i = 0; i <= len; i++) {
            if (i !== 0) {
                Login.vars.box[i].className += ' lowin-flip';
            }
        }
        Login.vars.forgot_link.addEventListener("click", function (e) {
            Login.forgot(e);
        });
        Login.vars.register_link.addEventListener("click", function (e) {
            Login.brand();
            Login.register(e);
        });
        Login.vars.login_link.addEventListener("click", function (e) {
            Login.brand();
            Login.login(e);
        });
        Login.vars.login_back_link.addEventListener("click", function (e) {
            Login.loginback(e);
        });
        Login.vars.signup.addEventListener("click", function (e) {
            Login.signup(e);
        });
        Login.vars.login_btn.addEventListener("click", function (e) {
            Login.signin(e);
        });
    },
    checkinput: function ($form) {
        var flag = true;
        $form.find('input').each(function () {
            if (!$(this).val()) {
                var group = $(this).parents('div.lowin-group:first');
                if (group.find('span.tip-error').length == 0)
                    group.append("<span class='tip-error'>" + group.find('label b').text() + "不能为空</span>");
                flag = false;
            }
        });
        return flag;
    },
    signin: function (e) {
        e.preventDefault();
        var form = $(Login.vars.lowin_login).find('form');
        var flag = Login.checkinput(form);
        if (flag) {
            // form.submit();
            Ajax.post(Login.vars.option.login_url,form.serialize(),Login.loginsuc,Login.error);
        }
    },
    loginsuc: function (res) {
        if (res.result) {
            window.location.href = res.data ? res.data : "/";
        } else
            Msg.isSuc(res.message, res.result);
    },
    error:function (XMLHttpRequest) {
        console.log(XMLHttpRequest);
    },
    signup: function (e) {
        var form = $(Login.vars.lowin_register).find('form');
        var flag = Login.checkinput(form);
        if (flag) {
            Ajax.post(Login.vars.option.register_url,form.serialize(),Login.registersuc,Login.error);
        }
        e.preventDefault();
    },
    registersuc: function (res) {
        console.log(res);
        if (res.result) {
            Login.login();
            Login.brand();
        }
        Msg.isSuc(res.message, res.result);
    },
}