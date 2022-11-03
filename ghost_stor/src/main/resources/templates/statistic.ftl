<#import "parts/common.ftl" as c>

<@c.page>
    <div align="center" id="frames">
        <div class="about">
            <iframe width="500" height="500" src=f"${url!}/counts"></iframe>
        </div>
        <div class="about">
            <iframe width="500" height="500" src=f"${url!}/stats"></iframe>
        </div>
    </div>
</@c.page>