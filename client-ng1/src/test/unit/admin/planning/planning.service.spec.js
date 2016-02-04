describe('Service PlanningService', function () {
  var service, $httpBackend, slots;

  beforeEach(module('cesar-planning'));


  beforeEach(inject(function ($injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = $injector.get('PlanningService');

    slots = { 'Amphi1' : [
      builders
        .createSlot(1)
        .range('2016-04-21T08:00:00Z', '2016-04-21T09:10:00Z')
        .label('planning.accueil')
        .build(),
      builders
        .createSlot(2)
        .range('2016-04-21T09:20:00:00Z', '2016-04-21T10:30:00Z')
        .session(builders.createSession(631).title('Microplugins avec Docker').lang('fr').build())
        .build(),
      builders
        .createSlot(3)
        .range('2016-04-21T10:30:00Z', '2016-04-21T11:40:00Z')
        .session(builders.createSession(711).title('24 Minutes chrono pour bâtir une appli mobile').lang('fr').build())
        .build(),
      builders
        .createSlot(4)
        .range('2016-04-21T13:30:00Z', '2016-04-21T14:20:00Z')
        .session(builders.createSession(771).title('Sirius : un schéma vaut mieux qu\'un long discours').lang('fr').build())
        .build()
    ]};

  }));

  describe('computeSlots', function(){

    it('should return 11 range for Amphi2 which has  no slots is in database', function () {
      service.computeSlots(slots, [ {
        "key" : "Amphi2",
        "name" : "Petit Amphi",
      }])
      .then(function(response){
        expect(response.Amphi2.length).toBe(11);
        expectRange(response.Amphi2, 0, '08:00', '09:00');
        expectRange(response.Amphi2, 10, '18:00', '19:00');
      });
    });

    it('should return 11 ranges for Amphi1 which has 3 slots in database', function () {
      service.computeSlots(slots, [ {
          "key" : "Amphi1",
          "name" : "Grand Amphi",
        }])
        .then(function(response){
          expect(response.Amphi1.length).toBe(13);

          expectRange(response.Amphi1, 0, '08:00', '09:10');
          expect(response.Amphi1[0].label).toBe('planning.accueil');

          expectRange(response.Amphi1, 1, '09:10', '09:20');
          expect(response.Amphi1[1].label).toBeUndefined();

          expectRange(response.Amphi1, 2, '09:20', '10:30');
          expect(response.Amphi1[2].label).toBeUndefined();
          expect(response.Amphi1[2].session.id).toBe(631);

          expectRange(response.Amphi1, 3, '10:30', '11:40');
          expect(response.Amphi1[3].label).toBeUndefined();
          expect(response.Amphi1[3].session.id).toBe(711);

          expectRange(response.Amphi1, 4, '11:40', '12:00');
          expectRange(response.Amphi1, 5, '12:00', '13:00');
          expectRange(response.Amphi1, 6, '13:00', '13:30');
          expectRange(response.Amphi1, 7, '13:30', '14:20');
          expect(response.Amphi1[7].label).toBeUndefined();
          expect(response.Amphi1[7].session.id).toBe(771);

          expectRange(response.Amphi1, 8, '14:20', '15:00');
          expectRange(response.Amphi1, 9, '15:00', '16:00');
          expectRange(response.Amphi1, 10, '16:00', '17:00');
          expectRange(response.Amphi1, 11, '17:00', '18:00');
          expectRange(response.Amphi1, 12, '18:00', '19:00');
        });
    });
  });


  describe('computeRange', function(){
    it('should retun 11 ranges of 1 hour between 8:00 and 19:00', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00:00Z'), { hour: 8, minute:0}, { hour: 19, minute:0});

      expect(ranges.length).toBe(11);
      expectRange(ranges, 0, '08:00', '09:00');
      expectRange(ranges, 10, '18:00', '19:00');
    });

    it('should conserve minutes for start time', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00:00Z'), { hour: 8, minute:10}, { hour: 10, minute:0});

      expect(ranges.length).toBe(2);
      expectRange(ranges, 0, '08:10', '09:00');
      expectRange(ranges, 1, '09:00', '10:00');
    });

    it('should compute a rang in PM', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00:00Z'), { hour: 14, minute:20}, { hour: 18, minute:0});

      expect(ranges.length).toBe(4);
      expectRange(ranges, 0, '14:20', '15:00');
      expectRange(ranges, 1, '15:00', '16:00');
      expectRange(ranges, 3, '17:00', '18:00');
    });

    it('should conserve minutes for end  time', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00:00Z'), { hour: 8, minute:10}, { hour: 10, minute:30});

      expect(ranges.length).toBe(3);
      expectRange(ranges, 0, '08:10', '09:00');
      expectRange(ranges, 1, '09:00', '10:00');
      expectRange(ranges, 2, '10:00', '10:30');
    });

    it('should return conserve range lesser than an hour ', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00:00Z'), { hour: 9, minute:10}, { hour: 9, minute:30});

      expect(ranges.length).toBe(1);
      expectRange(ranges, 0, '09:10', '09:30');
    });

    it('should return one plage lesser than one hour ', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00:00Z'), { hour: 9, minute:10}, { hour: 10, minute:0});
      expect(ranges.length).toBe(1);
      expectRange(ranges, 0, '09:10', '10:00');
    });

    it('should return nothing when end time upper than start date', function () {
      var ranges = service.computeRange(moment('2016-04-21 12:22:00:00Z'), { hour: 18, minute:10}, { hour: 10, minute:0});
      expect(ranges.length).toBe(0);
    });
  });

  describe('extractSessionToAffect', function() {
    it('should extract the existant sessions', function () {
      var sessions = [
        builders.createSession(631).title('Microplugins avec Docker').build(),
        builders.createSession(711).title('24 Minutes chrono pour bâtir une appli mobile').build(),
        builders.createSession(45).title('Ma session de test 1').build(),
        builders.createSession(771).title('Sirius : un schéma vaut mieux qu\'un long discours').build(),
        builders.createSession(56).title('Ma session de test 2').build()
      ];

      var sessionNotUsed = service.extractSessionToAffect(slots, sessions);

      expect(sessionNotUsed.length).toBe(2);
      expect(sessionNotUsed[0].id).toBe(45);
      expect(sessionNotUsed[1].id).toBe(56);
    });
  });

  function expectRange(ranges, index, start, end){
    expect(moment(ranges[index].start).format('HH:mm')).toBe(start);
    expect(moment(ranges[index].end).format('HH:mm')).toBe(end);
  }
});