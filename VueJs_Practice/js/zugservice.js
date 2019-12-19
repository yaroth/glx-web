var home = new Vue({
    el: '#home',
    data: {
        layout: 'home',
        lastName: '',
        firstName: '',
        birthDate: '',
        time: '08:30',
        from: 'Bern',
        to: 'Thun',
        //Rules for the field validations
        rules: {
            name: [
                {required: true, message: 'Bitte tragen sie Ihren Nachnamen ein!', trigger: 'blur'},
                {min: 0, max: 30, message: 'Es sind nur 30 Zeichen erlaubt!', trigger: 'blur'}
            ]
        }
    },
    methods: {
        getZugservices(e) {
            e.preventDefault();
            axios.post(location.protocol + '//' + location.host + '/.rest/demo/v1/zugservices', {
                time: this.correctTimeFormat(this.time),
                from: this.from,
                to: this.to
            })
                .then(response => (blog_list.zugservices = response.data))
                .catch(error => console.log(error));
            this.layout = '';
            blog_list.layout = 'list';
        },
        //Converts entered time into correct format for the request
        correctTimeFormat(enteredTime) {
            var correctTimeDisplay = enteredTime.getHours() + ':' + enteredTime.getMinutes();
            if (enteredTime.getHours() < 10) {
                return '0' + correctTimeDisplay;
            } else {
                return correctTimeDisplay;
            }
        }
    }
});

var blog_list = new Vue({
        el: '#js-grid-list',
        reservationConfirmation: {
            firstname: 'Fritz',
            lastname: 'Hubacher',
            dateOfBirth: '1969-10-19',
            zugserviceID: 'ebdc5352-3c37-43d4-a2bd-5db4521d22f3',
            wagenNumber: '10',
            sitzNumber: '11',
            departure: 'Bern',
            destination: 'Thun',
            message: 'OK',
            qrCode: ''
        },
        data: {
            layout: '',
            zugserviceId: '',
            zugservices: [{
                uuid: 'ebdc5352-3c37-43d4-a2bd-5db4521d22f3',
                name: 'Bern - Interlaken 8h34',
                departure: '08:34',
                arrival: '08:59',
                from: 'Bern',
                to: 'Thun',
                date: {
                    year: 2019,
                    month: "DECEMBER",
                    monthValue: 12,
                    dayOfMonth: 20,
                    dayOfWeek: "MONDAY",
                    era: "CE",
                    dayOfYear: 350,
                    leapYear: false,
                    chronology: {
                        id: "ISO",
                        calendarType: "iso8601"
                    }
                },
                nextDay: false,
                timetable: [
                    {
                        stopName: 'Bern',
                        timeIN: null,
                        timeOut: {
                            hour: 8,
                            minute: 34,
                            second: 0,
                            nano: 0
                        }
                    },
                    {
                        stopName: 'Münsingen',
                        timeIN: {
                            hour: 8,
                            minute: 49,
                            second: 0,
                            nano: 0
                        },
                        timeOut: {
                            hour: 8,
                            minute: 51,
                            second: 0,
                            nano: 0
                        }
                    },
                    {
                        stopName: 'Thun',
                        timeIN: {
                            hour: 8,
                            minute: 59,
                            second: 0,
                            nano: 0
                        },
                        timeOut: null
                    }
                ],
                zugkomposition: [{
                    uuid: '8989382e-4016-4d9d-9ff7-1b5cd71ca42c',
                    number: '10',
                    wagenplan: {
                        uuid: '6eaaa3e8-6023-4a7f-8ca9-f34dfc1d4127',
                        code: 'BLS Typ 01 - 2019',
                        description: 'Panoramawagen, 1. Klasse',
                        imageLink: '../img/sitzplan3.png',
                        wagentypen: ['Wagen mit Familienabteil', 'Wagen mit Veloverladmöglichkeit'],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V'],
                            reserved: false
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'gang',
                            options: [],
                            reserved: false
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '17',
                            location: 'mitte',
                            options: ['220V'],
                            reserved: false
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '15',
                            location: 'mitte',
                            options: ['220V'],
                            reserved: true
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '12',
                            location: 'fenster',
                            options: ['220V'],
                            reserved: false
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '14',
                            location: 'gang',
                            options: [],
                            reserved: true
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '16',
                            location: 'mitte',
                            options: ['220V'],
                            reserved: true
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '18',
                            location: 'mitte',
                            options: ['220V'],
                            reserved: true
                        }
                        ]
                    }
                }, {
                    uuid: '96e28d17-aebe-43da-9d31-7193e5d56a6b',
                    number: '11',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '../img/sitzplan3.png',
                        wagentypen: ['Normaler Wagen'],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '17',
                            location: 'mitte',
                            options: ['220V'],
                            reserved: false
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '15',
                            location: 'mitte',
                            options: ['220V'],
                            reserved: true
                        }
                        ]
                    }
                }, {
                    uuid: 'bf4a588a-4c92-4885-a68f-89ad2521322a',
                    number: '12',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '../img/sitzplan3.png',
                        wagentypen: ['Normaler Wagen'],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }
                        ]
                    }
                }
                ]
            }, {
                uuid: '64dbb5c7-03c4-4c61-a52a-66edd8a09265',
                name: 'Bern - Interlaken 9h35',
                departure: '09:35',
                arrival: '10:00',
                from: 'Bern',
                to: 'Thun',
                date: {
                    year: 2019,
                    month: "DECEMBER",
                    monthValue: 12,
                    dayOfMonth: 20,
                    dayOfWeek: "MONDAY",
                    era: "CE",
                    dayOfYear: 350,
                    leapYear: false,
                    chronology: {
                        id: "ISO",
                        calendarType: "iso8601"
                    }
                },
                nextDay: false,
                timetable: [{
                    stopName: 'Bern',
                    timeIN: null,
                    timeOut: {
                        hour: 9,
                        minute: 35,
                        second: 0,
                        nano: 0
                    }
                }, {
                    stopName: 'Münsingen',
                    timeIN: {
                        hour: 9,
                        minute: 50,
                        second: 0,
                        nano: 0
                    },
                    timeOut: {
                        hour: 9,
                        minute: 52,
                        second: 0,
                        nano: 0
                    }
                }, {
                    stopName: 'Thun',
                    timeIN: {
                        hour: 10,
                        minute: 0,
                        second: 0,
                        nano: 0
                    },
                    timeOut: null
                }
                ],
                zugkomposition: [{
                    uuid: '8989382e-4016-4d9d-9ff7-1b5cd71ca42c',
                    number: '10',
                    wagenplan: {
                        uuid: '6eaaa3e8-6023-4a7f-8ca9-f34dfc1d4127',
                        code: 'BLS Typ 01 - 2019',
                        description: 'Panoramawagen, 1. Klasse',
                        imageLink: '/dam/jcr:2db40bd4-3f97-46f8-885e-b9954bc63a88/Sitzplan.jpg',
                        wagentypen: [
                            'Wagen mit Familienabteil',
                            'Wagen mit Veloverladmöglichkeit'
                        ],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'gang',
                            options: []
                        }
                        ]
                    }
                }, {
                    uuid: '96e28d17-aebe-43da-9d31-7193e5d56a6b',
                    number: '11',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '/dam/jcr:d974d2be-f1a1-465a-b704-f3287b5108fb/Pano%202.%20Klasse.png',
                        wagentypen: [
                            'Normaler Wagen'
                        ],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }
                        ]
                    }
                }, {
                    uuid: 'bf4a588a-4c92-4885-a68f-89ad2521322a',
                    number: '12',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '/dam/jcr:d974d2be-f1a1-465a-b704-f3287b5108fb/Pano%202.%20Klasse.png',
                        wagentypen: ['Normaler Wagen'],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }
                        ]
                    }
                }
                ]
            }, {
                uuid: '78fb8b0d-2fea-45ee-846c-1b97da4f9233',
                name: 'Olten Interlaken 10h',
                departure: '07:00',
                arrival: '10:52',
                from: 'Bern',
                to: 'Thun',
                date: {
                    year: 2019,
                    month: "DECEMBER",
                    monthValue: 12,
                    dayOfMonth: 20,
                    dayOfWeek: "MONDAY",
                    era: "CE",
                    dayOfYear: 350,
                    leapYear: false,
                    chronology: {
                        id: "ISO",
                        calendarType: "iso8601"
                    }
                },
                nextDay: true,
                timetable: [{
                    stopName: 'Bern',
                    timeIN: null,
                    timeOut: {
                        hour: 10,
                        minute: 36,
                        second: 0,
                        nano: 0
                    }
                }, {
                    stopName: 'Thun',
                    timeIN: {
                        hour: 10,
                        minute: 52,
                        second: 0,
                        nano: 0
                    },
                    timeOut: null
                }
                ],
                zugkomposition: [{
                    uuid: '8989382e-4016-4d9d-9ff7-1b5cd71ca42c',
                    number: '10',
                    wagenplan: {
                        uuid: '6eaaa3e8-6023-4a7f-8ca9-f34dfc1d4127',
                        code: 'BLS Typ 01 - 2019',
                        description: 'Panoramawagen, 1. Klasse',
                        imageLink: '/dam/jcr:2db40bd4-3f97-46f8-885e-b9954bc63a88/Sitzplan.jpg',
                        wagentypen: ['Wagen mit Familienabteil', 'Wagen mit Veloverladmöglichkeit'],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'gang',
                            options: []
                        }]
                    }
                }, {
                    uuid: '96e28d17-aebe-43da-9d31-7193e5d56a6b',
                    number: '11',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '/dam/jcr:d974d2be-f1a1-465a-b704-f3287b5108fb/Pano%202.%20Klasse.png',
                        wagentypen: [
                            'Normaler Wagen'
                        ],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }
                        ]
                    }
                }, {
                    uuid: 'bf4a588a-4c92-4885-a68f-89ad2521322a',
                    number: '12',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '/dam/jcr:d974d2be-f1a1-465a-b704-f3287b5108fb/Pano%202.%20Klasse.png',
                        wagentypen: [
                            'Normaler Wagen'
                        ],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }
                        ]
                    }
                }
                ]
            }, {
                uuid: '78fb8b0d-2fea-45ee-846c-1b97da4f9233',
                name: 'Olten Interlaken 10h',
                departure: '08:00',
                arrival: '10:52',
                from: 'Bern',
                to: 'Thun',
                date: {
                    year: 2019,
                    month: "DECEMBER",
                    monthValue: 12,
                    dayOfMonth: 17,
                    dayOfWeek: "MONDAY",
                    era: "CE",
                    dayOfYear: 350,
                    leapYear: false,
                    chronology: {
                        id: "ISO",
                        calendarType: "iso8601"
                    }
                },
                nextDay: false,
                timetable: [{
                    stopName: 'Bern',
                    timeIN: null,
                    timeOut: {
                        hour: 10,
                        minute: 36,
                        second: 0,
                        nano: 0
                    }
                }, {
                    stopName: 'Thun',
                    timeIN: {
                        hour: 10,
                        minute: 52,
                        second: 0,
                        nano: 0
                    },
                    timeOut: null
                }
                ],
                zugkomposition: [{
                    uuid: '8989382e-4016-4d9d-9ff7-1b5cd71ca42c',
                    number: '10',
                    wagenplan: {
                        uuid: '6eaaa3e8-6023-4a7f-8ca9-f34dfc1d4127',
                        code: 'BLS Typ 01 - 2019',
                        description: 'Panoramawagen, 1. Klasse',
                        imageLink: '/dam/jcr:2db40bd4-3f97-46f8-885e-b9954bc63a88/Sitzplan.jpg',
                        wagentypen: ['Wagen mit Familienabteil', 'Wagen mit Veloverladmöglichkeit'],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'gang',
                            options: []
                        }]
                    }
                }, {
                    uuid: '96e28d17-aebe-43da-9d31-7193e5d56a6b',
                    number: '11',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '/dam/jcr:d974d2be-f1a1-465a-b704-f3287b5108fb/Pano%202.%20Klasse.png',
                        wagentypen: [
                            'Normaler Wagen'
                        ],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }
                        ]
                    }
                }, {
                    uuid: 'bf4a588a-4c92-4885-a68f-89ad2521322a',
                    number: '12',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '/dam/jcr:d974d2be-f1a1-465a-b704-f3287b5108fb/Pano%202.%20Klasse.png',
                        wagentypen: [
                            'Normaler Wagen'
                        ],
                        seats: [{
                            uuid: null,
                            klasse: 2,
                            id: '11',
                            location: 'fenster',
                            options: ['220V']
                        }, {
                            uuid: null,
                            klasse: 2,
                            id: '13',
                            location: 'mitte',
                            options: ['220V']
                        }
                        ]
                    }
                }
                ]
            }
            ]
        },
        methods: {
            showZugserviceDetail(uuid) {
                this.layout = 'zugservice-detail';
                this.zugserviceId = uuid;
                this.infopanel = 'yann';
                console.log("Zugservicedetails line clicked, uuid: " + uuid);
            }
            ,
            showSeatDetail() {
                console.log("showSeatDetail line clicked");
            },
            requestReservation(zugUuid, seatId, waggonNumber, from, to, date) {
                console.log("reservation requested, seat: " + seatId + ", waggon: " + waggonNumber + ", zug uuid: " + zugUuid + ", from: " + from + ", to: " + to);
                axios.post(location.protocol + '//' + location.host + '/.rest/demo/v1/reservation', {
                    firstname: 'Gael',
                    lastname: 'Zwirbel',
                    dateOfBirth: '1978-03-24',
                    zugserviceID: zugUuid,
                    wagenNumber: waggonNumber,
                    sitzNumber: seatId,
                    departure: from,
                    destination: to,
                    date: this.getDate(date)
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
                let resConf = home.reservationConfirmation;
                if (resConf.message == 'OK') {
                    let zugId = resConf.zugserviceID;
                    let wagNb = resConf.wagenNumber;
                    let sitzNb = resConf.sitzNumber;
                    let seat = this.getSeat(zugId, wagNb, sitzNb);
                    if (seat !== undefined) {
                        seat.reserved = true;
                        //Shows confirmation of reservation
                        //TODO: Add User Information like Name / Firstname / Birthdate
                        Swal.fire({
                            title: 'Ihre Reservation wurde bestätigt.',
                            text: 'Strecke: ' + this.reservationStatus.departure + '-' + this.reservationStatus.destination + '\n'
                                + 'Wagen: ' + wagNb + '\n'
                                + 'Sitz: ' + sitzNb,
                            imageUrl: location.protocol + '//' + location.host + '/.resources/bls/webresources/img/Test_QR_Code.png',
                            customClass: 'reservation-confirmation',
                            showConfirmButton: false,
                            showCloseButton: true,
                            icon: 'success'
                        })
                    }
                }

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
            //makes a multiline text for the reservation confirmation text in the sweetalert2 confirmation window
            reservationConfirmation(fName, lName, departure, destination, wagNb, seatNb) {
                var span = document.createElement("span");
                return span.innerHTML = 'Benutzer: ' + fName + ' ' + lName + '<br>'
                    + 'Strecke: ' + this.reservationStatus.departure + '-' + this.reservationStatus.destination + '<br>'
                    + 'Wagen: ' + wagNb + ' Sitz Nr.: ' + seatNb;
            },
            // converts entered date into correct date format to save
            correctDateFormat(enteredDate) {
                return enteredDate.getFullYear() + '-' + enteredDate.getMonth() + '-' + enteredDate.getDate();
            },
            setSeparator(nextDay) {
                return nextDay;
            },
            getDate(date) {
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
                return home.from + ' ––– ' + home.to;
            }
        }
    })
;