[#--
*** json-provider-utils **
is a collection of macros and functions to render pretty structured json from content apps by using cmsfn and damfn.
--]
[#assign CAMERAS_BASE_PATH = "/cameras" /]
[#assign MAKERS_BASE_PATH = "/makers" /]
[#assign undefined ="undefined" /]

[#assign SERVICE_MAKERS = "makers" /]
[#assign SERVICE_CAMERAS = "cameras" /]
[#assign SERVICE_ALLMAKERS = "allMakers" /]
[#assign SERVICE_ALLCAMERAS = "allCameras" /]
[#assign SERVICE_DEFAULT = SERVICE_CAMERAS /]

[#--[#assign listCameras = ctx.listCameras!undefined /]--]
[#assign path = ctx.path!undefined /]
[#assign uuid = ctx.uuid!undefined /]
[#assign service = ctx.service!SERVICE_DEFAULT /]

[#--
*** renderResponse ***
This is the main macro :-)
--]
[#macro renderResponse]
[#if service==SERVICE_ALLCAMERAS]
  [@renderAllCameras /]
[#elseif service==SERVICE_ALLMAKERS]
  [@renderAllMakers /]
[#elseif service==SERVICE_MAKERS && uuid!=undefined]
  [@createJsonForMakerByUuid makerNodeUuid=uuid /]
[#elseif service==SERVICE_MAKERS && path!=undefined]
  [@createJsonForMakerByPath makerPath=path /]
[#elseif service==SERVICE_CAMERAS && uuid!=undefined]
  [@createJsonForCameraByUuid cameraNodeUuid=uuid /]
[#elseif service==SERVICE_CAMERAS && path!=undefined]
  [@createJsonForCameraByPath cameraNodePath=path /]
[#else]
{ "error" : "Missing parameters for service, path or uuid." }
[/#if]
[/#macro]


[#--
*** renderAllCameras ***
Render all cameras as { cameras : [ {}, {}, ... ]
--]
[#macro renderAllCameras]
  [#assign cameraContentMaps = cmsfn.children(cmsfn.contentByPath(CAMERAS_BASE_PATH, "cameracollection"), "mgnl:camera") /]
  [#if cameraContentMaps?has_content]
[
  [#list cameraContentMaps as camContentMap]
  [@createJsonForCameraByContentMap camContentMap /][#if camContentMap_has_next], [/#if]
  [/#list]
]
  [/#if]
[/#macro]

[#--
Render all makers as { cameras : [ {}, {}, ... ]
--]
[#macro renderAllMakers]
  [#assign makerContentMaps = cmsfn.children(cmsfn.contentByPath(MAKERS_BASE_PATH, "cameracollection"), "mgnl:maker") /]
  [#if makerContentMaps?has_content]
[
  [#list makerContentMaps as makerContentMap]
  [@createJsonForMakeraByContentMap makerContentMap /][#if makerContentMap_has_next], [/#if]
  [/#list]
]
  [/#if]
[/#macro]


[#--
*** createJsonForMakerByUuid ***
A macro which renders JSON for a maker-node by a given uuid
--]
[#macro createJsonForMakerByUuid makerNodeUuid]
    [#assign noContentResponse = '{ "error": "no result 555" }'/]
    [#if makerNodeUuid?has_content]
        [#assign contentMap = cmsfn.contentById(makerNodeUuid, "cameracollection") /]
        [#if contentMap?has_content]
            [@createJsonForMakeraByContentMap contentMap /]
        [#else]
        ${noContentResponse}
        [/#if]
    [/#if]
[/#macro]

[#--
*** createJsonForMakerByPath ***
A macro which renders JSON for a maker-node by a given path.
--]
[#macro createJsonForMakerByPath makerPath]
    [#assign noContentResponse = '{ "error": "no result 666" }'/]
    [#if makerPath?has_content]
        [#assign contentMap = cmsfn.contentByPath(MAKERS_BASE_PATH+makerPath, "cameracollection") /]
        [#if contentMap?has_content]
            [@createJsonForMakeraByContentMap contentMap /]
        [#else]
        ${noContentResponse}
        [/#if]
    [#else]
    ${noContentResponse}
    [/#if]
[/#macro]

[#--
*** createJsonForMakeraByContentMap ***
A macro which renders JSON for a maker-node by a given contentMap
--]
[#macro createJsonForMakeraByContentMap _makerContentMap]
    [#assign noContentResponse = '{ "error": "no result 444 (in createJsonForMakeraByContentMap)" }'/]
    [#if _makerContentMap?has_content]
    [#assign makerContentMap = i18nAndEncodeWrapping(_makerContentMap) /]
{
    "path" : "${makerContentMap.@path}",
    "uuid" : "${makerContentMap.@uuid}",
    "nodeName" : "${makerContentMap.@name}",
    "makerPath" : "${makerContentMap.@path?replace(MAKERS_BASE_PATH, "", "f")!""}",
    "name" : "${makerContentMap.name!""}",
    "longName" : "${makerContentMap.longName!""}",
    "history" : "[#compress]${prettifyRichText(makerContentMap.history!"")}[/#compress]",
    "image" : {
  [#if makerContentMap.image?has_content]
        "assetKey" : "${makerContentMap.image}", "link" : "${damfn.getAssetLink(makerContentMap.image)!""}"
  [/#if]
    },
    "categories" : [
      [@renderCategories makerContentMap "categories"/]
    ]
}
    [#else]
    ${noContentResponse}
    [/#if]
[/#macro]


[#--
*** createJsonForCameraByContentMap ***
A macro which renders JSON for a camera-node by a given contentMap
--]
[#macro createJsonForCameraByContentMap _cameraContentMap]
    [#assign noContentResponse = '{ "error": "no result 333" }'/]
    [#if _cameraContentMap?has_content]
    [#assign cameraContentMap = i18nAndEncodeWrapping(_cameraContentMap) /]
{
  "path" : "${cameraContentMap.@path}",
  "uuid" : "${cameraContentMap.@uuid}",
  "nodeName" : "${cameraContentMap.@name}",
  "cameraPath" : "${cameraContentMap.@path?replace(CAMERAS_BASE_PATH, "", "f")}",
  "name" : "${cameraContentMap.name!""}",
  "composedName" : "[@renderCameraModelName cameraContentMap=cameraContentMap/]",
  "maker" : [@createJsonForMakerByUuid cameraContentMap.maker!"" /],
  "description" : "[#compress]${prettifyRichText(cameraContentMap.description!"")}[/#compress]",
  "categories" : [
   [@renderCategories cameraContentMap "categories"/]
  ],
  "images" : [
  [#if cameraContentMap.images?has_content]
      [#assign propertyValuesList = getPropertyValuesList(cmsfn.asJCRNode(cameraContentMap), "images") /]
      [#list propertyValuesList as assetKey]
          [#assign assetLink = damfn.getAssetLink(assetKey) /]
    { "assetKey" : "${assetKey}", "link" : "${assetLink}" }[#if assetKey_has_next], [/#if]
      [/#list]
  [/#if]
  ]
}
    [#else]
    ${noContentResponse}
    [/#if]
[/#macro]


[#--
*** createJsonForCameraByUuid ***
A macro which renders JSON for a camera-node by a given uuid
--]
[#macro createJsonForCameraByUuid cameraNodeUuid]
    [#assign noContentResponse = '{ "error": "no result 111" }'/]
    [#if cameraNodeUuid?has_content]
        [#assign contentMap = cmsfn.contentById(cameraNodeUuid, "cameracollection") /]
        [#if contentMap?has_content]
            [@createJsonForCameraByContentMap contentMap /]
        [#else]
        ${noContentResponse}
        [/#if]
    [/#if]
[/#macro]


[#--
*** createJsonForCameraByPath ***
A macro which renders JSON for a camera-node by a given path.
Parameter cameraPath should start with "/" and will be added to "cameras" which is the CAMERAS_BASE_PATH.
--]
[#macro createJsonForCameraByPath cameraNodePath]
    [#assign noContentResponse = '{ "error": "no result 222" }'/]
    [#if cameraNodePath?has_content]
        [#assign contentMap = cmsfn.contentByPath(CAMERAS_BASE_PATH+cameraNodePath, "cameracollection") /]
        [#if contentMap?has_content]
            [@createJsonForCameraByContentMap contentMap /]
        [#else]
        ${noContentResponse}
        [/#if]
    [#else]
    ${noContentResponse}
    [/#if]
[/#macro]


[#--
*** renderCategories ***
A macro which renders a categories list by the given node (mgnl:camera or mgnl:maker)
and the name of the property which keeps the values-list.
--]
[#macro renderCategories carrierContentMap categoriesPropertyName]
[#--"name" : "${carrierContentMap[categoriesPropertyName]}"--]
  [#if carrierContentMap[categoriesPropertyName]?has_content]
    [#assign propertyValuesList = getPropertyValuesList(cmsfn.asJCRNode(carrierContentMap), categoriesPropertyName) /]
    [#list propertyValuesList as uuid]
      [#assign catContentMap = cmsfn.contentById(uuid, "category")/]
      [#if catContentMap?has_content]
    { "uuid" : "${uuid}" , "name" : "${catContentMap.name}" }[#if uuid_has_next], [/#if]
      [/#if]
    [/#list]
  [/#if]
[/#macro]


[#--
*** getAssembledCameraModelName ***
This function prints the assembled name of a camera which containe the "maker name" and the "camera model name"
--]
[#macro renderCameraModelName cameraContentMap]
    [#compress]
        [#assign result ="" /]
        [#assign cameraName = cameraContentMap.name/]
        [#if cameraContentMap?has_content]
            [#assign makerNodeUuid = cameraContentMap.maker/]
            [#if makerNodeUuid?has_content]
                [#assign makerContentMap = cmsfn.contentById(makerNodeUuid, "cameracollection") /]
                [#if makerContentMap?has_content]
                    [#assign makerName = makerContentMap.name/]
                    [#if cameraName?has_content && makerName?has_content]
                        [#assign result = result + makerName + " " + cameraName /]
                    [/#if]
                [/#if]
            [/#if]
        [/#if]
    ${result}
    [/#compress]
[/#macro]

[#--
*** getPropertyValuesList ***
A function which returns a list of uuids for of a multivalueField storing the values in a single property
--]
[#function getPropertyValuesList carrierNode propertyName]
    [#assign result = [] /]
    [#assign property = carrierNode.getProperty(propertyName) /]
    [#if property?has_content]
        [#assign propertyValuesList = property.getValues() /]
        [#list propertyValuesList as propertyValue]
            [#assign result = result + [propertyValue.getString()] /]
        [/#list]
    [/#if]
    [#return result /]
[/#function]


[#--
*** i18nAndEncodeWrapping ***
This functions makes sure the given contentMap is i18n-wrapped and encodes html (using HTMLEscapingNodeWrapper).
--]
[#function i18nAndEncodeWrapping contentMap]
    [#if contentMap?has_content]
        [#assign node = cmsfn.asJCRNode(contentMap) /]
        [#assign _node = cmsfn.wrapForI18n(node) /]
        [#--[#assign node = cmsfn.encode(node) /]--]
        [#return cmsfn.asContentMap(_node) /]
    [/#if]
    [#return contentMap /]
[/#function]

[#--
*** prettifyRichText ***
This function makes a string originating froma Rich text field json compliant
--]
[#function prettifyRichText str]
    [#if str?has_content]
        [#return str?replace("\"", "\\\"")?replace("'", "\\'")?replace("^\\s+|\\s+$|\\n|\\r", "", "rm")?chop_linebreak /]
    [#else]
        [#return "" /]
    [/#if]
[/#function]