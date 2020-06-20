<#macro page>
    <!doctype html>
    <html lang="en">
        <head>
            <title>KTOR App</title>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <link rel="stylesheet" href="/static/uikit.min.css" />
            <script src="/static/uikit.min.js"></script>
            <script src="/static/uikit-icons.min.js"></script>
        </head>
        <body>
                <#include "navbar.ftl">

                 <div class="uk-container">
                    <#nested>
                    <#include "footer.ftl">
                 </div>

                <script
                  src="https://code.jquery.com/jquery-3.1.1.min.js"
                  integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
                  crossorigin="anonymous"></script>
                <script src="semantic/dist/semantic.min.js"></script>
            </body>
    </html>
<#macro>