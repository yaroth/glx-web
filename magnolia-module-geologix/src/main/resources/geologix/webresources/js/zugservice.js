var home = new Vue({
    el: '#home',
    data: {
        layout: 'home',
        time: '08:30',
        from: 'Bern',
        to: 'Thun',
        reservationConfirmation: ''
    },
    methods: {
        getZugservices(e) {
            e.preventDefault();
            axios.post('http://localhost:8080/.rest/demo/v1/zugservices', {
                time: this.time,
                from: this.from,
                to: this.to
            })
                .then(response => (blog_list.zugservices = response.data))
                .catch(error => console.log(error));
            this.layout = '';
            blog_list.layout = 'list';
        }
    }
});

var blog_list = new Vue({
        el: '#js-grid-list',
        data: {
            layout: '',
            zugserviceId: '',
            zugservices: ''
        },
        methods: {
            showZugserviceDetail(uuid) {
                this.layout = 'zugservice-detail';
                this.zugserviceId = uuid;
                console.log("Zugservicedetails line clicked, uuid: " + uuid);
            }
            ,
            showSeatDetail() {
                console.log("showSeatDetail line clicked");
            },
            requestReservation(zugUuid, seatId, waggonNumber, from, to) {
                console.log("reservation requested, seat: " + seatId + ", waggon: " + waggonNumber + ", zug uuid: " + zugUuid + ", from: " + from + ", to: " + to);
                axios.post('http://localhost:8080/.rest/demo/v1/reservation', {
                    firstname: 'Gael',
                    lastname: 'Zwirbel',
                    dateOfBirth: '1978-03-24',
                    zugserviceID: zugUuid,
                    wagenNumber: waggonNumber,
                    sitzNumber: seatId,
                    departure: from,
                    destination: to
                })
                    .then(response => (home.reservationConfirmation = response.data))
                    .catch(error => console.log(error))
            },
            backToHome(){
                home.layout = 'home';
                this.layout = '';
            }


        },
        computed: {
            infoRequest: function () {
                return 'Anfrage: ' + home.from + ' - ' + home.to + '  ab: ' + home.time;
            },
            infoTrainDetail: function () {
                for (var i = 0; i < this.zugservices.length; i++) {
                    if (this.zugservices[i].uuid === this.zugserviceId) {
                        let zug = this.zugservices[i];
                        return zug.from + ' ab ' + zug.departure + ' - ' + zug.to + ' an ' + zug.arrival;
                    }
                }

            }
        }
    })
;