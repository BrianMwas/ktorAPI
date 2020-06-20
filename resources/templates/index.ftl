<#-- @ftlvariable name="data" type="com.ktor.api.IndexData" -->
<html>
    <body>
        <ul>
        <#list blogs as blog>
            <li>${blog.title}</li>
            <p>${ blog.summary }</p>
            <p>${ blog.content }</p>
        </#list>
        </ul>
    </body>
</html>
