var home = new Vue({
    el: '#home',
    data: {
        layout: 'home',
        ruleForm: {
            time: '08:30',
            from: 'Bern',
            to: 'Thun'
        },
        //Rules for the field validations
        rules: {
            time: [
                {type: 'date', required: true, message: 'Bitte tragen Sie die Abfahrtszeit ein.', trigger: 'blur'},
            ],
            from: [
                {type: 'string', required: true, message: 'Bitte tragen Sie den Abfahrtsort ein.', trigger: 'blur'},
            ],
            to: [
                {type: 'string', required: true, message: 'Bitte tragen Sie den Zielort ein.', trigger: 'blur'},
            ]
        }
    },
    methods: {
        getZugservices(formName) {
            //Checks if the filled in data from the form is correct.
            //according to the result the REST post is executed
            //or it shows the necessary fields who still neededs to be filled.
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //REST post to get the trainservices.
                    //Removes spaces in the from/to strings for correct requesting
                    axios.post(location.protocol + '//' + location.host + '/.rest/bls/v1/zugservices', {
                        time: this.correctTimeFormat(this.ruleForm.time),
                        from: this.ruleForm.from.replace(/\s+/g, ''),
                        to: this.ruleForm.to.replace(/\s+/g, '')
                    })
                        .then(response => {
                            blog_list.zugservices = '';
                            blog_list.zugservices = response.data;
                        })
                        .catch(error => {
                            blog_list.zugservices = '';
                            console.log(error);
                        });
                    this.layout = '';
                    blog_list.layout = 'list';
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });

        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        //Converts entered time into correct format for the request
        correctTimeFormat(enteredTime) {
            var correctTimeDisplay = enteredTime.getHours();
            if(enteredTime.getMinutes() < 10){
                correctTimeDisplay = correctTimeDisplay + ':' + '0' + enteredTime.getMinutes();
            } else {
                correctTimeDisplay = correctTimeDisplay + ':' + enteredTime.getMinutes();
            }
            if(enteredTime.getHours() < 10) {
                return '0' + correctTimeDisplay;
            } else {
                return correctTimeDisplay;
            }
        }
    }
});

var blog_list = new Vue({
        el: '#js-grid-list',
        data: {
            layout: '',
            allowDateCheck: true,
            zugserviceId: '',
            zugservices: '',
            reservationStatus: ''
        },
        methods: {
            showZugserviceDetail(uuid) {
                this.layout = 'zugservice-detail';
                this.zugserviceId = uuid;
            },
            showSeatDetail() {
                console.log("showSeatDetail line clicked");
            },
            requestReservation(zugUuid, seatId, waggonNumber, from, to, date) {
                var seat = this.getSeat(zugUuid, waggonNumber, seatId);
                var seatInformation = this.reservationSeatText(seat);
                Swal.mixin({
                    confirmButtonText: 'Weiter',
                    confirmButtonColor: '#a0e100',
                    customClass: 'reservation-modal',
                    showCancelButton: true,
                    progressSteps: ['1', '2']
                }).queue([
                    {
                        title: 'Sitzübersicht',
                        html: seatInformation
                    },
                    {
                        title: 'Personalien eingeben zur Bestätigung',
                        html:  '<input id="swal-input1" placeholder="Name" class="swal2-input">' +
                            '<input id="swal-input2" placeholder="Vorname" class="swal2-input">' +
                            '<input id="swal-input3" type="date" placeholder="Geburtsdatum" class="swal2-input">',
                        preConfirm: () => {
                            this.lastName = document.getElementById('swal-input1').value;
                            this.firstName = document.getElementById('swal-input2').value;
                            this.birthDate = document.getElementById('swal-input3').value;
                        }
                    }
                ])
                .then((result) => {
                    if (result.value) {
                        axios.post(location.protocol + '//' + location.host + '/.rest/bls/v1/reservation', {
                            firstname: this.firstName,
                            lastname: this.lastName,
                            dateOfBirth: this.birthDate,
                            zugserviceID: zugUuid,
                            wagenNumber: waggonNumber,
                            sitzNumber: seatId,
                            departure: from,
                            destination: to,
                            date: this.getDate(date)
                        })
                            .then(response => {
                                this.reservationStatus = response.data;
                                let resConf = this.reservationStatus;
                                if (resConf.message === 'OK') {
                                    let zugId = resConf.zugserviceID;
                                    let wagNb = resConf.wagenNumber;
                                    let sitzNb = resConf.sitzNumber;
                                    let seat = this.getSeat(zugId, wagNb, sitzNb);
                                    if (seat !== undefined) {
                                        seat.reserved = true;
                                        //Shows confirmation of reservation
                                        this.reservationConfirmationModal(this.firstName, this.lastName,
                                            this.reservationStatus.departure, this.reservationStatus.destination, wagNb, sitzNb);
                                    }
                                }
                            })
                            .catch(error => console.log(error));
                    }
                })
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
            },
            //generates the multiline text for the seat information modal
            reservationSeatText(seat){
                //For the Locations of the seats
                var seatLocation;
                var optionsList;
                if(seat.location === 'fenster'){
                    seatLocation = 'Fenstersitz';
                } else if(seat.location === 'gang'){
                    seatLocation = 'Gangsitz';
                } else if(seat.location === 'mitte'){
                    seatLocation = 'Mittlerer Sitz';
                } else{
                    seatLocation = '';
                }
                //For the options of the seat
                if(seat.options.length < 1){
                    optionsList = 'Kein Zubehör';
                } else if (seat.options.length === 1){
                    optionsList = seat.options[0];
                    optionsList = optionsList.replace("table", "Tisch");
                } else {
                    optionsList = seat.options.join(' / ');
                    optionsList = optionsList.replace("table", "Tisch");
                }
                //The text, which is shown in the modal
                var span = document.createElement('span');
                span.innerHTML = 'Klasse: ' + seat.klasse + '. Klasse' + '<br>'
                    + 'Platz: ' + seatLocation + '<br>'
                    + 'Zubehör: ' + optionsList + '<br>';
                return span;
            },
            //makes a multiline text for the reservation confirmation text in the sweetalert2 confirmation window
            reservationConfirmationModal(fName, lName, departure, destination, wagNb, seatNb) {
                var span = document.createElement("span");
                span.innerHTML = 'Benutzer: ' + fName + ' ' + lName + '<br>'
                    + 'Strecke: ' + this.reservationStatus.departure + '-' + this.reservationStatus.destination + '<br>'
                    + 'Wagen: ' + wagNb + ' Sitz Nr.: ' + seatNb;
                //Sweetalert2 modal success display
                Swal.fire({
                    title: 'Reservation bestätigt.',
                    html: span,
                    imageUrl: location.protocol + '//' + location.host + '/.resources/bls/webresources/img/Test_QR_Code.png',
                    customClass: 'reservation-confirmation',
                    showConfirmButton: false,
                    showCloseButton: true,
                    icon: 'success'
                })
            },
            setSeparator(nextDay) {
                return nextDay;
            },
            getDate: function (date) {
                return date.year + '-' + date.monthValue + '-' + date.dayOfMonth;
            }
        },
        computed: {
            dateToday: function () {
                var today = new Date();
                var dd = String(today.getDate()).padStart(2, '0');
                var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                var yyyy = today.getFullYear();

                today = dd + '.' + mm + '.' + yyyy;
                return today;
            },
            travelDate: function () {
                for (var i = 0; i < this.zugservices.length; i++) {
                    if (this.zugservices[i].uuid === this.zugserviceId) {
                        let zug = this.zugservices[i];
                        return zug.date.dayOfMonth + '.' + zug.date.monthValue + '.' + zug.date.year;
                    }
                }

            },
            departureTime: function () {
                for (var i = 0; i < this.zugservices.length; i++) {
                    if (this.zugservices[i].uuid === this.zugserviceId) {
                        let zug = this.zugservices[i];
                        return zug.departure;
                    }
                }

            },
            departureStation: function () {
                for (var i = 0; i < this.zugservices.length; i++) {
                    if (this.zugservices[i].uuid === this.zugserviceId) {
                        let zug = this.zugservices[i];
                        return zug.from;
                    }
                }

            },
            destinationStation: function () {
                for (var i = 0; i < this.zugservices.length; i++) {
                    if (this.zugservices[i].uuid === this.zugserviceId) {
                        let zug = this.zugservices[i];
                        return zug.to;
                    }
                }

            },
            arrivalTime: function () {
                for (var i = 0; i < this.zugservices.length; i++) {
                    if (this.zugservices[i].uuid === this.zugserviceId) {
                        let zug = this.zugservices[i];
                        return zug.arrival;
                    }
                }

            },
            fromTo: function () {
                return home.ruleForm.from.charAt(0).toUpperCase() + home.ruleForm.from.slice(1) +
                    ' ––– ' + home.ruleForm.to.charAt(0).toUpperCase() + home.ruleForm.to.slice(1);
            }
        }
    })
;

