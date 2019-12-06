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
            axios.post(location.protocol + '//' + location.host + '/.rest/demo/v1/zugservices', {
                time: this.time,
                from: this.from,
                to: this.to
            })
                .then(response => {
                    blog_list.zugservices = '';
                    blog_list.zugservices = response.data;
                })
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
            axios.post(location.protocol + '//' + location.host + '/.rest/demo/v1/reservation', {
                firstname: 'Gael',
                lastname: 'Zwirbel',
                dateOfBirth: '1978-03-24',
                zugserviceID: zugUuid,
                wagenNumber: waggonNumber,
                sitzNumber: seatId,
                departure: from,
                destination: to
            })
                .then(response => {
                    home.reservationConfirmation = response.data;
                    let resConf = home.reservationConfirmation;
                    if (resConf.message == 'OK') {
                        let zugId = resConf.zugserviceID;
                        let wagNb = resConf.wagenNumber;
                        let sitzNb = resConf.sitzNumber;
                        let seat = this.getSeat(zugId, wagNb, sitzNb);
                        if (seat !== undefined) {
                            seat.reserved = true;
                        }
                    }
                })
                .catch(error => console.log(error));
        },
        backToHome() {
            home.layout = 'home';
            this.layout = '';
        },
        setReservation(zugUuid, waggonNb, seatNb) {
            let seat = this.getSeat(zugUuid, waggonNb, seatNb);
            if (seat !== undefined && seat.reserved) return 'reserved';
        },
        setDisabled(zugUuid, waggonNb, seatNb) {
            let seat = this.getSeat(zugUuid, waggonNb, seatNb);
            if (seat !== undefined && seat.reserved) return 'disabled';
        },
        getSeat(zugUuid, waggonNb, seatNb) {
            let wagenNbInt = parseInt(waggonNb);
            let seatNbInt = parseInt(seatNb);
            let trains = this.zugservices;
            for (let i = 0; i < trains.length; i++) {
                if (trains[i].uuid === zugUuid) {
                    let waggons = trains[i].zugkomposition;
                    for (let j = 0; j < waggons.length; j++) {
                        if (parseInt(waggons[j].number) === wagenNbInt) {
                            let seats = waggons[j].wagenplan.seats;
                            for (let k = 0; k < seats.length; k++) {
                                if (parseInt(seats[k].id) === seatNbInt) {
                                    return seats[k];
                                }
                            }
                        }
                    }
                }
            }
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
});