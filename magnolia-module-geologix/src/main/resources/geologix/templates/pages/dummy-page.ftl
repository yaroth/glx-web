<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/geologix/webresources/css/listview.css">
</head>
<body>

<div>

    <div id="home">
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

    <section id="js-grid-list" class="grid-list" v-cloak>

        <div class="tool-bar">
            <a class="back-icon" v-if="layout === 'zugservice-detail'" v-on:click="layout = 'list'" v-bind:class="{ 'active': layout == 'list'}" title="Back"></a>
            <div v-if="layout === 'list'" class="infopanel">{{infoRequest}}</div>
            <div v-else-if="layout === 'zugservice-detail'" class="infopanel">{{infoTrainDetail}}</div>
        </div>

        <ul v-if="layout === 'list'" class="list">
            <li v-for="zug in zugservices">
                <a v-on:click="showZugserviceDetail(zug.uuid)">
                    <p>{{zug.departure}} {{zug.from}} - {{zug.to}} {{zug.arrival}}</p>
                    <p v-for="waggon in zug.zugkomposition">{{waggon.number}}</p>
                </a>
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
                                <button v-on:click="requestReservation(zug.uuid, 11, waggon.number, zug.from, zug.to)" id="11" type="button" class="col1 ">
                                    Reservieren
                                </button>
                                <button v-on:click="requestReservation(zug.uuid, 13, waggon.number, zug.from, zug.to)" id="13" type="button" class="col2">
                                    Reservieren
                                </button>
                                <button type="button" class="col3 invisible">Reservieren</button>
                                <button v-on:click="requestReservation(zug.uuid, 17, waggon.number, zug.from, zug.to)" id="17" type="button" class="col4 ">
                                    Reservieren
                                </button>
                                <button v-on:click="requestReservation(zug.uuid, 15, waggon.number, zug.from, zug.to)" id="15" type="button" class="col5 reserved">
                                    Reservieren
                                </button>
                            </div>
                            <div class="row-placeholder row3">
                                <button type="button" class="col1 ">Reservieren</button>
                                <button type="button" class="col2 reserved">Reservieren</button>
                                <button type="button" class="col3 invisible">Reservieren</button>
                                <button type="button" class="col4 reserved">Reservieren</button>
                                <button type="button" class="col5 reserved">Reservieren</button>
                            </div>
                        </div>
                    </li>
                </template>
            </template>
        </ul>
    </section>



    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script src="${ctx.contextPath}/.resources/geologix/webresources/js/zugservice.js"></script>

</body>
</html>

