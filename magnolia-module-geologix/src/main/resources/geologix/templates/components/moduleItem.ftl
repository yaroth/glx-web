<div class="col-1 box colorbox">
    <div class="box-content">
        <span class="title uppercase">${content.title!}</span>
        <span class="headline">${content.headline!}</span>
        <a class="flowbutton morelink" href="#${content.title?string?lower_case!}">mehr erfahren</a>
    </div>
</div>
<div class="row row-grid" id="${content.title?string?lower_case!}" style="display: none;">
    <div class="col-4 box-flow-content">
        <div class="arrow-down"></div>
        <p><span class="inline-title">${content.inlineTitle!}</span> ${content.text!}</p>
        <div class="close"></div>
    </div>
</div>