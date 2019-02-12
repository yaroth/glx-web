[#assign target = cmsfn.contentById(content.pageUUID)!]
[#assign targetLink = cmsfn.link(target)!]

<div class='swiper-slide'>
    <a style='z-index: 100' href='${targetLink!}' class='link'>
        <span class='headline'>${content.headline!}</span>
        <a class='morelink' href='${targetLink!}'>mehr erfahren</a>
    </a>
</div>
