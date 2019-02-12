
[#assign member = cmsfn.contentById(content.memberUUID!,"contacts") /]

<div class="col-1 box emp-box">
    <div class="box-content bg" style="background-image:url(${damfn.getAssetLink(member.image!)!})"></div>
    <div class="emp-details bg" style="background-image: url(${damfn.getAssetLink(member.imageHover!)!}); display: none; opacity:1;">
        <span class="name">${member.firstName!}<br>${member.lastName!}</span>
        <span class="subline">${member.education!}<br>Leiter Softwareentwicklung</span>
        <span class="spacer">—</span>
        <span class="bottom">Tel. ${member.officePhoneNr!} <br>
            <a class="emo_email" href="mailto:ch.kaufmann@geologix.ch">E-Mail</a>
        </span>
    </div>
</div>