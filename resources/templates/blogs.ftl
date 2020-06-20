<#import "common/semantic.ftl" as b>

<@b.page>
    <#if blogs?? && ( blogs?size > 0 )>
        <#list blogs as blog>
            <div class="uk-card uk-card-small uk-card-default uk-card-body uk-width-1-2@m">
                <h3 class="uk-card-title">${blog.title}</h3>
                <p>${ blog.content }</p>
            </div>
        </#list>
    </#if>
    <form method="post" action="/blogs">
        <fieldset class="uk-fieldset">

            <legend class="uk-legend">Legend</legend>

            <div class="uk-margin">
                <input class="uk-input" type="text" placeholder="Input">
            </div>

            <div class="uk-margin">
                <textarea class="uk-textarea" rows="5" placeholder="Textarea"></textarea>
            </div>

           <button class="uk-button uk-button-primary">Add blog</button>
     </form>
</@b.page>