<#macro form title="" isRegistration=false>
    <div class="form-header">
        <a href="/">
            <svg width="3em" height="3em" viewBox="0 0 16 16" class="bi bi-arrow-left-short" fill="currentColor"
                 xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd"
                      d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5z"/>
            </svg>
        </a>
        <h3>${title}</h3>
    </div>
    <form method="post" action="${isRegistration?string("/auth/registration", "/auth/login")}">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <div class="form-group">
            <label for="exampleInputUsername1">Username</label>
            <input id="exampleInputUsername1" type="text" class="form-control"
                   name="username" placeholder="Username..." value="<#if login??>${login}</#if>">
            <#if usernameError??>
                <div class="validation-error">${usernameError}</div>
            </#if>
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">Password</label>
            <input type="password" class="form-control" name="password" placeholder="Password..."
                   id="exampleInputPassword1">
            <#if passwordError??>
                <div class="validation-error">
                    ${passwordError}
                </div>
            </#if>
        </div>
        <#if isRegistration>
            <div class="form-group">
                <label for="exampleInputPassword2">Confirm Password</label>
                <input type="password" class="form-control" name="confirmPassword" placeholder="Confirm password..."
                       id="exampleInputPassword2">
                <#if confirmPasswordError??>
                    <div class="validation-error">
                        ${confirmPasswordError}
                    </div>
                </#if>
            </div>
        </#if>
        <#if isRegistration>
            <div class="form-group">
                <label for="exampleInputEmail">Email</label>
                <input type="email" class="form-control" name="email" placeholder="Email..."
                       id="exampleInputEmail" aria-describedby="usernameHelp">
                <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone
                    else.</small>
                <#if emailError??>
                    <div class="validation-error">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </#if>
        <#if isRegistration>
            <div class="g-recaptcha" data-sitekey="6LcKRhQaAAAAANbbGuCl5n7IFg-k_O-FPILSRfWo"></div>
            <br/>
        </#if>
        <#if isRegistration>
            <button type="submit" class="btn btn-primary">Sign Up</button>
        <#else>
            <button type="submit" value="Submit" class="btn btn-primary">Sign In</button>
        </#if>
    </form>
</#macro>


