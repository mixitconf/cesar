describe('Service PlanningService', function () {
  var service, $httpBackend, slots,rooms;

  beforeEach(module('cesar-planning'));


  beforeEach(inject(function ($injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = $injector.get('PlanningService');

    rooms = [ {
      "key" : "Amphi1",
      "name" : "Petit Amphi",
    }, {
      "key" : "Amphi2",
      "name" : "Grand Amphi",
    }, {
      "key": "Salle1",
      "name": "Gosling",
    }];

    slots = { 'Amphi1' : [
      builders
        .createSlot(1)
        .range('2016-04-21T08:00', '2016-04-21T09:10')
        .label('planning.accueil')
        .build(),
      builders
        .createSlot(2)
        .range('2016-04-21T08:00', '2016-04-21T10:30')
        .session(builders.createSession(631).title('Microplugins avec Docker').lang('fr').build())
        .build(),
      builders
        .createSlot(3)
        .range('2016-04-21T10:30', '2016-04-21T11:40')
        .session(builders.createSession(711).title('24 Minutes chrono pour bâtir une appli mobile').lang('fr').build())
        .build(),
      builders
        .createSlot(4)
        .range('2016-04-21T13:30', '2016-04-21T14:20')
        .session(builders.createSession(771).title('Sirius : un schéma vaut mieux qu\'un long discours').lang('fr').build())
        .build(),
    ]};

  }));


  //it('should compute planning', function () {
  //  $httpBackend.expectGET('/api/planning').respond(slots);
  //
  //  service.computeSlots(rooms).then(function(response){
  //    expect(response).toEqual(slots);
  //  });
  //  $httpBackend.flush();
  //
  //});

  describe('computeRange', function(){
    it('should retun 11 ranges of 1 hour between 8:00 and 19:00', function () {

      var ranges = service.computeRange(moment('2016-04-21 12:22:00'), { hour: 8, minute:0}, { hour: 19, minute:0});

      expect(ranges.length).toBe(11);
      expect(ranges[0].start.format('HH:mm')).toBe('08:00');
      expect(ranges[0].end.format('HH:mm')).toBe('09:00');
      expect(ranges[10].start.format('HH:mm')).toBe('18:00');
      expect(ranges[10].end.format('HH:mm')).toBe('19:00');
    });

    it('should conserve minutes for start time', function () {

      var ranges = service.computeRange(moment('2016-04-21 12:22:00'), { hour: 8, minute:10}, { hour: 10, minute:0});

      expect(ranges.length).toBe(2);
      expect(ranges[0].start.format('HH:mm')).toBe('08:10');
      expect(ranges[0].end.format('HH:mm')).toBe('09:00');
      expect(ranges[1].start.format('HH:mm')).toBe('09:00');
      expect(ranges[1].end.format('HH:mm')).toBe('10:00');
    });

    it('should conserve minutes for end  time', function () {

      var ranges = service.computeRange(moment('2016-04-21 12:22:00'), { hour: 8, minute:10}, { hour: 10, minute:30});

      expect(ranges.length).toBe(3);
      expect(ranges[0].start.format('HH:mm')).toBe('08:10');
      expect(ranges[0].end.format('HH:mm')).toBe('09:00');
      expect(ranges[1].start.format('HH:mm')).toBe('09:00');
      expect(ranges[1].end.format('HH:mm')).toBe('10:00');
      expect(ranges[2].start.format('HH:mm')).toBe('10:00');
      expect(ranges[2].end.format('HH:mm')).toBe('10:30');
    });

    it('should return nothing when end time upper than start date', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00'), { hour: 18, minute:10}, { hour: 10, minute:0});
      expect(ranges.length).toBe(0);
    });
  });

});