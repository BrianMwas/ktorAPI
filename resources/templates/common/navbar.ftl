<div class="ui secondary pointing menu">
  <a href="/" class="active item">
    Home
  </a>
  <a  href="/about" class="item">
    About
  </a>
  <a href="blogs" class="tem">
          Blogs
   </a>

  <div class="right menu">
  <#if user??>
    <p>${ user.displayName }</p>
    <a href="/logout" class="ui item">
       Log Out
    </a>
    <#else>
        <a href="/signin" class="ui item">
            Sign in
         </a>
         <a href="blogs" class="ui item">
             Sign Up
          </a>
    </else>
  </#if>

  </div>
</div>

<nav class="uk-navbar-container" uk-navbar>
    <div class="uk-navbar-left">

        <ul class="uk-navbar-nav">
            <li class="uk-active">
                <a href="/">
                  Home
                </a>
              </li>
            <li>
                <a href="#">Our links</a>
                <div class="uk-navbar-dropdown">
                    <ul class="uk-nav uk-navbar-dropdown-nav">
                        <li class="uk-active">
                             <a  href="/about" class="item">
                                About
                              </a>
                        </li>
                        <li>
                            <a href="blogs" class="tem">
                                  Blogs
                           </a>
                        </li>

                    </ul>
                </div>
            </li>
            <#if user??>
                <p>${ user.displayName }</p>
                <a href="/logout" class="ui item">
                   Log Out
                </a>
                <#else>
                    <li>
                        <a href="/signin" class="ui item">
                            Sign in
                        </a>
                     </li>
                     <li>
                         <a href="/signup" class="ui item">
                             Sign Up
                         </a>
                      </li>
                </else>
              </#if>

        </ul>

    </div>
</nav>