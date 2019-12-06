<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/geologix/webresources/css/listview.css">
</head>
<body>

<div>

    <div id="home" v-if="layout === 'home'">
        <form @submit="getZugservices">
            Zeit:<br>
            <input type="text" v-model="time" placeholder="Abfahrt (hh:mm)"><br>
            Von:<br>
            <input type="text" v-model="from" placeholder="Start"><br>
            Nach:<br>
            <input type="text" v-model="to" placeholder="Ende"><br>
            <input type="submit" value="Submit">
        </form>
        <div id="reservation-confirmation">
            {{reservationConfirmation}}
        </div>
    </div>

    <section id="js-grid-list" v-if="layout === 'zugservice-detail' || layout === 'list'" class="grid-list" v-cloak>

        <div class="tool-bar">
            <div v-on:click="backToHome" class="home-btn" v-if="layout === 'list'"></div>
            <div v-on:click="layout = 'list'" class="back-icon" v-if="layout === 'zugservice-detail'"  title="Back"></div>
            <div v-if="layout === 'list'" class="infopanel">{{infoRequest}}</div>
            <div v-else-if="layout === 'zugservice-detail'" class="infopanel">{{infoTrainDetail}}</div>
        </div>

        <ul v-if="layout === 'list'" class="list">
            <li v-for="zug in zugservices" v-on:click="showZugserviceDetail(zug.uuid)" class="zugservice">
                <p>{{zug.departure}} {{zug.from}} - {{zug.to}} {{zug.arrival}}</p>
                <p v-for="waggon in zug.zugkomposition">{{waggon.number}}</p>
            </li>
        </ul>
        <ul v-if="layout === 'zugservice-detail'" class="zugservice-detail">
            <template v-for="zug in zugservices">
                <template v-if="zugserviceId === zug.uuid">
                    <li v-for="waggon in zug.zugkomposition" class="waggon">
                        <p>Waggon N°: {{waggon.number}} ({{waggon.wagenplan.description}})</p>
                        <div class="waggon-image">
                            <img v-bind:src="waggon.wagenplan.imageLink">
                            <div class="row-placeholder row1"></div>
                            <div class="row-placeholder row2">
                                <button v-on:click="requestReservation(zug.uuid, 11, waggon.number, zug.from, zug.to)" id="11" type="button" class="col1" :class="setReservation(zug.uuid, waggon.number, 11)" :disabled="setDisabled(zug.uuid, waggon.number, 11)"></button>
                                <button v-on:click="requestReservation(zug.uuid, 13, waggon.number, zug.from, zug.to)" id="13" type="button" class="col2" :class="setReservation(zug.uuid, waggon.number, 13)" :disabled="setDisabled(zug.uuid, waggon.number, 13)"></button>
                                <button type="button" class="col3 invisible"></button>
                                <button v-on:click="requestReservation(zug.uuid, 17, waggon.number, zug.from, zug.to)" id="17" type="button" class="col4" :class="setReservation(zug.uuid, waggon.number, 17)" :disabled="setDisabled(zug.uuid, waggon.number, 17)"></button>
                                <button v-on:click="requestReservation(zug.uuid, 15, waggon.number, zug.from, zug.to)" id="15" type="button" class="col5" :class="setReservation(zug.uuid, waggon.number, 15)" :disabled="setDisabled(zug.uuid, waggon.number, 15)"></button>
                            </div>
                            <div class="row-placeholder row3">
                                <button v-on:click="requestReservation(zug.uuid, 12, waggon.number, zug.from, zug.to)"type="button" id="12" class="col1" :class="setReservation(zug.uuid, waggon.number, 12)" :disabled="setDisabled(zug.uuid, waggon.number, 12)"></button>
                                <button v-on:click="requestReservation(zug.uuid, 14, waggon.number, zug.from, zug.to)"type="button" id="14" class="col2" :class="setReservation(zug.uuid, waggon.number, 14)" :disabled="setDisabled(zug.uuid, waggon.number, 14)"></button>
                                <button type="button" class="col3 invisible"></button>
                                <button v-on:click="requestReservation(zug.uuid, 18, waggon.number, zug.from, zug.to)"type="button" id="18" class="col4" :class="setReservation(zug.uuid, waggon.number, 18)" :disabled="setDisabled(zug.uuid, waggon.number, 18)"></button>
                                <button v-on:click="requestReservation(zug.uuid, 16, waggon.number, zug.from, zug.to)"type="button" id="16" class="col5" :class="setReservation(zug.uuid, waggon.number, 16)" :disabled="setDisabled(zug.uuid, waggon.number, 16)"></button>
                            </div>
                        </div>
                    </li>
                </template>
            </template>
        </ul>
    </section>

</div>

<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="${ctx.contextPath}/.resources/geologix/webresources/js/zugservice.js"></script>

</body>
</html>

