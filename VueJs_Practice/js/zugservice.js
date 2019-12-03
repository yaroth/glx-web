var home = new Vue({
    el: '#home',
    data: {
        time: '09:30',
        from: 'Bern',
        to: 'Thun',
        reservationConfirmation: ''
    },
    methods: {
        getZugservices() {
            e.preventDefault();
            axios.post('http://localhost:8080/.rest/demo/v1/zugservices', {
                time: this.time,
                from: this.from,
                to: this.to
            })
                .then(response => (blog_list.zugservices = response.data))
                .catch(error => console.log(error))
        }
    }
});

var blog_list = new Vue({
        el: '#js-grid-list',
        data: {
            layout: 'list',
            zugserviceId: '',
            zugservices: [{
                uuid: 'ebdc5352-3c37-43d4-a2bd-5db4521d22f3',
                name: 'Bern - Interlaken 8h34',
                departure: '08:34',
                arrival: '08:59',
                from: 'Bern',
                to: 'Thun',
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
                        imageLink: '../images/sitzplan.jpg',
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
                        imageLink: '../images/sitzplan.jpg',
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
                }, {
                    uuid: 'bf4a588a-4c92-4885-a68f-89ad2521322a',
                    number: '12',
                    wagenplan: {
                        uuid: '4751dad2-9b3b-487d-aa03-a5004f91b909',
                        code: 'BLS Typ 02 - 2018',
                        description: 'Bistro, 1. Klasse',
                        imageLink: '../images/sitzplan2.png',
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
                departure: '10:00',
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
            }


        }
    })
;